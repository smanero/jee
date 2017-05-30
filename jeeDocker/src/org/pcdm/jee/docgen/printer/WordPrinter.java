package org.pcdm.jee.docgen.printer;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

import org.pcdm.jee.docgen.model.ElParsed;
import org.pcdm.util.parser.WsdlParser;
import org.pcdm.util.parser.XsdParser;

public class WordPrinter extends Printer {

   private String outputPath;

   public WordPrinter(final String _outputPath) {
      super(_outputPath);
   }
   
   public void print(final WsdlParser _wsdl, final Map<String, XsdParser> _xsdList,
         final Map<String, ElParsed> _typeTable) throws Exception {
      for (String wsName : _wsdl.getOperations().keySet()) {
         printWSDL(wsName, _wsdl, _xsdList, _typeTable);
      }
   }

   /** Generar la informacion del wsdl y pintarla en un html **/
   private void printWSDL(final String _wsName, final WsdlParser _wsdl, 
         final Map<String, XsdParser> _xsdList, final Map<String, ElParsed> _typeTable)
         throws Exception {
      // recorre mapa de simbolos para presentar tipos anidados en secuencia
      File htmlFile = new File(outputPath + File.separator + _wsName + ".doc");
      FileWriter fw = new FileWriter(htmlFile);
      StringBuffer sb = new StringBuffer();
      sb.append("TEST GENERACION WORDDOC");

      fw.write(sb.toString());
      fw.flush();
      fw.close();
   }
}
