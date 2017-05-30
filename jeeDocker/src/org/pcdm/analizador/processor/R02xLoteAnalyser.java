package org.pcdm.analizador.processor;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.pcdm.analizador.context.R02xContext;
import org.pcdm.analizador.context.node.R02xProfile;
import org.pcdm.analizador.context.node.R02xProfileFactory;


/**
 * @author Ibermatica
 */
public class R02xLoteAnalyser extends R02xAbstractAnalyser {

	public R02xLoteAnalyser(final R02xContext ctx) {
		super(ctx);
	}

	/**
	 * @throws Exception
	 */
	public void analyse() throws Exception {
		// report lote analisis iniciado
		ctx.currentLote().reportIni(ctx.getProps().reportStats());
		// analisis cada profile � lote
		for (File file : ctx.currentLote().getFiles()) {
			// nuevo profile segun aplicacion
			R02xProfile profile = R02xProfileFactory.get(ctx.currentLote(), file);
			if (null != profile) {
				try {
					ctx.currentLote().addChild(profile);
					R02xProfileAnalyser analyser = new R02xProfileAnalyser(this.ctx);
					// analisis
					ctx.currentProfile(profile);
					analyser.analyse();
					ctx.currentProfile(null);
				} catch (Exception e) {
					profile.report("ERROR " + e.getMessage());
					e.printStackTrace();
				}
			} else {
				if (ctx.getProps().reportNotAnalysed()) {
					ctx.currentLote().report(file.getAbsolutePath() + " no analizado --NOANALYSED.");
				}
			}
		}


		// retoques final de la migracion
		if (null != ctx.getProps().getMigrDir()) {
			// limpieza jars repetidos y svn
			migrLibDirCleanup(new File(ctx.getMigrDir().getAbsoluteFile() + "/R02zClasses/lib/"));
		}
		// report lote analisis finalizado
		ctx.currentLote().reportEnd(ctx.getProps().reportStats());
		// report mensajes de lote
		ctx.getReporter().lote(ctx.currentLote().getMessages());

		// generacion de Csv de lote
		if (ctx.getProps().reportCsv()) {
			List<String> reportCsv = ctx.currentLote().reportCsv(ctx.getProps().reportCsvParcial());
			ctx.getReporter().csv(reportCsv);
		}

		// cierre de TODOS los reports
		ctx.getReporter().close();
	}
	
	
	private void migrLibDirCleanup(final File migrLibDir) throws Exception {
		if (migrLibDir.isDirectory()) {
			// limpieza svn de copia
			File migrationLibSvn = new File(migrLibDir.getAbsolutePath() + File.separator + ".svn");
			if (migrationLibSvn.exists()) {
				FileUtils.forceDelete(migrationLibSvn);
			}
			// limpieza jars manteniendo version superior o sin version
			Map<String, String> jarsMap = new HashMap<String, String>(0);
			String[] jars = migrLibDir.list();
			for (int i = 0; i< jars.length; i++) {
				
				int versionIndex = jars[i].lastIndexOf("-");
				if (-1 != versionIndex) {
					String jarName = jars[i].substring(0, versionIndex);
					String jarVersion = jars[i].substring(versionIndex+1, jars[i].length());
					
					String jarMapVersion = jarsMap.get(jarName);
					if (null == jarMapVersion) {
						jarsMap.put(jarName, jarVersion);
					} else {
						if (jarVersion.compareTo(jarMapVersion) > 0) {
							jarsMap.put(jarName, jarVersion);
							// borrado version previa
							FileUtils.forceDelete(new File(migrLibDir.getAbsolutePath() + File.separator + jarName + "-"+ jarMapVersion));
						} else {
							FileUtils.forceDelete(new File(migrLibDir.getAbsolutePath() + File.separator + jarName + "-"+ jarVersion));
						}
					}
				} else {
					String jarName = jars[i].replaceAll("[.]jar", "");
					String jarMapVersion = jarsMap.get(jarName);
					if (null == jarMapVersion) {
						jarsMap.put(jarName, "0.0.jar");
					} else {
						FileUtils.forceDelete(new File(migrLibDir.getAbsolutePath() + File.separator + jars[i]));
					}
				}
			}
		}
	}
}
