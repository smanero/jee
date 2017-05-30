package org.dsc;

import org.dsc.db.OracleSchemaRipper;
import org.dsc.db.model.CrSchema;
import org.dsc.db.reporter.DotReporter;
import org.dsc.db.reporter.ExcelReporter;
import org.dsc.db.reporter.HtmlReporter;
import org.dsc.db.reporter.XmlReporter;

/**
 * select sum(bytes)/1024/1024 "Real Meg" from user_segments where segment_name in (
   select SEGMENT_NAME from user_lobs where TABLE_NAME in ('R02G01T00', 'R02G04T00', 'R02G06T00'));
 * @author bimalose
 */
public final class DbDocker {
	
	public DbDocker(final String schemaName, final String jdbcDriver, final String connectionChain, 
			String genFlag,  boolean verbose, String outputDir) throws Exception {
		if (null == schemaName || "".equals(schemaName)) throw new Exception("schemaName " + schemaName + " no definido correctamente");
		if (null == jdbcDriver || "".equals(jdbcDriver)) throw new Exception("jdbcDriver " + jdbcDriver + " no definido correctamente");
		if (null == connectionChain || "".equals(connectionChain)) throw new Exception("connectionChain " + connectionChain + " no definido correctamente");
		if (null == outputDir || "".equals(outputDir)) throw new Exception("outputDir " + outputDir + " no definido correctamente");
		if (!outputDir.endsWith("/")) outputDir += "/";
		
		// ripear schema de bbdd
		OracleSchemaRipper db = new OracleSchemaRipper(schemaName, jdbcDriver, connectionChain, verbose);
		CrSchema crschema = db.rip();
		// report de E/R
		if (-1 != genFlag.indexOf("H")) {
			HtmlReporter htmlReporter = new HtmlReporter(outputDir, schemaName);
			htmlReporter.generate(crschema);
			
			ExcelReporter xlsReporter = new ExcelReporter(outputDir, schemaName);
			xlsReporter.generate(crschema);
		}
		if (-1 != genFlag.indexOf("X")) {
			XmlReporter xmlReporter = new XmlReporter(outputDir, schemaName);
			xmlReporter.generate(crschema);
		}
		if (-1 != genFlag.indexOf("D")) {
			DotReporter dotReporter = new DotReporter(outputDir, schemaName);
			dotReporter.generate(crschema);
		}
		// TODO chequear reglas de normativa
	}
	
	public static void main(final String[] args) throws Exception {
		int i = 0;
		String schemaName = "";
		String jdbcDriver = "";
		String connectionChain = "";
		String genFlag = "H";
		boolean verbose = false;
		String outputDir = "";
		
		if (args.length > i) schemaName = args[i++];
		if (args.length > i) jdbcDriver = args[i++];
		if (args.length > i) connectionChain = args[i++]; 
		if (args.length > i) genFlag = args[i++];
		if (args.length > i) verbose = "true".equals(args[i++]);
		if (args.length > i) outputDir = args[i++];
		
		new DbDocker(schemaName, jdbcDriver, connectionChain, genFlag, 
				verbose, outputDir);
	}
}
