package com.dev.cmielke.util;

import static com.dev.cmielke.util.ApplicationContants.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;

import org.apache.log4j.Logger;

import com.dev.cmielke.gui.beans.Options;

public class FileSystemUtils {
	private static Logger log = Logger.getLogger(FileSystemUtils.class);
	
	public static void cleanOutputDirectory() throws IOException
	{
		log.debug("> cleanOutputDirectory...");
		File directory = new File(OUTPUT_DIRECTORY);
		cleanDirectory(directory);
		log.debug("< cleanOutputDirectory");
	}
	
	private static void cleanDirectory(File directory) throws IOException
	{
		for(File file : directory.listFiles())
		{
			if(file.exists() && file.isFile())
			{
				if(file.delete())
				{
					log.debug("File ["+ file.getPath() +"] successfully deleted.");
				}
				else
				{
//					log.error("Could not delete file ["+ file.getPath() +"]");
					throw new IOException("Could not delete file ["+ file.getPath() +"]");
				}
			}
			else if(file.exists() && file.isDirectory())
			{
				FileSystemUtils.cleanDirectory(file);
				if(file.delete())
				{
					log.debug("Directory ["+ file.getPath() +"] successfully deleted.");
				}
				else
				{
					throw new IOException("Could not delete file ["+ file.getPath() +"]");
				}
			}
		}
	}
	
	public static void waitForDeletion()
	{
		log.debug("> WaitForDeletion");
		int count = 0;
		for(int i = 0; i < 1000000000; i++){ count++; count--;}
		count = 0;
		for(int i = 0; i < 1000000000; i++){ count++; count--;}
		log.debug("< WaitForDeletion");
	}
	
	private static boolean copyFile(String source, String dest)
	{
		log.debug("> copyFile...");
		log.debug("Source: [" + source + "]");
		log.debug("Destimation: [" + dest + "]");
		
		boolean isCopied = false;
		try
		{
			URL url = FileSystemUtils.class.getResource(source);
			File sourceFile = new File(url.getFile());
			RandomAccessFile raSourceFile = new RandomAccessFile(sourceFile, "r");
			
			File destFile = new File(dest);
	        RandomAccessFile raDestFile   = new RandomAccessFile(destFile, "rw");
	        
	        while (raDestFile.length() < sourceFile.length()) {
	        	raDestFile.write(raSourceFile.read());
	        }
	        raSourceFile.close();
	        raDestFile.close();
	        isCopied = true;
		}
		catch (FileNotFoundException e) 
		{
			log.error("Could not find file ["+ source + ","+ dest +"]", e);
		}
		catch ( IOException ioe)
		{
			log.error("Error occured during copying ["+ source +","+ dest +"]", ioe);
		}
		
		log.debug("< copyFile");
		return isCopied;
	}
	
	public static boolean copyContainerXML()
	{
		boolean isCopied = false;
		log.debug("> copyContainerXML...");
		File metaInfDirectory = new File(CONTAINER_OUTPUT_DIRECTORY);
		if (metaInfDirectory.isDirectory()) 
		{
			log.debug("Directory [" + CONTAINER_OUTPUT_DIRECTORY + "] already exists.");
		} 
		else 
		{
			log.debug("Directory [" + CONTAINER_OUTPUT_DIRECTORY + "] does not exists, creating it.");
			metaInfDirectory.mkdir();
		}
		isCopied = copyFile(TEMPLATE_DIRECTORY + CONTAINER_XML, CONTAINER_OUTPUT_DIRECTORY + CONTAINER_XML);
		log.debug("< copyContainerXML");
		return isCopied;
	}
	
	public static boolean copyEPubCSS()
	{
		boolean isCopied = false;
		log.debug("> copyEPubCSS...");
		isCopied = copyFile(TEMPLATE_DIRECTORY + Options.getCSSFileName(), OUTPUT_DIRECTORY + EPUB_CSS);
		log.debug("< copyEPubCSS");
		return isCopied;
	}
	
	public static boolean copyMimetypeFile()
	{
		boolean isCopied = false;
		log.debug("> copyMimetypeFile...");
		isCopied = copyFile(TEMPLATE_DIRECTORY + MIMETYPE_FILE, OUTPUT_DIRECTORY + MIMETYPE_FILE);
		log.debug("< copyMimetypeFile");
		return isCopied;
	}
	
	public static void main(String[] args)
	{
		try
		{	
			LoggingUtils.configureLog4J();
			cleanOutputDirectory();
		}
		catch (IOException e)
		{
			log.error("Error occured during cleaning of directory!", e);
		}
	}
}
