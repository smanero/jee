package org.dsc.db;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.dsc.db.dao.OracleSchemaDao;
import org.dsc.db.model.CrColumn;
import org.dsc.db.model.CrSchema;
import org.dsc.db.model.CrTable;
import org.dsc.db.model.CrView;
import org.dsc.db.reporter.TextReporter;

import schemacrawler.schema.CheckConstraint;
import schemacrawler.schema.Column;
import schemacrawler.schema.Database;
import schemacrawler.schema.ForeignKey;
import schemacrawler.schema.ForeignKeyColumnMap;
import schemacrawler.schema.Index;
import schemacrawler.schema.IndexColumn;
import schemacrawler.schema.PrimaryKey;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;
import schemacrawler.schema.View;
import schemacrawler.schemacrawler.DatabaseConnectionOptions;
import schemacrawler.schemacrawler.InclusionRule;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevel;
import schemacrawler.utility.SchemaCrawlerUtility;

/**
 * 
 * @author bimalose
 */
public class OracleSchemaRipper {
	private String schemaName;
	private String jdbcDriver;
	private String connectionChain; // pej. jdbc:oracle:thin:@ejhp67:1524:ede2
	private TextReporter reporter;

	private Schema schema;
	private Connection conn;

	public OracleSchemaRipper(final String schemaName, final String jdbcDriver, final String connectionChain,
	      boolean verbose) {
		this.schemaName = schemaName;
		this.jdbcDriver = jdbcDriver;
		this.connectionChain = connectionChain;

		reporter = new TextReporter(verbose);
	}

	private void connect() throws Exception {
		if (null == conn) {
			DatabaseConnectionOptions connectionOptions = new DatabaseConnectionOptions(this.jdbcDriver,
			      this.connectionChain);
			connectionOptions.setUser(this.schemaName);
			connectionOptions.setPassword(this.schemaName);
			this.conn = connectionOptions.createConnection();

			reporter.print("Connected to: " + this.connectionChain + ":" + this.schemaName + ":" + this.schemaName);

			SchemaCrawlerOptions options = configure();
			Database database = SchemaCrawlerUtility.getDatabase(this.conn, options);
			// for (final Schema schema : database.getSchemas()) {
			for (int i = 0; i < database.getSchemas().length; i++) {
				Schema schemaIt = database.getSchemas()[i];
				if (this.schemaName.toUpperCase().equals(schemaIt.getName())) {
					this.schema = schemaIt;
					break;
				}
			}
		}

		if (null == this.schema) {
			throw new Exception("Esquema no encontrado en bd con nombre " + this.schemaName);
		}
	}

	private void close() throws Exception {
		conn.close();
	}

	private SchemaCrawlerOptions configure() {
		// Create the options
		SchemaCrawlerOptions options = new SchemaCrawlerOptions();
		// Set what details are required in the schema - this affects the
		// time taken to crawl the schema
		options.setSchemaInfoLevel(SchemaInfoLevel.standard());
		options.setProcedureInclusionRule(new InclusionRule(InclusionRule.NONE, InclusionRule.ALL));
		options.setSchemaInclusionRule(new InclusionRule(schemaName.toUpperCase(), InclusionRule.NONE));
		// Sorting options
		options.setAlphabeticalSortForTableColumns(true);
		options.setAlphabeticalSortForForeignKeys(true);
		options.setAlphabeticalSortForIndexes(true);
		options.setAlphabeticalSortForTables(true);

		return options;
	}

