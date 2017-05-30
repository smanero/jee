package com.dev.cmielke.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.dev.cmielke.beans.BookType;
import com.dev.cmielke.beans.PerryRhodanBook;
import com.dev.cmielke.beans.PerryRhodanBook.Chapter;
import com.dev.cmielke.util.ApplicationContants;
import com.dev.cmielke.util.LoadWriteUtils;
import com.dev.cmielke.util.ApplicationContants.ChapterFormat;

public class HTMLGenerator {
	private final PerryRhodanBook book;
	private final String htmlTemplate;

	private enum HTMLWriteMode {
		SINGLE_FILE, MULTI_FILE
	};

	private static final Logger log = Logger.getLogger(HTMLGenerator.class);

	public HTMLGenerator(PerryRhodanBook book) {
		this.book = book;
		this.htmlTemplate = LoadWriteUtils.getHTMLTemplate();
	}

	public void generateEPub() {
		log.trace("> generateEPub...");
		replacePlaceholders(HTMLWriteMode.MULTI_FILE);
		processCover();
		log.trace("< generateEPub");
	}

	private void replacePlaceholders(HTMLWriteMode mode) {
		log.trace("> Replacing Placeholders...");
		switch (mode) {
		case SINGLE_FILE: {
			generateSingleHTMLFile();
			break;
		}
		case MULTI_FILE: {
			generateMultiHTMLFiles();
			break;
		}
		}
		log.trace("< Replacing Placeholders");
	}

	private void generateMultiHTMLFiles() {
		log.trace("> generateMultiHTMLFiles...");

		if(book.containsIntroduction()) {
			log.debug("Generating file for introduction");
			String copy = htmlTemplate.replace("%TITLE%", getPlainTitle());
			StringBuilder buffer = new StringBuilder();
			if(book.getType()==BookType.HEFTROMAN) {
				buffer.append(getFormatedBookNumber()).append("\n");
			}
			buffer.append(getFormatedTitle()).append("\n");
			buffer.append(getFormatedSubtitle()).append("\n");
			if(book.containsAuthors()) buffer.append(getFormatedAuthors()).append("\n");
			if(book.containsIntroduction()) buffer.append(getFormatedIntroduction()).append("\n");
			if(book.containsCharacters()) buffer.append(getFormatedCharacters()).append("\n");
			if(book.containsPreChapter()) buffer.append(getFormatedPreChapter()).append("\n");
			copy = copy.replace("%TEXT%", buffer.toString());
			LoadWriteUtils.writeHTML(copy, "introduction"+ApplicationContants.DEFAULT_HTML_FILE_EXTENSION);
			log.trace("introduction page complete!");
		}

		log.debug("Generating files for every chapter");
		for (Chapter chapter : book.getChapters()) {
			log.trace("> Processing Chapter "+chapter);
			StringBuilder text = new StringBuilder();
			text.append(getFormatedChapterText(chapter));
			text.append("\n");
			
			String copy = htmlTemplate.replace("%TITLE%", getPlainTitle());
			copy = copy.replace("%TEXT%", text.toString());
			LoadWriteUtils.writeHTML(copy, chapter.getHtmlFileName());
			log.trace(chapter + " complete.");
		}
		log.debug("all chapters processed!");
		log.trace("< generateMultiHTMLFiles");
	}

	/**
	 * TODO wird nicht laufen da mittlerweile zu viele Änderungen am Ablauf.
	 * wurde sowieso nicht benutzt!
	 */
	private void generateSingleHTMLFile() {
		log.trace("> generateSingleHTMLFile...");
		String copy = htmlTemplate.replace("%TITLE%", getPlainTitle());

		StringBuffer buffer = new StringBuffer();
		buffer.append(getFormatedBookNumber()).append("\n");
		buffer.append(getFormatedTitle()).append("\n");
		buffer.append(getFormatedSubtitle()).append("\n");
		if(book.containsAuthors()) buffer.append(getFormatedAuthors()).append("\n");
		buffer.append(getFormatedIntroduction()).append("\n");
		if(book.containsCharacters()) buffer.append(getFormatedCharacters()).append("\n");

		for (Chapter chapter : book.getChapters()) {
			log.trace("> Processing Chapter "+chapter);
			buffer.append(getFormatedChapterText(chapter));
			buffer.append("\n");
			
			log.trace("< Chapter " + chapter + " complete.");
		}
		copy = copy.replace("%TEXT%", buffer.toString());
		LoadWriteUtils.writeHTML(copy, "content");

		log.trace("< generateSingleHTMLFile");
	}

