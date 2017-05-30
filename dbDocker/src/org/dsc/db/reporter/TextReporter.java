package org.dsc.db.reporter;

import org.dsc.db.model.CrSchema;

/**
 * @author BIMALOSE
 */
public final class TextReporter implements IReporter {
	private static int COLUMN_TAB;

	private boolean verbose = false;

	public TextReporter(boolean verbose) {
		this.verbose = verbose;
	}

	public void print(String string) {
		System.out.println(string);
	}

	public void headSchema(final Object obj) throws Exception {
		if (verbose) {
			System.out.println(LINE_SEP);
			System.out.println(obj);
			System.out.println(LINE_SEP);
		}
	}

	public void footSchema() {
	}

	public void headTable(final Object obj) throws Exception {
		if (verbose) {
			System.out.println(LINE_SEP);
			System.out.println(TAB + obj);
		}
	}

	public void footTable() {
	}

	public void headView(final Object obj) {
		if (verbose) {
			System.out.println(LINE_SEP);
			System.out.println(TAB + obj + " (VIEW)");
		}
	}

	public void descView(String string) {
		if (verbose) {
			System.out.println(TAB2 + " AS_SQL: " + string);
		}
	}

	public void footView() {
	}

	public void printPk(String string) {
		if (verbose) {
			System.out.print(TAB2 + " PK " + string + ": ");
		}
	}

	public void descPk(String string) {
		if (verbose) {
			System.out.println(" " + string);
		}
	}

	public void printFk(String string) {
		if (verbose) {
			System.out.print(TAB2 + " FK " + string + ": ");
		}
	}

	public void descFk(String string) {
		if (verbose) {
			System.out.println(" " + string);
		}
	}

	public void printIx(String string) {
		if (verbose) {
			System.out.print(TAB2 + " IX " + string + ": ");
		}
	}

	public void descIx(String string) {
		if (verbose) {
			System.out.println(" " + string);
		}
	}

	public void printCk(String string) {
		if (verbose) {
			System.out.print(TAB2 + " CK " + string + ": ");
		}
	}

	public void descCk(String string) {
		if (verbose) {
			System.out.println(" " + string);
		}
	}

	public void printSy(String string) {
		if (verbose) {
			System.out.println(TAB2 + " SY " + string);
		}
	}

	public void headCol(String string) {
		if (verbose) {
			System.out.print(TAB2 + " - " + string);
			COLUMN_TAB = MAX_COLNAME - string.length();
		}
	}

	public void printColType(String string) {
		if (verbose) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < COLUMN_TAB; i++) {
				sb.append(" ");
			}
			System.out.print(sb.toString() + " " + string);
			COLUMN_TAB = MAX_COLTYPE - string.length();
		}
	}

	public void printColAttr(String string) {
		if (verbose) {
			if (null == string || string.length() <= 0) {
				string = "-";
			}
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < COLUMN_TAB; i++) {
				sb.append(" ");
			}
			System.out.print(sb.toString() + " (" + string + ")");
			COLUMN_TAB = MAX_COLATTR - string.length() + 2;
		}
	}

	public void printColComm(String string) {
		if (verbose) {
			if (null != string && string.length() > 0) {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < COLUMN_TAB; i++) {
					sb.append(" ");
				}
				System.out.print(sb.toString() + " -- " + string);
			}
		}
	}

	public void footCol() {
	}

	public void generate(CrSchema crschema) throws Exception {
		// TODO Auto-generated method stub
	}
}
