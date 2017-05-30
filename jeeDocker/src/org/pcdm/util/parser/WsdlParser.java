package org.pcdm.util.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.pcdm.jee.docgen.model.ElType;
import org.pcdm.jee.docgen.model.WsdlOp;
import org.pcdm.jee.docgen.model.XsdInfo;
import org.pcdm.util.parser.model.wsdl.TBinding;
import org.pcdm.util.parser.model.wsdl.TBindingOperation;
import org.pcdm.util.parser.model.wsdl.TBindingOperationFault;
import org.pcdm.util.parser.model.wsdl.TBindingOperationMessage;
import org.pcdm.util.parser.model.wsdl.TDefinitions;
import org.pcdm.util.parser.model.wsdl.TDocumented;
import org.pcdm.util.parser.model.wsdl.TExtensibleAttributesDocumented;
import org.pcdm.util.parser.model.wsdl.TFault;
import org.pcdm.util.parser.model.wsdl.TMessage;
import org.pcdm.util.parser.model.wsdl.TOperation;
import org.pcdm.util.parser.model.wsdl.TParam;
import org.pcdm.util.parser.model.wsdl.TPart;
import org.pcdm.util.parser.model.wsdl.TPort;
import org.pcdm.util.parser.model.wsdl.TPortType;
import org.pcdm.util.parser.model.wsdl.TService;
import org.pcdm.util.parser.model.wsdl.TTypes;
import org.pcdm.util.parser.model.xsd.Schema;

public class WsdlParser {
   /** JAXB context */
   public static final String CTX_PATH = "org.pcdm.util.parser.model.wsdl";

   /** Nombre del wsdl */
   private String wsdlName;

   /** Root del wsdl */
   private TDefinitions definitions = null;
   /** Esquemas importados en el wsdl */
   private List<XsdInfo> relatedXsds = new ArrayList<XsdInfo>();

   /** Mapa svcName - Lista de operaciones */
   private Map<String, List<String>> operations = new HashMap<String, List<String>>();
   /** Arbol operacionName - Elemento input y ouput en el xsd */
   private TreeMap<String, WsdlOp> messages = new TreeMap<String, WsdlOp>();

   public WsdlParser(final File _wsdlFile) throws Exception {
      parse(_wsdlFile);
   }

   public String getWsdlName() {
      return wsdlName;
   }

   public TDefinitions getDefinitions() {
      return definitions;
   }

   public TreeMap<String, WsdlOp> getMessages() {
      return messages;
   }

   public Map<String, List<String>> getOperations() {
      return operations;
   }

   public List<XsdInfo> getRelatedXsds() {
      return relatedXsds;
   }

