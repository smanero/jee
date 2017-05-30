package com.dev.cmielke.gui.dialogs;

import static com.dev.cmielke.gui.util.DialogConstants.ADD_PARAM_NAME_PERRY_RHODAN_BOOK;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.dev.cmielke.beans.PerryRhodanBook;
import com.dev.cmielke.gui.dialogs.components.DialogBox;
import com.dev.cmielke.test.PerryRhodanBookExample;
import com.dev.cmielke.util.LoggingUtils;

public class DialogFactory
{
	private static final Logger log = Logger.getLogger(DialogFactory.class);
	
	public static DialogBox getMetaDataDialog(PerryRhodanBook book)
	{
		HashMap<String, Object> addParams = new HashMap<String, Object>();
		addParams.put(ADD_PARAM_NAME_PERRY_RHODAN_BOOK, book);
		return new MetaDataDialog("Bearbeitung der Meta-Daten", null, addParams);
	}
	
	public static void main(String[] args)
	{
		LoggingUtils.configureLog4J();
		PerryRhodanBook book = PerryRhodanBookExample.getInstance();
		book.getCharacters().clear();
		
		log.debug(LoggingUtils.filteredObjectToString(book));
		
		DialogBox dialog = DialogFactory.getMetaDataDialog(book);
		int code = dialog.showDialog();
		log.debug("Dialog ends with ReturnCode: ["+ code +"]");
		
		log.debug(LoggingUtils.filteredObjectToString(book));
		
		
	}
}
