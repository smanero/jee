package org.dsc.db.reporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dsc.db.model.CrColumn;
import org.dsc.db.model.CrSchema;
import org.dsc.db.model.CrTable;
import org.dsc.db.model.CrView;


/**
 * 1 archivo html por schema - indice de tablas
 * 1 archivo html por tabla/vista
 * @author bimalose
 */
public final class HtmlReporter extends AbstractReporter {
	private PrintStream out;
	private PrintStream indexOut;
	
	public HtmlReporter(final String outDir, final String schemaName) throws Exception {
		super(outDir, schemaName);
	}

	public void generate(final CrSchema crschema) {
		try {
			File outFile = new File(outputFile.getAbsolutePath() + File.separator + "index.html");
			FileOutputStream fos = new FileOutputStream(outFile);
			indexOut = new PrintStream(fos);
			
			headSchema(crschema.name);
			
			// tablas
			List<String> tableList = new ArrayList<String>(crschema.tables.keySet());
			Collections.sort(tableList);
			indexOut.println(IReporter.TAB + "<h3>TABLES (" + tableList.size() + " tables)</h3><ul>");
			
			StringBuffer sb = new StringBuffer("<table>");
			sb.append("<thead>")
			  .append("<th>").append("NOMBRE").append("</th>")
			  .append("<th>").append("SINONIMO").append("</th>")
			  .append("<th>").append("LG.").append("</th>")
			  .append("<th>").append("NUM ROWS").append("</th>")
			  .append("</thead><tbody>");
//			StringBuffer sb = new StringBuffer(IReporter.TAB);
			for (String tableName : tableList) {
				crtable = (CrTable) crschema.tables.get(tableName);
				// archivo html desc table
				generateTable();

				// indice de tablas . 1 tabla -> 1 row
				sb.append("<tr>")
				  .append("<td><a href='").append(tableName).append(".html'>").append(crtable.name).append("</a></td>")
				  .append("<td><a href='").append(tableName).append(".html'>").append(crtable.synonym).append("</a></td>")
				  .append("<td><a href='").append(tableName).append(".html'>").append(crtable.avlg).append("</a></td>")
				  .append("<td><a href='").append(tableName).append(".html'>").append(crtable.numRows).append("</a></td>")
				  .append("</tr>");
//				sb.append("<li><a href='").append(tableName).append(".html'>").append(crtable.fullName()).append("</a>")
//				  .append("  Lg. ").append(crtable.avlg).append(" bytes")
//				  .append("  NumRows. ").append(crtable.numRows).append("</li>");
			}
			sb.append("</tbody></table>");
			indexOut.println(sb.toString());			
			
			// vistas
			List<String> viewList = new ArrayList<String>(crschema.views.keySet());
			Collections.sort(viewList);
			indexOut.println(IReporter.TAB + "</ul><hr/><h3>VIEWS (" + viewList.size() + " views)</h3><ul>");
			for (String viewName : viewList) {
				crview = (CrView) crschema.views.get(viewName);
				indexOut.println(IReporter.TAB + "<li><a href='Vw" + viewName + ".html'>" + crview.fullName() + "</a></li>");
				// archivo html desc view
				generateView();
			}
			indexOut.println("</ul>");
			footSchema();
		} catch (Exception e) {
			print(e.getMessage());
		}
	}

	public void print(String string) {
		System.out.println(string);
		if (null != out) {
			out.println("<p>" + string + "</p>");
		}
	}

	protected void headSchema(final Object obj) {
		System.out.println(obj);		
		indexOut.println("<html><head>");
		HtmlReporter.printInnerStyle(indexOut);
		indexOut.println("</head>");
		indexOut.println("<body><h1>" + schemaName.toUpperCase() + "</h1>");
	}

	protected void footSchema() {
		indexOut.println("</body></html>");
		indexOut.close();
	}

	// TABLES	
	private void generateTable() throws Exception {
		System.out.println(crtable.name);
		
		File outFile = new File(outputFile.getAbsolutePath() + File.separator + crtable.name + ".html");
//			out = new PrintStream(outFile);
		FileOutputStream fos = new FileOutputStream(outFile);
		out = new PrintStream(fos);
		
		// head con inner style
		out.println("<html><head>");
		HtmlReporter.printInnerStyle(out);
		out.println("</head>");
		// body con synonym
		out.println("<body>");
		
		StringBuffer sb = new StringBuffer("<h1>");
		sb.append(crtable.name);		
		if (null != crtable.synonym && !"".equals(crtable.synonym)) {
			sb.append(" - ").append(crtable.synonym);
		} else {
			sb.append(" - ").append("#WARNING#");
		}
		sb.append("</h1>");
		out.println(sb.toString());
		
		sb = new StringBuffer("<h2>");
		if (null != crtable.comment && !"".equals(crtable.comment)) {
			sb.append(crtable.comment);
		} else {
			sb.append("#WARNING#");
		}
		sb.append("</h2>");
		out.println(sb.toString());
		
		sb = new StringBuffer("<h3>Estimacion BD Lg.=");
		sb.append(crtable.avlg).append(" Bytes</h3>");
		out.println(sb.toString());
		
		sb = new StringBuffer("<h3>Numero Filas=");
		sb.append(crtable.numRows).append("</h3>");		
		out.println(sb.toString());
		
		// table con synonym
		// columnas
		printCols();
		// constraints
		printCks();
		// relations
		printRels();
		// references
		printRefs();
		
		out.println("</body></html>");
		out.close();
	}
	
