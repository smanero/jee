package org.pcdm.jee.docgen.pattern;

import org.pcdm.jee.docgen.model.ElParsed;
import org.pcdm.jee.docgen.model.ElParsedList;
import org.pcdm.jee.docgen.model.XsdCard;
import org.pcdm.jee.docgen.model.XsdType;

/**
 * Patron para reconocer método elemento con elemento hijo de cardinalidad 1 a N <br/>
 * |--> element: el::documentList:T:fcp:TDocumentList:C:O1:H:null:F:null <br/>
 * |--> element: ct:fcp:TDocumentList:C:NUL:H:1:F:null <br/>
 * |--> element: cc:fcp:null:C:NUL:H:1:F:null <br/>
 * |--> element: et:fcp:null:B:ttn:AExtensionPoint:C:NUL:H:1:F:null <br/>
 * |--> element: el:fcp:document:T:fcp:TDocument:C:N1:H:null:F:null
 * @author BIMALOSE
 */
public class N1CardPattern extends APattern {
   private static final long serialVersionUID = 1L;

   @Override
   public void define() {
      this.add(new ElParsed(null, XsdType.EL));
      this.add(new ElParsed(null, XsdType.CT));
      this.add(new ElParsed(null, XsdType.CC));
      this.add(new ElParsed(null, XsdType.ET));
      ElParsed el = new ElParsed(null, XsdType.EL);
      el.setCard(XsdCard.N1);
      this.add(el);
   }

   @Override
   public boolean matches(final ElParsedList ofound) {
      if (this.size() == 0) {
         define();
      }
      // el tamaño debe ser >=
      if (ofound.size() < this.size()) {
         return false;
      }
      // buscar secuencia definida como final de la secuencia completa
      int subseqLength = ofound.size() - this.size();
      boolean matched = true;
      for (int i = 0; i < this.size(); i++) {
         ElParsed epT = this.get(i);
         ElParsed epO = ofound.get(i + subseqLength);
         if (epT.compareTo(epO) != 0) {
            // si los elementos comparados son diferentes, no puede cumplir el patron
            matched = false;
            break;
         }
      }
      return matched;
   }
}
