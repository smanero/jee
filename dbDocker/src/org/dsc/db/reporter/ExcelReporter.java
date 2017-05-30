package org.dsc.db.reporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.dsc.db.model.CrSchema;


/**
 * 1 archivo html por schema - indice de tablas 1 archivo html por tabla/vista
 * 
 * @author bimalose
 */
public final class ExcelReporter extends AbstractReporter {

	public ExcelReporter(final String outDir, final String schemaName) throws Exception {
		super(outDir, schemaName);
	}

	public void generate(final CrSchema crschema) {
		try {
			File outFile = new File(outputFile.getAbsolutePath() + File.separator + crschema.name + "-EstimacionBD.xls");
			// outFile.createNewFile();
			// HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(outFile.getAbsolutePath()));

			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet tablas = wb.createSheet("Volumen Estimado Tablas");

//			HSSFSheet indices = wb.createSheet("Volumen Estimado Indices");

			HSSFCellStyle boldSquared = wb.createCellStyle();
			boldSquared.setAlignment(CellStyle.ALIGN_CENTER);
			boldSquared.setBorderTop(CellStyle.BORDER_THIN);
			boldSquared.setBorderLeft(CellStyle.BORDER_THIN);
			boldSquared.setBorderRight(CellStyle.BORDER_THIN);
			boldSquared.setBorderBottom(CellStyle.BORDER_THIN);
			boldSquared.setWrapText(false);
			
			HSSFFont font = wb.createFont();
			font.setFontName("MS Sans Serif");
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			font.setColor(Font.COLOR_NORMAL);
			font.setFontHeightInPoints((short) 10);
			
			
			
			int i = 0;
			HSSFRow row = tablas.createRow(i++);
			int j = 0;
			HSSFCell cell = row.createCell(j++);
			cell.setAsActiveCell();
			
			cell = row.createCell(j++);
			cell = row.createCell(j++);
			
			cell = row.createCell(j++);
			cell.setCellStyle(boldSquared);
			HSSFRichTextString string = new HSSFRichTextString("Num de Filas");
			string.applyFont(font);
			cell.setCellValue(string);
			
			cell = row.createCell(j++);
			
			cell = row.createCell(j++);
			cell.setCellStyle(boldSquared);
			string = new HSSFRichTextString("Volumen Estimado (K)");
			string.applyFont(font);
			cell.setCellValue(string);
			
			
			cell = row.createCell(j++);
			tablas.addMergedRegion(new CellRangeAddress(0, 0, 3, 4));
			tablas.addMergedRegion(new CellRangeAddress(0, 0, 5, 6));

			row = tablas.createRow(i++);
			j = 0;
			cell = row.createCell(j++);
			cell.setCellStyle(boldSquared);
			string = new HSSFRichTextString("Tablas con tamano");
			string.applyFont(font);
			cell.setCellValue(string);
			
			cell = row.createCell(j++);
			cell.setCellStyle(boldSquared);
			string = new HSSFRichTextString("Descripcion");
			string.applyFont(font);
			cell.setCellValue(string);
			
			cell = row.createCell(j++);
			cell.setCellStyle(boldSquared);
			string = new HSSFRichTextString("Lg.");
			string.applyFont(font);
			cell.setCellValue(string);
			
			cell = row.createCell(j++);
			cell.setCellStyle(boldSquared);
			string = new HSSFRichTextString("Minimo");
			string.applyFont(font);
			cell.setCellValue(string);
			
			cell = row.createCell(j++);
			cell.setCellStyle(boldSquared);
			string = new HSSFRichTextString("Maximo");
			string.applyFont(font);
			cell.setCellValue(string);

			cell = row.createCell(j++);
			cell.setCellStyle(boldSquared);
			string = new HSSFRichTextString("Minimo");
			string.applyFont(font);
			cell.setCellValue(string);
			
			cell = row.createCell(j++);
			cell.setCellStyle(boldSquared);
			string = new HSSFRichTextString("Maximo");
			string.applyFont(font);
			cell.setCellValue(string);

			row = tablas.createRow(i++);
			j = 0;
			cell = row.createCell(j++);
			cell = row.createCell(j++);
			cell = row.createCell(j++);
			cell = row.createCell(j++);
			cell = row.createCell(j++);
			cell = row.createCell(j++);
			cell = row.createCell(j++);

			row = tablas.createRow(i++);
			j = 0;
			cell = row.createCell(j++);
			cell = row.createCell(j++);
			cell = row.createCell(j++);
			cell = row.createCell(j++);
			cell = row.createCell(j++);
			cell = row.createCell(j++);
			cell = row.createCell(j++);

			row = tablas.createRow(i++);
			j = 0;
			cell = row.createCell(j++);
			cell.setCellStyle(boldSquared);
			string = new HSSFRichTextString("Total tablas actuales");
			string.applyFont(font);
			cell.setCellValue(string);

			cell = row.createCell(j++);
			cell = row.createCell(j++);
			cell = row.createCell(j++);
			cell = row.createCell(j++);
			cell = row.createCell(j++);
			cell = row.createCell(j++);

			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(new File(outFile.getAbsolutePath()));
				wb.write(fos);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.flush();
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			// // tablas
			// List tableList = new ArrayList(crschema.tables.keySet());
			// Collections.sort(tableList);
			// indexOut.println(IReporter.TAB + "<h3>TABLES (" + tableList.size() + " tables)</h3><ul>");
			// // for (String tableName : tableList) {
			// Iterator it = tableList.iterator();
			// while (it.hasNext()) {
			// String tableName = (String) it.next();
			// crtable = (CrTable) crschema.tables.get(tableName);
			// indexOut.println(IReporter.TAB + "<li><a href='" + tableName + ".html'>" + crtable.fullName() +
			// "</a></li>");
			// // archivo html desc table
			// generateTable();
			// }
			//			
			// // vistas
			// List viewList = new ArrayList(crschema.views.keySet());
			// Collections.sort(viewList);
			// indexOut.println(IReporter.TAB + "</ul><hr/><h3>VIEWS (" + viewList.size() + " views)</h3><ul>");
			// // for (String viewName : viewList) {
			// it = viewList.iterator();
			// while (it.hasNext()) {
			// String viewName = (String) it.next();
			// crview = (CrView) crschema.views.get(viewName);
			// indexOut.println(IReporter.TAB + "<li><a href='Vw" + viewName + ".html'>" + crview.fullName() +
			// "</a></li>");
			// // archivo html desc view
			// generateView();
			// }
			// indexOut.println("</ul>");
			// footSchema();
		} catch (Exception e) {
			print(e.getMessage());
		}
	}

