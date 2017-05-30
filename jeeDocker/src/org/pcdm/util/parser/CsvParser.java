package org.pcdm.util.parser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

public class CsvParser {
   public static final String BASEPATH = "D:/tmp";

   public static void main(String[] args) {
      File csvFile = new File(BASEPATH + "/20140611-uso.webservices.csv");
      CsvParser cp = new CsvParser();
      cp.loadCsv(csvFile);
   }

   private static final Map<String, String> wsPublic = new HashMap<String, String>();
   private static final Map<String, String> wsPrivate = new HashMap<String, String>();

   public void loadCsv(final File csvFile) {
      try {
         wsPublic.clear();
         wsPrivate.clear();
         CSVReader reader = new CSVReader(new FileReader(csvFile.getAbsolutePath()));
         String[] nextLine;
         while ((nextLine = reader.readNext()) != null) {
            if (nextLine.length == 4) {
               if (null != nextLine[3] && !"".equals(nextLine[3])) {
                  // webservice publico
                  wsPublic.put(key(nextLine[0], nextLine[1]), nextLine[3]);
               } else {
                  // webservice privado
                  wsPrivate.put(key(nextLine[0], nextLine[1]), nextLine[2]);
               }
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   @SuppressWarnings("unchecked")
   public List<String>[] filter(final String _svc, final List<String> _ops) {
      List<String>[] ops = new List[2];
      ops[0] = new ArrayList<String>();
      ops[1] = new ArrayList<String>();
      for (String ope : _ops) {
         // filtrar publicas
         if (wsPublic.containsKey(CsvParser.key(_svc, ope))) {
            ops[0].add(ope);
         } else {
            ops[1].add(ope);
         }
      }
      return ops;
   }

   public static String key(final String svc, final String ope) {
      return svc + "." + ope;
   }
}
