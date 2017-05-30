package org.pcdm.analizador.processor.wl81.file;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.pcdm.analizador.context.R02xContext;
import org.pcdm.analizador.processor.R02xAbstractAnalyser;


public class R02xConfigPrpAnalyser extends R02xAbstractAnalyser {
	public static final String REGEX = ".*[.]properties";

	public R02xConfigPrpAnalyser(final R02xContext ctx) {
		super(ctx);
	}

	public void analyse() throws Exception {
		Set<String> mapRefJpd = new HashSet<String>(0);
		Set<String> mapRefJpdMethod = new HashSet<String>(0);

		String strLine;
		BufferedReader br = ctx.currentFile().getBufferedReader();
		while ((strLine = br.readLine()) != null) {
			if (strLine.matches(".*Q99.*") || strLine.matches(".*N61.*")) {
				String refJpd = strLine.substring(strLine.indexOf("=") + 1).trim();
				if (!"".equals(refJpd)) {
					mapRefJpd.add(refJpd);
				}

			} else if (strLine.matches(".*[jJ]pd.*[mM]ethod.*") || strLine.matches(".*[iI]ntegration.*[mM]ethod.*")) {
				String refJpdMethod = strLine.substring(strLine.indexOf("=") + 1).trim();
				if (!"".equals(refJpdMethod)) {
					mapRefJpdMethod.add(refJpdMethod);
				}
			}
		}
		br.close();

		// resumen general
		List<String> lst = new ArrayList<String>(mapRefJpd);
		Collections.sort(lst);
		for (String refJpd : lst) {
			ctx.currentFile().report(" - " + refJpd);
		}
	}
}
