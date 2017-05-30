package org.dsc.media.format;

import org.dsc.media.model.Type;


public abstract class Format<T> {
   public static final Type[] DEF = {}
   private Type[] format;

   public abstract String format(final T b);

   public Type[] getFormat() {
      return format;
   }

   public void setFormat(Type[] format) {
      this.format = format;
   }
}
