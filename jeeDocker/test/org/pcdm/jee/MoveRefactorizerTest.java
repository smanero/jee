package org.pcdm.jee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pcdm.jee.migrator.total.Action;

public final class MoveRefactorizerTest {
   private static final File BASEPATH = new File("D:\\eadmin\\trunk\\aplic\\");

   private static List<Action> lstAction = new ArrayList<Action>();
   static {
      lstAction.add(new Action("R02z", "R02r", Arrays.asList("r02zEscScProcessEJB"), Arrays.asList("com/ejie/r02z/manager/sc")));
      lstAction.add(new Action("R02z", "R02s", Arrays.asList("r02zShpProcessEJB"), Arrays.asList("com/ejie/r02z/manager/shp")));
      lstAction.add(new Action("R02z", "R02e", Arrays.asList("r02zSrtProcessEJB"), null));
      
      lstAction.add(new Action("R02h", "R02w", Arrays.asList("r02hProceedingCatalogEJB"), null));
      lstAction.add(new Action("R02h", "R02w", Arrays.asList("r02hTaskManagerEJB"), null));
      lstAction.add(new Action("R02h", "R02w", Arrays.asList("r02hProcessingHelpSystemEJB"), null));
      lstAction.add(new Action("R02h", "R02w", Arrays.asList("r02hProceedingCatalogEJB"), null));
      lstAction.add(new Action("R02z", "R02w", Arrays.asList("r02zMassiveProcessEJB"), Arrays.asList("com/ejie/r02z/manager/massive")));
      lstAction.add(new Action("R02z", "R02w", Arrays.asList("r02zTramitacionProcessEJB"), Arrays.asList("com/ejie/r02z/manager/tramitacion")));
   }

   private static FileFilter ffSvn = new FileFilter() {
      public boolean accept(File directory) {
         return !directory.getAbsolutePath().endsWith("svn");
      }
   };

   public static void main(String[] args) throws Exception {
      for (Action action : lstAction) {
         String appSrc = action.getAppSrc();
         String appDst = action.getAppDst();
         File pathSrc = null;
         File pathDst = null;
         MoveRefactorizerTest MR = new MoveRefactorizerTest();
         // lista de módulos a migrar
         if (null != action.getLstModules()) {
            pathSrc = new File(BASEPATH.getAbsolutePath() + File.separator + appSrc.toLowerCase() + File.separator + "aplic");
            pathDst = new File(BASEPATH.getAbsolutePath() + File.separator + appDst.toLowerCase() + File.separator + "aplic");
            for (String module : action.getLstModules()) {
               // para cada modulo a refactorizar
               if (pathSrc.exists()) {
                  MR.renamefactor(module, appSrc, pathSrc, appDst, pathDst);
               }
            }
         }
         // lista de classes en el EARClasses a migrar
         if (null != action.getLstClasses()) {
            pathSrc = new File(BASEPATH.getAbsolutePath() + File.separator + appSrc.toLowerCase() + File.separator + "aplic" + File.separator
                               + appSrc.toLowerCase() + "EARClasses" + File.separator + "src");
            pathDst = new File(BASEPATH.getAbsolutePath() + File.separator + appDst.toLowerCase() + File.separator + "aplic" + File.separator
                               + appDst.toLowerCase() + "EARClasses" + File.separator + "src");
            for (String tclass : action.getLstClasses()) {
               File tclassSrc = new File(pathSrc.getAbsolutePath() + File.separator + tclass);
               String itemDst = tclass.replaceAll(appSrc, appDst).replaceAll(appSrc.toUpperCase(), appDst.toUpperCase()).replaceAll(
                     appSrc.toLowerCase(), appDst.toLowerCase());
               File tclassDst = pathDst;
               String lastdir = "";
               for (String dir : itemDst.split("/")) {
                  tclassDst = new File(tclassDst.getAbsolutePath() + File.separator + dir);
                  if (!tclassDst.exists()) {
                     tclassDst.mkdir();
                  }
                  lastdir = dir;
               }

               MR.renamefactor(lastdir, appSrc, new File(tclassSrc.getParent()), appDst, new File(tclassDst.getParent()));
            }
         }
      }
   }

   private void renamefactor(final String itemSrc, final String appSrc, final File pathSrc, final String appDst, final File pathDst) throws Exception {
      File fileSrc = new File(pathSrc.getAbsolutePath() + File.separator + itemSrc);
      if (fileSrc.exists()) {
         String itemDst = itemSrc.replaceAll(appSrc, appDst).replaceAll(appSrc.toUpperCase(), appDst.toUpperCase()).replaceAll(appSrc.toLowerCase(),
               appDst.toLowerCase());
         File fileDst = new File(pathDst.getAbsolutePath() + File.separator + itemDst);
         if (fileSrc.isDirectory()) {
            // si archivo origen es directory
            fileDst.mkdir();
            for (File nxtFile : fileSrc.listFiles(ffSvn)) {
               renamefactor(nxtFile.getName(), appSrc, fileSrc, appDst, fileDst);
            }
         } else {
            // si archivo origen es fichero
            refactor(appSrc, fileSrc, appDst, fileDst);
         }
      }
   }

   private void refactor(final String appSrc, final File fileSrc, final String appDst, final File fileDst) throws Exception {
      if (fileDst.exists()) {
         fileDst.delete();
      }
      String strLine;
      FileInputStream fis = new FileInputStream(fileSrc);
      BufferedReader br = new BufferedReader(new InputStreamReader(fis));
      PrintWriter writer = new PrintWriter(fileDst);
      while ((strLine = br.readLine()) != null) {

         strLine = strLine.trim().replaceAll(appSrc, appDst).replaceAll(appSrc.toUpperCase(), appDst.toUpperCase()).replaceAll(appSrc.toLowerCase(),
               appDst.toLowerCase());
         // caso particular para la clase Config
         strLine = strLine.replaceAll(appDst + "Config", appDst.toUpperCase() + "Config");

         writer.println(strLine);
      }
      fis.close();
      writer.close();
   }
   
   private void delete(final File file) {
      if (file.exists()) {
         file.delete();
      }
   }
}
