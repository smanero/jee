package com.dev.cmielke.util.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.Locale;

import com.dev.cmielke.util.SpellCheckHelper;
import com.inet.jortho.UserDictionaryProvider;
import com.inet.jortho.WordIterator;

public class PerryRhodanDictionaryProvider implements UserDictionaryProvider
{
	public static final String FILE_PREFIX = "PerryRhodanDictionary_";
	public static final String FILE_EXTENSION = ".txt";

	private final String fileBase;
	private File file;

	/**
	 * Create a FileUserDictionary with the dictionaries in the root of the
	 * current application.
	 */
	public PerryRhodanDictionaryProvider()
	{
		this("");
	}

	/**
	 * Create a FileUserDictionary with the dictionaries on a specific location.
	 * 
	 * @param fileBase
	 *            the base
	 */
	public PerryRhodanDictionaryProvider(String fileBase)
	{
		if (fileBase == null)
		{
			fileBase = "";
		}
		fileBase = fileBase.trim();
		fileBase = fileBase.replace('\\', '/');
		if (fileBase.length() > 0 && !fileBase.endsWith("/"))
		{
			fileBase += "/";
		}
		this.fileBase = fileBase;
	}

	@Override
	public void addWord(String word)
	{
		try
		{
			FileOutputStream output = new FileOutputStream(file, true);
			Writer writer = new OutputStreamWriter(output, "UTF8");
			if (file.length() > 0)
			{
				writer.write("\n");
			}
			writer.write(word);
			writer.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	@Override
	public Iterator<String> getWords(Locale locale)
	{ 
		URL url = SpellCheckHelper.class.getResource(fileBase + FILE_PREFIX + locale + FILE_EXTENSION);
		try
		{
			file = new File(url.toURI());

		}
		catch (URISyntaxException e)
		{
			file = new File(url.getPath());
		}
		System.out.println(file.getPath());
		try
		{
			FileInputStream input = new FileInputStream(file);
			return new WordIterator(input, "UTF8");
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public void setUserWords(String wordList)
	{
		try
		{
			FileOutputStream output = new FileOutputStream(file);
			Writer writer = new OutputStreamWriter(output, "UTF8");
			writer.write(wordList);
			writer.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
