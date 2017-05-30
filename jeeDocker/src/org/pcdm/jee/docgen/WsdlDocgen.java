package org.pcdm.jee.docgen;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.pcdm.jee.docgen.model.ElParsed;
import org.pcdm.jee.docgen.model.XsdInfo;
import org.pcdm.jee.docgen.model.XsdPrefix;
import org.pcdm.jee.docgen.printer.Printer;
import org.pcdm.jee.docgen.printer.PrinterFactory;
import org.pcdm.jee.docgen.printer.PrinterFactory.PrinterType;
import org.pcdm.util.parser.WsdlParser;
import org.pcdm.util.parser.XsdParser;

public class WsdlDocgen {
   /** Tabala de elementos definidos en los archivos procesados */
   private Map<String, ElParsed> typeTable = new TreeMap<String, ElParsed>();

   /** WSDL en procesamiento */
   private WsdlParser wsdl = null;
   /** Lista de XSDs en procesamiento */
   private Map<String, XsdParser> xsdLst = new HashMap<String, XsdParser>();
   /** Lista de xsd procesados location - preffix */
   private Map<String, XsdPrefix> xsdPfx = new HashMap<String, XsdPrefix>();

   public WsdlDocgen() {
   }

   /**
    * Parseo e impresion del documento
    * @param _wsName
    * @param _wsdlFile
    * @param _printer
    * @throws Exception
    */
   public void generate(final File _wsdlFile, final PrinterType _printerType, final String outputPath) throws Exception {
      if (null == _wsdlFile || !_wsdlFile.exists()) {
         throw new Exception(" - Parseo WSDL Ruta de fichero invalida " + _wsdlFile.getAbsolutePath());
      }

      if (null == _printerType) {
         throw new Exception(" - Printer no inicializado " + _wsdlFile.getAbsolutePath());
      }

      // directorio en el que se encuentran todos los archivos
      String workingdir = _wsdlFile.getParentFile().getAbsolutePath();
      // procesamiento del wsdl indicado
      this.wsdl = new WsdlParser(_wsdlFile);
      if (null != wsdl.getOperations()) {
         // lista de operaciones definidas en el wsdl
         if (null != wsdl.getOperations() && wsdl.getOperations().keySet().size() > 0) {
            // si tiene operaciones definidas
            // procesamiento de todos los xsd implicados
            recursiveXsd(workingdir, this.wsdl.getRelatedXsds(), XsdPrefix.NUL);

            System.out.println("\n - Contruccion de la tabla de tipos");
            // recorrido de todos los tipos definidos en cada xsd
            for (String location : xsdLst.keySet()) {
               XsdParser xsd = xsdLst.get(location);
               // recorrido de todos los tipos definidos en un xsd
               for (String typeKey : xsd.getElements().keySet()) {
                  ElParsed xsdEl = xsd.getElements().get(typeKey);
                  typeTable.put(typeKey, xsdEl);
                  System.out.println("   - typeTable add " + xsdEl);
               }
            }
            // limpieza generica tabla simbolos
            this.typeTable.remove("");
            System.out.println(" - Número de tipos definidos: " + typeTable.size());
         }
      }
      // generacion del documento
      Printer printer = PrinterFactory.newInstance(outputPath, _printerType);
      printer.print(this.wsdl, this.xsdLst, this.typeTable);
   }

   private void recursiveXsd(final String _workingdir, final List<XsdInfo> _xsdList, final XsdPrefix _pfx) throws Exception {
      for (XsdInfo xsdDef : _xsdList) {
         // procesamiento de cada xsd asociado (import o include)
         String location = xsdDef.getLocation();
         String namespace = xsdDef.getNamespace();
         if (null == this.xsdPfx.get(location)) {
            // xsd location aun no procesadda
            // por defecto, se supone include - mantiene prefijo del padre
            XsdPrefix workingPfx = _pfx;
            if (null != namespace) {
               // es import - procesa con el prefijo que le corresponde
               workingPfx = xsdDef.getPreffix();
            }
            // procesamiento de los xsd
            System.out.println(" - PreParseo " + location + " --> pfx:" + workingPfx);
            File xsdFile = new File(_workingdir + File.separator + location);
            XsdParser xsd = new XsdParser(xsdFile, workingPfx.prefix());
            // actualizacion mapa xsd location - preffix
            this.xsdLst.put(location, xsd);
            this.xsdPfx.put(location, workingPfx);
            // recorrido en depth, left
            recursiveXsd(_workingdir, xsd.getRelatedXsds(), workingPfx);
         } else {
            System.out.println(" - Parseo XSD " + location + " yet done");
         }
      }
   }
}
