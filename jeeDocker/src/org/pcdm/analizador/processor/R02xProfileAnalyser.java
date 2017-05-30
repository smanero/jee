package org.pcdm.analizador.processor;

import java.io.File;

import org.pcdm.analizador.context.R02xContext;
import org.pcdm.analizador.context.node.R02xModule;
import org.pcdm.jee.migrator.wli.WLIMigrator;

/**
 * @author Ibermatica
 */
public class R02xProfileAnalyser extends R02xAbstractAnalyser {

	public R02xProfileAnalyser(final R02xContext ctx) throws Exception {
		super(ctx);
	}

	public void analyse() throws Exception {
		// report profile analisis iniciado
		ctx.currentProfile().reportIni(ctx.getProps().reportStats());
		// analisis cada module � profile
		for (File file : ctx.currentProfile().getFiles()) {
			// nuevo module segun aplicacion
			R02xModule module = new R02xModule(ctx.currentProfile(), file);
			R02xIAnalyser analyser = R02xAnalyserFactory.get(module, ctx);
			if (null != analyser) {
				try {
					ctx.currentProfile().addChild(module);
					// analisis
					ctx.currentModule(module);
					analyser.analyse();
					ctx.currentModule(null);
				} catch (Exception e) {
					module.report("ERROR " + e.getMessage());
					e.printStackTrace();
				}
			} else {
				if (ctx.getProps().reportNotAnalysed()) {
					ctx.currentProfile().report(module.getFileName() + " no analizado --NOANALYSED.");
				}
			}
		}
		// report profile analisis finalizado
		ctx.currentProfile().reportEnd(ctx.getProps().reportStats());
		// report mensajes de profile
		ctx.getReporter().profile(ctx.currentProfile().getMessages());

		// comenzar migracion
		if (null != ctx.getProps().getMigrDir()) {
		   WLIMigrator migrator = new WLIMigrator(ctx);
			migrator.doIt();
		}
	}
}
