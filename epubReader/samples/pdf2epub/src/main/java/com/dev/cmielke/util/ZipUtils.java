package com.dev.cmielke.util;

import static com.dev.cmielke.util.ApplicationContants.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

import com.dev.cmielke.gui.beans.Options;

public class ZipUtils {	
	private static Logger log = Logger.getLogger(ZipUtils.class);

	public static boolean buildEPubFile(String filename)
	{
		boolean isZipped = false;
		try
		{
			zipDir(new File(Options.getOutputPath(),filename+ DEFAULT_ZIP_FILE_EXTENSION).getCanonicalPath(), OUTPUT_DIRECTORY, true);
			isZipped = true;
		}
		catch (IOException e)
		{
			log.error("error occured during creation of zip-file!", e);
		}
		return isZipped;
	}
	
	private static void zipDir(String zipFileName, String dir, boolean recursive) throws IOException
	{
		File dirObj = new File(dir);
		if (!dirObj.isDirectory())
		{
			throw new IOException(dir + " is not a directory");
		}

		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
		
		log.debug("Creating : " + zipFileName);
		addDir(dirObj, out, recursive);
		out.close();
	}

	private static void addDir(File dirObj, ZipOutputStream out, boolean recursive)
			throws IOException
	{
		File[] files = dirObj.listFiles();
		byte[] tmpBuf = new byte[1024];
		
		files = sortFileList(files);

		for (int i = 0; i < files.length; i++)
		{
			if (files[i].isDirectory() && recursive)
			{
				addDir(files[i], out, recursive);
				continue;
			}

			if(files[i].getName().equals("mimetype"))
			{
				FileInputStream in = new FileInputStream(files[i].getPath());
				log.debug("Adding: " + files[i].getPath().substring(files[i].getPath().indexOf("\\")+1));
				ZipEntry entry = new ZipEntry(files[i].getPath().substring(files[i].getPath().indexOf("\\")+1));
				entry.setMethod(ZipEntry.STORED);
				entry.setSize(files[i].length());
				entry.setCrc(getCRC(files[i]));
				out.putNextEntry(entry);
				
				// Transfer from the file to the ZIP file
				int len;
				while ((len = in.read(tmpBuf)) > 0)
				{
					out.write(tmpBuf, 0, len);
				}
	
				// Complete the entry
				out.flush();
				out.closeEntry();
				in.close();
			}
			else
			{
				FileInputStream in = new FileInputStream(files[i].getPath());
				log.debug("Adding: " + files[i].getPath().substring(files[i].getPath().indexOf("\\")+1));
	
				out.putNextEntry(new ZipEntry(files[i].getPath().substring(files[i].getPath().indexOf("\\")+1)));
	
				// Transfer from the file to the ZIP file
				int len;
				while ((len = in.read(tmpBuf)) > 0)
				{
					out.write(tmpBuf, 0, len);
				}
	
				// Complete the entry
				out.flush();
				out.closeEntry();
				in.close();
			}
		}
	} 

	private static long getCRC(File file) throws IOException
	{
		long value = 0L;
		byte[] tmpBuf = new byte[1024];
		CRC32 crc = new CRC32();
	    FileInputStream in = new FileInputStream(file);
	    int read;
	    while((read = in.read(tmpBuf, 0, 1024)) != -1)
	      crc.update(tmpBuf, 0, read);
	    in.close();
	    value = crc.getValue();
	    return value;
	}
	
	private static File[] sortFileList(File[] files)
	{
		ArrayList<File> sorted = new ArrayList<File>();
		for (int index = 0; index < files.length; index++)
		{
			if(files[index] != null)
			{
				if(files[index].getName().contains("mimetype"))
				{
					sorted.add(0, files[index]);
					files[index] = null;
				}
				else if(files[index].getName().contains("content"))
				{
					sorted.add(files[index]);
					files[index] = null;
				}
				else if(files[index].getName().contains("toc"))
				{
					sorted.add(files[index]);
					files[index] = null;
				}
				else if(files[index].getName().contains("META"))
				{
					sorted.add(files[index]);
					files[index] = null;
				}
			}
		}
		
		for (int index = 0; index < files.length; index++)
		{
			if(files[index] != null)
			{
				sorted.add(files[index]);
			}
		}
		File[] sortedArray = new File[sorted.size()];
		return sorted.toArray(sortedArray);
	}

	public static void main(String[] args)
	{
		ZipUtils.buildEPubFile("testFile");
	}
}
