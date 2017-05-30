package org.dsc.db.reporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dsc.db.model.CrSchema;
import org.dsc.db.model.CrTable;
import org.dsc.db.model.CrView;


/**
 * 1 archivo xml por schema
 * @author BIMALOSE
 */
public final class XmlReporter extends AbstractReporter {
	private PrintStream out;
	
	public XmlReporter(final String outDir, final String schemaName) throws Exception {
		super(outDir, schemaName);
	}
	
	public void generate(final CrSchema crschema) {
		try {
			headSchema(crschema);
			// tablas
			List<String> tableList = new ArrayList<String>(crschema.tables.keySet());
			Collections.sort(tableList);
			for (String tableName : tableList) {
				CrTable table = (CrTable) crschema.tables.get(tableName);
				String tableNameSyn = tableName;
				if (null != table.synonym) {
					tableNameSyn += " - " + table.synonym;
				}
				// archivo xml desc table
				headTable(table);
				footTable();
			}
			
			// vistas
			List<String> viewList = new ArrayList<String>(crschema.views.keySet());
			Collections.sort(viewList);
			for (String viewName : viewList) {
				CrView view = (CrView) crschema.views.get(viewName);
				String viewNameSyn = viewName;
				if (null != view.synonym) {
					viewNameSyn += " - " + view.synonym;
				}
				// archivo html desc view
				headView(view);
				footView();
			}
			footSchema();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void print(String string) {
		if (null != string && !"".equals(string)) {
			out.println("<comment>" + string + "</comment>");
		}
	}

	protected void headSchema(final Object obj) throws Exception {
		File outFile = new File(outputFile.getAbsolutePath() + File.separator + schemaName + ".xml");
//		indexOut = new PrintStream(outFile);
		FileOutputStream fos = new FileOutputStream(outFile);
		out = new PrintStream(fos);
		
		out.println("<schema name='" + obj + "'>");
	}

	protected void footSchema() {
		out.println("</schema>");
		out.close();
	}

	// TABLES
	
	protected void headTable(final Object obj) throws Exception {
		crtable = (CrTable) obj;
		
		out.println(IReporter.TAB + "<table name='" + crtable.name + "' comment='" + crtable.comment + "'>");
		if (null != crtable.synonym) {
			printSy(crtable.synonym);
		}
//		crtable.pks.values().iterator()
//		crtable.fks.values().iterator()
//		crtable.ixs.values().iterator()
//		crtable.cks.values().iterator()
//		crtable.columns.values().iterator()
	}

	protected void footTable() {
		out.println(IReporter.TAB + "</table>");
	}

	protected void printPk(String string) {
		out.print(IReporter.TAB2 + "<pk id='" + string + "'");
	}

	protected void descPk(String string) {
		out.println(" desc='" + string + "'/>");
	}

	protected void printFk(String string) {
		out.print(IReporter.TAB2 + "<fk id='" + string + "'");
	}

	protected void descFk(String string) {
		out.println(" desc='" + string + "'/>");
	}

	protected void printIx(String string) {
		out.print(IReporter.TAB2 + "<ix id='" + string + "'");
	}

	protected void descIx(String string) {
		out.println(" desc='" + string + "'/>");
	}

	protected void printCk(String string) {
		out.print(IReporter.TAB2 + "<ck id='" + string + "'");
	}

	protected void descCk(String string) {
		out.println(" desc='" + string + "'/>");
	}

	protected void printSy(String string) {
		out.println(IReporter.TAB2 + "<sy id='" + string + "'/>");
	}

	protected void headCol(String string) {
		out.print(IReporter.TAB2 + "<col id='" + string + "'");
	}

	protected void printColType(String string) {
		out.print(" type='" + string + "'");
	}

	protected void printColAttr(String string) {
		if (null != string && string.length() > 0) {
			out.print(" attr='" + string + "'");
		}
	}

	protected void printColComm(String string) {
		out.print(" desc='" + string + "'");
	}

	protected void footCol() {
		out.println("/>");
	}
	
	// VIEWS
	
	protected void headView(final Object obj) {
		crview = (CrView) obj;
		out.println(IReporter.TAB + "<view name='" + obj + "'>");
		if (null != crview.synonym) {
			printSy(crview.synonym);
		}
		descView(crview.def);
	}
	
	protected void descView(String string) {
		out.println(IReporter.TAB2 + " <related_table desc='" + string + "'/>");
	}

	protected void footView() {
		out.println(IReporter.TAB + "</view>");
	}
}
