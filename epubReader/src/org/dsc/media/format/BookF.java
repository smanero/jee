package org.dsc.media.format;

import org.dsc.media.model.Book;

public final class BookF extends Format<Book> {

   @Override
   public String format(final Book b) {
      b.getAlbum();
      b.getTitle();
      b.getYear();
      b.getSurname();
      b.getName();
      return null;
   }

}
