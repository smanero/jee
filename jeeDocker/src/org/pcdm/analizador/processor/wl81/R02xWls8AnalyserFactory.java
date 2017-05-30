package org.pcdm.analizador.processor.wl81;

import org.pcdm.analizador.context.R02xContext;
import org.pcdm.analizador.context.node.R02xFile;
import org.pcdm.analizador.context.node.R02xModule;
import org.pcdm.analizador.context.node.R02xNode;
import org.pcdm.analizador.processor.R02xIAnalyser;
import org.pcdm.analizador.processor.wl81.file.R02xConfigPrpAnalyser;
import org.pcdm.analizador.processor.wl81.file.R02xJavaAnalyser;
import org.pcdm.analizador.processor.wl81.module.R02xEarAnalyser;
import org.pcdm.analizador.processor.wl81.module.R02xModuleWalkerAnalyser;

public class R02xWls8AnalyserFactory {

	public static R02xIAnalyser get(final R02xNode node, final R02xContext ctx) {
		R02xIAnalyser analyser = null;

		if (node instanceof R02xModule) {
			// analizar modulo
			analyser = getModule((R02xModule) node, ctx);

		} else if (node instanceof R02xFile) {
			// analizar fichero process
			analyser = getFile((R02xFile) node, ctx);

		}
		return analyser;
	}

	private static R02xIAnalyser getModule(final R02xNode module, final R02xContext ctx) {
		R02xIAnalyser analyser = null;
		if (module.getFileName().matches(R02xEarAnalyser.REGEX)) {
			// analizar modulo ear
			analyser = new R02xEarAnalyser(ctx);

		} else {
			// analizar modulo classes
			analyser = new R02xModuleWalkerAnalyser(ctx);
		}

		// } else if (module.getName().matches(R02xClassesAnalyser.REGEX)) {
		// // analizar modulo classes
		// analyser = new R02xClassesAnalyser(ctx);
		// } else if (module.getName().matches(R02xEjbAnalyser.REGEX)) {
		// // analizar modulo ejb
		// analyser = new R02xEjbAnalyser(ctx);
		//
		// } else if (module.getName().matches(R02xTestingWarAnalyser.REGEX)) {
		// // analizar modulo TestingWar
		// analyser = new R02xTestingWarAnalyser(ctx);
		//
		// } else if (module.getName().matches(R02xWarAnalyser.REGEX)) {
		// // analizar modulo war
		// analyser = new R02xWarAnalyser(ctx);
		//
		// } else if (module.getName().matches(R02xWsAnalyser.REGEX)) {
		// // analizar modulo webservice
		// analyser = new R02xWsAnalyser(ctx);
		// }
		return analyser;
	}

	private static R02xIAnalyser getFile(final R02xNode file, final R02xContext ctx) {
		R02xIAnalyser analyser = null;
		if (file.getFileName().matches(R02xConfigPrpAnalyser.REGEX)) {
			// analizar fichero properties
			analyser = new R02xConfigPrpAnalyser(ctx);

//		} else if (file.getFileName().matches(R02xJavaConstAnalyser.REGEX)) {
//			// analizar fichero properties
//			analyser = new R02xJavaConstAnalyser(ctx);
			
		} else if (file.getFileName().matches(R02xJavaAnalyser.REGEX)) {
			// analizar fichero properties
			analyser = new R02xJavaAnalyser(ctx);
		}
		
		return analyser;
	}
}
