package org.pcdm.jee.docgen.model;

import java.util.ArrayList;

public class ElParsedList extends ArrayList<ElParsed> implements Comparable<ElParsedList> {
   private static final long serialVersionUID = 1L;

   @Override
   public int compareTo(final ElParsedList o) {
      if (o.size() > this.size()) {
         return -1;
      }
      if (o.size() < this.size()) {
         return 1;
      }
      int cmp = 0;
      for (int i = 0; i < this.size(); i++) {
         cmp = this.get(i).compareTo(o.get(i));
         if (cmp != 0) {
            return cmp;
         }
      }
      return cmp;
   }
}