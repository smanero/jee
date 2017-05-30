package org.pcdm.jee.docgen.model;

public enum XsdType {
   
   CC("cc", "complexContent"),
   CT("ct", "complexType"),
   EL("el", "element"),
   ET("et", "extensionType"),
   FC("fc", "facet"),
   RT("rt", "restrictionType"),
   SC("sc", "simpleContent"),
   SCH("sch", "schema"),
   ST("st", "simpleType"),
   UM("um", "unionMember"),
   NUL("", "");
   
   private final String sym;
   private final String entity;

   XsdType(String _sym, String _entity) {
      sym = _sym;
      entity = _entity;
   }

   public String sym() {
       return sym;
   }
   
   public String entity() {
      return entity;
  }   

   public static XsdType fromPrefix(String _sym) {
       for (XsdType c: XsdType.values()) {
           if (_sym.equals(c.sym)) {
               return c;
           }
       }
//       throw new IllegalArgumentException(_prefix);
       return NUL;
   }

   public static XsdType fromEntity(String _entity) {
      for (XsdType c: XsdType.values()) {
          if (_entity.equals(c.entity)) {
              return c;
          }
      }
//      throw new IllegalArgumentException(_entity);
      return NUL;
  }

   public static boolean is(final XsdType o1, final XsdType o2) {
      return o1.compareTo(o2) == 0;
   }
}
