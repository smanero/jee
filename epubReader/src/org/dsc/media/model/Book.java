package org.dsc.media.model;

public final class Book extends Media {
   private String ISBN;

   public String getISBN() {
      return ISBN;
   }

   public void setISBN(String iSBN) {
      ISBN = iSBN;
   }
}
