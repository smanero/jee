package org.pcdm.util.parser;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.pcdm.jee.docgen.model.ElParsed;
import org.pcdm.jee.docgen.model.ElType;
import org.pcdm.jee.docgen.model.XsdCard;
import org.pcdm.jee.docgen.model.XsdInfo;
import org.pcdm.jee.docgen.model.XsdPrefix;
import org.pcdm.jee.docgen.model.XsdType;
import org.pcdm.util.parser.model.xsd.Annotated;
import org.pcdm.util.parser.model.xsd.Annotation;
import org.pcdm.util.parser.model.xsd.Attribute;
import org.pcdm.util.parser.model.xsd.ComplexContent;
import org.pcdm.util.parser.model.xsd.ComplexType;
import org.pcdm.util.parser.model.xsd.Documentation;
import org.pcdm.util.parser.model.xsd.Element;
import org.pcdm.util.parser.model.xsd.ExplicitGroup;
import org.pcdm.util.parser.model.xsd.ExtensionType;
import org.pcdm.util.parser.model.xsd.Facet;
import org.pcdm.util.parser.model.xsd.Group;
import org.pcdm.util.parser.model.xsd.Import;
import org.pcdm.util.parser.model.xsd.Include;
import org.pcdm.util.parser.model.xsd.LocalElement;
import org.pcdm.util.parser.model.xsd.NoFixedFacet;
import org.pcdm.util.parser.model.xsd.NumFacet;
import org.pcdm.util.parser.model.xsd.OpenAttrs;
import org.pcdm.util.parser.model.xsd.Pattern;
import org.pcdm.util.parser.model.xsd.Restriction;
import org.pcdm.util.parser.model.xsd.RestrictionType;
import org.pcdm.util.parser.model.xsd.Schema;
import org.pcdm.util.parser.model.xsd.SimpleContent;
import org.pcdm.util.parser.model.xsd.SimpleType;
import org.pcdm.util.parser.model.xsd.TopLevelComplexType;
import org.pcdm.util.parser.model.xsd.TopLevelElement;
import org.pcdm.util.parser.model.xsd.TopLevelSimpleType;
import org.pcdm.util.parser.model.xsd.TotalDigits;
import org.pcdm.util.parser.model.xsd.Union;
import org.pcdm.util.parser.model.xsd.WhiteSpace;

public class XsdParser {
   /* // JAXB context */
   public static final String CTX_PATH = "org.pcdm.util.parser.model.xsd";

   public static void parseRelatedXsds(final Schema _schema, final List<XsdInfo> _relatedXsds)
         throws Exception {
      // esquemas importados en el xsd
      for (int i = 0; i < _schema.getIncludeOrImportOrRedefine().size(); i++) {
         org.pcdm.util.parser.model.xsd.OpenAttrs openAtts = _schema.getIncludeOrImportOrRedefine()
               .get(i);
         if (null != openAtts) {
            if (openAtts instanceof Import) {
               Import oa = (Import) openAtts;
               _relatedXsds.add(new XsdInfo(oa.getSchemaLocation(), oa.getNamespace()));
               System.out.println("   - imported schema: " + oa.getSchemaLocation()
                     + ", namespace: " + oa.getNamespace());

            } else if (openAtts instanceof Include) {
               Include oa = (Include) openAtts;
               _relatedXsds.add(new XsdInfo(oa.getSchemaLocation()));
               System.out.println("   - included schema: " + oa.getSchemaLocation());
            }
         }
      }
   }

   private Schema schema = null;
   private File xsdFile = null;
   private String pfx = null;
   private ElParsed schemaDoc = null;
   private List<XsdInfo> relatedXsds = new ArrayList<XsdInfo>();

   private TreeMap<String, ElParsed> elements = new TreeMap<String, ElParsed>();

   public XsdParser(final File _xsdFile, final String _preffix) throws Exception {
      xsdFile = _xsdFile;
      pfx = _preffix;
      parse(_xsdFile);
   }

   public TreeMap<String, ElParsed> getElements() {
      return elements;
   }

   public String getPreffix() {
      return pfx.replaceAll(":", "");
   }

   public List<XsdInfo> getRelatedXsds() {
      return relatedXsds;
   }

   public Schema getSchema() {
      return schema;
   }

   public ElParsed getSchemaDoc() {
      return schemaDoc;
   }