   public TDefinitions parse(final File _wsdlFile) throws Exception {
      if (null == _wsdlFile || !_wsdlFile.exists()) {
         throw new Exception(" - Parseo WSDL Ruta de fichero invalida " + _wsdlFile.getAbsolutePath());
      }
      // nombre del archivo wsdl
      this.wsdlName = _wsdlFile.getName();
      // parseo jaxb y root del wsdl
      System.out.println(" - Parseo WSDL " + _wsdlFile.getAbsolutePath());
      JAXBContext jc = JAXBContext.newInstance(CTX_PATH, org.pcdm.util.parser.model.wsdl.ObjectFactory.class.getClassLoader());
      Unmarshaller um = jc.createUnmarshaller();
      Source source = new StreamSource(new FileInputStream(_wsdlFile));
      JAXBElement<TDefinitions> root = um.unmarshal(source, TDefinitions.class);
      definitions = root.getValue();

      JAXBElement<TTypes> jttp = null;
      Map<String, TMessage> tmsg = new HashMap<String, TMessage>();
      Map<String, TPortType> tprt = new HashMap<String, TPortType>();
      Map<String, TBinding> tbng = new HashMap<String, TBinding>();
      Map<String, TService> tsvc = new HashMap<String, TService>();
      Map<String, Map<String, String>> tprtpope = new HashMap<String, Map<String, String>>();

      for (TDocumented tdoc : definitions.getAnyTopLevelOptionalElement()) {
         // TImport TTypes TMessage TPortType TBinding TService
         if (tdoc instanceof TTypes) {
            // procesar schema definido en el wsdl
            TTypes ttp = (TTypes) tdoc;
            jttp = new JAXBElement<TTypes>(new QName(CTX_PATH, "types"), TTypes.class, ttp);
            parseSchema(jttp);

         } else if (tdoc instanceof TMessage) {
            tmsg.put(((TMessage) tdoc).getName(), (TMessage) tdoc);

         } else if (tdoc instanceof TPortType) {
            tprt.put(((TPortType) tdoc).getName(), (TPortType) tdoc);

         } else if (tdoc instanceof TBinding) {
            tbng.put(((TBinding) tdoc).getName(), (TBinding) tdoc);

         } else if (tdoc instanceof TService) {
            tsvc.put(((TService) tdoc).getName(), (TService) tdoc);
         }
      }

      // para cada servicio definido en el wsdl, operaciones definidas
      for (TService svc : tsvc.values()) {
         String svcName = svc.getName();
         operations.put(svcName, new ArrayList<String>());
         // service
         for (TPort port : svc.getPort()) {
            // port indicado por el service
            String portName = port.getName();
            String bindingName = port.getBinding().getLocalPart();
            // binding indicado por el port
            TBinding bng = tbng.get(bindingName);
            bng.getName();
            bng.getAny(); // <soap:binding transport="http://schemas.xmlsoap.org/soap/http" style="document"/>
            String porttypeName = bng.getType().getLocalPart();
            // porttype indicado por el binding
            TPortType prtp = tprt.get(porttypeName);
            prtp.getName();
            for (TOperation op : prtp.getOperation()) {
               // operation dentro de un porttype
               Map<String, String> tparam = new HashMap<String, String>();
               for (JAXBElement<? extends TExtensibleAttributesDocumented> je : op.getRest()) {
                  if (je.getValue() instanceof TParam) {
                     // operation input y output
                     tparam.put(je.getName().getLocalPart(), ((TParam) je.getValue()).getMessage().getLocalPart());
                  } else if (je.getValue() instanceof TFault) {
                     // operation fault
                     tparam.put(je.getName().getLocalPart(), ((TFault) je.getValue()).getMessage().getLocalPart());
                  }
               }
               tprtpope.put(op.getName(), tparam);
            }
            // binding operation
            for (TBindingOperation bop : bng.getOperation()) {
               String opeName = bop.getName();
               bop.getAny(); // <soap:operation soapAction="http://www.openuri.org/publishNotification"
                             // style="document"/>
               // input
               TBindingOperationMessage input = bop.getInput();
               if (null != input) {
                  input.getAny(); // <soap:body use="literal"/>
               }
               // output
               TBindingOperationMessage output = bop.getOutput();
               if (null != output) {
                  output.getAny(); // <soap:body use="literal"/>
               }
               // fault
               List<TBindingOperationFault> tbopf = bop.getFault();
               // enlace con porttype operation
               ElType typeIn = null;
               ElType typeOut = null;
               Map<String, String> param = tprtpope.get(opeName);
               if (null != param) {
                  // enlace con message In
                  String msgIn = param.get("input");
                  TMessage msg = tmsg.get(msgIn);
                  msgIn = parseMessagePart(msg, 0);
                  if (null != msgIn) {
                     typeIn = new ElType("", msgIn);
                  }
                  // enlace con message Out
                  String msgOut = param.get("output");
                  msg = tmsg.get(msgOut);
                  msgOut = parseMessagePart(msg, 1);
                  if (null != msgOut) {
                     typeOut = new ElType("", msgOut);
                  }
               }
               
               WsdlOp opParams = new WsdlOp(opeName, typeIn, typeOut);

               messages.put(opeName, opParams);
               operations.get(svcName).add(opeName);

            }
         }
      }
      return definitions;
   }

   private String parseMessagePart(final TMessage _msg, int _idx) throws Exception {
      if (null != _msg) {
         // parts
         for (TPart part : _msg.getPart()) {
            part.getName();
            if (null != part.getElement()) {
               return part.getElement().getLocalPart();
            }
         }
         return null;
      } else {
         if (_idx == 0) {
            // error: binding operation sin mensaje request asociado
            throw new Exception("Error: binding operation sin mensaje request asociado");
         } else {
            // no es error dado que las operaciones void se indican de esta manera
            return null;
         }
      }
   }

   /** Seccion de esquemas del wsdl */
   private Schema schema = null;

   public Schema getSchema() {
      return schema;
   }

   /**
    * Procesamiento de los xsd importados en el elemento types
    * @param _jttp Elemento types
    * @throws Exception En caso de error
    */
   private void parseSchema(final JAXBElement<TTypes> _jttp) throws Exception {
      // tratamiento de la parte types como xml
      JAXBContext jc = JAXBContext.newInstance(CTX_PATH, org.pcdm.util.parser.model.wsdl.ObjectFactory.class.getClassLoader());
      Marshaller ms = jc.createMarshaller();
      ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
      ms.setProperty(Marshaller.JAXB_FRAGMENT, true);
      StringWriter writer = new StringWriter();
      ms.marshal(_jttp, writer);
      String stringXML = writer.toString();
      int idx = stringXML.indexOf(">");
      int fdx = stringXML.indexOf("</ns2:types>");
      stringXML = stringXML.substring(idx + 1, fdx).trim();
      // root de la seccion schema
      JAXBContext xjc = JAXBContext.newInstance(XsdParser.CTX_PATH, org.pcdm.util.parser.model.xsd.ObjectFactory.class.getClassLoader());
      Unmarshaller xum = xjc.createUnmarshaller();
      JAXBElement<Schema> xroot = xum.unmarshal(new StreamSource(new StringReader(stringXML)), Schema.class);
      schema = xroot.getValue();
      // esquemas importados (en este caso en el wsdl)
      XsdParser.parseRelatedXsds(schema, relatedXsds);
   }
}
