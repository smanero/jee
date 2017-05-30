package org.pcdm.util;


public abstract class R02xUtil {
	public static String capitalLetter(String string) {
//		char[] c = string.toLowerCase().toCharArray();
		char[] c = string.toCharArray();
		if (c.length > 0) {
			c[0] = Character.toUpperCase(c[0]);
			return new String(c);
		} else {
			return "";
		}
		
	}
}