	private String getPlainTitle() {
		switch(book.getType()) {
		case HEFTROMAN:
			return "Band " + book.getBookNumber() + " - " + StringEscapeUtils.escapeHtml(book.getTitle());
		case SILBERBAND:
			return "Silberband " + book.getBookNumber() + " - " + StringEscapeUtils.escapeHtml(book.getSubtitle());
		default:
			return StringEscapeUtils.escapeHtml(book.getTitle());
		}
	}

	private String getFormatedTitle() {
		String title = "<h1 class=\"bookTitle\">" + StringEscapeUtils.escapeHtml(book.getTitle()) + "</h1>";
		return title;
	}

	private String getFormatedBookNumber() {
		String number = "<h2 class=\"bookNumber\">" + "Nr. " + book.getBookNumber() + "</h2>";
		return number;
	}

	private String getFormatedSubtitle() {
		String subtitle = "<h2 class=\"subtitle\">" + StringEscapeUtils.escapeHtml(book.getSubtitle()) + "</h2>";
		return subtitle;
	}

	private String getFormatedAuthors() {
		String authors = "<h3 class=\"authors\">von " + StringEscapeUtils.escapeHtml(book.getAuthors()) + "</h3>";
		return authors;
	}

	private String getFormatedCoverImage() {
		String image = "<img src=\"" + "cover.jpg\"/>";
		return image;
	}

	private String getFormatedCharacters() {
		String characters = "<h4>Die Hautpersonen des Romans:</h4>";
		characters += "<ul class=\"characterList\">";
		for (String character : book.getCharacters()) {
			String[] asCharacter = character.split("-");
			characters += "<li>";
			for (int index = 0; index < asCharacter.length; index++) {
				if (index == 0) {
					characters += "<strong>" + StringEscapeUtils.escapeHtml(asCharacter[index]) + "</strong>";
					characters += " - ";
				} else {
					characters += StringEscapeUtils.escapeHtml(asCharacter[index]);
				}
			}
			characters += "</li>";
		}
		characters += "</ul>";

		return characters;
	}

	private String getFormatedIntroduction() {
		String titel="";
		String bookIntroduction = book.getIntroduction();
		String header = getHeader(bookIntroduction);	//TODO entfernen
		if(header!=null) {
			bookIntroduction = bookIntroduction.replaceAll("%"+header+"%","");
			if(header.equals("PROLOG")) {
				header = "Prolog";
			}else if(header.equals("EINLEITUNG")) {
				header = "Einleitung";
			}else if(header.equals("VORSPIEL")) {
				header = "Vorspiel";
			}
			titel="<h3 class=\"chapterHeader\">" + StringEscapeUtils.escapeHtml(header)+ "</h3>\n";
		}
		bookIntroduction = bookIntroduction.replaceAll("\n", " ").replaceAll("\t", "");
		bookIntroduction = StringEscapeUtils.escapeHtml(bookIntroduction);

		return titel + "<p class=\"introduction\">" + bookIntroduction + "</p>";
	}

	private String getFormatedPreChapter() {
		String text = book.getPreChapter();
		text = text.replaceAll("\n", " ").replaceAll("\t", "");
		text = StringEscapeUtils.escapeHtml(text);

		return "<p class=\"prechapter\">" + text + "</p>";
	}

