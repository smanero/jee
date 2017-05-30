package org.pcdm.analizador.context;

import java.io.File;
import java.util.List;

public final class R02xProperties {
	// directorio de salida del analisis
	private File outputDir;

	// lista de cadenas a buscar en codigo java
	private List<String> lstSearchFor =  null;

	// directorio de salida de la migracion
	private File migrDir;
	// prefijo appCode de salida de la migracion
	private String migrAppCode;
	private boolean migrResetDir;
	
	// generacion de estadisticas y analisis
	private boolean reportNotAnalysed = false;
	private boolean reportStats = false;
	private boolean reportCsv = false;
	private boolean reportCsvParcial = false;

	public void reportStats(boolean reportStats) {
		this.reportStats = reportStats;
	}
	public boolean reportStats() {
		return reportStats;
	}
	
	public void reportNotAnalysed(boolean reportNotAnalysed) {
		this.reportNotAnalysed = reportNotAnalysed;
	}
	public boolean reportNotAnalysed() {
		return reportNotAnalysed;
	}	

	public void reportCsv(boolean reportCsv) {
		this.reportCsv = reportCsv;
	}
	public boolean reportCsv() {
		return reportCsv;
	}

	public boolean reportCsvParcial() {
		return reportCsvParcial;
	}
	
	public void setOutputDir(final File outputDir) {
		this.outputDir = outputDir;
	}
	public File getOutputDir() {
		return this.outputDir;
	}
	
	public void setSearchForUseList(final List<String> lstSearchFor) {
		this.lstSearchFor = lstSearchFor;
	}
	public List<String> getSearchForUseList() {
		return this.lstSearchFor;
	}
	
	public void setMigrDir(final File migrDir) {
		this.migrDir = migrDir;
	}
	public File getMigrDir() {
		return this.migrDir;
	}
	
	public void setMigrAppCode(final String migrAppCode) {
		this.migrAppCode = migrAppCode.toLowerCase();
	}
	public String getMigrAppCode() {
	   return this.migrAppCode;
   }
	
	public void setMigrResetDir(boolean migrResetDir) {
		this.migrResetDir = migrResetDir;
	}
	public boolean getMigrResetDir() {
		return this.migrResetDir;
	}	
}
