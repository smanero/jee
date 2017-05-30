package org.pcdm.jee.docgen.model;

import java.util.List;

public class ElParsed implements Comparable<ElParsed>, Cloneable {

   public int deep = -1;
   
   private String pfx = null;
   private XsdType xtype = null;

   private String name = null;
   private ElType type = null;
   private boolean ref = false;
   private boolean fixed = false;

   private List<String> attributes = null;
   private XsdCard card = XsdCard.NUL;
   private String anotation = null;

   private ElType base = null;
   private ElParsedList facets = null;

   private ElParsedList sons = null;

   public ElParsed(String _pfx, XsdType _xtype) {
      this.pfx = _pfx;
      this.xtype = _xtype;
   }

   public String pfx() {
      return this.pfx;
   }

   public XsdType xtype() {
      return this.xtype;
   }

   public String getName() {
      return name;
   }

   public ElType getType() {
      return type;
   }

   public String getAnotation() {
      return anotation;
   }

   public List<String> getAttributes() {
      return attributes;
   }

   public ElType getBase() {
      return this.base;
   }

   public XsdCard getCard() {
      return card;
   }

   public List<ElParsed> getFacets() {
      if (null == facets) {
         facets = new ElParsedList();
      }
      return facets;
   }

   public List<ElParsed> getSons() {
      if (null == sons) {
         sons = new ElParsedList();
      }
      return sons;
   }

   public boolean isFixed() {
      return fixed;
   }

   public boolean isRef() {
      return ref;
   }

   public void setAnotation(String anotation) {
      this.anotation = anotation;
   }

   public void setAttributes(List<String> attributes) {
      this.attributes = attributes;
   }

   public void setBase(ElType base) {
      this.base = base;
   }

   public void setCard(XsdCard card) {
      this.card = card;
   }

   public void setFacets(ElParsedList facets) {
      this.facets = facets;
   }

   public void setFixed(boolean fixed) {
      this.fixed = fixed;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setSons(ElParsedList sons) {
      this.sons = sons;
   }

   public void setType(ElType type, boolean ref) {
      this.type = type;
      this.ref = ref;
   }

   public String key() {
      String key = null;
      if (null != name) {
         key = ((null != this.pfx && this.pfx.length() > 0) ? this.pfx + ":" : "") + this.name;
      } else if (null != type) {
         key = this.type.toString();
      }
      return key;
   }

   public ElParsed cloneMe() {
      try {
         return (ElParsed) this.clone();
      } catch (CloneNotSupportedException e) {
         e.printStackTrace();
         return null;
      }
   }
   
   @Override
   public String toString() {
      String sxtype = (null != this.xtype ? this.xtype.sym() : "null");
      String ssons = (null != this.sons ? ""+this.sons.size() : "null");
      String sfacets = (null != this.facets ? ""+this.facets.size() : "null");
      
      StringBuffer sb = new StringBuffer();
      sb.append(sxtype).append(":").append(this.pfx).append(":").append(this.name);
      if (null != this.type) {
         sb.append((this.ref ? ":R:" : ":T:")).append(this.type);
      }
      if (null != this.base) {
         sb.append(":B:").append(this.base);
      }
      sb.append(":C:").append(this.card)
        .append(":H:").append(ssons)
        .append(":F:").append(sfacets);
      return sb.toString();
   }

   @Override
   public int compareTo(final ElParsed o) {
      int cmp = 0;
      if (null != this.pfx) {
         cmp = this.pfx.compareTo(o.pfx);
      }
      if (0 == cmp && null != this.xtype) {
         cmp = this.xtype.compareTo(o.xtype);
      }
      if (0 == cmp && null != this.name) {
         cmp = this.name.compareTo(o.name);
      }
      if (0 == cmp && null != this.type) {
         cmp = this.type.compareTo(o.type);
      }
      if (0 == cmp && null != this.attributes) {
         if (this.attributes.size() == o.attributes.size()) {
            // list string igual a otra
            // cmp = this.attributes.compareTo(o.attributes);
         } else {
            cmp = (this.attributes.size() > o.attributes.size() ? 1 : -1);
         }
      }
      if (0 == cmp && XsdCard.NUL != this.card) {
         cmp = this.card.compareTo(o.card);
      }
      if (0 == cmp && null != this.anotation) {
         cmp = this.anotation.compareTo(o.anotation);
      }
      if (0 == cmp && null != this.base) {
         cmp = this.base.compareTo(o.base);
      }
      if (0 == cmp && null != this.facets) {
         if (this.facets.size() == o.facets.size()) {
            // list string igual a otra
            cmp = this.facets.compareTo(o.facets);
         } else {
            cmp = (this.facets.size() > o.facets.size() ? 1 : -1);
         }
      }
      if (0 == cmp && null != this.sons) {
         if (this.sons.size() == o.sons.size()) {
            // list string igual a otra
            cmp = this.sons.compareTo(o.sons);
         } else {
            cmp = (this.sons.size() > o.sons.size() ? 1 : -1);
         }
      }
      return cmp;
      // if (null != this.ref)
      // if (null != this.fixed)
   }
}