   public File getXsdFile() {
      return xsdFile;
   }

   /**
    * // @param _xsdFile // @return // @throws Exception
    */
   public Schema parse(final File _xsdFile) throws Exception {
      if (null == _xsdFile || !_xsdFile.exists()) {
         throw new Exception(" - Parseo XSD Ruta de fichero invalida " + _xsdFile.getAbsolutePath());
      }

      System.out.println(" - Parseo XSD " + _xsdFile.getAbsolutePath() + " - Preffix: " + pfx);
      JAXBContext jc = JAXBContext.newInstance(CTX_PATH,
            org.pcdm.util.parser.model.xsd.ObjectFactory.class.getClassLoader());
      Unmarshaller um = jc.createUnmarshaller();
      Source source = new StreamSource(new FileInputStream(_xsdFile));
      JAXBElement<Schema> root = um.unmarshal(source, Schema.class);
      schema = root.getValue();

      String ns = schema.getTargetNamespace();
      XsdPrefix xsdPfx = XsdPrefix.fromNamespace(ns);
      if (!xsdPfx.prefix().equals(pfx)) {
         throw new Exception(" - Preffix en import xsd y targetNamespace deben ser el mismo.");
      }
      // pfx = (!XsdPrefix.is(XsdPrefix.NUL, xsdPfx) ? xsdPfx.prefix() + ":" :
      // "");
      schemaDoc = new ElParsed(pfx, XsdType.SCH);
      schemaDoc.setName(xsdFile.getName());

      // esquemas importados en el wsdl
      parseRelatedXsds(schema, relatedXsds);

      // elementos y tipos definidos en el esquema
      List<ElParsed> schemaEls = new ArrayList<ElParsed>();
      for (OpenAttrs oatt : schema.getSimpleTypeOrComplexTypeOrGroup()) {
         if (oatt instanceof TopLevelComplexType) {
            TopLevelComplexType tl = (TopLevelComplexType) oatt;
            parseComplexType(tl, schemaEls);

         } else if (oatt instanceof TopLevelSimpleType) {
            TopLevelSimpleType tl = (TopLevelSimpleType) oatt;
            parseSimpleType(tl, schemaEls);

         } else if (oatt instanceof TopLevelElement) {
            TopLevelElement tl = (TopLevelElement) oatt;
            parseElement(tl, schemaEls);

         } else if (oatt instanceof Annotation) {
            // anotaciones del schema
            extractAnnotation((Annotation) oatt, schema.getOtherAttributes(), schemaDoc);
         }
      }
      // informar la tabla de simbolos
      for (ElParsed el : schemaEls) {
         if (null != el && null != el.key()) {
            elements.put(el.key(), el);
         }
      }

      System.out.println(" - Parseo XSD " + _xsdFile.getAbsolutePath() + " - Num tipos: "
            + elements.size() + " FIN");
      return schema;
   }

   private void extractAnnotation(final Annotation annot, final Map<QName, String> oatts,
         final ElParsed el) {
      // anotaciones del elemento
      if (annot != null) {
         if (null != annot.getAppinfoOrDocumentation()) {
            // documentacion del elemento
            StringBuffer sb = new StringBuffer();
            for (Object objdoc : annot.getAppinfoOrDocumentation()) {
               Documentation doc = (Documentation) objdoc;
               for (Object objCont : doc.getContent()) {
                  // para cada linea en la documentacion
                  sb.append("" + objCont);
               }
            }
            el.setAnotation(sb.toString());
         }
      }
      // otros atributos del elemento
      if (null != oatts && oatts.size() > 0) {

      }
   }

   private void extractAttributes(final List<Annotated> attrs, final ElParsed el) {
      List<String> atributes = new ArrayList<String>();
      if (null != attrs && attrs.size() > 0) {
         for (Annotated ant : attrs) {
            Attribute attr = (Attribute) ant;
            atributes.add(attr.getName() + ": " + attr.getType().getLocalPart());
         }
         el.setAttributes(atributes);
      }
   }

