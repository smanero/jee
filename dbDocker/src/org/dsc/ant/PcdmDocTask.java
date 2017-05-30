package org.dsc.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.dsc.DbDocker;


/**
 * Tarea ant para la generacion documentacion capa base datos
 * @usage 
 * <taskdef name="platea-doc-db" classname="com.ejie.ant.OracleSchemaDocTask" classpathref="platea.ant.path" />
 * <platea-doc-db schemaName="r02d" 
 * 	jdbcDriver="oracle.jdbc.OracleDriver" 
 * 	connectionChain="jdbc:oracle:thin:@ejhp67:1524:ede2" 
 * 	genFlag="H" verbose="true" outputDir="/tmp" >
 */
public class PcdmDocTask extends Task implements org.apache.tools.ant.TypeAdapter {
	private static final String CONNECTION_CHAIN = "jdbc:oracle:thin:@ejhp67:1524:ede2";
	private static final String JDBC_DRIVER = "oracle.jdbc.OracleDriver";

	private String schemaName;
	private String jdbcDriver = JDBC_DRIVER;
	private String connectionChain = CONNECTION_CHAIN;

	private String genFlag = "H";
	private boolean verbose = false;

	private String outputDir;

	// method executing the task
	public void execute() throws BuildException {
		try {
			new DbDocker(schemaName, jdbcDriver, connectionChain, genFlag, verbose, outputDir);
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}
	
   public void setSchemaName(String schemaName) {
      this.schemaName = schemaName;
   }
   public void setJdbcDriver(String jdbcDriver) {
      this.jdbcDriver = jdbcDriver;
   }
   public void setConnectionChain(String connectionChain) {
      this.connectionChain = connectionChain;
   }
   public void setGenFlag(String genFlag) {
      this.genFlag = genFlag;
   }
   public void setVerbose(boolean verbose) {
      this.verbose = verbose;
   }
   public void setOutputDir(String outputDir) {
      this.outputDir = outputDir;
   }

	public void checkProxyClass(Class arg0) {
	   // TODO Auto-generated method stub
	   
   }

	public Object getProxy() {
	   // TODO Auto-generated method stub
	   return null;
   }

	public void setProxy(Object arg0) {
	   // TODO Auto-generated method stub
	   
   }   
}
