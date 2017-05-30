package org.pcdm.analizador.processor;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.pcdm.analizador.context.R02xContext;
import org.pcdm.analizador.context.node.R02xModule;


/**
 * @author Ibermatica
 */
public class R02xWsdlAnalyser extends R02xAbstractAnalyser {

   private static List<String> lstWsR02 = Arrays.asList(
         "X43WsArchivoDigitalEJB",
         "X43CdTEsc",
         "X43CdTFolderEsc",
         "X43FCA",
         "X43FdCdP",
         "X43FdCdTT",
         "X43GAT",
         "X43InteropRAG",
         "X43Nora",
         "X43OCE",
         "X43OPnl",
         "X43Pin",
         "X43PNT",
         "X43PNTEsc",
         "X43Registro",
         "X43RegistroEsc",
         "X43SHF",
         "X43SHNEsc",
         "X43SolicitudesYAportaciones",
         "X43TramitacionIdS",
         "X43TramitacionIdSFirmasDocs",
         "X43TramitacionIdSNotificaciones",
         "X43TramitacionPagos",
         "X43TramitacionREE",
         "X43TramitacionREEAdjuntos",
         "X43TramitacionREEFolder",
         "X43TramitacionREEMasivo",
         "X43TramitacionREEMassiveProcess",
         "X43TramitacionREEProceedings",
         "X43Utiles",
         "X43VdC",
         "X43VdCAsincrono",
         "X43VdCVolcadoTotal" );
   
	public R02xWsdlAnalyser(final R02xContext ctx) throws Exception {
		super(ctx);
	}

	public void analyse() throws Exception {
		// report profile analisis iniciado
		ctx.currentProfile().reportIni(ctx.getProps().reportStats());
		// analisis cada module n profile
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
	}
}