   private void extractCardinalidad(final BigInteger _minOccurrs, final String _maxOccurs,
         final ElParsed el) {
      // cardinalidad del elemento
      String minOccurs = "" + _minOccurrs;
      String maxOccurs = "" + _maxOccurs;
      if ("1".equals(maxOccurs)) {
         if ("1".equals(minOccurs)) {
            // Obligatorio
            el.setCard(XsdCard.O1);
         } else if ("0".equals(minOccurs)) {
            // Opcional
            el.setCard(XsdCard.O0);
         }
      } else if ("unbounded".equals(maxOccurs)) {
         if ("1".equals(minOccurs)) {
            // 1 a N elementos
            el.setCard(XsdCard.N1);
         } else if ("0".equals(minOccurs)) {
            // 0 a N elementos
            el.setCard(XsdCard.N0);
         }
      }
   }

   private void extractType(final QName type, final boolean isRef, final ElParsed el) {
      el.setType(null, isRef);
      if (null != type) {
         // NA String ns = type.getNamespaceURI();
         String px = type.getPrefix();
         String tp = type.getLocalPart();
         ElType elType = new ElType(px, tp);
         el.setType(elType, isRef);
         // // caso de que sea ref
         // if (isRef && (null == el.getName() || el.getName().length() == 0)) {
         // // en caso de ref, el name es el mismo que el type
         // el.setName(elType.toString());
         // }
      }
   }

   private void extractBase(final QName type, final ElParsed el) {
      el.setBase(null);
      if (null != type) {
         // NA String ns = type.getNamespaceURI();
         String px = type.getPrefix();
         String tp = type.getLocalPart();
         ElType elType = new ElType(px, tp);
         el.setBase(elType);
      }
   }

   private void parseComplexType(final ComplexType le, List<ElParsed> elements) {
      if (null != le) {
         ElParsed el = new ElParsed(pfx, XsdType.CT);
         elements.add(el);
         // otros campos del elemento xml
         le.getFinal();
         le.getBlock();
         le.isAbstract();
         le.isMixed();
         // name
         el.setName(le.getName());
         // anotaciones del elemento
         extractAnnotation(le.getAnnotation(), le.getOtherAttributes(), el);
         // atributos del elemento
         extractAttributes(le.getAttributeOrAttributeGroup(), el);
         // hijos del elemento
         if (null != le.getAll()) {
            Group ae = le.getAll();
            parseGroup(ae, el.getSons());

         } else if (null != le.getChoice()) {
            Group ch = le.getChoice();
            parseGroup(ch, el.getSons());

         } else if (null != le.getGroup()) {
            Group gr = le.getGroup();
            parseGroup(gr, el.getSons());

         } else if (null != le.getSequence()) {
            Group sq = le.getSequence();
            parseGroup(sq, el.getSons());

         } else {
            // si define un content
            if (null != le.getComplexContent()) {
               ElParsed elcc = new ElParsed(pfx, XsdType.CC);
               el.getSons().add(elcc);
               ComplexContent cc = le.getComplexContent();
               extractAnnotation(cc.getAnnotation(), cc.getOtherAttributes(), elcc);
               if (null != cc.getExtension()) {
                  parseExtensionType(cc.getExtension(), elcc.getSons());
               } else if (null != cc.getRestriction()) {
                  parseRestrictionType(cc.getRestriction(), elcc.getSons());
               }

            } else if (null != le.getSimpleContent()) {
               ElParsed elsc = new ElParsed(pfx, XsdType.SC);
               el.getSons().add(elsc);
               SimpleContent sc = le.getSimpleContent();
               extractAnnotation(sc.getAnnotation(), sc.getOtherAttributes(), elsc);
               if (null != sc.getExtension()) {
                  parseExtensionType(sc.getExtension(), elsc.getSons());
               } else if (null != sc.getRestriction()) {
                  parseRestrictionType(sc.getRestriction(), elsc.getSons());
               }
            }
         }
      }
   }

   private void parseElement(final Element le, List<ElParsed> elements) {
      if (null != le) {
         ElParsed el = new ElParsed(pfx, XsdType.EL);
         elements.add(el);
         // otros campos del elemento xml
         le.getBlock();
         le.getDefault();
         le.getFinal();
         le.getForm();
         le.getIdentityConstraint();

         el.setName(le.getName());
         el.setFixed("true".equals(le.getFixed()));

         // tipo
         if (null != le.getType()) {
            extractType(le.getType(), false, el);
         } else if (null != le.getRef()) {
            extractType(le.getRef(), true, el);
         } else if (null != le.getSubstitutionGroup()) {
            extractType(le.getSubstitutionGroup(), true, el);
         }
         // cardinalidad del elemento
         extractCardinalidad(le.getMinOccurs(), le.getMaxOccurs(), el);
         // anotaciones del elemento
         extractAnnotation(le.getAnnotation(), le.getOtherAttributes(), el);
         // hijos del elemento
         if (null != le.getSimpleType()) {
            parseSimpleType(le.getSimpleType(), el.getSons());
         } else if (null != le.getComplexType()) {
            parseComplexType(le.getComplexType(), el.getSons());
         }
      }
   }

