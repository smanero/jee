package org.pcdm.jee.docgen.pattern;

import org.pcdm.jee.docgen.model.ElParsedList;

public abstract class APattern extends ElParsedList {
   private static final long serialVersionUID = 1L;
   
   public abstract void define();
   
   public abstract boolean matches(final ElParsedList pattern);
}
