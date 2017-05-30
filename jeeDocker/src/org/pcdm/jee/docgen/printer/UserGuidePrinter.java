package org.pcdm.jee.docgen.printer;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.pcdm.jee.docgen.model.ElParsed;
import org.pcdm.jee.docgen.model.ElParsedList;
import org.pcdm.jee.docgen.model.WsdlOp;
import org.pcdm.jee.docgen.model.XsdCard;
import org.pcdm.jee.docgen.model.XsdType;
import org.pcdm.util.parser.WsdlParser;
import org.pcdm.util.parser.XsdParser;

public class UserGuidePrinter extends Printer {
   private FileWriter fw = null;
   private StringBuffer sb = new StringBuffer();

   private WsdlParser wsdl;
   // private Map<String, XsdParser> xsdList;
   private Map<String, ElParsed> typeTable;

   public UserGuidePrinter(final String _outputPath) {
      super(_outputPath);
   }

   public void print(final WsdlParser _wsdl, final Map<String, XsdParser> _xsdList,
         final Map<String, ElParsed> _typeTable) throws Exception {
      try {
         this.wsdl = _wsdl;
         // procesamiento de la tabla de simbolos para su impresion como guia de
         // usuario
         this.typeTable = _typeTable;
         System.out.println("\n - Print WSDL " + "PLATEA-4.2-Guia_Usuario-" + _wsdl.getWsdlName()
               + ".html");
         // recorre mapa de simbolos para presentar tipos anidados en
         // secuencia
         File htmlFile = new File(outputPath + File.separator + "PLATEA-4.2-Guia_Usuario-"
               + _wsdl.getWsdlName() + ".html");
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
            sb.append("<h3>").append(wsdlOp.getOp()).append("</h3>\n");
            // request
            sb.append("<strong>Parámetro de entrada</strong><br/>\n");
            if (null != wsdlOp.getReqType()) {
               ElParsed ep = typeTable.get(wsdlOp.getReqType().toString());
               ElParsedList sequenceEP = new ElParsedList();
               breakOnThrough(ep, 0, -1, sequenceEP);
               paintInBlack(sequenceEP);
            }
            // response
            sb.append("<strong>Parámetro de salida</strong><br/>\n");
            if (null != wsdlOp.getRspType() && null != wsdlOp.getRspType().toString()) {
               ElParsed ep = typeTable.get(wsdlOp.getRspType().toString());
               ElParsedList sequenceEP = new ElParsedList();
               breakOnThrough(ep, 0, -1, sequenceEP);
               paintInBlack(sequenceEP);
            } else {
               sb.append(" Sin parámetro de salida<br/>\n");
            }
         }
      }
   }

   /** Recorre la tabla de simbolos para cada wsdlOp generando la lista de elementos que la compone */
   private void breakOnThrough(ElParsed ep, int deep, int eldeep, final ElParsedList sequenceEP) {
      // write(deep, " |--> element: " + ep);
      int nextDeep = deep + 1;
      if (null != ep) {
         ElParsed epClon = ep.cloneMe();
         epClon.deep = eldeep;
         // segun tipo de elemento xsd
         if (XsdType.is(XsdType.EL, ep.xtype())) {            
            epClon.deep = eldeep + 1;
            sequenceEP.add(epClon);

            ElParsed refType = null;
            if (null != ep.getType()) {
               refType = typeTable.get(ep.getType().toString());
            }

            if (ep.isRef()) {
               // indica referencia
               if (null == refType) {
                  write(deep, " ERROR TYPE NOT FOUND in " + ep);
               } else {
                  // si tipo referenciado existe en la tahla de simbolos
                  ElParsed epRefered = typeTable.get(refType.getType().toString());
                  if (null != epRefered) {
                     // hay que imprimirlo
                     breakOnThrough(epRefered, nextDeep, epClon.deep, sequenceEP);
                  }
               }
            } else if (null != refType) {
               // indica tipo
               breakOnThrough(refType, nextDeep, epClon.deep, sequenceEP);
            }

         } else if (XsdType.is(XsdType.CT, ep.xtype())) {
            // xsd complexType

         } else if (XsdType.is(XsdType.ST, ep.xtype())) {
            // xsd simpleType

         } else if (XsdType.is(XsdType.ET, ep.xtype())) {
            // xsd extensionType
            
         } else if (XsdType.is(XsdType.RT, ep.xtype())) {
            // xsd restrictionType
         }

         // print hijos del elemento en proceso
         if (null != ep.getSons() && ep.getSons().size() > 0) {
            for (ElParsed son : ep.getSons()) {
               breakOnThrough(son, nextDeep, epClon.deep, sequenceEP);
            }
         }
      }
   }

   /** Recorre la lista de elementos que compone una wsdlOp tratando casos especiales */
   private void paintInBlack(final ElParsedList sequenceEP) {
      ElParsedList sequenceEPinBlack = new ElParsedList();
      for (int i = 0; i < sequenceEP.size(); i++) {
         ElParsed ep = sequenceEP.get(i);
         // siguiente elemento para la busqueda de patrones
         ElParsed nextEp = null;
         if (i + 1 < sequenceEP.size()) {
            nextEp = sequenceEP.get(i + 1);
         }
         // patron definicion lista
         if (null != nextEp && ep.deep < nextEp.deep && XsdCard.N1 == nextEp.getCard()) {
            if (null != ep.getType() && "ttn:TLocaleText".equals(ep.getType().toString())) {
               // caso particular definicion texto multidioma
               ep.setAnotation("Texto en castellano y en euskera");
            } else {
               // definicion lista
               ep.setAnotation(XsdCard.N1.card() + " elementos " + nextEp.getName());
            }
            // actualizacion deep elementos hijos
            for (int j = i + 2; j < sequenceEP.size(); j++) {
               if (sequenceEP.get(j).deep > nextEp.deep) {
                  sequenceEP.get(j).deep--;
               } else {
                  break;
               }
            }
            // trick para saltar elemento que cumple patron
            i++;
         }
         sequenceEPinBlack.add(ep);
      }
      // dibujado ya con formato
      colourMyDreams(sequenceEPinBlack, new ArrayList<String>(), sb);
   }

   /** Pinta en html una wsdlOp tratando el anidamiento de sus elementos componentes */
   private void colourMyDreams(final ElParsedList sequenceEP, List<String> ulAdded,
         final StringBuffer sb) {
      if (sequenceEP.size() == 1) {
         // patrón de método request sin parámetros
         sb.append(sequenceEP.get(0).getName()).append(" Sin parámetros de entrada<br/>\n");

      } else {
         for (int idx = 0; idx < sequenceEP.size(); idx++) {
            // para cada uno de los xsd elements procesados
            ElParsed ep = sequenceEP.get(idx);
            String name = ep.getName();
            String card = ep.getCard().card();
            // tipo del elemento
            ElParsed refEl = null;
            if (null != ep.getType()) {
               refEl = typeTable.get(ep.getType().toString());
               if (ep.isRef()) {
                  // si es referencia
                  name = refEl.getName();
               }
            }
            // siguiente elemento para la busqueda de patrones
            ElParsed nextEp = null;
            if (idx + 1 < sequenceEP.size()) {
               nextEp = sequenceEP.get(idx + 1);
            }

            write(ep.deep, ep.deep + " |--> element: " + ep);

            if (idx > 0) {
               sb.append("<li>");
            }
            sb.append("<em>").append(name).append("</em>").append("&nbsp;&nbsp;");
            if (XsdCard.NUL != ep.getCard()) {
               sb.append("[").append(card).append("]&nbsp;&nbsp;");
            }
            if (null != ep.getAnotation()) {
               sb.append(ep.getAnotation()).append("&nbsp;&nbsp;");
            } else if (null != ep.getType()) {
               sb.append(ep.getType()).append("&nbsp;&nbsp;");
            }
            if (null != ep.getAttributes()) {
               for (String s : ep.getAttributes()) {
                  sb.append("&nbsp;&nbsp;").append(s);
               }
            }
            if (idx > 0) {
               sb.append("</li>\n");
            }

            if (null != nextEp) {
               if (ep.deep < nextEp.deep) {
                  // si pasamos a un nivel más profundo
                  if (ulAdded.size() - 1 < ep.deep) {
                     // y para el nivel actual no se ha puesto aun ul
                     ulAdded.add(ep.toString());
                     sb.append("<ul>");
                  }
               } else if (ep.deep > nextEp.deep) {
                  // subimos en el nivel de anidamiento
                  if (ulAdded.size() == ep.deep) {
                     // si el agrupamiento ya fue definido,lo cerramos
                     ulAdded = ulAdded.subList(0, nextEp.deep);
                     sb.append("</ul>");
                  }
               }
            }
         }
         // cerramos elementos anidados pendientes
         for (int idx = 0; idx < ulAdded.size(); idx++) {
            sb.append("</ul>");
         }
      }
   }

   /** Muestra en la consola los valores del parametro en arbol **/
   private void write(int deep, String text) {
      StringBuffer deepPfx = new StringBuffer("");
      for (int i = 0; i < deep; i++)
         deepPfx.append(" ");
      System.out.println(deepPfx.toString() + text);
   }
}
