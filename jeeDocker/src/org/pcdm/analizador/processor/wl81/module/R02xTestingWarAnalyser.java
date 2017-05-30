package org.pcdm.analizador.processor.wl81.module;

import java.io.File;

import org.pcdm.analizador.context.R02xContext;
import org.pcdm.analizador.processor.R02xAbstractAnalyser;


public class R02xTestingWarAnalyser extends R02xAbstractAnalyser {
	public static final String REGEX = ".*TestingWar";

	public R02xTestingWarAnalyser(final R02xContext ctx) {
		super(ctx);
	}

	public void analyse() throws Exception {
		File srcPath = new File(ctx.currentModule().getAbsolutePath() + File.separator + "src");
		walk(srcPath);
	}
}
