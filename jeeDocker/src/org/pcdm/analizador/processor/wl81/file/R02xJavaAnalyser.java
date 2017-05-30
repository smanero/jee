package org.pcdm.analizador.processor.wl81.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.pcdm.analizador.context.R02xContext;
import org.pcdm.analizador.processor.R02xAbstractAnalyser;


public class R02xJavaAnalyser extends R02xAbstractAnalyser {
	public static final String REGEX = ".*Helper.java";

	private static final String REGEX_PACKAGE = "package";
	private static final String REGEX_IMPORT_DEC = ".*import.*;";
	private static final String REGEX_CLASS_DEC = ".*class.*";
	private static final String REGEX_CONST_DEC = ".*public.*static.*final.*String.*=.*;";
	private static final String REGEX_MEMBER_DEC = ".*[public|private]?[static|final]?.*;";
	private static final String REGEX_METHOD_DEC = ".*[public|private|protected].*[(].*[)].*";

	public R02xJavaAnalyser(final R02xContext ctx) {
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
			
			} else if (strLine.matches(REGEX_MEMBER_DEC)) {

			} else if (strLine.matches(REGEX_METHOD_DEC)) {
				numMethods++;
				String methodName = strLine.replaceAll("[\t| ]*public", "");
				methodName = methodName.replaceAll("[\t| ]*private", "");
				methodName = methodName.replaceAll("[\t| ]*protected", "");
				methodName = methodName.replaceAll("[(].*[)].*", "");
				
				StringBuffer methodBody = new StringBuffer();
				int countLlaves = 0;
				if (strLine.indexOf("{") != -1) {
					countLlaves++;
					methodBody.append(strLine);
				}
				
				do {
					// method body
					strLine = br.readLine();
					if (null == strLine) {
						break;
					}
					strLine = strLine.trim();
					if (strLine.indexOf("{") != -1) {
						countLlaves++;
					}
					if (strLine.indexOf("}") != -1) {
						countLlaves--;
					}
					methodBody.append(strLine);					
				} while (countLlaves != 0);
				
				strLine = methodBody.toString();
				for (String searchStr : searchForUseList) {
					if (strLine.indexOf(searchStr) != -1) {
						// encontrada
						String constantName = ctx.currentFile().getNameExt()[0] + ":" + searchStr + ":" + methodName;
						ctx.currentFile().report(constantName);
					}
				}
				
			} else {
				// otros elementos de la clase
			}
		}
		br.close();
		fis.close();

		if (numMethods > 0) {
			ctx.currentFile().count("methods", numMethods);
		}
	}
}
