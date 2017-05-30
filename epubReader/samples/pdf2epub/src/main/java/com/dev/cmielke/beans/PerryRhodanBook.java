package com.dev.cmielke.beans;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import com.dev.cmielke.util.ApplicationContants;
import com.dev.cmielke.util.LoadWriteUtils;
import com.dev.cmielke.util.ApplicationContants.ChapterFormat;
import com.dev.cmielke.util.ApplicationContants.ChapterFormatInfo;

public class PerryRhodanBook {	
	private BookType type = BookType.OTHER;
	private int bookNumber;
	private String title="";
	private String subtitle="";
	private String authors="";
	private String introduction="";
	private String preChapter="";
	private String cycle;
	private final ArrayList<String> characters = new ArrayList<String>();
	private final ArrayList<Chapter> chapterList = new ArrayList<Chapter>();
	private BufferedImage cover;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	/**
	 * Introduction ist der Text direkt nach der Titelseite vor den Hauptpersonen.
	 * 
	 * @return Intro
	 */
	public String getIntroduction() {
		return introduction == null ? "" : introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	public boolean containsIntroduction() {
		return this.introduction!=null && this.introduction.length()>0;
	}
	
	/**
	 * Das Pre-Chapter ist ein eventuell nach den Hauptpersonen auftretender Text ohne Kapitelüberschrift.
	 * Der Text wird auch auf die Einführungsseite geschrieben und startet kein eigenes Kapitel da er meist zu
	 * kurz ist.<br/>
	 * Desweiteren erhält er eine spezielle Formatierung.
	 * 
	 * @return
	 */
	public String getPreChapter() {
		return preChapter == null ? "" : preChapter;
	}

	public void setPreChapter(String text) {
		this.preChapter = text;
	}
	
	public boolean containsPreChapter() {
		return this.preChapter!=null && this.preChapter.length()>0;
	}

	public boolean containsCharacters() {
		return this.characters!=null && !this.characters.isEmpty();
	}

	public ArrayList<String> getCharacters() {
		return characters;
	}

	public int getBookNumber() {
		return bookNumber;
	}

	public void setBookNumber(int bookNumber) {
		this.bookNumber = bookNumber;
	}

	public boolean containsAuthors() {
		return this.authors != null && this.authors.length() > 0;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	public BufferedImage getCover() {
		return cover;
	}

	public void setCover(BufferedImage cover) {
		this.cover = cover;
	}

	public boolean containsCover() {
		return this.cover != null;
	}

	public String getBookFilename() {
		String pattern = null;
		ResourceBundle rBundle = ResourceBundle.getBundle("pdf2epub");
		if (type == BookType.HEFTROMAN) {
			pattern = rBundle.getString("default.HeftFileName");
		} else if (type == BookType.SILBERBAND) {
			pattern = rBundle.getString("default.SilberbandFileName");
		}
		return pattern == null || pattern.isEmpty() ? "PerryRhodan_Band" + this.bookNumber : replaceTokens(pattern);
	}
	
	/**
	 * Use the pattern to replace the book information.
	 * <ul>
	 * <li>%-  Separator</li>
	 * <li>%A  Autor</li>
	 * <li>%T  Buch Titel</li>
	 * <li>%S  Buch Untertitel</li>
	 * <li>%C  PR Zyklus</li>
	 * <li>%N  Heft / Buch nummer</li>
	 * </ul>
	 * @param pattern
	 * @return
	 */
	public String replaceTokens(String pattern) {
		final String SEP = " - ";
		StringBuilder name = new StringBuilder(pattern);
		int pos;
		
		while( (pos=name.indexOf("%")) != -1) {
			char token = name.charAt(pos+1);
			switch(token) {
			case '-':
				name.replace(pos, pos+2, SEP);
				break;	// ignore first
			case 'A':	// Author
				name.replace(pos, pos+2, this.authors==null?"":this.authors);
				break;
			case 'T':	// Titel
				name.replace(pos, pos+2, this.title==null?"":this.title);
				break;
			case 'S':	// SubTitel
				name.replace(pos, pos+2, this.subtitle==null?"":this.subtitle);
				break;
			case 'C':	// Cyc�e
				name.replace(pos, pos+2, this.cycle==null?"":this.cycle);
				break;
			case 'N':	// Book number
				name.replace(pos, pos+2, this.bookNumber!=0?String.valueOf(this.bookNumber):"");
				break;
			default:
				throw new IllegalArgumentException("token %"+token+" is not allowed for filename pattern");
			}
		}
		
		if(name.length()>2 && name.substring(0, 3).equals(SEP)) {
			name.replace(0, 3, "");
		}
		while( (pos=name.indexOf(SEP+SEP)) != -1) {
			name.replace(pos, pos+2, "");	// delete double separator
		}
		return name.toString();

	}
	
	public String getDocumentTitle(String pattern) {
		return pattern==null||pattern.isEmpty() ? getBookNumber() + " - Perry Rhodan - " + getCycle() + " - " + getTitle() : replaceTokens(pattern);
	}

	public BookType getType() {
		return type;
	}

	public void setType(BookType type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		String buf = "";
		buf += "Title:"+(this.bookNumber!=0?String.valueOf(this.bookNumber)+" ":"");
		buf += this.title!=null ? this.title+" " : "";
		return buf;
	}

	/**
	 * Flag ob alle Meta-Daten gefüllt sind.
	 * 
	 * @return <code>true</code> wenn Nummer, Titel und Typ gefüllt ist.
	 */
	public boolean isMetaComplete() {
		return this.bookNumber>0 && this.title != null && this.type != null;
	}
	
	public int getChapterCount() {
		return chapterList.size();
	}
	
	public Chapter newChapter(Integer number, String titel) {
		Chapter c = new Chapter(number, titel);
		this.chapterList.add(c);
		return c;
	}

	public Chapter newChapter(Integer number, ChapterFormatInfo formatInfo) {
		Chapter c = new Chapter(number, formatInfo);
		this.chapterList.add(c);
		return c;
	}
	
	public void removeChapter(Chapter chapter) {
		this.chapterList.remove(chapter);
	}

	public List<Chapter> getChapters() {
		return Collections.unmodifiableList(this.chapterList);
	}
	public Chapter getCurrentChapter() {
		if(this.chapterList.size()==0) {
			throw new IllegalStateException("No chapter was started!");
		}
		return this.chapterList.get(this.chapterList.size()-1);
	}
	
	
	public class Chapter {
		private Integer number;
		private String titel;
		private ChapterFormat format;
		private final StringBuilder text = new StringBuilder();
		
		public Chapter(Integer number, String header) {
			this.number = number;
			this.titel = header;
			this.format = ChapterFormat.TEXT;
		}
		
		public Chapter(Integer number, ChapterFormatInfo info) {
			this.number = number;
			this.titel = info.titel;
			this.format = info.format;
		}
		
		public void setChapterNumber(int number) {
			this.number = Integer.valueOf(number);
		}
		public void setTitel(String titel) {
			this.titel = titel;
		}
		
		/**
		 * Check whether the chapter header is a numeric one.
		 * @return <code>true</code> Chapter has a numeric header
		 */
		public boolean hasChapterNumber() {
			return this.number!=null;
		}
		
		public int getChapterNumber() {
			return this.number!=null ? this.number.intValue() : -1;
		}
		public String getTitel() {
			return this.titel;
		}
		
		public ChapterFormat getFormat() {
			return this.format;
		}
		public void setFormat(ChapterFormat format) {
			this.format = format;
		}
		
		public void appendToText(String text) {
			if(this.text.length()>7) {
				String check = this.text.substring(this.text.length()-6);
				if(check.matches(".*[\\S][\\s]?-[\\s]{0,2}[\\n]?")) {
					int cut = check.length() - check.lastIndexOf("-");
					if(check.charAt(check.length()-cut-1) == ' ') {
						cut++;
					}
					this.text.replace(this.text.length()-cut, this.text.length(), "");
					
					int first=-1;
					while(text.charAt(first+1)==' ') {
						first++;
					}
					if(first>=0) {
						text = text.substring(first+1);
					}
				}
			}
			this.text.append(text);
		}
		
		public String  getText() {
			return this.text.toString();
		}
		public void setText(String text) {
			this.text.delete(0, this.text.length());
			this.text.append(text);
		}
		
		public String getHtmlFileName() {
			if(number!=null) {
				String formattedChapterNumber = LoadWriteUtils.formatChapterNumber(number.intValue(), ApplicationContants.DEFAULT_DIGIT_COUNT);
				if(titel!=null) {
					return formattedChapterNumber+ApplicationContants.DEFAULT_HTML_FILE_EXTENSION;
				}else{
					return formattedChapterNumber+ApplicationContants.DEFAULT_HTML_FILE_EXTENSION;
				}
			}else{
				if(titel!=null) {
					return titel.toLowerCase()+ApplicationContants.DEFAULT_HTML_FILE_EXTENSION;
				}else{
					throw new IllegalStateException("Keine Kapitelnummer oder Titel vergeben");
				}
			}
		}
		
		@Override
		public String toString() {
			return "Chapter: "+(number!=null?number+".":"")+(titel==null?"":" "+titel);
		}
	}
}
