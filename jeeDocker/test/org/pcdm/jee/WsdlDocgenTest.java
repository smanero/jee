package org.pcdm.jee;

import java.io.File;

import org.pcdm.jee.docgen.WsdlDocgen;
import org.pcdm.jee.docgen.printer.PrinterFactory.PrinterType;

public class WsdlDocgenTest {
   private static final String OUTPUTPATH = "D:/tmp";
   private static final String BASEPATH = "D:\\eadmin\\trunk\\aplic\\x43s\\aplic\\MODULE\\src\\wsdls";

   /**
    * Listado de wsdls de los que generar documentacion * No se generan de los webservices privados * A modo test sólo
    * se genera del FCP
    */
   private static String[][] list = new String[][] {
       // {"x43WsFcpEJB", "X43FCPi.wsdl"},
       { "x43WsFcpEJB", "X43FCP.wsdl", "X43FCP" },
       // {"x43WsIdSFirmasDocsEJB", "X43ISFDi.wsdl"},
       { "x43WsIdSFirmasDocsEJB", "X43ISFD.wsdl", "X43ISFD" },
       // {"x43WsInteropRagEJB", "X43RAGi.wsdl"},
       // {"x43WsNoraEJB", "X43NORAi.wsdl"},
//       { "x43WsOceEJB", "X43OCE.wsdl", "X43OCE" },
       // {"x43WsPinEJB", ""},
       // { "x43WsPnlEJB", "X43PNL.wsdl", "X43PNL" },
       { "x43WsPntEJB", "X43PNT.wsdl", "X43PNT" },
       { "x43WsSrtEJB", "X43SRT.wsdl", "X43SRT" },
       // {"x43WsShnEJB", "X43SHNi.wsdl"},
       { "x43WsShnEJB", "X43SHN.wsdl", "X43SHN" },
       // {"x43WsUtilesEJB", "X43UTLi.wsdl"},
       { "x43WsUtilesEJB", "X43UTL.wsdl", "X43UTL" },
       // {"x43WsVdCEJB", "X43VCi.wsdl"},
       { "x43WsVdCEJB", "X43VC.wsdl", "X43VC" } };

   private static String[][] listTest = new String[][] { { "x43WsFcpEJB", "X43FCP.wsdl", "X43FCP" } };

   public static void main(String[] args) {
      try {
         WsdlDocgen wsdlDocGen = new WsdlDocgen();
         for (String[] module : listTest) {
            String wsdlPath = BASEPATH.replaceAll("MODULE", module[0]);
//            String wsdlPath = "F:/prjs/PcdmJ2eeDoc/wsdls";
            File wsdlFile = new File(wsdlPath + File.separator + module[1]);
            // generacion del documento del wsdl
            wsdlDocGen.generate(wsdlFile, PrinterType.USERGUIDE, OUTPUTPATH);
            // PrinterType.HTML, PrinterType.WORD, PrinterType.XML
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