	private void printCols() {
		out.println(IReporter.TAB + "<h3>COLUMNS</h3>");
		out.println(IReporter.TAB + "<table width='100%'><tr><td><b>ID</b></td><td><b>TYPE</b></td><td><b>ATTR</b></td><td><b>COMMENT</b></td></tr>");
		List<String> columnList = new ArrayList<String>(crtable.columns.keySet());
		Collections.sort(columnList);
		StringBuffer sb;
		for (String columnName : columnList) {
			CrColumn crcolumn = (CrColumn) crtable.columns.get(columnName);
			// recalculo atributos columna
			crcolumn.attrs = "";
			if (crcolumn.pk) { crcolumn.attrs += "P"; }
			if (crcolumn.fk) { crcolumn.attrs += "F"; }
			if (crcolumn.ix) { crcolumn.attrs += "U"; }
			if (crcolumn.nb) { crcolumn.attrs += "N"; }
			
			sb = new StringBuffer("<tr>");
			sb.append("<td>").append(crcolumn.name).append("</td>")
					.append("<td>").append(crcolumn.type).append("</td>")
					.append("<td>").append(crcolumn.attrs).append("</td>");
			
			if (null != crcolumn.comment && !"".equals(crcolumn.comment)) {
				sb.append("<td>").append(crcolumn.comment).append("</td>");
			} else {
				sb.append("<td>#WARNING#</td>");
			}
			sb.append("</tr>");
			out.println(IReporter.TAB2 + sb.toString());
		}
		out.println(IReporter.TAB + "</table><hr/>");
	}
	
	private void printCks() {
		if (null != crtable.cks && crtable.cks.size() > 0) {
			out.println(IReporter.TAB + "<h3>CONSTRAINTS</h3>");
			out.println(IReporter.TAB + "<table width='100%'><tr><td><b>ID</b></td><td><b>DEF</b></td></tr>");
			StringBuffer sb;
			for (String ckname : crtable.cks.keySet()) {
				String ckdef = (String) crtable.cks.get(ckname);			
				sb = new StringBuffer("<tr>");
				sb.append("<td>").append(ckname).append("</td>")
						.append("<td>").append(ckdef).append("</td>")
						.append("</tr>");
				out.println(IReporter.TAB2 + sb.toString());
			}
			out.println(IReporter.TAB + "</table><hr/>");
		}
	}
	
	private void printRels() {
		if (null != crtable.relations && crtable.relations.size() > 0) {
			out.println(IReporter.TAB + "<h3>RELATIONS</h3>");
			out.println(IReporter.TAB + "<table width='100%'><tr><td><b>TABLE</b></td><td><b>COLUMN</b></td></tr>");
			StringBuffer sb;
			for (String fkTableName : crtable.relations.keySet()) {
				sb = new StringBuffer("<tr>");
				sb.append("<td><a href='").append(fkTableName).append(".html'>").append(fkTableName).append("</a></td>");
				sb.append("<td>");
				for (String fkColumnName : crtable.relations.get(fkTableName)) {
					sb.append(fkColumnName);
				}
				sb.append("</td></tr>");
				out.println(IReporter.TAB2 + sb.toString());
			}
			out.println(IReporter.TAB + "</table><hr/>");
		}
	}
	
	private void printRefs() {
		if (null != crtable.references && crtable.references.size() > 0) {
			out.println(IReporter.TAB + "<h3>REFERENCES</h3>");
			out.println(IReporter.TAB + "<table width='100%'><tr><td><b>TABLE</b></td><td><b>COLUMN</b></td></tr>");
			StringBuffer sb;
			for (String rkTableName : crtable.references.keySet()) {
				sb = new StringBuffer("<tr>");
				sb.append("<td><a href='").append(rkTableName).append(".html'>").append(rkTableName).append("</a></td>");
				sb.append("<td>");
				for (String rkColumnName : crtable.references.get(rkTableName)) {
					sb.append(rkColumnName);
				}
				sb.append("</td></tr>");
				out.println(IReporter.TAB2 + sb.toString());
			}
			out.println(IReporter.TAB + "</table><hr/>");
		}
	}

	// VIEWS	
	private void generateView() throws Exception {
		System.out.println(crview.name);

		File outFile = new File(outputFile.getAbsolutePath() + File.separator + "Vw" + crview.name + ".html");
		FileOutputStream fos = new FileOutputStream(outFile);
		out = new PrintStream(fos);

		out.println("<html><head></head>");
		out.println("<body><h1>" + crview.name + "</h1>");
		if (null != crview.synonym) {
			out.println(IReporter.TAB + "<h2>" + crview.synonym + "</h2>");
		}
		System.out.println(crview.def);
		out.println(IReporter.TAB + "<p>" + crview.def + "</p>");
		
		out.println("</body></html>");
		out.close();
	}
	
	// CSS STYLE
	public static void printInnerStyle(PrintStream outPr) {
		outPr.println(IReporter.TAB + "<style type=\"text/css\">");
		outPr.println(IReporter.TAB2 + "H1 {text-align: center; font-family: Verdana, Helvetica, Arial, sans-serif;}");
		outPr.println(IReporter.TAB2 + "H2 {text-align: center; font-family: Verdana, Helvetica, Arial, sans-serif;}");
		outPr.println(IReporter.TAB2 + "H3 {text-align: left; font-family: Verdana, Helvetica, Arial, sans-serif;}");
		outPr.println(IReporter.TAB2 + "table {text-align: left; font-family: Verdana, Helvetica, Arial, sans-serif;}");
		outPr.println(IReporter.TAB2 + "td {text-size: 10px;}");
		outPr.println(IReporter.TAB2 + "a {text-align: left; font-family: Verdana, Helvetica, Arial, sans-serif;}");
		outPr.println(IReporter.TAB + "</style>");
	}
}
