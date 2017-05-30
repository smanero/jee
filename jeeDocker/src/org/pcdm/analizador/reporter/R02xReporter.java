package org.pcdm.analizador.reporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

public class R02xReporter {
	private String outputPath;

	private PrintStream loteOut;
	private PrintStream profileOut;
	private PrintStream csvOut = null;

	public R02xReporter(final File outputDir, boolean reportCsv) throws Exception {
		this.outputPath = outputDir.getAbsolutePath();
		File reportAll = new File(outputPath + File.separator + "report-txt.txt");
		this.loteOut = new PrintStream(new FileOutputStream(reportAll));

		if (reportCsv) {
			File csvFile = new File(outputPath + File.separator + "report-csv.csv");
			FileOutputStream csvSt = new FileOutputStream(csvFile);
			this.csvOut = new PrintStream(csvSt);
		}
	}

	public void setProfileReport(final String app) throws Exception {
		if (null != profileOut) {
			this.profileOut.close();
			this.profileOut = null;
		}
		File reportApp = new File(outputPath + File.separator + app + "-report.txt");
		FileOutputStream appSt = new FileOutputStream(reportApp);
		this.profileOut = new PrintStream(appSt);
	}

	public void lote(final List<String> lines) {
		for (String line : lines) {
			loteOut.println(line);
		}
	}

	public void profile(final List<String> lines) {
		if (null != profileOut) {
			for (String line : lines) {
				profileOut.println(line);
			}
		}
	}

	public void csv(final List<String> lines) {
		if (null != csvOut) {
			for (String line : lines) {
				csvOut.println(line);
			}
		}
	}

	public void close() {
		loteOut.flush();
		loteOut.close();
		if (null != profileOut) {
			profileOut.flush();
			profileOut.close();
		}
		if (null != csvOut) {
			csvOut.flush();
			csvOut.close();
		}
	}
}
