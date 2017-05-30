package org.pcdm.jee.docgen.printer;

public abstract class PrinterFactory {
   public enum PrinterType {
      HTML("html"), WORD("doc"), XML("xml"), USERGUIDE("ug");

      private final String value;

      PrinterType(String _v) {
         value = _v;
      }

      public String value() {
         return value;
      }

      public static PrinterType fromValue(String _v) {
         for (PrinterType c : PrinterType.values()) {
            if (_v.equals(c.value)) {
               return c;
            }
         }
         throw new IllegalArgumentException(_v);
      }
   }

   public static Printer newInstance(final String _outputPath, final PrinterType _pt) throws Exception {
      if (PrinterType.HTML.equals(_pt)) {
         return new HtmlPrinter(_outputPath);

      } else if (PrinterType.WORD.equals(_pt)) {
         return new WordPrinter(_outputPath);

      } else if (PrinterType.XML.equals(_pt)) {
         return new XmlPrinter(_outputPath);
         
      } else if (PrinterType.USERGUIDE.equals(_pt)) {
         return new UserGuidePrinter(_outputPath);         

      } else {
         throw new IllegalArgumentException(_pt.value);
      }
   }

}