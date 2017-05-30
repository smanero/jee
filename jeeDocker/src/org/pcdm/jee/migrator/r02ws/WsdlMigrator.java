package org.pcdm.jee.migrator.r02ws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.pcdm.analizador.context.R02xConst;
import org.pcdm.analizador.context.R02xContext;
import org.pcdm.jee.migrator.AMigrator;
import org.pcdm.util.parser.CsvParser;
import org.pcdm.util.parser.WsdlParser;
import org.pcdm.util.parser.XsdParser;
import org.pcdm.util.parser.model.xsd.FormChoice;
import org.pcdm.util.parser.model.xsd.Schema;
import org.pcdm.util.velocity.R02xVelocity;

public class WsdlMigrator extends AMigrator {
   public static final String OUTPUTPATH = "D:/tmp";
   // ctx.getOutputDir().getAbsolutePath()
   public static final String BASEPATH = "D:/eadmin/dev/aplic";
   public static final String CSVFILE = "/uso.webservices.csv";

   public static List<String[]> ws_lst = new ArrayList<String[]>();
   static {
//      ws_lst.add(new String[] { "X43FdCdPWS", "com/ejie/x43s/fcp/ws", "/r02d/pcdm/wsdls/r02dCpProcessWar/R02FdCdP.wsdl" });
//      ws_lst.add(new String[] { "X43RegistroWS", "com/ejie/x43s/srt/ws", "/r02e/pcdm/wsdls/r02eSrtProcessWar/R02Registro.wsdl" });
      ws_lst.add(new String[] { "X43ShfWS", "com/ejie/x43s/shf/ws", "/r02f/pcdm/wsdls/r02fShfProcessWar/R02Shf.wsdl" });
//      ws_lst.add(new String[] { "X43ArchivoDigitalWS", "com/ejie/x43s/ad/ws", "/r02g/pcdm/wsdls/r02gRdeProcessWar/R02ArchivoDigital.wsdl" });
//      ws_lst.add(new String[] { "X43TramitacionIdSFirmasDocsWS", "com/ejie/x43s/ids/docs/ws",
//                               "/r02g/pcdm/wsdls/r02gRdeProcessWar/R02TramitacionIdSFirmasDocs.wsdl" });
//      ws_lst.add(new String[] { "X43ShnEscWS", "com/ejie/x43s/shn/ws", "/r02i/pcdm/wsdls/r02iEscShnProcessWar/R02SHNEsc.wsdl" });
//      ws_lst.add(new String[] { "X43TramitacionIdSNotificacionesWS", "com/ejie/x43s/ids/notif/ws",
//                               "/r02i/pcdm/wsdls/r02iShnProcessWar/R02TramitacionIdSNotificaciones.wsdl" });
//      ws_lst.add(new String[] { "X43InteropRAGWS", "com/ejie/x43s/interop/rag/ws", "/r02l2/pcdm/wsdls/r02l2RagProcessWar/R02InteropRAG.wsdl" });
//      ws_lst.add(new String[] { "X43OceWS", "com/ejie/x43s/oce/ws", "/r02oc/pcdm/wsdls/r02ocOceProcessWar/R02Oce.wsdl" });
//      ws_lst.add(new String[] { "X43PnlWS", "com/ejie/x43s/pnl/ws", "/r02o/pcdm/wsdls/r02oPublishNotificationBatchWar/R02OPnlWS.wsdl" });
//      ws_lst.add(new String[] { "X43PntWS", "com/ejie/x43s/pnt/ws", "/r02o/pcdm/wsdls/r02oPntProcessWar/R02Pnt.wsdl" });
//      ws_lst.add(new String[] { "X43PntEscWS", "com/ejie/x43s/pnt/ws", "/r02o/pcdm/wsdls/r02oEscPntProcessWar/R02PNTEsc.wsdl" });
//      ws_lst.add(new String[] { "X43VdCWS", "com/ejie/x43s/vdc/ws", "/r02r/pcdm/wsdls/r02rScProcessWar/R02VdC.wsdl" });
//      ws_lst.add(new String[] { "X43VdCAsyncWS", "com/ejie/x43s/vdc/async/ws", "/r02r/pcdm/wsdls/r02rScProcessWar/R02VdCAsincrono.wsdl" });
//      ws_lst.add(new String[] { "X43FcaWS", "com/ejie/x43s/fca/ws", "/r02s/pcdm/wsdls/r02sFcaWar/R02FCA.wsdl" });
//      ws_lst.add(new String[] { "X43CommonWS", "com/ejie/x43s/common/ws", "/r02y/pcdm/wsdls/r02yArquitecturaProcessWar/R02Arquitectura.wsdl" });
   }

