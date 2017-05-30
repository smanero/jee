package org.pcdm.analizador.processor;

import java.io.File;

import org.pcdm.analizador.context.R02xConst;
import org.pcdm.analizador.context.R02xContext;
import org.pcdm.analizador.context.node.R02xFile;


/**
 * @author Ibermatica
 */
public abstract class R02xAbstractAnalyser implements R02xIAnalyser {

	protected R02xContext ctx;

	public R02xAbstractAnalyser(final R02xContext ctx) {
		this.ctx = ctx;
	}

	protected void walk(final File curFile) throws Exception {
		if (curFile.exists()) {
			for (File nextFile : curFile.listFiles()) {
				if (nextFile.isDirectory() && !nextFile.getName().matches(R02xConst.FILES_TO_IGNORE)) {
					walk(nextFile);
				} else if (nextFile.isFile() && !nextFile.getName().matches(R02xConst.FILES_TO_IGNORE)) {
					// nuevo file segun aplicacion
					ctx.currentModule().getFiles().add(nextFile);
					R02xFile file = new R02xFile(ctx.currentModule(), nextFile);
					R02xIAnalyser analyser = R02xAnalyserFactory.get(file, ctx);
					if (null != analyser) {
						try {
							ctx.currentModule().addChild(file);
							// pre-report file analisis iniciado
							file.reportIni(ctx.getProps().reportStats());
							// analisis
							ctx.currentFile(file);
							analyser.analyse();
							ctx.currentFile(null);
							// post-report file analisis finalizado
							file.reportEnd(ctx.getProps().reportStats());
						} catch (Exception e) {
							file.report("ERROR " + e.getMessage());
							e.printStackTrace();
						}
					} else {
						if (ctx.getProps().reportNotAnalysed()) {
							ctx.currentModule().report(nextFile.getAbsolutePath() + " no analizado --NOANALYSED.");
						}
					}
				}
			}
		}
	}
}
