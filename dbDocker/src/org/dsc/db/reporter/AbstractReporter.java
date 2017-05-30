package org.dsc.db.reporter;

import java.io.File;

import org.dsc.db.model.CrSchema;
import org.dsc.db.model.CrTable;
import org.dsc.db.model.CrView;


/**
 * @author BIMALOSE
 */
abstract class AbstractReporter implements IReporter {
	protected String schemaName;
	
	protected File outputFile;

	// tabla pivote
	protected CrTable crtable = null;
	protected CrView crview = null;
	
	protected String columnName;

	public AbstractReporter(final String outDir, final String schemaName) throws Exception {
		this.schemaName = schemaName;
		outputFile = new File(outDir + schemaName);
		outputFile.mkdirs();
	}
	
	public void print(String string) {
	}
	
	public void generate(final CrSchema crschema) throws Exception {
	}

	protected void headSchema(final Object obj) throws Exception {
	}
	
	protected void descView(String string) {
	}

	protected void footSchema() {
	}

	protected void headTable(final Object obj) throws Exception {
	}

	protected void footTable() {
	}

	protected void headView(final Object obj) {
	}

	protected void footView() {
	}

	protected void printPk(String string) {
	}

	protected void descPk(String string) {
	}

	protected void printFk(String string) {
	}

	protected void descFk(String string) {
	}

	protected void printIx(String string) {
	}

	protected void descIx(String string) {
	}

	protected void printCk(String string) {
	}

	protected void descCk(String string) {
	}

	protected void printSy(String string) {
	}

	protected void headCol(String string) {
		this.columnName = string;
	}

	protected void printColType(String string) {
	}

	protected void printColAttr(String string) {
	}

	protected void printColComm(String string) {
	}

	protected void footCol() {
	}
}