	// public void print(String string) {
	// System.out.println(string);
	// if (null != out) {
	// out.println("<p>" + string + "</p>");
	// }
	// }
	//
	// protected void headSchema(final Object obj) {
	// System.out.println(obj);
	// indexOut.println("<html><head>");
	// ExcelReporter.printInnerStyle(indexOut);
	// indexOut.println("</head>");
	// indexOut.println("<body><h1>" + schemaName.toUpperCase() + "</h1>");
	// }
	//
	// protected void footSchema() {
	// indexOut.println("</body></html>");
	// indexOut.close();
	// }
	//
	// // TABLES
	// private void generateTable() throws Exception {
	// System.out.println(crtable.name);
	//		
	// File outFile = new File(outputFile.getAbsolutePath() + File.separator + crtable.name + ".html");
	// // out = new PrintStream(outFile);
	// FileOutputStream fos = new FileOutputStream(outFile);
	// out = new PrintStream(fos);
	//		
	// // head con inner style
	// out.println("<html><head>");
	// ExcelReporter.printInnerStyle(out);
	// out.println("</head>");
	// // body con synonym
	// out.println("<body>");
	//		
	// StringBuffer sb = new StringBuffer("<h1>");
	// sb.append(crtable.name);
	// if (null != crtable.synonym && !"".equals(crtable.synonym)) {
	// sb.append(" - ").append(crtable.synonym);
	// } else {
	// sb.append(" - ").append("#WARNING#");
	// }
	// sb.append("</h1>");
	// out.println(sb.toString());
	//		
	// sb = new StringBuffer("<h2>");
	// sb.append("SIZE=").append(crtable.size).append(" Bytes</h2>");
	// out.println(sb.toString());
	//		
	// // table con synonym
	// // columnas
	// printCols();
	// // constraints
	// printCks();
	// // relations
	// printRels();
	// // references
	// printRefs();
	//		
	// out.println("</body></html>");
	// out.close();
	// }
	//	
	// private void printCols() {
	// out.println(IReporter.TAB + "<h3>COLUMNS</h3>");
	// out.println(IReporter.TAB +
	// "<table width='100%'><tr><td><b>ID</b></td><td><b>TYPE</b></td><td><b>ATTR</b></td><td><b>COMMENT</b></td></tr>");
	// List columnList = new ArrayList(crtable.columns.keySet());
	// Collections.sort(columnList);
	// StringBuffer sb;
	// // for (String columnName : columnList) {
	// Iterator it = columnList.iterator();
	// while (it.hasNext()) {
	// String columnName = (String) it.next();
	// CrColumn crcolumn = (CrColumn) crtable.columns.get(columnName);
	// // recalculo atributos columna
	// crcolumn.attrs = "";
	// if (crcolumn.pk) { crcolumn.attrs += "P"; }
	// if (crcolumn.fk) { crcolumn.attrs += "F"; }
	// if (crcolumn.ix) { crcolumn.attrs += "U"; }
	// if (crcolumn.nb) { crcolumn.attrs += "N"; }
	//			
	// sb = new StringBuffer("<tr>");
	// sb.append("<td>").append(crcolumn.name).append("</td>")
	// .append("<td>").append(crcolumn.type).append("</td>")
	// .append("<td>").append(crcolumn.attrs).append("</td>");
	//			
	// if (null != crcolumn.comment && !"".equals(crcolumn.comment)) {
	// sb.append("<td>").append(crcolumn.comment).append("</td>");
	// } else {
	// sb.append("<td>#WARNING#</td>");
	// }
	// sb.append("</tr>");
	// out.println(IReporter.TAB2 + sb.toString());
	// }
	// out.println(IReporter.TAB + "</table><hr/>");
	// }
	//	
	// private void printCks() {
	// if (null != crtable.cks && crtable.cks.size() > 0) {
	// out.println(IReporter.TAB + "<h3>CONSTRAINTS</h3>");
	// out.println(IReporter.TAB + "<table width='100%'><tr><td><b>ID</b></td><td><b>DEF</b></td></tr>");
	// StringBuffer sb;
	// // for (String ckname : crtable.cks.keySet()) {
	// Iterator it = crtable.cks.keySet().iterator();
	// while (it.hasNext()) {
	// String ckname = (String) it.next();
	// String ckdef = (String) crtable.cks.get(ckname);
	// sb = new StringBuffer("<tr>");
	// sb.append("<td>").append(ckname).append("</td>")
	// .append("<td>").append(ckdef).append("</td>")
	// .append("</tr>");
	// out.println(IReporter.TAB2 + sb.toString());
	// }
	// out.println(IReporter.TAB + "</table><hr/>");
	// }
	// }
	//	
	// private void printRels() {
	// if (null != crtable.relations && crtable.relations.size() > 0) {
	// out.println(IReporter.TAB + "<h3>RELATIONS</h3>");
	// out.println(IReporter.TAB + "<table width='100%'><tr><td><b>TABLE</b></td><td><b>COLUMN</b></td></tr>");
	// StringBuffer sb;
	// // for (String fkTableName : crtable.relations.keySet()) {
	// Iterator it = crtable.relations.keySet().iterator();
	// while (it.hasNext()) {
	// String fkTableName = (String) it.next();
	// sb = new StringBuffer("<tr>");
	// sb.append("<td><a href='").append(fkTableName).append(".html'>").append(fkTableName).append("</a></td>");
	// sb.append("<td>");
	// // for (String fkColumnName : crtable.relations.get(fkTableName)) {
	// List lstRelations = (List) crtable.relations.get(fkTableName);
	// it = lstRelations.iterator();
	// while (it.hasNext()) {
	// String fkColumnName = (String) it.next();
	// sb.append(fkColumnName);
	// }
	// sb.append("</td></tr>");
	// out.println(IReporter.TAB2 + sb.toString());
	// }
	// out.println(IReporter.TAB + "</table><hr/>");
	// }
	// }
	//	
	// private void printRefs() {
	// if (null != crtable.references && crtable.references.size() > 0) {
	// out.println(IReporter.TAB + "<h3>REFERENCES</h3>");
	// out.println(IReporter.TAB + "<table width='100%'><tr><td><b>TABLE</b></td><td><b>COLUMN</b></td></tr>");
	// StringBuffer sb;
	// // for (String rkTableName : crtable.references.keySet()) {
	// Iterator it = crtable.references.keySet().iterator();
	// while (it.hasNext()) {
	// String rkTableName = (String) it.next();
	// sb = new StringBuffer("<tr>");
	// sb.append("<td><a href='").append(rkTableName).append(".html'>").append(rkTableName).append("</a></td>");
	// sb.append("<td>");
	// // for (String rkColumnName : crtable.references.get(rkTableName)) {
	// List lstReferences = (List) crtable.references.get(rkTableName);
	// it = lstReferences.iterator();
	// while (it.hasNext()) {
	// String rkColumnName = (String) it.next();
	// sb.append(rkColumnName);
	// }
	// sb.append("</td></tr>");
	// out.println(IReporter.TAB2 + sb.toString());
	// }
	// out.println(IReporter.TAB + "</table><hr/>");
	// }
	// }
	//
	// // VIEWS
	// private void generateView() throws Exception {
	// System.out.println(crview.name);
	//
	// File outFile = new File(outputFile.getAbsolutePath() + File.separator + "Vw" + crview.name + ".html");
	// FileOutputStream fos = new FileOutputStream(outFile);
	// out = new PrintStream(fos);
	//
	// out.println("<html><head></head>");
	// out.println("<body><h1>" + crview.name + "</h1>");
	// if (null != crview.synonym) {
	// out.println(IReporter.TAB + "<h2>" + crview.synonym + "</h2>");
	// }
	// System.out.println(crview.def);
	// out.println(IReporter.TAB + "<p>" + crview.def + "</p>");
	//		
	// out.println("</body></html>");
	// out.close();
	// }
	//	
	// // CSS STYLE
	// public static void printInnerStyle(PrintStream outPr) {
	// outPr.println(IReporter.TAB + "<style type=\"text/css\">");
	// outPr.println(IReporter.TAB2 + "H1 {text-align: center; font-family: Verdana, Helvetica, Arial, sans-serif;}");
	// outPr.println(IReporter.TAB2 + "H2 {text-align: center; font-family: Verdana, Helvetica, Arial, sans-serif;}");
	// outPr.println(IReporter.TAB2 + "H3 {text-align: left; font-family: Verdana, Helvetica, Arial, sans-serif;}");
	// outPr.println(IReporter.TAB2 + "table {text-align: left; font-family: Verdana, Helvetica, Arial, sans-serif;}");
	// outPr.println(IReporter.TAB2 + "td {text-size: 10px;}");
	// outPr.println(IReporter.TAB2 + "a {text-align: left; font-family: Verdana, Helvetica, Arial, sans-serif;}");
	// outPr.println(IReporter.TAB + "</style>");
	// }

