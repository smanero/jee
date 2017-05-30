package org.pcdm.jee.migrator.wli;

import org.pcdm.analizador.context.R02xContext;
import org.pcdm.jee.migrator.AMigrator;

/**
 * @author Ibermatica
 */
public class WLIMigrator extends AMigrator {

	public WLIMigrator(final R02xContext ctx) throws Exception {
		super(ctx);
	}

	public void doIt() throws Exception {
//		// report profile analisis iniciado
//		R02xWliProfile profile = (R02xWliProfile) ctx.currentProfile();
//		// nuevo profile migracion
//		R02xWls8MigrProfile migrProfile = new R02xWls8MigrProfile(ctx.currentLote(), ctx.getMigrDir(), "");
//		
//		// migration lib
//		File migrationLib = migrProfile.getLibDir();
//		migrationLib.mkdir();
//		// copia del APP-INF/lib al lib de migracion
//		R02xConst.copyDirectory(profile.getLibDir(), migrationLib);
//		
//		// migration config
//		File srcConfig = new File(profile.getConfigDir().getAbsolutePath() + File.separator + profile.getAppCode());
//		File dstConfig = new File(migrProfile.getConfigDir().getAbsolutePath() + File.separator + profile.getAppCode());
//			// new File(ctx.getMigrDir().getAbsolutePath() + File.separator + "config" + File.separator + profile.getAppCode());
//		dstConfig.mkdir();
//		// copia de archivos config al migration config
//		R02xConst.copyDirectory(srcConfig, dstConfig);
//		// copia de archivos estaticos html al migration config
//		R02xConst.copyDirectory(profile.getHtmlDir(), dstConfig);
//		
//		ctx.push("com.ejie.r02z");
//		// migracion de cada module � profile
//		R02xIMigrator propertiesMigrator = null; 
//		R02xModule propertiesModule = null; 
//		for (R02xNode child : profile.getChilds()) {
//			R02xModule module = (R02xModule) child;
//			R02xIMigrator migrator = R02xWliMigratorFactory.get(module, ctx);
//			
//			if (null != migrator) {
//				if (migrator instanceof R02xProcessMigrator) {
//					String sLog = " - " + module.getFileName();
//					System.out.println(sLog);
//					
//					ctx.currentModule(module);
//					migrator.migrate();
//					ctx.currentModule(null);
//				} else if (migrator instanceof R02xPropertiesMigrator) {	
//					propertiesMigrator = migrator;
//					propertiesModule = module;
//				}
//			}
//		}
//		if (null != propertiesModule) {
//			String sLog = " - " + propertiesModule.getFileName();
//			System.out.println(sLog);
//			
//			ctx.currentModule(propertiesModule);
//			propertiesMigrator.migrate();
//			ctx.currentModule(null);
//		}
//		ctx.pop();
//		
//		// analisis cada jws � profile
//		List<R02xFile> lstJws = new ArrayList<R02xFile>(profile.lstJws.values());
//		Collections.sort(lstJws);
//		for (R02xFile file : lstJws) {
//			for (R02xMethod metodo : file.metodos) {
//				System.out.println(metodo.toString());
//			}
//		}
	}
}
