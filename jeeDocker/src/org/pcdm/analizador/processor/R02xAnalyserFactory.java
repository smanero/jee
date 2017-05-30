package org.pcdm.analizador.processor;

import org.pcdm.analizador.context.R02xContext;
import org.pcdm.analizador.context.node.R02xNode;
import org.pcdm.analizador.context.node.R02xWls8Profile;
import org.pcdm.analizador.processor.wl81.R02xWls8AnalyserFactory;

public class R02xAnalyserFactory {

	public static R02xIAnalyser get(final R02xNode module, final R02xContext ctx) {
		R02xIAnalyser analyser = null;
		if (ctx.currentProfile() instanceof R02xWls8Profile) {
			analyser = R02xWls8AnalyserFactory.get(module, ctx);

//		} else if (ctx.currentProfile() instanceof R02xWliProfile) {
//			analyser = R02xWliAnalyserFactory.get(module, ctx);
		}
		return analyser;
	}
}
