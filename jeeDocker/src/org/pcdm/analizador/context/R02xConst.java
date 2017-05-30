package org.pcdm.analizador.context;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public abstract class R02xConst {
	// nombre del charset UTF-8
	public static final String UTF8 = "UTF-8";
	public static final String MCHARSET = System.getProperty("file.encoding", UTF8);

	// regex archivos a no incluir en el analisis / migracion
	public static final String FILES_TO_IGNORE = ".*svn.*";
	// |.*[Tt]est.*|.*[Dd]ummy.*

	public static final String RESOURCES = "resources";
	public static final String AUTOBUILD = RESOURCES + "/autobuild";
	public static final String VELOCITY = RESOURCES + "/velocity";
	
	public static final String ARQUETIPO_WSGEN = RESOURCES + "/wsgen/arquetipo";
	public static final String PROPS_WSGEN = RESOURCES + "/wsgen/props";
	
	public static final String ARQUETIPO_EJB = RESOURCES + "/arquetipo/myeclipseEJB";
	public static final String ARQUETIPO_WAR = RESOURCES + "/arquetipo/myeclipseWAR";
	// templates velocity para generacion de archivos
	public static final String TPLT_ECLIPSE_PROJECT = "/project.vm";
//	public static final String TPLT_ECLIPSE_MYMETADATA =  "/mymetadata.vm";
	public static final String TPLT_X43WS_WSDL =  VELOCITY + "/x43ws.wsdl.vm";
	public static final String TPLT_CLASS_JAVA =  VELOCITY + "/class-code-java.vm";
	public static final String TPLT_TEST_WS_JMX =  VELOCITY + "/jmeter-jmx-jws.vm";
	public static final String TPLT_EXPO_OSB_DOC =  VELOCITY + "/Solicitud_Exposicion_servicio_ERPI.vm";
	
	
	// xslt
	public static final File XSLT_JPD2EJB = new File("resources/jpd2bp.xslt");

	public static double NANOSEGS = 1000;
	public static double MSEGS = 1000 * NANOSEGS;
	public static double SEGS = 1000 * MSEGS;
	public static double MINS = 60 * SEGS;
	public static double HOURS = 60 * MINS;
	public static double DAYS = 24 * HOURS;

	public static File getResourceFile(final String fileName) throws Exception {
		File file = new File(RESOURCES + File.separator + fileName);
		if (!file.exists()) {
			throw new Exception("Resource " + fileName + " no existe");
		}
		return file;
	}
	
	private static final Set<String> filter = new HashSet<String>();
	static {
		filter.add("checkstyle");
		filter.add("myeclipse");
		filter.add("settings");
		filter.add("classpath");
		filter.add("pmd");
		filter.add("xdoclet");
	};
	public static void copyDirectory(final File src, final File dst) throws Exception {
		File[] files = src.listFiles();
		if (null != files) {
			for (File nextSrc : files) {
				String fileName = nextSrc.getName();
				if (filter.contains(fileName)) {
					fileName = "." + fileName;
				}
				if (nextSrc.isDirectory()) {
					// recursividad copiado en directorios
					if (!fileName.matches(".*[.]svn")) {
						// filtrado de .svn
						// construccion dir destino
						File nextDst = new File(dst.getAbsolutePath() + File.separator + fileName);
						if (!nextDst.exists()) {
							nextDst.mkdir();
						}
						copyDirectory(nextSrc, nextDst);
					}
				} else {
					if (!fileName.matches(".*[.]vm")) {
						// copia de ficheros
						FileUtils.copyFile(nextSrc, new File(dst.getAbsolutePath() + File.separator + fileName));
					}
				}
			}
		}
	}
	
	public static void copyFile(final File src, final File dst) throws Exception {
		FileUtils.copyFile(src, dst);
	}
}
