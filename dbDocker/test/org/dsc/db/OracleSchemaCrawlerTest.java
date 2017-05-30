package org.dsc.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import org.dsc.DbDocker;
import org.dsc.db.reporter.HtmlReporter;
import org.dsc.db.reporter.IReporter;

/**
 * 
 * @author SMANEROL
 * 
 */
public final class OracleSchemaCrawlerTest {
	private static final String OUTPUT_FOLDER = "D:/tmp/docu-db-schema/";
	private static final String CONNECTION_CHAIN = "jdbc:oracle:thin:@ejhp67:1524:ede50";
	private static final String JDBC_DRIVER = "oracle.jdbc.OracleDriver";

	private static final List<String> lstSchemas = Arrays.asList("x53js","x53je"
	// "r01i", "r02d", "r02e", "r02g", "r02h",
	// "r02i", "r02l", "r02m", "r02n", "r02o",
	// "r02oc", "r02q", "r02r", "r02s", "r02t",
	// "r02v", "r02w", "r02y"
	      );

	public static void main(final String[] args) throws Exception {

		long ini = new GregorianCalendar().getTimeInMillis();

		File dir = new File(OUTPUT_FOLDER);
		dir.mkdirs();
		File outFile = new File(dir.getAbsolutePath() + File.separator + "index.html");
		FileOutputStream fos = new FileOutputStream(outFile);
		PrintStream index = new PrintStream(fos);
		index.println("<html><head>");
		HtmlReporter.printInnerStyle(index);
		index.println("</head>");
		index.println("<body><h1>Listado de schemas</h1>");
		for (String appCode : lstSchemas) {
			new DbDocker(appCode, JDBC_DRIVER, CONNECTION_CHAIN, "H", true, OUTPUT_FOLDER);

			index.println();
			index.println(IReporter.TAB + "<li><a href='./" + appCode + "/index.html'>" + appCode + "</a></li>");
		}
		index.println("</body></html>");
		index.close();
		fos.close();

		System.out.println("Duration: " + ((new GregorianCalendar().getTimeInMillis() - ini) / 1000) + " segs.");
	}
}
