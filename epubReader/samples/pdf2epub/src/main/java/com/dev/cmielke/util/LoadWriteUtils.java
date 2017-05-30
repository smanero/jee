package com.dev.cmielke.util;

import static com.dev.cmielke.util.ApplicationContants.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

/**
 * @author Administrator
 */
public class LoadWriteUtils {	
	private static Logger log = Logger.getLogger(LoadWriteUtils.class);
	
	private static String getResourceFileAsString(String filename)
	{
		String retValue = "";
		URL url = LoadWriteUtils.class.getResource(filename);
		File file = new File(url.getFile());

		try
		{
			BufferedReader bReader = new BufferedReader(new FileReader(file));
			String line = bReader.readLine();
			while(line != null)
			{
				retValue += line;
				line = bReader.readLine();
			}
			bReader.close();
		}
		catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
			System.exit(1);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		return retValue;
	}
	
	public static String getHTMLTemplate()
	{
		return getResourceFileAsString(TEMPLATE_DIRECTORY + HTML_TEMPLATE_NAME);
	}
	
	public static String getContainerXML()
	{
		return getResourceFileAsString(TEMPLATE_DIRECTORY + CONTAINER_XML);
	}
	
	public static String getContentOPF()
	{
		return getResourceFileAsString(TEMPLATE_DIRECTORY + CONTENT_OPF);
	}
	
	public static String getTOCFile()
	{
		return getResourceFileAsString(TEMPLATE_DIRECTORY + TOC_NCX_FILE);
	}
	
	public static List<File> getAllGeneratedImageFiles()
	{
		final String[] fileExtensions = new String[]{".jpg"};
		TreeSet<File> files = new TreeSet<File>();
		
		File dir = new File(OUTPUT_DIRECTORY);
		File[] xhtml = dir.listFiles(new FileFilter() 
        {
			public boolean accept(File f)
			{
				boolean accept = false;
				for (int index = 0; index < fileExtensions.length; index++)
				{
					if( f.getName().toLowerCase().endsWith(fileExtensions[index]) && f.isFile() )
					{
						accept = true;
						break;
					}
				}
				return accept; 
			}
		});
		
		for (int index = 0; index < xhtml.length; index++)
		{	
			files.add(xhtml[index]);
		}
		
		ArrayList<File> sortedList = new ArrayList<File>(files);
		return sortedList;
	}
	
	public static List<File> getAllGeneratedHTMLFiles()
	{
		final String[] fileExtensions = new String[]{".xhtml"};
		TreeSet<File> files = new TreeSet<File>();
		
		File dir = new File(OUTPUT_DIRECTORY);
		File[] xhtml = dir.listFiles(new FileFilter() 
        {
			public boolean accept(File f)
			{
				boolean accept = false;
				for (int index = 0; index < fileExtensions.length; index++)
				{
					if( f.getName().toLowerCase().endsWith(fileExtensions[index]) && f.isFile() )
					{
						accept = true;
						break;
					}
				}
				return accept; 
			}
		});
		
		for (int index = 0; index < xhtml.length; index++)
		{	
			files.add(xhtml[index]);
		}
		
		ArrayList<File> sortedList = new ArrayList<File>();
		sortedList.add(files.pollLast());
		sortedList.add(files.pollLast());
		sortedList.addAll(files);		
		
		return sortedList;
	}
	
	private static void writeFile(String data, String filename)
	{
		File file = new File(filename);
		BufferedWriter bWriter;
		try
		{
			FileWriter fWriter = new FileWriter(file);
			bWriter = new BufferedWriter(fWriter);
			bWriter.write(data);
			bWriter.flush();
			bWriter.close();
		}
		catch (IOException e)
		{
			log.error("Error occured during saving file ["+ filename +"]", e);
		}
	}
	
	public static void writeHTML(String html, String filename)
	{
		writeFile(html, OUTPUT_DIRECTORY + filename);
	}
	
	public static void writeContentOPF(String data)
	{
		writeFile(data, OUTPUT_DIRECTORY + CONTENT_OPF);
	}
	
	public static void writeTOCFile(String data)
	{
		writeFile(data, OUTPUT_DIRECTORY + TOC_NCX_FILE);
	}
	
	public static void writeCoverImage(BufferedImage bImage, String filename)
	{
		try
		{			
//			Image scaledImage = bImage.getScaledInstance(DEFAULT_SCALE_WIDTH, DEFAULT_SCALE_HEIGHT, Image.SCALE_SMOOTH);
//
//			BufferedImage outImg = new BufferedImage(DEFAULT_SCALE_WIDTH, DEFAULT_SCALE_HEIGHT, BufferedImage.TYPE_INT_RGB);
//			Graphics g = outImg.getGraphics();
//			g.drawImage(scaledImage, 0, 0, null);
//			g.dispose();

			ImageIO.write(bImage, DEFAULT_IMAGE_MIME_TYPE, new File(OUTPUT_DIRECTORY + filename + DEFAULT_IMAGE_FILE_EXTENSION));
		}
		catch (IOException e)
		{
			log.error("Error occured during scaling and saving image object!", e);
		}
	}
	
	/**
	 * Methode, um Integer zu formatieren, d.h. ihnen f�hrende '0' voranzustellen
	 * um so eine Sortierung einfacher bzw. erst m�glich zu machen.
	 * 
	 * @param number - Nummer (meist Kapitel-Nummer)
	 * @param digits - Anzahl der f�hrenden Nullen
	 * @return Nummer mit f�hrenden Nullen
	 */
	public static String formatChapterNumber(int number, int digits)
	{
		String formattedNumber = "" + number;
		int offset = digits - formattedNumber.length();
		
		if(offset > 0)
		{
			String leadingNumbers = "";
			for(int index = 0; index < offset; index++)
			{
				leadingNumbers = "0" + leadingNumbers;
			}
			formattedNumber = leadingNumbers + formattedNumber;
		}
		
		return formattedNumber;
	}
	
	public static File loadEPubFile(String filename)
	{
		URL url = LoadWriteUtils.class.getResource(filename); 
		return new File(url.getFile());
	}
	
	public static void main(String[] args)
	{
		LoggingUtils.configureLog4J();
		log.debug("SIZE: " + LoadWriteUtils.getAllGeneratedHTMLFiles().size());
		log.debug("SIZE: " + LoadWriteUtils.getAllGeneratedImageFiles().size());
		log.debug("Number: " + formatChapterNumber(1,2));
	}
}