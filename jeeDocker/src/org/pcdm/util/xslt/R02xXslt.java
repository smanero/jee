package org.pcdm.util.xslt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class R02xXslt {
	
	public static void transform(final File srcFile, final File dstFile, final File xsltFile)
	      throws Exception {
		PrintStream dstGen = new PrintStream(new FileOutputStream(dstFile));
		Source xmlSource = new StreamSource(srcFile);
		Result result = new StreamResult(dstGen);
		Source xsltSource = new StreamSource(xsltFile);
		// create an instance of TransformerFactory
		TransformerFactory transFact = TransformerFactory.newInstance();
		Transformer trans = transFact.newTransformer(xsltSource);
		trans.transform(xmlSource, result);
		dstGen.close();
	}
}
