package org.pcdm.jee.docgen.pattern;

import org.pcdm.jee.docgen.model.ElParsed;
import org.pcdm.jee.docgen.model.ElParsedList;
import org.pcdm.jee.docgen.model.ElType;
import org.pcdm.jee.docgen.model.XsdType;

/**
 * Patron para reconocer método sin parámetros de entrada <br/>
 * |--> element: el::getAllDocuments:H:1:F:null <br/>
 * |--> element: ct::null:H:1:F:null <br/>
 * |--> element: cc::null:H:1:F:null <br/>
 * |--> element: et::null:B:ttn:AExtensionPoint:H:0:F:null
 * @author BIMALOSE
 */
public class NoParamsPattern extends APattern {
   private static final long serialVersionUID = 1L;

   @Override
   public void define() {
      this.add(new ElParsed(null, XsdType.EL));
      this.add(new ElParsed(null, XsdType.CT));
      this.add(new ElParsed(null, XsdType.CC));
      ElParsed el = new ElParsed(null, XsdType.ET);
      el.setBase(new ElType("ttn", "AExtensionPoint"));
      el.getSons(); // 0 hijos
      this.add(el);
   }

   @Override
   public boolean matches(final ElParsedList ofound) {
      if (this.size() == 0) {
         define();
      }
      return compareTo(ofound) == 0;
   }
}
