package org.pcdm.analizador.context.node;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.pcdm.util.R02xUtil;


public class R02xModule extends R02xNode {

	private static final Map<String, String> traductor = new HashMap<String, String>();
	static {
		traductor.put("commonutilities", "Common");
		traductor.put("horizontalsystemofnotification", "ShnEsc");
		traductor.put("horizontalsystemofsignature", "Shf");
		traductor.put("ifz", "Ids");
		traductor.put("registry", "AutoReg");
		traductor.put("autoreg", "AutoReg");
		traductor.put("servicesifz", "Ids");
		traductor.put("telematicnotification", "Snt");
		traductor.put("transactionclient", "Ct");
	};
	
	protected String functionalName;
	protected String moduleName;
	protected File srcDir;
	
	public R02xModule(final R02xProfile profile, final File path) {
		super(profile, path, R02xNodeType.MODULE, 2);
		
		// nombre del modulo funcional
		moduleName = getFileName();
		functionalName = moduleName.replaceAll(profile.getAppCode(), "")
			.replaceAll("[Pp]rocess", "").replaceAll("[Ss]ervices", "")
			.replaceAll("[Ee]jb", "").replaceAll("[Ww]ar", "")
			.toLowerCase();
		// traduccion nombre funcional
		if (null != R02xModule.traductor.get(functionalName)) {
			functionalName = R02xModule.traductor.get(functionalName);
		} else {
			functionalName = R02xUtil.capitalLetter(functionalName);
		}
		// caso particular escenarios
		if (functionalName.startsWith("q99d")) {
			functionalName = "Esc" + functionalName;
		} else if (functionalName.startsWith("q99m")) {
			functionalName = "Esc" + functionalName;
		} else if (functionalName.startsWith("q99o")) {
			functionalName = "Esc" + functionalName;
		}
		
		srcDir = new File(getFile().getAbsolutePath() + File.separator + "src");
	}
	
	public String getFunctionalName() {
		return functionalName;	
	}
	public String getModuleName() {
		return moduleName;	
	}
	public File srcDir() {
		return srcDir;	
	}
}
