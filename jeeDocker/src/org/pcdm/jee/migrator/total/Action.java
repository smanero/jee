package org.pcdm.jee.migrator.total;

import java.util.List;

public class Action {
   private String appSrc;
   private String appDst;
   private List<String> lstModules;
   private List<String> lstClasses;

   public Action(String appSrc, String appDst, List<String> lstModules, List<String> lstClasses) {
      this.appSrc = appSrc;
      this.appDst = appDst;
      this.lstModules = lstModules;
      this.lstClasses = lstClasses;
   }

   public void setAppSrc(String appSrc) {
      this.appSrc = appSrc;
   }

   public String getAppSrc() {
      return appSrc;
   }

   public void setAppDst(String appDst) {
      this.appDst = appDst;
   }

   public String getAppDst() {
      return appDst;
   }

   public void setLstModules(List<String> lstModules) {
      this.lstModules = lstModules;
   }

   public List<String> getLstModules() {
      return lstModules;
   }

   public void setLstClasses(List<String> lstClasses) {
      this.lstClasses = lstClasses;
   }

   public List<String> getLstClasses() {
      return lstClasses;
   }

}
