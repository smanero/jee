package com.dev.cmielke.gui.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.dev.cmielke.util.ApplicationContants;

public class Options {
	private static final Logger LOG = Logger.getLogger(Options.class);
	private static final String PROPERTY_FILE_NAME="PerryRhodan_PDFconverter.conf";
	private static final String SHOW_BOOK_DIALOG="ShowBookDialog";
	private static final String IGNORE_MISSING_COVER_IMAGE="IgnoreMissingCover";
	private static final String COVER_IMAGE_SEARCHPATH="CoverImageSearchPath";
	private static final String COVER_IMAGE_AUTOSEARCH="CoverImageAutoSearch";
	private static final String DESTINATION_PATH="OutputPath";
	private static final String SOURCE_PATH="InputPath";
	private static final String CSS_FILENAME="StyleSheetTemplate";
	
	private static Properties properties;
	
	static {
		read();
	}
	
	/**
	 * Read the user properties from Home directory and overwrite all options.
	 */
	public static void read() {
		properties = new Properties();
		try {
			FileInputStream inStream = new FileInputStream(new File(System.getProperty("user.home"),PROPERTY_FILE_NAME));
			properties.load(inStream);
		}catch (FileNotFoundException e) {
			// nothing to do, defaults are used
		}catch(IOException e) {
			LOG.error("Could not read user options file",e);
		}
	}
	
	/**
	 * Option that the bookDialog should be shown always.
	 * 
	 * @return
	 */
	public static boolean showBookMetaDataDialog() {
		if(properties.containsKey(SHOW_BOOK_DIALOG)) {
			return Boolean.valueOf((String)properties.get(SHOW_BOOK_DIALOG));
		}
		return false;
	}
	
	public static boolean ignoreMissingCoverImage() {
		if(properties.containsKey(IGNORE_MISSING_COVER_IMAGE)) {
			return Boolean.valueOf((String)properties.get(IGNORE_MISSING_COVER_IMAGE));
		}
		return true;
	}
	
	public static File getCoverSearchPath() {
		if(properties.containsKey(COVER_IMAGE_SEARCHPATH)) {
			return new File((String)properties.get(COVER_IMAGE_SEARCHPATH));
		}
		return null;
	}
	
	public static boolean isCoverAutoSearch() {
		if(properties.containsKey(COVER_IMAGE_AUTOSEARCH)) {
			return Boolean.valueOf((String)properties.get(COVER_IMAGE_AUTOSEARCH));
		}
		return false;
	}
	/**
	 * Anzahl der Kapitelnummer die als Lücke akzeptiert werden.
	 * Es gibt einige Romane wo nicht alle Kapitel gekennzeichnet sind und
	 * einige be denen zufällig eine Nummer mit '.' alleine in einer Zeile steht.
	 * 
	 * @return Größe der Kapitellücke
	 */
	public static int getAllowedChapterGap() {
		return 3;	// zZt noch festwert
	}
	public static File getOutputPath() {
		if(properties.containsKey(DESTINATION_PATH)) {
			return new File((String)properties.get(DESTINATION_PATH));
		}
		return new File("buildDir");
	}

	public static File getInputPath() {
		if(properties.containsKey(SOURCE_PATH)) {
			return new File((String)properties.get(SOURCE_PATH));
		}
		return new File(System.getProperty("user.home"));
	}
	
	/**
	 * Template for CSS style sheet.<br/>
	 * The file must located within the template direcory.
	 */
	public static String getCSSFileName() {
		if(properties.containsKey(CSS_FILENAME)) {
			return (String)properties.getProperty(CSS_FILENAME);
		}
		return ApplicationContants.EPUB_CSS;
	}
}
