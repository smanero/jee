package org.pcdm.analizador.context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.pcdm.analizador.context.node.R02xFile;
import org.pcdm.analizador.context.node.R02xLote;
import org.pcdm.analizador.context.node.R02xModule;
import org.pcdm.analizador.context.node.R02xProfile;
import org.pcdm.analizador.reporter.R02xReporter;
import org.pcdm.util.R02xUtil;


public class Context {
	// propiedades del analisis
	private final R02xProperties props;
	// reporter del analisis
	private final R02xReporter reporter;

	// lote de profiles a tratar
	private final R02xLote lote;
	// cursor de profile � lote en analisis
	private R02xProfile profile;
	// cursor del module � profile en analisis
	private R02xModule module;
	// cursor del file � module en analisis
	private R02xFile file;
	
	/**
	 * Constructor por parametros.
	 * @param lote
	 * @param outputDir
	 * @param props
	 * @throws Exception
	 */
	public Context(final R02xLote lote, final R02xProperties props) 
			throws Exception {
		// lote
		this.lote = lote;
		// propiedades
		this.props = props;
		// directorio de salida analisis
		if (props.getOutputDir().exists()) {
			FileUtils.deleteDirectory(props.getOutputDir());
		}
		props.getOutputDir().mkdir();
		
		if (null != props.getMigrDir()) {
			// directorio de salida migracion
			if (props.getMigrResetDir() && props.getMigrDir().exists()) {
				FileUtils.deleteDirectory(props.getMigrDir());
			}
			props.getMigrDir().mkdir();
			// profile de migracion
			migrProfile = new R02xProfile(null, props.getMigrDir(), "");
		}
		// reporter
		this.reporter = new R02xReporter(props.getOutputDir(), props.reportCsv());
	}

	public R02xLote currentLote() {
		return this.lote;
	}

	public R02xProperties getProps() {
		return this.props;
	}

	public R02xReporter getReporter() {
		return this.reporter;
	}

	public File getOutputDir() {
		return this.props.getOutputDir();
	}
	public File getMigrDir() {
		return this.props.getMigrDir();
	}
	public String getMigrAppCode() {
		return this.props.getMigrAppCode();
   }
	public String getMigrFilePrefix() {
	   return R02xUtil.capitalLetter(this.props.getMigrAppCode());
   }

	public void currentProfile(final R02xProfile profile) throws Exception {
		this.profile = profile;
		if (null != profile) {
			this.reporter.setProfileReport(profile.getAppCode());
		}
	}

	public R02xProfile currentProfile() {
		return this.profile;
	}

	public void currentModule(final R02xModule module) {
		this.module = module;
	}

	public R02xModule currentModule() {
		return this.module;
	}

	public void currentFile(final R02xFile file) {
		this.file = file;
	}

	public R02xFile currentFile() {
		return this.file;
	}
	
	// cursor del module � profile producto de la migracion
	
	private R02xProfile migrProfile;
	private R02xModule migrModule;
	private List<String> packages = new ArrayList<String>(4);
	public void push(String pkg) {
		packages.add(0, pkg);
	}
	public void pop() {
		packages.remove(0);
	}
	public String top() {
		return packages.get(0);
	}
	
	public void migrProfile(final R02xProfile profile) {
		this.migrProfile = profile;
	}
	public R02xProfile migrProfile() {
		return this.migrProfile;
	}
	
	public void migrModule(final R02xModule module) {
		this.migrModule = module;
	}
	public R02xModule migrModule() {
		return this.migrModule;
	}
}
