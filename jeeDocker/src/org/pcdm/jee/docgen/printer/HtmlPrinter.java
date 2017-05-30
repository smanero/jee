package org.pcdm.jee.docgen.printer;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

import org.pcdm.jee.docgen.model.ElParsed;
import org.pcdm.jee.docgen.model.WsdlOp;
import org.pcdm.util.parser.WsdlParser;
import org.pcdm.util.parser.XsdParser;

public class HtmlPrinter extends Printer {

   private FileWriter fw = null;
   private StringBuffer sb = new StringBuffer();

   private WsdlParser wsdl;
   // private Map<String, XsdParser> xsdList;
   private Map<String, ElParsed> typeTable;

   public HtmlPrinter(final String _outputPath) {
      super(_outputPath);
   }

   public void print(final WsdlParser _wsdl, final Map<String, XsdParser> _xsdList, final Map<String, ElParsed> _typeTable) throws Exception {
      try {
         this.wsdl = _wsdl;
         // this.xsdList = _xsdList;
         this.typeTable = _typeTable;
         System.out.println("\n - Print HTML " + _wsdl.getWsdlName() + ".html");
         // recorre mapa de simbolos para presentar tipos anidados en secuencia
         File htmlFile = new File(outputPath + File.separator + _wsdl.getWsdlName() + ".html");

         fw = new FileWriter(htmlFile);
         sb.append("<html><head><title>" + _wsdl.getWsdlName() + "</title></br></head><body>");
         for (String wsName : _wsdl.getOperations().keySet()) {
            // para cada service definido en el wsdl
            printWS(wsName);
         }
         sb.append("</body></html>");
         fw.write(sb.toString());
         fw.flush();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         fw.close();
      }
   }

   private void printWS(final String _wsName) throws Exception {
      sb.append("<h1>" + _wsName + "</h1></br>");
      // lista de operaciones definidas en el wsdl
      Map<String, WsdlOp> wsdlOps = wsdl.getMessages();
      if (wsdlOps.size() > 0) {
         for (WsdlOp wsdlOp : wsdlOps.values()) {
            sb.append("<h2>").append(wsdlOp.getOp()).append("</h2>");
            if (null != wsdlOp.getReqType()) {
               // request
               sb.append("<h3><em>").append(wsdlOp.getReqType()).append("</em></h3>");
               ElParsed ep = typeTable.get(wsdlOp.getReqType().toString());
               printElParsed(ep, 1, sb);
            }
            if (null != wsdlOp.getRspType()) {
               // response
               sb.append("<h3><em>").append(wsdlOp.getRspType()).append("</em></h3>");
               ElParsed ep = typeTable.get(wsdlOp.getRspType().toString());
               printElParsed(ep, 1, sb);
            }
         }
      }
   }

   /** Pinta la informacion del xsd, en el html **/
   private void printElParsed(ElParsed ep, int deep, StringBuffer sb) {
      write(deep, " |--> element: " + ep);
      int nextDeep = deep + 1;
      if (null != ep) {
         // regla del pulgar: o tiene type o tiene hijos
         if (ep.isRef() && null != typeTable.get(ep.getType().toString())) {
            ep = typeTable.get(ep.getType().toString());
         }
         if (null != ep.getName()) {
            sb.append("<ul>");
            // si el tipo es una referencia, se muestra el elemento referenciado
            sb.append("<li><em>").append(ep.getName()).append("</em>").append("&nbsp;&nbsp;");
            if (null != ep.getType())
               sb.append(ep.getType()).append("&nbsp;&nbsp;");
            if (null != ep.getCard())
               sb.append(ep.getCard().card()).append("&nbsp;&nbsp;");
            if (null != ep.getAnotation())
               sb.append(ep.getAnotation()).append("&nbsp;&nbsp;");
            if (null != ep.getAttributes()) {
               for (String s : ep.getAttributes()) {
                  sb.append("&nbsp;&nbsp;").append(s);
               }
            }
            sb.append("</li>");
         }
         // regla del pulgar: o tiene type o tiene hijos
         // print del tipo, si lo tiene definido
         if (null != ep.getType() && null != typeTable.get(ep.getType().toString())) {
            ElParsed epType = typeTable.get(ep.getType().toString());
            printElParsed(epType, nextDeep, sb);
         }
         // print hijos del elemento en proceso
         if (null != ep.getSons() && ep.getSons().size() > 0) {
            for (ElParsed son : ep.getSons()) {
               printElParsed(son, nextDeep, sb);
            }
         }
         if (null != ep.getName()) {
            sb.append("</ul>");
         }
      }
   }

   /** Pinta la informacion del xsd, en el html **/
   // private void dibujarHijosXSD(final String name, final ElType type, int
   // deep, StringBuffer sb) {
   // write(deep, " |--> element: " + name + " type: " + type);
   // int nextDeep = deep + 1;
   //
   // if (null != type && null != typeTable.get(type.toString())) {
   // ElParsed ep = typeTable.get(type.toString());
   // if (null != ep) {
   // sb.append("<ul>");
   // // si el tipo es una referencia, se muestra el elemento referenciado
   // if (ep.isRef()) {
   // ep = typeTable.get(ep.getType().toString());
   // }
   // if (null != ep.getName() && ep.getName().length() > 0) {
   // sb.append("<li><em>").append(ep.getName()).append("</em>").append("&nbsp;&nbsp;").append(ep.getType()).append("&nbsp;&nbsp;").append(
   // ep.getCardinalidad());
   //
   // if (ep.getAttributes() != null) {
   // for (String s : ep.getAttributes()) {
   // sb.append("&nbsp;&nbsp;").append(s);
   // }
   // }
   // sb.append("</li>");
   // }
   // // dinujado de hijos
   // if (null != ep.getSons() && ep.getSons().size() > 0) {
   // for (ElParsed son : ep.getSons()) {
   // dibujarHijosXSD(son.getName(), son.getType(), nextDeep, sb);
   // }
   // }
   // sb.append("</ul>");
   // }
   // }
   // }

   /** Muestra en la consola los valores del parametro en arbol **/
   private void write(int deep, String text) {
      StringBuffer deepPfx = new StringBuffer("");
      for (int i = 0; i < deep; i++)
         deepPfx.append("  ");
      System.out.println(deepPfx.toString() + text);
   }
}
