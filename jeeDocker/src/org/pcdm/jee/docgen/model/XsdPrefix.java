package org.pcdm.jee.docgen.model;

public enum XsdPrefix {
   
   AD("ad", "com/ejie/x43s/xml/da"),
   FCP("fcp", "com/ejie/x43s/xml/fcp"),
   IDS("docs", "com/ejie/x43s/xml/docs"),
   NORA("nora", "com/ejie/x43s/xml/nora"),
   OCE("oce", "com/ejie/x43s/xml/oce"),
   PNL("pnl", "com/ejie/x43s/xml/pnl"),
   PNT("pnt", "com/ejie/x43s/xml/pnt"),
   RAG("rag", "com/ejie/x43s/xml/rag"),
   RTN("rtn", "com/ejie/x43s/xml/rtn"),
   SHN("shn", "com/ejie/x43s/xml/shn"),
   TTN("ttn", "com/ejie/x43s/xml/types"),
   UTL("utl", "com/ejie/x43s/xml/da"),
   VDC("vdc", "com/ejie/x43s/xml/vdc"),
   NUL("", "");
   
   private final String prefix;
   private final String namespace;

   XsdPrefix(String _prefix, String _namespace) {
      prefix = _prefix;
      namespace = _namespace;
   }

   public String prefix() {
       return prefix;
   }
   
   public String namespace() {
      return namespace;
  }   

   public static XsdPrefix fromPrefix(String _prefix) {
       for (XsdPrefix c: XsdPrefix.values()) {
           if (_prefix.equals(c.prefix)) {
               return c;
           }
       }
//       throw new IllegalArgumentException(_prefix);
       return NUL;
   }

   public static XsdPrefix fromNamespace(String _namespace) {
      for (XsdPrefix c: XsdPrefix.values()) {
          if (_namespace.equals(c.namespace)) {
              return c;
          }
      }
//      throw new IllegalArgumentException(_namespace);
      return NUL;
  }

   public static boolean is(final XsdPrefix o1, final XsdPrefix o2) {
      return o1.compareTo(o2) == 0;
   }
}
