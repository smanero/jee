package org.pcdm.analizador.processor.wl81.module;

import java.io.File;

import org.pcdm.analizador.context.R02xContext;
import org.pcdm.analizador.context.node.R02xFile;
import org.pcdm.analizador.processor.R02xAbstractAnalyser;
import org.pcdm.analizador.processor.R02xIAnalyser;
import org.pcdm.analizador.processor.wl81.file.R02xConfigPrpAnalyser;


public class R02xClassesAnalyser extends R02xAbstractAnalyser {
	public static final String REGEX = ".*Classes";

	public R02xClassesAnalyser(final R02xContext ctx) {
		super(ctx);
	}

	public void analyse() throws Exception {
		// procesar *.properties
		if (null != ctx.currentProfile().getConfigPrp() && this.ctx.currentProfile().getConfigPrp().exists()) {
			try {
				R02xFile file = new R02xFile(ctx.currentModule(), ctx.currentProfile().getConfigPrp());
				// report file analisis iniciado
				file.reportIni(ctx.getProps().reportStats());
				ctx.currentFile(file);
				R02xIAnalyser analyser = new R02xConfigPrpAnalyser(ctx);
				analyser.analyse();
				ctx.currentFile(null);
				// report file analisis finalizado
				file.reportEnd(ctx.getProps().reportStats());
			} catch (Exception e) {
				ctx.currentFile().report("ERROR " + e.getMessage());
				e.printStackTrace();
			}
		}
		// procesar clases base *.java
		File srcPath = new File(ctx.currentModule().getAbsolutePath() + File.separator + "src");
		walk(srcPath);
	}
}
