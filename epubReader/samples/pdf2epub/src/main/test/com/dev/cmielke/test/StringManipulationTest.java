package com.dev.cmielke.test;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class StringManipulationTest
{

	private static Logger log = Logger.getLogger(StringManipulationTest.class);
	
	public static void main(String[] args)
	{
		BasicConfigurator.configure();
		
		String[] words = new String[]{"von", "und", "zu"};
		
		String s1 = "CLARK DALTON";
		String s2 = "HANS P. MUSTERMANN";
		String s3 = "Max von Karl";
		String s4 = "Peter von und zu Taubenuss";
		
		log.debug("S1:" + transform(s1, words));
		log.debug("S2:" + transform(s2, words));
		log.debug("S3:" + transform(s3, words));
		log.debug("S4:" + transform(s4, words));
	}

	private static String transform(String text, String[] words)
	{
		text = text.toLowerCase();
		String retValue = "";
		
		String[] parts = text.split(" ");
		for (int index = 0; index < parts.length; index++)
		{
			if(!contains(parts[index], words))
			{
				retValue += StringUtils.capitalize(parts[index]);
			}
			else
			{
				retValue += parts[index];
			}
			if(index + 1 < parts.length)
			{
				retValue += " ";
			}
		}
		
		return retValue;
	}

	private static boolean contains(String text, String[] words)
	{
		boolean retValue = false;
		
		for (String string : words)
		{
			if(text.contains(string) || text.equals(string))
			{
				retValue = true;
				break;
			}
		}
		
		return retValue;
	}
}
