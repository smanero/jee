package org.pcdm.analizador.processor.wl81.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.pcdm.analizador.context.R02xConst;
import org.pcdm.analizador.context.R02xContext;
import org.pcdm.analizador.processor.R02xAbstractAnalyser;

public class R02xApplicationXmlAnalyser extends R02xAbstractAnalyser {
	public static final String REGEX = "application[.]xml";

	public R02xApplicationXmlAnalyser(final R02xContext ctx) {
		super(ctx);
	}

	public void analyse() throws Exception {
		// filtrado de application.xml para adecuar a namespace de xmlbean
		File temp = new File("temp");
		FileInputStream fis = new FileInputStream(this.ctx.currentProfile().getAppXml().getAbsolutePath());
		FileOutputStream fos = new FileOutputStream(temp);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis, R02xConst.UTF8));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, R02xConst.UTF8));
		String strLine;
		while ((strLine = br.readLine()) != null) {
			strLine = strLine.trim();
			if ("<application>".equals(strLine)) {
				strLine = "<application xmlns='http://java.sun.com/xml/ns/j2ee' >";
			}
			bw.write(strLine);
		}
		br.close();
		fis.close();
		bw.close();
		fos.close();
		// procesamiento java-sax del application.xml del proyecto
		List<String> lstMod = new ArrayList<String>(0);
		fis = new FileInputStream(temp);
//		XmlApplicationDocumentBean xApp = XmlApplicationDocumentBean.Factory.parse(fis);
//		for (XmlModuleTypeBean xMod : xApp.getApplication().getModuleArray()) {
//			if (null != xMod.getJava()) {
//				lstMod.add(xMod.getJava().getStringValue().replaceAll("[.]jar", ""));
//			} else if (null != xMod.getEjb()) {
//				lstMod.add(xMod.getEjb().getStringValue().replaceAll("[.]jar", ""));
//			} else if (null != xMod.getWeb()) {
//				lstMod.add(xMod.getWeb().getWebUri().getStringValue().replaceAll("[.]war", "") + " ["
//				      + xMod.getWeb().getContextRoot().getStringValue() + "]");
//			}
//		}
		fis.close();
		// ordenar y reportar
		Collections.sort(lstMod);
		for (String smod : lstMod) {
			ctx.currentModule().report(" EARMOD: " + smod);
		}
	}

}