   private void parseExtensionType(final ExtensionType et, final List<ElParsed> elements) {
      if (null != et) {
         ElParsed el = new ElParsed(pfx, XsdType.ET);
         elements.add(el);
         // extension
         extractBase(et.getBase(), el);
         extractAnnotation(et.getAnnotation(), et.getOtherAttributes(), el);
         extractAttributes(et.getAttributeOrAttributeGroup(), el);

         if (null != et.getAll()) {
            Group ae = et.getAll();
            parseGroup(ae, el.getSons());

         } else if (null != et.getChoice()) {
            Group ch = et.getChoice();
            parseGroup(ch, el.getSons());

         } else if (null != et.getGroup()) {
            Group gr = et.getGroup();
            parseGroup(gr, el.getSons());

         } else if (null != et.getSequence()) {
            Group sq = et.getSequence();
            parseGroup(sq, el.getSons());
         }
      }
   }

   private void parseFacets(final List<Object> facets, final List<ElParsed> elements) {
      if (null != facets) {
         for (Object obj : facets) {
            if (null != obj) {
               ElParsed el = new ElParsed(pfx, XsdType.FC);
               elements.add(el);

               if (obj instanceof TotalDigits) {
                  // Specifies the maximum number of decimal places allowed.
                  // Must be equal to or greater than zero
                  TotalDigits fc = (TotalDigits) obj;
                  el.setName("TotalDigits: " + fc.getValue());
                  extractAnnotation(fc.getAnnotation(), fc.getOtherAttributes(), el);

               } else if (obj instanceof Pattern) {
                  // pattern Defines the exact sequence of characters that are
                  // acceptable
                  Pattern fc = (Pattern) obj;
                  el.setName("Pattern: " + fc.getValue());
                  extractAnnotation(fc.getAnnotation(), fc.getOtherAttributes(), el);

               } else if (obj instanceof WhiteSpace) {
                  // whiteSpace Specifies how white space (line feeds, tabs,
                  // spaces, and carriage returns) is handled
                  WhiteSpace fc = (WhiteSpace) obj;
                  el.setName("WhiteSpace: " + fc.getValue());
                  extractAnnotation(fc.getAnnotation(), fc.getOtherAttributes(), el);

               } else if (obj instanceof NoFixedFacet) {
                  NoFixedFacet fc = (NoFixedFacet) obj;
                  el.setName("NoFixedFacet: " + fc.getValue());
                  extractAnnotation(fc.getAnnotation(), fc.getOtherAttributes(), el);

               } else if (obj instanceof Facet) {
                  Facet fc = (Facet) obj;
                  el.setName("Facet: " + fc.getValue());
                  extractAnnotation(fc.getAnnotation(), fc.getOtherAttributes(), el);
               } else if (obj instanceof JAXBElement) {
                  // Enumeration Defines a list of acceptable values fraction
                  JAXBElement fc = (JAXBElement) obj;

                  if (fc.getValue() instanceof NumFacet) {
                     NumFacet nf = (NumFacet) fc.getValue();
                     el.setName(fc.getName().getLocalPart() + ": " + nf.getValue());
                  }
                  // } else if (obj instanceof TotalLength) {
                  // Specifies the exact number of characters or list items
                  // allowed.
                  // Must be equal to or greater than zero
                  // } else if (obj instanceof MaxExclusive) {
                  // maxExclusive Specifies the upper bounds for numeric values
                  // Must be less than this value
                  // } else if (obj instanceof MaxInclusive) {
                  // maxInclusive Specifies the upper bounds for numeric values
                  // Must be less than or equal to this value
                  // } else if (obj instanceof MaxLength) {
                  // maxLength Specifies the maximum number of characters or
                  // list items allowed.
                  // Must be equal to or greater than zero
                  // } else if (obj instanceof MinExclusive) {
                  // minExclusive Specifies the lower bounds for numeric values
                  // Must be greater than this value
                  // } else if (obj instanceof MinInclusive) {
                  // minInclusive Specifies the lower bounds for numeric values
                  // Must be greater than or equal to this value
                  // } else if (obj instanceof MinLength) {
                  // minLength Specifies the minimum number of characters or
                  // list items allowed.
                  // Must be equal to or greater than zero
               }
            }
         }
      }
   }

