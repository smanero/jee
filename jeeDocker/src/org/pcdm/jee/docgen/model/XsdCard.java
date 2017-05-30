package org.pcdm.jee.docgen.model;

public enum XsdCard {
   
   O0("0", "Opcional"),
   O1("1", "Obligatorio"),
   N0("0N", "0 a N"),
   N1("1N", "1 a N"),
   NUL("", "");
   
   private final String pfx;
   private final String card;

   XsdCard(String _pfx, String _card) {
      pfx = _pfx;
      card = _card;
   }

   public String pfx() {
       return pfx;
   }
   
   public String card() {
      return card;
  }   

   public static XsdCard fromPrefix(String _pfx) {
       for (XsdCard c: XsdCard.values()) {
           if (_pfx.equals(c.pfx)) {
               return c;
           }
       }
//       throw new IllegalArgumentException(_prefix);
       return NUL;
   }

   public static XsdCard fromcard(String _card) {
      for (XsdCard c: XsdCard.values()) {
          if (_card.equals(c.card)) {
              return c;
          }
      }
//      throw new IllegalArgumentException(_card);
      return NUL;
  }

   public static boolean is(final XsdCard o1, final XsdCard o2) {
      return o1.compareTo(o2) == 0;
   }
}
