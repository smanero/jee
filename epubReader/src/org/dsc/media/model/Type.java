package org.dsc.media.model;

public enum Type {
   N("NAME"), S("SURNAME"), Y("YEAR"), T("TITLE"), A("ALBUM");

   Type(final String value) {
      this.setValue(value);
   }

   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   private String value;
}