	private String getFormatedChapterText(Chapter chapter) {
		String chapterText = "";
		String titel  = "";
		
		if(chapter.hasChapterNumber()) {
			titel = chapter.getChapterNumber() + ".";
		}
		if(chapter.getTitel()!=null) {
			if(titel.length()>0) titel+=" ";
			titel+=chapter.getTitel();
		}
		if(titel.length()>0) {
			chapterText+="<h3 class=\"chapterHeader\">" + StringEscapeUtils.escapeHtml(titel)+ "</h3>\n<div class=\"chapterContainer\">\n";
		}
		
		switch (chapter.getFormat()) {
		case PREFORMATED:
		case TEXT: {
			boolean preFormatted = chapter.getFormat()==ChapterFormat.PREFORMATED;
			List<String> paragraphs = getParagraphs(chapter.getText());

			for (int index = 0; index < paragraphs.size(); index++) {
				String paragraph = paragraphs.get(index);
				if (paragraph.contains("%E N D E%")) {
					chapterText += "<p class=\"endSeparator\">E N D E</p>\n";
					paragraph = paragraph.replaceAll("%E N D E%", "");
					chapterText += formatInnerParagraphs(paragraph, preFormatted);
				} else {
					chapterText += formatInnerParagraphs(paragraph, preFormatted);
					if (!((index + 2) >= paragraphs.size())) {
						chapterText += "<p class=\"separator\">*</p>\n";

					}
				}
			}
			break;
		}
		case ZEITTAFEL: {
			chapterText += "<div id=\"TimeTable\"><table>\n";
			for (String timeframe : chapter.getText().split("\\n")) {
				timeframe = timeframe.trim();
				if (timeframe.length() > 0) {
					int pos = timeframe.indexOf(" ");
					chapterText += "<tr><th class=\"TimeTableTag\" valign=\"top\"><b>";
					chapterText += StringEscapeUtils.escapeHtml(timeframe.substring(0, pos)) + "&nbsp;&nbsp;</b></th><th class=\"TimeTableText\">";
					chapterText += StringEscapeUtils.escapeHtml(timeframe.substring(pos + 1)) + "</th></tr>\n";
				}
			}
			chapterText += "</table></div>\n";
			break;
		}
		case GLOSSAR: {
			chapterText += "<div id=\"Glossar\">\n";
			for (String part : chapter.getText().split("\\n")) {
				part = part.trim();
				if (part.length() > 0) {
					if(part.endsWith("-")) {
						chapterText += "<p class=\"GlossarTag\">"+StringEscapeUtils.escapeHtml(part.substring(0, part.length()-1).trim())+"</p>\n";
					}else{
						chapterText += "<p class=\"GlossarText\">"+StringEscapeUtils.escapeHtml(part)+"</p>\n";
					}
				}
			}
			chapterText += "</div>\n";
			break;
		}
		default:
			throw new IllegalStateException("Kapitel Format nicht behandelt! ["+chapter.getFormat()+"]");
		}

		chapterText += "</div>\n";
		return chapterText;
	}
	
	private String getHeader(String text) {
		if(text.startsWith("\n\t\t%")) {
			int end=text.indexOf("%", 4);
			return  text.substring(4,end);
		}
		return null;
	}
	
	static HashMap<String,String> paragraphToken = new HashMap<String,String>();
	static {
		paragraphToken.put("%E N D E%","<p class=\"endSeparator\">E N D E</p>\n");
	}

	private List<String> getParagraphs(String text) {
		ArrayList<String> paragraphs = new ArrayList<String>();
		int startPos = 0;
		int endPos = text.indexOf("%*%");
		while (endPos > 0) {
			paragraphs.add(text.substring(startPos, endPos));
			startPos = endPos + 3;

			endPos = text.indexOf("%*%", startPos);
		}
		endPos = text.length();

		String paragraph = text.substring(startPos, endPos);
		if (paragraph.contains("%E N D E%")) {
			String[] asParagraphs = paragraph.split("%E N D E%");
			for (int index = 0; index < asParagraphs.length; index++) {
				if ((index + 1) >= asParagraphs.length) {
					paragraphs.add("%E N D E%" + asParagraphs[index]);
				} else {
					paragraphs.add(asParagraphs[index]);
				}
			}
		} else {
			paragraphs.add(paragraph);
		}
		return paragraphs;
	}
	
	private String formatInnerParagraphs(String paragraph, boolean preFormatted) {
		if(preFormatted) {
			return "<p class=\"chapterText\"><pre>" + StringEscapeUtils.escapeHtml(paragraph) + "</pre></p>\n";
		}else{
			StringBuilder b = new StringBuilder();
			for (String text : paragraph.split("[\n]{2,4}")) {
				b.append("<p class=\"chapterText\">" + StringEscapeUtils.escapeHtml(text) + "</p>\n");
			}
			return b.toString();
		}
	}

	private void processCover() {
		log.trace("> processCover...");
		String copy;
		if (book.containsCover()) {
			copy = htmlTemplate.replace("%TITLE%", getPlainTitle() + " - Titelbild");
			LoadWriteUtils.writeCoverImage(book.getCover(), "cover");
			copy = copy.replace("%TEXT%", getFormatedCoverImage()+getFormatedTitle()+getFormatedSubtitle());
		}else{
			copy = htmlTemplate.replace("%TITLE%", getPlainTitle());
			copy = copy.replace("%TEXT%", getFormatedTitle()+getFormatedSubtitle());
		}
		LoadWriteUtils.writeHTML(copy, "title"+ApplicationContants.DEFAULT_HTML_FILE_EXTENSION);
		log.trace("< processCover");
	}
}