   public static String[] WS_LST_F2 = new String[] { "/r02h/pcdm/wsdls/r02hSatProcessWar/R02FdCdTT.wsdl",
                                                    "/r02h/pcdm/wsdls/r02hGatProcessWar/R02Gat.wsdl",
                                                    "/r02t/pcdm/wsdls/r02tReeFacadeWar/R02TramitacionREEAdjuntos.wsdl",
                                                    "/r02t/pcdm/wsdls/r02tReeFacadeWar/R02TramitacionREEFolder.wsdl",
                                                    "/r02t/pcdm/wsdls/r02tReeFacadeWar/R02TramitacionREEMassiveProcess.wsdl",
                                                    "/r02t/pcdm/wsdls/r02tReeFacadeWar/R02TramitacionREEProceedings.wsdl",
                                                    "/r02z/pcdm/wsdls/r02zEscCtProcessWar/R02CdTEsc.wsdl",
                                                    "/r02z/pcdm/wsdls/r02zEscCtProcessWar/R02CdTFolderEsc.wsdl",
                                                    "/r02z/pcdm/wsdls/r02zEscSrtProcessWar/R02RegistroEsc.wsdl",
                                                    "/r02z/pcdm/wsdls/r02zEscPpsProcessWar/R02SolicitudesYAportaciones.wsdl",
                                                    "/r02z/pcdm/wsdls/r02zEscIdsProcessWar/R02TramitacionIdS.wsdl",
                                                    "/r02z/pcdm/wsdls/r02zShpProcessWar/R02TramitacionPagos.wsdl",
                                                    "/r02z/pcdm/wsdls/r02zTramitacionProcessWar/R02TramitacionREE.wsdl",
                                                    "/r02z/pcdm/wsdls/r02zCommonProcessWar/R02Utilidades.wsdl",
                                                    "/r02z/pcdm/wsdls/r02zMassiveProcessWar/R02TramitacionREEMasivo.wsdl",
                                                    "/r02z/pcdm/wsdls/r02zEscScProcessWar/R02VdCVolcadoTotal.wsdl" };

   public WsdlMigrator(R02xContext ctx) {
      super(ctx);
   }

   @Override
   public void doIt() throws Exception {
      // cargar diccionario de webservices publicos y privados
      File csvFile = new File(OUTPUTPATH + CSVFILE);
      CsvParser csv = new CsvParser();
      csv.loadCsv(csvFile);

      // para cada wsdl de la lista a migrar
      for (String[] wsCfg : ws_lst) {
         System.out.println("Generacion webservice " + wsCfg[0]);

         WsdlParser wsdl = new WsdlParser(new File(BASEPATH + wsCfg[2]));
         Map<String, List<String>> svc = wsdl.getOperations();
         if (svc.size() > 0) {
            for (String svcName : svc.keySet()) {
               // para cada servicio definido en el wsdl
               // division de operacion en publicas y privadas
               List<String>[] ops = csv.filter(svcName, svc.get(svcName));
               // division elementos xsd definidos en publicos y privados
               File xsdFile = generateSchemaWsdl(wsdl.getSchema(),wsCfg[0], wsCfg[1] + "/xml", OUTPUTPATH + "/");
               XsdParser xsd = new XsdParser(xsdFile, null);

               // cotejar ops wsdl frente a ops 
               // xsd.parse( ops[0]);
               // generate public wsdl
               generateXsd(xsd.getSchema(), wsCfg[0], OUTPUTPATH + "/wsdls/");
               generateWsdl(wsCfg[0], wsCfg[1], ops[0], OUTPUTPATH + "/wsdls/");

               // cotejar ops wsdl frente a ops 
               // xsd.parse( ops[0]);
               // generate private wsdl
               generateXsd(xsd.getSchema(), wsCfg[0] + "P", OUTPUTPATH + "/wsdlsp/");
               generateWsdl(wsCfg[0] + "P", wsCfg[1] + "p", ops[1], OUTPUTPATH + "/wsdlsp/");
            }

         } else {
            throw new Exception("Error en WsdlParser");
         }
      }
   }

