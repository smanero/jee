package org.pcdm.analizador.processor.wl81.module;

import java.io.File;

import org.pcdm.analizador.context.R02xContext;
import org.pcdm.analizador.processor.R02xAbstractAnalyser;


public class R02xModuleWalkerAnalyser extends R02xAbstractAnalyser {
	public static final String REGEX = ".*";

	public R02xModuleWalkerAnalyser(final R02xContext ctx) {
		super(ctx);
	}

	public void analyse() throws Exception {
		ctx.currentModule().reportIni(ctx.getProps().reportStats());
		// procesar clases
		File srcPath = new File(ctx.currentModule().getAbsolutePath() + File.separator + "src");
		walk(srcPath);
		// report module analisis finalizado
		ctx.currentModule().reportEnd(ctx.getProps().reportStats());
	}
}