	//	
	// public static void main(String[] args) {
	// String fileName = "D:/tmp/literales.xls";
	// try {
	// HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(filename));
	//
	// System.out.println("Data dump:\n");
	//
	// for (int k = 0; k < wb.getNumberOfSheets(); k++) {
	// HSSFSheet sheet = wb.getSheetAt(k);
	// int rows = sheet.getPhysicalNumberOfRows();
	// System.out.println("Sheet " + k + " \"" + wb.getSheetName(k)
	// + "\" has " + rows + " row(s).");
	//				
	// FileOutputStream fileOutEs = new FileOutputStream("D:/tmp/" + wb.getSheetName(k) + "-es.properties");
	// String lineEs = "";
	// FileOutputStream fileOutEu = new FileOutputStream("D:/tmp/" + wb.getSheetName(k) + "-eu.properties");
	// String lineEu = "";
	//				
	// for (int r = 1; r < rows; r++) {
	// HSSFRow row = sheet.getRow(r);
	// if (row == null) {
	// continue;
	// }
	//					
	// int cells = row.getPhysicalNumberOfCells();
	// System.out.println("\nROW " + row.getRowNum() + " has "
	// + cells + " cell(s).");
	// for (int c = 0; c < cells; c++) {
	// HSSFCell cell = row.getCell(c);
	// if (cell == null) {
	// continue;
	// }
	// String value = null;
	//						
	// switch (cell.getCellType()) {
	//
	// case HSSFCell.CELL_TYPE_FORMULA:
	// value = "FORMULA value=" + cell.getCellFormula();
	// break;
	//
	// case HSSFCell.CELL_TYPE_NUMERIC:
	// value = "NUMERIC value="
	// + cell.getNumericCellValue();
	// break;
	//
	// case HSSFCell.CELL_TYPE_STRING:
	// value = "STRING value=" + cell.getStringCellValue();
	// break;
	//
	// default:
	// }
	// System.out.println("CELL col=" + cell.getColumnIndex()
	// + " VALUE=" + value);
	// }
	// // castellano
	// HSSFCell cell = row.getCell(0);
	// if (null != cell) {
	// String value = cell.getStringCellValue().trim();
	// if (null != value && !"".equals(value)) {
	// lineEs = value + " = ";
	// cell = row.getCell(2);
	// if (null != cell) {
	// value = cell.getStringCellValue().trim();
	// if (null != value && !"".equals(value)) {
	// lineEs += value + "\r\n";
	// } else {
	// cell = row.getCell(1);
	// if (null != cell) {
	// lineEs += cell.getStringCellValue().trim() + "\r\n";
	// }
	// }
	// }
	// }
	// }
	// fileOutEs.write(lineEs.getBytes());
	// // euskera
	// cell = row.getCell(0);
	// if (null != cell) {
	// String value = cell.getStringCellValue().trim();
	// if (null != value && !"".equals(value)) {
	// lineEu = value + " = ";
	// cell = row.getCell(4);
	// if (null != cell) {
	// value = cell.getStringCellValue().trim();
	// if (null != value && !"".equals(value)) {
	// lineEu += value + "\r\n";
	// } else {
	// cell = row.getCell(3);
	// if (null != cell) {
	// lineEu += cell.getStringCellValue().trim() + "\r\n";
	// }
	// }
	// }
	// }
	// }
	// fileOutEu.write(lineEu.getBytes());
	// }
	// fileOutEs.close();
	// fileOutEu.close();
	// }
	// } else if (args.length == 2) {
	// if (args[1].toLowerCase().equals("write")) {
	// System.out.println("Write mode");
	// long time = System.currentTimeMillis();
	// HSSFReadWrite.testCreateSampleSheet(fileName);
	//
	// System.out.println("" + (System.currentTimeMillis() - time)
	// + " ms generation time");
	// } else {
	// System.out.println("readwrite test");
	// HSSFWorkbook wb = HSSFReadWrite.readFile(fileName);
	// FileOutputStream stream = new FileOutputStream(args[1]);
	//
	// wb.write(stream);
	// stream.close();
	// }
	// } else if (args.length == 3 &&
	// args[2].toLowerCase().equals("modify1")) {
	// // delete row 0-24, row 74 - 99 && change cell 3 on row 39 to
	// string "MODIFIED CELL!!"
	//
	// HSSFWorkbook wb = HSSFReadWrite.readFile(fileName);
	// FileOutputStream stream = new FileOutputStream(args[1]);
	// HSSFSheet sheet = wb.getSheetAt(0);
	//
	// for (int k = 0; k < 25; k++) {
	// HSSFRow row = sheet.getRow(k);
	//
	// sheet.removeRow(row);
	// }
	// for (int k = 74; k < 100; k++) {
	// HSSFRow row = sheet.getRow(k);
	//
	// sheet.removeRow(row);
	// }
	// HSSFRow row = sheet.getRow(39);
	// HSSFCell cell = row.getCell(3);
	// cell.setCellValue("MODIFIED CELL!!!!!");
	//
	// wb.write(stream);
	// stream.close();
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
}
