package org.pcdm.jee.docgen.model;

public class ElType implements Comparable<ElType> {
   private String px = "";
   private String tp = "";

   public ElType(String px, String tp) {
      this.px = px;
      this.tp = tp;
   }

   @Override
   public String toString() {
      return (null != px && px.length() > 0 ? px + ":" + tp : tp);
   }
   
   @Override
   public int compareTo(final ElType o) {
      return this.tp.compareTo(o.tp);
   }
}
