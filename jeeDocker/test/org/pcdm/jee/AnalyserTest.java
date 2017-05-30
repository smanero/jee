package org.pcdm.jee;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class AnalyserTest {
   private static final File BASEPATH = new File("D:\\eadmin\\dev\\aplic\\x43s\\aplic\\");

   private static List<String> lstAppX43s = Arrays.asList("x43WsArchivoDigitalEJB", 
   		"x43WsFcpEJB", 
   		"x43WsIdSFirmasDocsEJB", 
   		"x43WsInteropRagEJB", 
   		"x43WsNoraEJB", 
   		"x43WsOceEJB", 
   		"x43WsPinEJB", 
   		"x43WsPnlEJB", 
   		"x43WsPntEJB", 
   		"x43WsRegistroEJB", 
   		"x43WsShnEJB",
         "x43WsUtilesEJB", 
         "x43WsVdCEJB");

   private static FileFilter ffWar = new FileFilter() {
      public boolean accept(File directory) {
         return directory.getAbsolutePath().endsWith("War");
      }
   };
   
   private static FileFilter ffinterno = new FileFilter() {
      public boolean accept(File directory) {
         return directory.getAbsolutePath().endsWith("interno");
      }
   };
   
   private static FileFilter ffxsd = new FileFilter() {
      public boolean accept(File directory) {
         return directory.getAbsolutePath().endsWith("xsd");
      }
   };

   private static FileFilter ffWsdl = new FileFilter() {
      public boolean accept(File directory) {
         return directory.getAbsolutePath().endsWith("wsdl");
      }
   };

   private static List<String[]> list;
   static {
      list = new ArrayList<String[]>();
      // 2014 - Fase 1
      list.add(new String[] { "0", "r02g", "R02ArchivoDigital.wsdl", "r02gRdeProcessWar", "Rde", "ad" });
      list.add(new String[] { "1", "r02s", "R02FCA.wsdl", "r02sFcaWar", "Nora", "fca" });
      list.add(new String[] { "0", "r02d", "R02FdCdP.wsdl", "r02dCpProcessWar", "Cpt", "fcp" });
      list.add(new String[] { "0", "r02l2", "R02InteropRAG.wsdl", "r02l2RagProcessWar", "Interop", "rag" });
      list.add(new String[] { "1", "r02s", "R02Nora.wsdl", "r02sFcaWar", "Nora", "nora" });
      list.add(new String[] { "0", "r02oc", "R02Oce.wsdl", "r02ocOceProcessWar", "Interop", "oce" });
      list.add(new String[] { "0", "r02o", "R02OPnlWS.wsdl", "r02oPublishNotificationBatchWar", "Shn", "pnl" });      
      list.add(new String[] { "0", "r02o", "R02Pnt.wsdl", "r02oPntProcessWar", "Shn", "pnt" });
      list.add(new String[] { "0", "r02o", "R02PNTEsc.wsdl", "r02oEscPntProcessWar", "Shn", "pnt.esc" });
      list.add(new String[] { "0", "r02e", "R02Registro.wsdl", "r02eSrtProcessWar", "Srt", "srt" });
      list.add(new String[] { "0", "r02f", "R02Shf.wsdl", "r02fShfProcessWar", "Shf", "shf" });      
      list.add(new String[] { "0", "r02i", "R02SHNEsc.wsdl", "r02iEscShnProcessWar", "Shn", "shn.esc" });      
      list.add(new String[] { "0", "r02g", "R02TramitacionIdSFirmasDocs.wsdl", "r02gRdeProcessWar", "TramitacionIdS", "trami.ids.rde" });
      list.add(new String[] { "0", "r02i", "R02TramitacionIdSNotificaciones.wsdl", "r02iShnProcessWar", "TramitacionIdS", "trami.ids.shn" });
      list.add(new String[] { "0", "r02r", "R02VdC.wsdl", "r02rScProcessWar", "VdC", "vdc" });
      list.add(new String[] { "0", "r02r", "R02VdCAsincrono.wsdl", "r02rScProcessWar", "VdC", "vdc.aync" });
      
      list.add(new String[] { "1", "r02y", "R02Arquitectura.wsdl", "r02yArquitecturaProcessWar", "Utiles", "utiles" });
      list.add(new String[] { "1", "r02z", "R02Utilidades.wsdl", "r02zCommonProcessWar", "Utiles", "utiles" });

      // 2015 - Fase 2
      list.add(new String[] { "0", "r02h", "R02FdCdTT.wsdl", "r02hSatProcessWar", "Cpt", "fctt" });
      list.add(new String[] { "0", "r02h", "R02Gat.wsdl", "r02hGatProcessWar", "Tramitacion", "trami.gat" });
      list.add(new String[] { "0", "r02z", "R02CdTEsc.wsdl", "r02zEscCtProcessWar", "CdT", "cdt" });
      list.add(new String[] { "0", "r02z", "R02CdTFolderEsc.wsdl", "r02zEscCtProcessWar", "CdT", "cdt.folder" });
      list.add(new String[] { "0", "r02z", "R02RegistroEsc.wsdl", "r02zEscSrtProcessWar", "Srt", "srt.esc" });
      list.add(new String[] { "0", "r02z", "R02SolicitudesYAportaciones.wsdl", "r02zEscPpsProcessWar", "Tramitacion", "trami.pps" });
      list.add(new String[] { "0", "r02z", "R02TramitacionIdS.wsdl", "r02zEscIdsProcessWar", "TramitacionIdS", "trami.ids" });
      list.add(new String[] { "0", "r02z", "R02TramitacionPagos.wsdl", "r02zShpProcessWar", "Interop", "shp" });
      list.add(new String[] { "0", "r02z", "R02TramitacionREE.wsdl", "r02zTramitacionProcessWar", "TramitacionREE", "trami.ree" });
      list.add(new String[] { "0", "r02t", "R02TramitacionREEAdjuntos.wsdl", "r02tReeFacadeWar", "TramitacionREE", "trami.ree.attach" });
      list.add(new String[] { "0", "r02t", "R02TramitacionREEFolder.wsdl", "r02tReeFacadeWar", "TramitacionREE", "trami.ree.folder" });
      list.add(new String[] { "0", "r02t", "R02TramitacionREEProceedings.wsdl", "r02tReeFacadeWar", "TramitacionREE", "trami.ree.proceedings" });
      list.add(new String[] { "0", "r02t", "R02TramitacionREEMassiveProcess.wsdl", "r02tReeFacadeWar", "TramitacionMasiva", "trami.masivo" });
      list.add(new String[] { "0", "r02z", "R02TramitacionREEMasivo.wsdl", "r02zMassiveProcessWar", "TramitacionMasiva", "trami.masivo" });
      list.add(new String[] { "0", "r02z", "R02VdCVolcadoTotal.wsdl", "r02zEscScProcessWar", "VdC", "vdc.total" });
   }

   public static void main(String[] args) throws Exception {
     // createNewWS();
   	AnalyserTest at=new AnalyserTest();
   	at.listR02WS();
   }

   private void listR02WS() throws Exception {
      final List<File> files = new ArrayList<File>(0);
      // EJB-s de x43s
      for (String appCode : lstAppX43s) {
         // directorio de la aplicacion/profile
         File appPath = new File(BASEPATH.getAbsolutePath() + File.separator + appCode + File.separator + "wsdls");
         if (appPath.exists()) {
         	for (File wsdlPath : appPath.listFiles(ffWsdl)) {
	         	files.add(wsdlPath);
	            System.out.println("list.add(new String[] {\"0\", \"" + wsdlPath.getName() + "\", \"\", \"\", \"\"});");  
         	}
         	for (File wsdlPath : appPath.listFiles(ffxsd)) {
	         	files.add(wsdlPath);
	            System.out.println("list.add(new String[] {\"0\", \"" + wsdlPath.getName() + "\", \"\", \"\", \"\"});");
	           
         	}
         	for (File wsdlDir : appPath.listFiles(ffinterno)) {
	              for (File wsdlPath : wsdlDir.listFiles(ffWsdl)) {
	                  files.add(wsdlPath);
	                  System.out.println("list.add(new String[] {\"0\", \"" + wsdlPath.getName() + "\", \"\", \"\", \"\"});");
	              }
	              for (File wsdlPath : wsdlDir.listFiles(ffxsd)) {
	                  files.add(wsdlPath);
	                  System.out.println("list.add(new String[] {\"0\", \"" + wsdlPath.getName() + "\", \"\", \"\", \"\"});");
	              }
          	}
         }
      }
   }

  /* private static void createNewWS() throws Exception {
      for (String[] data : list) {
         if ("0".equals(data[0])) {

         }
      }
   }*/
}
