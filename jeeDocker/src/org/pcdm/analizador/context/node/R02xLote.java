package org.pcdm.analizador.context.node;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class R02xLote extends R02xNode {

	public R02xLote(final File path, final List<String> appCodes) throws Exception {
		super(null, path, R02xNodeType.LOTE, 0);
		// profiles presentes en el lote
		for (String appCode : appCodes) {
			// directorio de la aplicacion/profile
			File appPath = new File(path.getAbsolutePath() + File.separator + appCode);
			if (appPath.exists()) {
				files.add(appPath);
			}
		}
	}

	/**
	 * Llamar una vez finalizado el analisis del lote
	 * @param reportCsvParcial
	 * @return
	 */
	public List<String> reportCsv(boolean reportCsvParcial) {
		long loteNumMetodos = 0;
		List<String> msgCsv = new ArrayList<String>(1);
		msgCsv.add("APLICACION;PROYECTO;FICHERO;TIPO;NUMERO;FASE1;FASE2;TEST");
		StringBuffer sbCsv = null;
		R02xProfile profile = null;
		for (R02xNode nodeProfile : getChilds()) {
			// profile
			profile = (R02xProfile) nodeProfile;
			if (profile.isCovered()) {
				// para cada module � profile
				long profileNumMetodos = 0;
				R02xModule module = null;
				for (R02xNode nodeModule : profile.getChilds()) {
					// module
					module = (R02xModule) nodeModule;
					if (module.isCovered()) {
						// para cada file � module
						long moduleNumMetodos = 0;
						R02xFile file = null;
						for (R02xNode nodeFile : module.getChilds()) {
							// file
							file = (R02xFile) nodeFile;
							if (file.isCovered()) {
								long numCounters = file.getCounters().values().size();
								if (numCounters == 1) {
									String[] nameExt = file.getNameExt();
									// formato linea
									sbCsv = new StringBuffer();
									sbCsv.append(profile.getFileName()).append(";").append(module.getFileName()).append(";")
									      .append(nameExt[0]).append(";").append(nameExt[1]).append(";");
									for (Long value : file.getCounters().values()) {
										loteNumMetodos += value;
										profileNumMetodos += value;
										moduleNumMetodos += value;
										sbCsv.append(value).append(";");
									}
									sbCsv.append("No").append(";").append("No").append(";").append("No").append(";");
									// linea en el csv
									msgCsv.add(sbCsv.toString());
								}
							}
						} // end for (R02xNode nodeFile : module.getChilds()) {
						if (reportCsvParcial && moduleNumMetodos > 0) {
							msgCsv.add(";MODULE;;TOTAL;" + moduleNumMetodos + ";;;");
						}
					}
				} // end for (R02xNode nodeModule : profile.getChilds()) {
				if (reportCsvParcial && profileNumMetodos > 0) {
					msgCsv.add("APLICACION;;;TOTAL;" + profileNumMetodos + ";;;");
					msgCsv.add(";;;;;;;");
				}
			}
		} // end for (R02xNode nodeProfile : getChilds()) {
		if (reportCsvParcial && loteNumMetodos > 0) {
			msgCsv.add(";;;TOTAL;" + loteNumMetodos + ";;;");
		}
		return msgCsv;
	}
}
