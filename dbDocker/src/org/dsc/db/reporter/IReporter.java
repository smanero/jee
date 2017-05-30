package org.dsc.db.reporter;

import org.dsc.db.model.CrSchema;

/**
 * @author BIMALOSE
 */
public interface IReporter {

	public static final String TAB = "   ";
	public static final String TAB2 = TAB + TAB;
	public static final String TAB3 = TAB2 + TAB;
	public static final String LINE_SEP = "-------------------------------------------------------";
	public static final int MAX_COLNAME = 35;
	public static final int MAX_COLTYPE = 15;
	public static final int MAX_COLATTR = 10;

	public void print(String string);
	
	public void generate(final CrSchema crschema) throws Exception;
}
