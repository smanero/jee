package org.pcdm.analizador.context.node;

import java.io.File;

/**
 * Profile de una aplicacion WLS8.
 * @author BIMALOSE
 */
public class R02xWls8Profile extends R02xProfile {
	public static final String APP_TYPE = "WLS8";
	public static final String APP_REGEX = "r02[a-z].*|r01i.*";

	/**
	 * Constructor con chequeos.
	 * @param basePath path absoluto de la aplicacion.
	 * @param appCode codigo de la aplicacion.
	 * @param appDesc descripcion funcional de la aplicacion.
	 * @throws Exception en caso de error.
	 */
	public R02xWls8Profile(final R02xLote lote, final File profilePath, final String appDesc) throws Exception {
		super(lote, profilePath, appDesc);
		// estructura de la aplicacion
		setAppDir(profilePath, appCode);
		discoverModules(appCode + ".*");
	}

	/**
	 * Establece los directorios basicos de una aplicacion WLS8.
	 * @param basePath path absoluto de la aplicacion.
	 * @param appCode codigo de la aplicacion.
	 * @throws Exception en caso de error.
	 */
	public void setAppDir(final File profilePath, final String appCode) throws Exception {
		super.setAppDir(profilePath);
		// profile app bea weblogic server
		String appPath = appDir.getAbsolutePath();
		this.configDir = new File(appPath + "/" + appCode + "Classes/" + appCode);
		this.htmlDir = new File(appPath + "/" + appCode + "Classes/html/datos/");
		this.scriptsDir = new File(appPath + "/scripts/");
		this.modDir = new File(appPath);
		this.libDir = new File(appPath + "/" + appCode + "Classes/lib/");

		this.appXml = new File(appPath + "/" + appCode + "EAR/META-INF/application.xml");
		this.projectXml = new File(appPath + "/" + appCode + "EAR/project.xml");
		this.configPrp = new File(configDir.getAbsolutePath() + "/" + appCode + ".properties");
	}
}
