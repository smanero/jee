package org.pcdm.jee.docgen.printer;

import java.util.Map;

import org.pcdm.jee.docgen.model.ElParsed;
import org.pcdm.util.parser.WsdlParser;
import org.pcdm.util.parser.XsdParser;

public abstract class Printer {
   protected String outputPath;
   
   public Printer(final String _outputPath) {
      this.outputPath = _outputPath;
   }
   
   public void print(final WsdlParser _wsdl, final Map<String, XsdParser> _xsdList,
         final Map<String, ElParsed> _typeTable) throws Exception {
   }

}