   private void generateWsdl(final String _service, final String _namespace, final List<String> _ops, final String _outPath) throws Exception {
      System.out.println(" - Generacion WSDL webservice " + _service);
      // generate wsdl
      final Map<String, Object> p = new HashMap<String, Object>(1);
      p.put("service", _service);
      p.put("namespace", _namespace);
      p.put("operations", _ops);
      R02xVelocity.merge(p, R02xConst.TPLT_X43WS_WSDL, _outPath + _service + ".wsdl");
      // generate xsd
      // TODO
      // File xsdFile = new File(_outPath + _service + ".xsd");
      // WsdlParser.generate(xsdFile, _jttp);
   }

   public static void main(String[] args) {
      try {
         // Context ctx = new Context();
         WsdlMigrator migrator = new WsdlMigrator(null);
         migrator.doIt();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
   
   public File generateSchemaWsdl(final Schema _schema, final String _svc, final String _namespace, final String _outPath) throws Exception {
      File xsdFile = null;
      if (null != _schema) {
         System.out.println(" - Generacion Types File namespace " + _namespace);
         StringWriter sw = new StringWriter();
         JAXBContext jc = JAXBContext.newInstance(WsdlParser.CTX_PATH, org.pcdm.util.parser.model.wsdl.ObjectFactory.class.getClassLoader());
         Marshaller mm = jc.createMarshaller();
         mm.marshal(_schema, sw);

         String sTypes = sw.toString().replaceAll("<types xmlns:ns2=\"http://schemas.xmlsoap.org/wsdl/\">", "").replaceAll("</types>", "")
            .replaceAll("xmlns[:]ope=\"http[:]//www[.]openuri[.]org/\"", "")
            .replaceAll("http[:]//www[.]openuri[.]org/", _namespace).replaceAll("[:]s=", ":xs=")
            .replaceAll("[<]s[:]", "<xs:").replaceAll("[</]s[:]", "/xs:")
            .replaceAll("\"s[:]", "\"xs:").replaceAll("\"ope[:]", "\"xs:")
            .replaceAll("[</]xs[:]schema[>]", "xs:import namespace=\"com/ejie/x43s/xml/types\" schemaLocation=\"http://www.euskadi.net/x43sTypes.xsd\" /> </xs:schema>");
         // String test = new String("test=http://www.openuri.org/ test").replaceAll("http[:]//www[.]openuri[.]org/",
         // _namespace);
         xsdFile = new File(_outPath + "/" + _svc + ".xsd");
         OutputStream os = null;
         try {
            os = new FileOutputStream(xsdFile);
            os.write(sTypes.getBytes());            
         } finally {
            if (null != os) os.close();
         }
      }
      return xsdFile;
   }

   public void generateXsd(final Schema _schema, final String _svc, final String _outPath) throws Exception {
      OutputStream os = null;
      try {
         if (null != _schema) {
            System.out.println(" - Generacion XSD namespace " + _svc);
            _schema.setElementFormDefault(FormChoice.QUALIFIED);
            _schema.setAttributeFormDefault(FormChoice.UNQUALIFIED);
            _schema.setVersion("2.0");

            File xsdFile = new File(_outPath + "/" + _svc + ".xsd");
            xsdFile.createNewFile();

            os = new FileOutputStream(xsdFile);
            JAXBContext jc = JAXBContext.newInstance(XsdParser.CTX_PATH, org.pcdm.util.parser.model.xsd.ObjectFactory.class.getClassLoader());
            Marshaller mm = jc.createMarshaller();
            mm.marshal(_schema, os);
         }
      } finally {
         if (null != os)
            os.close();
      }
   }   
}
