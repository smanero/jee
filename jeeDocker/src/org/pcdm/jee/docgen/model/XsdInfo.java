package org.pcdm.jee.docgen.model;

public class XsdInfo {
   private String location;
   private String namespace;
   
   public XsdInfo(String _location) {
      this.location = _location;
   }
   
   public XsdInfo(String _location, String _namespace) {
      this.location = _location;
      this.namespace = _namespace;
   }

   public String getLocation() {
      return location;
   }

   public String getNamespace() {
      return namespace;
   }

   public XsdPrefix getPreffix() {
      XsdPrefix preffix = XsdPrefix.NUL;
      if (null != namespace) {
         preffix = XsdPrefix.fromNamespace(this.namespace);
      }
      return preffix;
   }
}
