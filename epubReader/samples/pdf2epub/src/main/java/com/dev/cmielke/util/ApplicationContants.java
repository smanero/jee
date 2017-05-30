package com.dev.cmielke.util;

import java.util.HashMap;
import java.util.Map;

public final class ApplicationContants {
	private ApplicationContants() {}
	
	public static final String OUTPUT_DIRECTORY = "generatedFiles/";
	public static final String TEMPLATE_DIRECTORY = "/templates/";
	public static final String CONTAINER_OUTPUT_DIRECTORY = OUTPUT_DIRECTORY + "META-INF/";
	
	public static final String SPELL_CHECK_USER_DICTIONARY_DIRECTORY = "/dictionaries/";
	public static final String SPELL_CHECK_DICTIONARY_PATH           = "/dictionaries/dictionary_de.ortho";
	
	public static final int DEFAULT_SCALE_WIDTH  = 150;
	public static final int DEFAULT_SCALE_HEIGHT = 219;
	
	public static final int DEFAULT_DIGIT_COUNT = 2;
	
	public static final String HTML_TEMPLATE_NAME = "HTMLTemplate.xhtml";
	public static final String CONTAINER_XML      = "container.xml";
	public static final String CONTENT_OPF        = "content.opf";
	public static final String TOC_NCX_FILE       = "toc.ncx";
	public static final String EPUB_CSS			  = "epub.css";
	public static final String MIMETYPE_FILE	  = "mimetype";
	
	public static final String DEFAULT_HTML_FILE_EXTENSION  = ".xhtml";
	public static final String DEFAULT_IMAGE_FILE_EXTENSION = ".jpg";
	public static final String DEFAULT_IMAGE_MIME_TYPE      = "jpeg";
	
	public static final String DEFAULT_ZIP_FILE_EXTENSION = ".epub";
	
	// SpellCheckerOptions - Parameter-Names
	public static final String SPELL_CHECK_PARAMETER_NAME_CASE_SENSITIVE            = "spellCheck.option.caseSensitive";
	public static final String SPELL_CHECK_PARAMETER_NAME_IGNORE_ALL_CAP_WORDS      = "spellCheck.option.ignoreAllCapsWords";
	public static final String SPELL_CHECK_PARAMETER_NAME_IGNORE_WORDS_WITH_NUMBERS = "spellCheck.option.ignoreWordsWithNumbers";
	public static final String SPELL_CHECK_PARAMETER_NAME_IGNORE_CAPITALIZATION     = "spellCheck.option.ignoreCapitalization";
	public static final String SPELL_CHECK_PARAMETER_NAME_SUGGESTIONS_LIMIT_DIALOG  = "spellCheck.option.suggestionsLimitDialog";
	public static final String SPELL_CHECK_PARAMETER_NAME_SUGGESTIONS_LIMIT_MENU    = "spellCheck.option.suggestionsLimitMenu";
	public static final String SPELL_CHECK_PARAMETER_NAME_LANG_DISABLED_VISIBLE     = "spellCheck.option.languagesDisabledVisible";
	
	private static final Map<String,ChapterFormatInfo> kapitelRenaming = new HashMap<String,ChapterFormatInfo>();
	
	static {
		kapitelRenaming.put("PERRY RHODAN-TERMINOLOGIE",new ChapterFormatInfo("Terminologie",ChapterFormat.GLOSSAR));
		kapitelRenaming.put("PROLOG",new ChapterFormatInfo("Prolog",ChapterFormat.TEXT));
		kapitelRenaming.put("EINLEITUNG",new ChapterFormatInfo("Einleitung",ChapterFormat.TEXT));
		kapitelRenaming.put("VORSPIEL",new ChapterFormatInfo("Vorspiel",ChapterFormat.TEXT));
		kapitelRenaming.put("VORWORT",new ChapterFormatInfo("Vorwort",ChapterFormat.TEXT));
		kapitelRenaming.put("EPILOG",new ChapterFormatInfo("Epilog",ChapterFormat.PREFORMATED));
		kapitelRenaming.put("ZEITTAFEL",new ChapterFormatInfo("Zeittafel",ChapterFormat.ZEITTAFEL));
	}
	
	/**
	 * Prüft ob der angegebene Text eine KapitelÜberschrift ist.
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isChapterTitel(String text) {
		return kapitelRenaming.containsKey(text.trim().toUpperCase());
	}
	
	public static String getChapterTitel(String text) {
		if(!isChapterTitel(text)) {
			throw new IllegalArgumentException("'"+text+"' ist keine gültige KapitelÜberschrift!");
		}
		return kapitelRenaming.get(text.toUpperCase()).titel;
	}
	
	public static ChapterFormatInfo getChapterFormatInfo(String text) {
		if(!isChapterTitel(text)) {
			throw new IllegalArgumentException("'"+text+"' ist keine gültige KapitelÜberschrift!");
		}
		return kapitelRenaming.get(text.toUpperCase());
	}
	
	public static class ChapterFormatInfo {
		public final String titel;
		public final ChapterFormat format;
		
		public ChapterFormatInfo(String titel, ChapterFormat format) {
			this.titel = titel;
			this.format = format;
		}
	}
	
	public enum ChapterFormat {
		TEXT,
		PREFORMATED,
		ZEITTAFEL,
		GLOSSAR;
	}
}