   private void parseGroup(final Group gr, final List<ElParsed> elements) {
      if (null != gr) {
         // parseo name
         // el.setName(pfx + gr.getName());
         // tipo
         // extractType(gr.getRef(), true, el);
         // cardinalidad del elemento
         // extractCardinalidad(gr.getMinOccurs(), gr.getMaxOccurs(), el);
         // anotaciones del elemento
         // extractAnnotation(gr.getAnnotation(), gr.getOtherAttributes(), el);

         // hijos del elemento
         if (null != gr.getParticle()) {
            for (Object obj : gr.getParticle()) {
               // para cada elemento de la sequence
               if (obj instanceof JAXBElement) {
                  JAXBElement je = (JAXBElement) obj;
                  if (null != je.getValue()) {
                     if (je.getValue() instanceof LocalElement) {
                        LocalElement le = (LocalElement) je.getValue();
                        parseElement(le, elements);
                     }
                     if (je.getValue() instanceof ExplicitGroup) {
                        ExplicitGroup eg = (ExplicitGroup) je.getValue();
                        parseGroup(eg, elements);
                     }
                  }
               }
            } // FIN para cada elemento de la sequence
         }

      }
   }

   private void parseRestrictionType(final RestrictionType rt, final List<ElParsed> elements) {
      if (null != rt) {
         ElParsed el = new ElParsed(pfx, XsdType.RT);
         elements.add(el);
         // restriction
         extractBase(rt.getBase(), el);
         extractAnnotation(rt.getAnnotation(), rt.getOtherAttributes(), el);
         extractAttributes(rt.getAttributeOrAttributeGroup(), el);

         if (null != rt.getAll()) {
            Group ae = rt.getAll();
            parseGroup(ae, el.getSons());

         } else if (null != rt.getChoice()) {
            Group ch = rt.getChoice();
            parseGroup(ch, el.getSons());

         } else if (null != rt.getGroup()) {
            Group gr = rt.getGroup();
            parseGroup(gr, el.getSons());

         } else if (null != rt.getSequence()) {
            Group sq = rt.getSequence();
            parseGroup(sq, el.getSons());

         } else if (null != rt.getFacets()) {
            parseFacets(rt.getFacets(), el.getFacets());

         } else if (null != rt.getFacets()) {
            parseSimpleType(rt.getSimpleType(), el.getSons());
         }
      }
   }

   private void parseSimpleType(final SimpleType le, final List<ElParsed> elements) {
      if (null != le) {
         ElParsed el = new ElParsed(pfx, XsdType.ST);
         elements.add(el);
         // otros campos del elemento xml
         le.getFinal();
         le.getList();
         // name
         el.setName(le.getName());
         // anotaciones del elemento
         extractAnnotation(le.getAnnotation(), le.getOtherAttributes(), el);
         // hijos del elemento
         // simpletype - retriction
         if (null != le.getRestriction()) {
            Restriction er = le.getRestriction();
            extractBase(er.getBase(), el);
            extractAnnotation(er.getAnnotation(), er.getOtherAttributes(), el);
            if (null != er.getFacets())
               parseFacets(er.getFacets(), el.getFacets());
            if (null != er.getSimpleType())
               parseSimpleType(er.getSimpleType(), el.getSons());
         }
         // simpletype - union
         if (null != le.getUnion()) {
            Union ur = le.getUnion();
            extractAnnotation(ur.getAnnotation(), ur.getOtherAttributes(), el);
            if (null != ur.getMemberTypes()) {
               for (QName qn : ur.getMemberTypes()) {
                  ElParsed son = new ElParsed(pfx, XsdType.UM);
                  extractType(qn, false, son);
                  el.getSons().add(son);
               }
            } else if (null != ur.getSimpleType()) {
               for (SimpleType st : ur.getSimpleType()) {
                  parseSimpleType(st, el.getSons());
               }
            }
         }
      }
   }
}
