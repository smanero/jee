package com.dev.cmielke.util;

import static com.dev.cmielke.util.ApplicationContants.SPELL_CHECK_PARAMETER_NAME_CASE_SENSITIVE;
import static com.dev.cmielke.util.ApplicationContants.SPELL_CHECK_PARAMETER_NAME_IGNORE_ALL_CAP_WORDS;
import static com.dev.cmielke.util.ApplicationContants.SPELL_CHECK_PARAMETER_NAME_IGNORE_CAPITALIZATION;
import static com.dev.cmielke.util.ApplicationContants.SPELL_CHECK_PARAMETER_NAME_IGNORE_WORDS_WITH_NUMBERS;
import static com.dev.cmielke.util.ApplicationContants.SPELL_CHECK_PARAMETER_NAME_LANG_DISABLED_VISIBLE;
import static com.dev.cmielke.util.ApplicationContants.SPELL_CHECK_PARAMETER_NAME_SUGGESTIONS_LIMIT_DIALOG;
import static com.dev.cmielke.util.ApplicationContants.SPELL_CHECK_PARAMETER_NAME_SUGGESTIONS_LIMIT_MENU;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.text.JTextComponent;

import com.dev.cmielke.util.provider.PerryRhodanDictionaryProvider;
import com.inet.jortho.SpellChecker;
import com.inet.jortho.SpellCheckerOptions;

public class SpellCheckHelper
{
	private static SpellCheckHelper instance;
	private static ResourceBundle rBundle = ResourceBundle.getBundle("pdf2epub");
	
	private SpellCheckHelper()
	{		
		SpellChecker.setUserDictionaryProvider(new PerryRhodanDictionaryProvider(ApplicationContants.SPELL_CHECK_USER_DICTIONARY_DIRECTORY));
		URL url = SpellCheckHelper.class.getResource(ApplicationContants.SPELL_CHECK_DICTIONARY_PATH);
		SpellChecker.registerDictionaries( url, "de", "de" );
		setSpellCheckerOptions();
	}
	
	private void setSpellCheckerOptions()
	{
		SpellCheckerOptions options = SpellChecker.getOptions();
		options.setCaseSensitive(Boolean.parseBoolean(rBundle.getString(SPELL_CHECK_PARAMETER_NAME_CASE_SENSITIVE)));
		options.setIgnoreAllCapsWords(Boolean.parseBoolean(rBundle.getString(SPELL_CHECK_PARAMETER_NAME_IGNORE_ALL_CAP_WORDS)));
		options.setIgnoreCapitalization(Boolean.parseBoolean(rBundle.getString(SPELL_CHECK_PARAMETER_NAME_IGNORE_CAPITALIZATION)));
		options.setIgnoreWordsWithNumbers(Boolean.parseBoolean(rBundle.getString(SPELL_CHECK_PARAMETER_NAME_IGNORE_WORDS_WITH_NUMBERS)));
		options.setLanguageDisableVisible(Boolean.parseBoolean(rBundle.getString(SPELL_CHECK_PARAMETER_NAME_LANG_DISABLED_VISIBLE)));
		options.setSuggestionsLimitDialog(Integer.parseInt(rBundle.getString(SPELL_CHECK_PARAMETER_NAME_SUGGESTIONS_LIMIT_DIALOG)));
		options.setSuggestionsLimitMenu(Integer.parseInt(rBundle.getString(SPELL_CHECK_PARAMETER_NAME_SUGGESTIONS_LIMIT_MENU)));
	}
	
	public static void registerComponent(JTextComponent comp)
	{
		if(instance == null)
		{
			instance = new SpellCheckHelper();
		}
		SpellChecker.register(comp);
	}
}
