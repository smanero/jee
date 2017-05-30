package com.dev.cmielke.util;

import org.apache.commons.lang.StringUtils;

public final class StringUtilities {
	public static final String[] IGNORED_WORDS = new String[] { "von", "und", "zu" };

	private StringUtilities() {}
	
	public static String capitalizeWords(String text) {
		return capitalizeWords(text, StringUtilities.IGNORED_WORDS);
	}

	public static String capitalizeWords(String text, String[] words) {
		text = text.toLowerCase();
		String retValue = "";

		String[] parts = text.split(" ");
		for (int index = 0; index < parts.length; index++) {
			if (!contains(parts[index], words)) {
				retValue += StringUtils.capitalize(parts[index]);
			} else {
				retValue += parts[index];
			}
			if (index + 1 < parts.length) {
				retValue += " ";
			}
		}

		return retValue;
	}

	private static boolean contains(String text, String[] words) {
		boolean retValue = false;

		for (String string : words) {
			if (text.contains(string) || text.equals(string)) {
				retValue = true;
				break;
			}
		}

		return retValue;
	}
	
	/**
	 * Extrahiert eine Zahl aus den angegebenen String.
	 * 
	 * @param text
	 * @return Zahl oder <code>null</code> wenn keine Ziffern gefunden wurden
	 * @throws IllegalArgumentException wenn im text meherere Zahlen gefunden wurden
	 */
	public static Integer extractNumber(String text) {
		int pos = -1;
		int end = -1;

		for(int i=0 ; i<text.length() ;i++) {
			if(pos==-1 && -1 != "0123456789".indexOf(text.charAt(i))) {
				pos = i;
			}else if(pos > -1 && -1 != "0123456789".indexOf(text.charAt(i))) {
				end = i;
			}else if(pos>-1 && end>-1 && -1 != "0123456789".indexOf(text.charAt(i))) {
				throw new IllegalArgumentException("String enthält mehr als eine Zahl >"+text+"<");
			}			
		}
		
		if(pos>-1) {
			return Integer.valueOf(text.substring(pos,end+1));
		}
		return null;
	}
}
