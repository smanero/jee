package com.dev.cmielke.util;

import java.lang.reflect.Field;
import java.net.URL;

import org.apache.log4j.PropertyConfigurator;

import com.dev.cmielke.Start;

public class LoggingUtils
{
	public static void configureLog4J()
	{
		URL url = Start.class.getResource("/properties/log4j.properties");
		PropertyConfigurator.configure(url);
	}
	
	/**
	 * Holt per Reflection alle Felder\Variablen des uebergebenen Objekts und gibt diese Felder\Variblen
	 * mit aktuellem Wert als String zurueck. Dieser String besteht aus mehreren Zeilen.
	 *  
	 * @param o beliebiges Java-Objekt
	 * @return String
	 */
	public static String filteredObjectToString(final Object o) 
	{
	    return filteredObjectToString(o, org.apache.commons.lang.builder.ToStringStyle.MULTI_LINE_STYLE);
	}
	
	/**
	 * Holt per Reflection alle Felder\Variablen des uebergebenen Objekts und gibt diese Felder\Variblen
	 * mit aktuellem Wert als String zurueck. Mit Hilfe des zweiten Parameters kann das Layout des Strings
	 * bestimmt werden (MULTI_LINE_STYLE, SIMPLE_STYLE), siehe Klasse org.apache.commons.lang.builder.ToStringStyle.
	 * @param o Beliebiges Java-Objekt
	 * @param style org.apache.commons.lang.builder.ToStringStyle
	 * @return String
	 */
	public static String filteredObjectToString(final Object o,	org.apache.commons.lang.builder.ToStringStyle style)
	{
		return (new org.apache.commons.lang.builder.ReflectionToStringBuilder(
				o, style)
		{
			@Override
			protected Object getValue(Field f) throws IllegalArgumentException,
					IllegalAccessException
			{
				//Hier kann das aktuelle Feld abgefangen werden und der Wert gegebenen Falls bearbeitet werden.
				//(z.B. kann der Wert, wenn er nicht angezeigt werden soll durch Asteriks-Symbole ersetzt werden.
				//System.out.println(f.getName()); // --> Gibt den Namen des Feldes\Varibale aus.
				//System.out.println(super.getValue(f)); // --> Gibt den Wert des Feldes\Variable aus.
				
				//Abfangen des Property 'passwd' eines User-Objects, ersetzen der Zeichen des Passworts durch Asterisks
//				if(f.getName().equals("passwd"))
//				{	
//					return "<-- SECURED -->";
//				}
//				if(f.getName().equals("parameterMap"))
//				{	
//					HashMap<String, String> params = (HashMap<String, String>)((HashMap<?, ?>)super.getValue(f)).clone();
//					params.put("passwd", "<-- SECURED -->");
//					return (params);
//				}
				//Ansonsten einfache Rueckgabe des Property-Wertes
//				else
//				{
					return super.getValue(f);
//				}
			}
		}).toString();
	}
}
