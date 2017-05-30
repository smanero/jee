package org.pcdm.analizador.processor.wl81.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.pcdm.analizador.context.R02xContext;
import org.pcdm.analizador.processor.R02xAbstractAnalyser;


public class R02xJavaConstAnalyser extends R02xAbstractAnalyser {
	public static final String REGEX = ".*Const.java";

	private static final String REGEX_PACKAGE = "package";
	private static final String REGEX_IMPORT_DEC = ".*import.*;";
	private static final String REGEX_CLASS_DEC = ".*class.*";
	private static final String REGEX_CONST_DEC = ".*public.*static.*final.*String.*=.*;";
	private static final String REGEX_MEMBER_DEC = ".*[public|private]?[static|final]?.*;";
	private static final String REGEX_METHOD_DEC = ".*[public|private|protected].*[(].*[)].*";

	public R02xJavaConstAnalyser(final R02xContext ctx) {
		super(ctx);
	}

	public void analyse() throws Exception {
		
		List<String> searchForUseList = ctx.getProps().getSearchForUseList();
		
		long numMethods = 0;
		FileInputStream fis = new FileInputStream(ctx.currentFile().getFile());
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String strLine;
		
		while ((strLine = br.readLine()) != null) {
			strLine = strLine.trim();
			
			if (strLine.indexOf(REGEX_PACKAGE) != -1) {
//				String packageName = "PKG " + strLine.substring(REGEX_PACKAGE.length() + 1, strLine.length() - 1);
//				ctx.currentFile().report(packageName);
				
			} else if (strLine.matches(REGEX_IMPORT_DEC)) {
			} else if (strLine.matches(REGEX_CLASS_DEC)) {

			} else if (strLine.matches(REGEX_CONST_DEC)) {
				if (null != searchForUseList && searchForUseList.size() > 0) {
					int idxString = strLine.indexOf("\"");
					int idxEqual = strLine.lastIndexOf("\"");
					String strConstValue = strLine.substring(idxString+1, idxEqual);
					if (searchForUseList.contains(strConstValue)) {
						// si encontrada la cadena como constante
						strLine = strLine.replaceAll(".*public.*static.*String ", "");
						strLine = strLine.replaceAll("=", ";");
						String constantName = ctx.currentFile().getNameExt()[0] + "." + strLine;
						ctx.currentFile().report(constantName);
					}
				}
			
			} else if (strLine.matches(REGEX_MEMBER_DEC)) {

			} else if (strLine.matches(REGEX_METHOD_DEC)) {
//				numMethods++;
//
//				strLine = strLine.replaceAll("java[.]lang[.]", "").replaceAll("java[.]util[.]", "")
//				      .replaceAll("java[.]io[.]", "");
//
//				StringBuffer sb = new StringBuffer();
//				String[] result = strLine.split("[\\s(),;]");
//				for (int i = 0; i < result.length; i++) {
//					if (!"".equals(result[i]) && !" ".equals(result[i])) {
//						sb.append(" : ").append(result[i]);
//					}
//				}
//				ctx.currentFile().report(numMethods + ") " + sb.toString());
			} else {
				// method body
			}
		}
		br.close();
		fis.close();

		if (numMethods > 0) {
			ctx.currentFile().count("jcx-methods", numMethods);
		}
	}
}