	public CrSchema rip() throws Exception {
		CrSchema crschema = new CrSchema(schemaName);

		connect();
		StringBuffer sb;
		boolean comma = false;

		for (final Table table : schema.getTables()) {
			// table name
			String canonicalTableName = table.getName();
			if (!canonicalTableName.startsWith(this.schemaName.toUpperCase())) {
			}
			// tablas y vistas pertenecientes al esquema synonyms
			String tableSyn = OracleSchemaDao.getTableSynonym(conn, canonicalTableName);
			String tableComment = OracleSchemaDao.getTableComment(conn, canonicalTableName);

			if (table instanceof View) {
				CrView crview = new CrView(canonicalTableName);
				crschema.views.put(canonicalTableName, crview);
				crview.synonym = tableSyn;
				crview.comment = tableComment;
				crview.def = OracleSchemaDao.getViewDefinition(conn, canonicalTableName);

			} else {
				CrTable crtable = (CrTable) crschema.tables.get(canonicalTableName);
				if (null == crtable) {
					long numRows = OracleSchemaDao.getRowNum(conn, canonicalTableName);

					crtable = new CrTable(canonicalTableName, numRows);
					crschema.tables.put(canonicalTableName, crtable);
				}
				crtable.synonym = tableSyn;
				crtable.comment = tableComment;
				// columns
				// for (final Column column : table.getColumns()) {
				for (int j = 0; j < table.getColumns().length; j++) {
					Column column = table.getColumns()[j];
					// column size
					long lg = OracleSchemaDao.getColumnSize(conn, canonicalTableName, column);
					// column name, type
					CrColumn crcolumn = new CrColumn(column, lg);
					// column attrs
					sb = new StringBuffer();
					if (column.isPartOfPrimaryKey()) {
						sb.append("P");
						crcolumn.pk = true;
					}
					if (column.isPartOfForeignKey()) {
						sb.append("F");
						crcolumn.fk = true;
					}
					if (column.isPartOfUniqueIndex()) {
						sb.append("U");
						crcolumn.ix = true;
					}
					if (column.isNullable()) {
						sb.append("N");
						crcolumn.nb = true;
					}
					crcolumn.attrs = sb.toString();
					// column comments in db
					crcolumn.comment = OracleSchemaDao.getColumnComment(conn, canonicalTableName, crcolumn.name);
					crtable.columns.put(crcolumn.name, crcolumn);
				}

				// calculo del average lg de la tabla
				crtable.dimension();

				// primary keys
				PrimaryKey pk = table.getPrimaryKey();
				if (null != pk) {
					sb = new StringBuffer();
					comma = false;
					// for (final IndexColumn ic : pk.getColumns()) {
					for (int k = 0; k < pk.getColumns().length; k++) {
						IndexColumn ic = pk.getColumns()[k];
						if (comma) {
							sb.append(", ");
						}
						sb.append(ic.getName());
						comma = true;
						CrColumn ccl = (CrColumn) crtable.columns.get(ic.getName());
						ccl.pk = true;
					}
					crtable.pks.put(pk.getName(), sb.toString());
				}
				// foreign keys
				// for (final ForeignKey fk : table.getForeignKeys()) {
				for (int l = 0; l < table.getForeignKeys().length; l++) {
					ForeignKey fk = table.getForeignKeys()[l];
					sb = new StringBuffer();
					comma = false;
					// for (final ForeignKeyColumnMap fkcm : fk.getColumnPairs()) {
					for (int m = 0; m < fk.getColumnPairs().length; m++) {
						ForeignKeyColumnMap fkcm = fk.getColumnPairs()[m];
						String colName = fkcm.getPrimaryKeyColumn().getName();
						Column fkCol = fkcm.getForeignKeyColumn();
						String fkColName = fkCol.getName();
						String fkTableName = fkCol.getParent().getName();

						if (comma) {
							sb.append(", ");
						}
						sb.append(colName).append("->").append(fkTableName).append(".").append(fkColName);
						comma = true;
						if (!fkTableName.equals(canonicalTableName)) {
							// relaciones de la columna
							if (null != crtable.columns.get(colName)) {
								CrColumn crcolumn = (CrColumn) crtable.columns.get(colName);
								crcolumn.fk = true;
								if (null == crcolumn.relations) {
									crcolumn.relations = new ArrayList<String>();
								}
								crcolumn.relations.add(fkTableName + "." + fkColName);
							}
							// relaciones de la tabla
							if (null == crtable.relations.get(fkTableName)) {
								crtable.relations.put(fkTableName, new ArrayList<String>());
							}
							List<String> lstRel = (List<String>) crtable.relations.get(fkTableName);
							lstRel.add(colName + " &rArr; " + fkColName);
							// tabla referenciada por
							CrTable fkTable = (CrTable) crschema.tables.get(fkTableName);
							if (null == fkTable) {
								long numRows = OracleSchemaDao.getRowNum(conn, fkTableName);
								fkTable = new CrTable(fkTableName, numRows);

								crschema.tables.put(fkTableName, fkTable);
							}
							if (null == fkTable.references.get(canonicalTableName)) {
								fkTable.references.put(canonicalTableName, new ArrayList<String>());
							}
							List<String> lstRef = (List<String>) fkTable.references.get(canonicalTableName);
							lstRef.add(fkColName + " &lArr; " + colName);
						}
					}
					crtable.fks.put(fk.getName(), sb.toString());
				}
				// indexes
				// for (final Index index : table.getIndices()) {
				for (int n = 0; n < table.getIndices().length; n++) {
					Index index = table.getIndices()[n];
					sb = new StringBuffer();
					comma = false;
					// for (final IndexColumn ic : index.getColumns()) {
					for (int o = 0; o < index.getColumns().length; o++) {
						IndexColumn ic = index.getColumns()[o];
						if (comma) {
							sb.append(", ");
						}
						sb.append(ic.getName());
						comma = true;
						CrColumn crcolumn = (CrColumn) crtable.columns.get(ic.getName());
						crcolumn.ix = true;
					}
					crtable.ixs.put(index.getName(), sb.toString());
				}

				// check contraints
				// for (final CheckConstraint ck : table.getCheckConstraints()) {
				for (int p = 0; p < table.getCheckConstraints().length; p++) {
					CheckConstraint ck = table.getCheckConstraints()[p];
					crtable.cks.put(ck.getName(), ck.getDefinition());
				}
			} // end for tables
		}
		close();
		return crschema;
	}
}
