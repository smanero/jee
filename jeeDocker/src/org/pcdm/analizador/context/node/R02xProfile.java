package org.pcdm.analizador.context.node;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Profile: Informacion asociada a una aplicacion Ejie - Profile = Properties + Lib + U Modules - Module = U Classes -
 * Class = U Deps + U Methods Tipos de aplicaciones soportadas: - WLS8 - WLI En desarrollo tipo de aplicacion WLS11
 * @author Ibermatica
 */
public class R02xProfile extends R02xNode {
	/** app code */
	protected String appCode;
	/** app desc */
	protected String appDesc;

	/** app base dir */
	protected File appDir;

	/** application.xml ear dir */
	protected File appXml;

	/** config base dir */
	protected File configDir;

	/** config file */
	protected File configPrp;

	/** scripts html datos dir */
	protected File htmlDir;

	/** scripts base dir */
	protected File scriptsDir;

	/** modules base dir */
	protected File modDir;

	/** lib base dir */
	protected File libDir;

	/** maven project.xml file */
	protected File projectXml;

	/** lista de librerias */
	protected Map<String, File> libs = new HashMap<String, File>(0);

	public R02xProfile(final R02xLote lote, final File path, final String appDesc) throws Exception {
		super(lote, path, R02xNodeType.PROFILE, 1);
		this.appCode = path.getName().toLowerCase();
		this.appDesc = appDesc;
	}

	public String getAppCode() {
		return appCode;
	}

	public String getAppDesc() {
		return appDesc;
	}

	public void setAppDir(final File profilePath) throws Exception {
		// directorio base de la aplicacion
		this.appDir = profilePath;
	}

	public File getAppDir() {
		return appDir;
	}

	public File getAppXml() {
		return appXml;
	}

	public File getConfigDir() {
		return configDir;
	}

	public File getHtmlDir() {
		return htmlDir;
	}

	public File getScriptsDir() {
		return scriptsDir;
	}

	public File getModDir() {
		return modDir;
	}

	public File getLibDir() {
		return libDir;
	}

	public File getConfigPrp() {
		return configPrp;
	}

	public File getProjectXml() {
		return projectXml;
	}

	/**
	 * A partir del directorio de modulos se listan los directorios que componen la aplicacion.
	 * @throws Exception en caso de error.
	 */
	protected void discoverModules(final String regex) throws Exception {
		if (modDir.exists() && modDir.isDirectory()) {
			String[] ficheros = modDir.list();
			if (null != ficheros) {
				for (int x = 0; x < ficheros.length; x++) {
					File modulePath = new File(modDir.getAbsolutePath() + "/" + ficheros[x]);
					if (ficheros[x].matches(regex) && modulePath.exists() && modulePath.isDirectory()) {
						files.add(modulePath);
					}
				}
			}
		} else {
			throw new Exception(modDir.getAbsolutePath() + " no existe o no es un directorio.");
		}
	}
}
