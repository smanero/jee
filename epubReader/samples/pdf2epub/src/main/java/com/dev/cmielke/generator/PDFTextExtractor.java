package com.dev.cmielke.generator;

import static com.dev.cmielke.util.StringUtilities.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.util.PDFTextStripper;

import com.dev.cmielke.beans.BookType;
import com.dev.cmielke.beans.PerryRhodanBook;
import com.dev.cmielke.beans.PerryRhodanCycles;
import com.dev.cmielke.beans.PerryRhodanSilberbandCycles;
import com.dev.cmielke.beans.PerryRhodanBook.Chapter;
import com.dev.cmielke.gui.beans.Options;
import com.dev.cmielke.util.ApplicationContants;
import com.dev.cmielke.util.LoggingUtils;
import com.dev.cmielke.util.ApplicationContants.ChapterFormat;
import com.dev.cmielke.util.ApplicationContants.ChapterFormatInfo;

public class PDFTextExtractor {
	private static final Logger log = Logger.getLogger(PDFTextExtractor.class);

	private PerryRhodanBook book;
	private PDDocument document;
	private PDFTextStripper stripper;
	
	private boolean warning;

	private int startPage;
	private int endPage;
	private int currentPage;
	private State state = State.UNDEFINED;
	private boolean imgPageFound;
	/**
	 * Wir zZ gesetzt wenn mind. ein Kapitel gefunden wird numerisch beginnt.
	 */
	private Boolean ignoreChapterNumberWithText;

	public PDFTextExtractor(File file) throws IOException {
		try {
			this.document = PDDocument.load(file);
		} catch (IOException e) {
			log.error("Loading of PDF-Document failed!", e);
			throw e;
		}
		try {
			this.stripper = new PDFTextStripper();
		} catch (IOException e) {
			log.error("Error occured during instanciating PDFTextStripper!", e);
			throw e;
		}

		this.book = new PerryRhodanBook();
		this.startPage = 1;
		this.endPage = getDocument().getNumberOfPages();
		this.currentPage = startPage;
	}

	/**
	 * Analysiert das PDF document und extrahiert den Perry Rhodan Roman.
	 */
	public void processDocument() {
		try {
			while (hasNextPage()) {
				log.debug("CURRENT-PAGE: [" + getCurrentPage() + "]");

				getStripper().setStartPage(getCurrentPage());
				getStripper().setEndPage(getCurrentPage());

				StringReader sReader = new StringReader(getStripper().getText(getDocument()));
				BufferedReader bReader = new BufferedReader(sReader);

				processingBuffer(bReader);

				getNextPage();
			}
		} catch (IOException e) {
			throw new RuntimeException("Problem during PDF read!",e);
		}finally{
			try {
				getDocument().close();
			} catch (IOException e) {
				log.error("Error occured during closing PDDocument!", e);
			}
		}
	}
	
	/**
	 * Liefert das extrahierte PDF als Buch.
	 * @return Extrahierte Metainformationen und Kapitel.
	 */
	public PerryRhodanBook getBook() {
		return this.book;
	}
	/**
	 * Falg og warnungen bei der Konvertierung aufgetreten sind.
	 * 
	 * @return <code>true</code> wenn bei der Konvertierung Probleme aufgetreten sind die das Ergebniss verfÃ¤lschen kÃ¶nnten.
	 */
	public boolean hasWarnings() {
		return this.warning;
	}

	private void processingBuffer(BufferedReader reader) throws IOException {
		reader.mark(200);
		{
			String line = getNextLine(reader, true, true, false);
			if(line != null && line.matches(".* M Perry Rhodan")) {
				log.trace("First line look like Page Header, ignoring!");
			}else{
				reader.reset();
			}
		}
		// Versuchen aus den ersten beiden Seiten das Cover Image / Heftnummer und
		// Titel zu extrahieren
		if (getCurrentPage() < 6 && (state==State.UNDEFINED || state==State.INTRODUCTION_NEXT || state==State.PERSONS_NEXT || state==State.PERSONS_PENDING) ) {
			if (!imgPageFound) { // Picture auf der Titelseite extrahieren
				imgPageFound = extractTitleImage(reader);
				if(imgPageFound) {
					reader.mark(2048);
					String line = getNextLine(reader,true, true, false);
					if(line==null) {	// einige Hefte nur mit Titelbild auf der ersten Seite
						return;
					}
					reader.reset();
				}
			}
			
			if (state==State.UNDEFINED) {
				getMetaInfoFromFirstPages(reader);
				if(state!=State.UNDEFINED) {
					log.debug("PDF MetaInfo  "+book);
				}
			}
			if(getCurrentPage()==1) {
				skipUnconvertableCoverPage(reader);
			}
			if(state==State.UNDEFINED || state==State.INTRODUCTION_NEXT) {
				extractIntroduction(reader);
			}
			if(state==State.PERSONS_NEXT || state==State.PERSONS_PENDING) {
				extractPersons(reader);
			}
		}

		if(state==State.BOOK_META_COMPLETE) {	// between "charaters" and first chapter
			String line = null;
			StringBuilder buf = new StringBuilder();
			while ( (line=getNextLine(reader, true, true, false))!=null && !isChapterMark(line)) {
				if(line.trim().length()>0 || buf.length()>0) {	// skip if first lines are empty
					buf.append(line).append("\n");
				}
				reader.mark(10000);
			}
			if(line!=null && isChapterMark(line)) {
				state = State.BOOK_PRECHAPTER;
				reader.reset();
				if(getChapterNumber(line)!=null) {
					int chapter = getChapterNumber(line);
					if(chapter == 1) {	// korrekter Kapitelstart
						if(buf.length() > 0) {
							book.setPreChapter(book.getPreChapter()+buf.toString());
						}
					}else if(chapter >= 2) {	// Sonderfall Kap.1. ohne einleitendes '1.'
						// Introduction und vorhandenen Text als Kapitel 1. übernehmen
						log.warn("Kapitelmarkierung '"+chapter+".' gefunden. Lege Text als Kapitel 1. an" );
						warning=true;
						//TODO Kapitelnummerierung bzw. Inhaltsverzeichnis berichtigen !  
						state = State.BOOK_CHAPTERS;
						Chapter c = book.newChapter(1, getChapterFormatInfo(line));
						c.appendToText(book.getPreChapter()+buf.toString());
						book.setPreChapter(null);
					}else if(chapter==1 && book.getChapterCount()>0) {
						Chapter c = book.getCurrentChapter();
						c.setChapterNumber(1);
					}
				}else if(buf.length() > 0) {	// Sonderkapitel, Text als Intro behandeln
					book.setPreChapter(book.getPreChapter()+buf.toString());
				}
			}else if(line==null) {
				book.setPreChapter(book.getPreChapter()+buf.toString());
				return;
			}else{
				throw new IllegalStateException("Unexpected line found, only a chapter tag expected!\n      LINE>"+line);
			}
		}
		if(state != State.BOOK_PRECHAPTER && state != State.BOOK_CHAPTERS) {
			String line;
			StringBuilder buf = new StringBuilder();
			while( (line = getNextLine(reader, true, false, true)) != null) {
				buf.append(line).append("\n");
			}
			if(buf.length()>0) {
				log.warn("Unerwarteter Text vor Kapitel state="+state+", wird zum PreChapter text hinzugefügt!");
				book.setPreChapter(book.getPreChapter()+" "+buf.toString().trim());
			}
			return;
		}

		if (state == State.BOOK_PRECHAPTER) {
			state=State.BOOK_CHAPTERS;
		}else if(state != State.BOOK_CHAPTERS){
			throw new IllegalStateException("TextExtractor not in State BOOK_CHAPTERS");
		}

		// Normale Linien
		String line;
		while( (line=getNextLine(reader, false, true, true)) != null) {
			processLine(line);
		}
	}


	private void processLine(String line) {
		line = line.trim();
		if (!isPageNumber(line)) {	// ignore page numbers
			if (isChapterMark(line)) {	// is it a chapter
				PerryRhodanBook.Chapter currentChapter = null;
				Integer chapter = getChapterNumber(line);

				if(book.getChapterCount()>0) {
					currentChapter = book.getCurrentChapter();
				}
				if(chapter==null || currentChapter == null) {	// aktuelles Kapitel ohne nummer oder noch kein Kapitel vorhanden
					log.debug("Found chapter header '"+line+"'");
					book.newChapter(chapter,getChapterFormatInfo(line)); // starte neues Kapitel
				}else if(currentChapter.hasChapterNumber() && currentChapter.getChapterNumber()+1 == chapter.intValue()) {	// Kapitel nummerierung aufsteigend
					log.debug("Found chapter "+chapter);
					book.newChapter(chapter,getChapterFormatInfo(line)); // starte neues Kapitel
				}else if(currentChapter.hasChapterNumber() && currentChapter.getChapterNumber() < chapter.intValue()
						&& currentChapter.getChapterNumber()+Options.getAllowedChapterGap() >= chapter.intValue()
				   ) {	// Kapitel nummerierung mit Lücken
					log.warn("Unerwartete Kapitelnummer "+chapter+". anstelle von "+(currentChapter.getChapterNumber()+1)+". gefunden PDF Seite #"+this.currentPage+". Kapitel werden übersprungen!");
					warning = true;
					book.newChapter(chapter,getChapterFormatInfo(line)); // starte neues Kapitel
				}else if(currentChapter.hasChapterNumber()) {	// Vorheriges Kapitel hat eine Numerierung
					log.warn("Zeile mit Kapitelnummerierung gefunden '" + chapter + ".' PDF Seite #"+this.currentPage+", stimmt aber nicht mit dem erwarteten Kapitel überein. Wird ignoriert.\n        LINE>"+line);
					warning=true;
					currentChapter.appendToText(line+"\n");
				}else{	// laufendes Kapitel hat keine Nummerierung
					//TODO was ist wenn vorher schon mal eine Nummer vorhanden war ??
					log.debug("Found chapter "+chapter);
					book.newChapter(chapter, getChapterFormatInfo(line));
				}
			} else {
				PerryRhodanBook.Chapter currentChapter = book.getCurrentChapter();	//TODO was ist mit Kapitel 1.
				switch(currentChapter.getFormat()) {
				case ZEITTAFEL:
					if(line.matches("[\\s]*[0-9][0-9/]*[\\s].*")) {
						currentChapter.appendToText("\n"+line);
					}else{
						currentChapter.appendToText(" "+line);
					}
					break;
				case GLOSSAR: {
					if(line.length()!=0) {
						if(line.endsWith("-") && line.length()<40) {	// Begriff endet immer mit '-'; Länge begrenzt wg. Trennstrich
							// Begriff
							currentChapter.appendToText("\n"+line+"\n");
						}else{
							currentChapter.appendToText(line+" ");
						}
					}
				}
				break;
				case PREFORMATED:
				case TEXT:
					if (line.matches("\\*")) { // Separator
						currentChapter.appendToText("\n\t\t%" + line + "%\n\n");
					} else if (line.replaceAll(" ", "").toLowerCase().equals("ende")) {
						currentChapter.appendToText("\n\t\t%E N D E%\n\n");
					} else {
						currentChapter.appendToText(line + "\n");
					}
					break;
					default:
						throw new UnsupportedOperationException("ChapterFormat "+currentChapter.getFormat()+" not implemented!");
				}
			}
		}
	}

	private boolean getMetaInfoFromFirstPages(BufferedReader reader) throws IOException {
		boolean headerFound = false;

		if (!reader.markSupported()) {
			throw new UnsupportedOperationException("BufferedReader does not support 'mark'");
		}
		reader.mark(2048);

		headerFound = getMeta4HeftRoman(reader);
		reader.reset();
		if(!headerFound) headerFound = getMeta4Silberband(reader);
		reader.reset();

		return headerFound;
	}

	/**
	 * Heftroman Format, Seite beginnt mit<br/>
	 * Nr. xxx
	 * Titel
	 * Untertitel
	 * Author
	 * Kurzinhalt "Die Hauptpersonen des Romans" => pro Zeile eine Person
	 *  "1." => Start des ersten kapitel
	 */
	private boolean getMeta4HeftRoman(BufferedReader bReader) throws IOException {
		String line = getNextLine(bReader, true, true, false);
		if(line==null) {
			log.trace("Only empty lines found");
			return false;
		}

		try {
			String heftNo;
			int pos = line.indexOf("Nr.");
			if(pos>-1) {
				int end=line.indexOf(" ",pos+4);
				heftNo = line.substring(pos+3,end==-1?line.length():end).trim();
				pos = end==-1?line.length():end;
			}else if( (pos=line.indexOf("Nr"))>-1){
				int end=line.indexOf(" ",pos+3);
				heftNo = line.substring(pos+2,end==-1?line.length():end);
				pos = end==-1?line.length():end;
			}else{
				log.trace("Heftnummer nicht gefunden, kein Heftroman Format");
				return false;
			}
			book.setBookNumber(Integer.parseInt(heftNo));
			if(line.length()>pos && line.substring(pos).trim().length()>0) {
				// sollte titel enthalten
				book.setTitle(line.substring(pos).trim());
			}
		} catch (IndexOutOfBoundsException e) {
			log.trace("Heftnummer nicht gefunden, kein Heftroman Format");
			return false;
		} catch (NumberFormatException e) {
			log.trace("Heftnummer nicht gefunden, kein Heftroman Format");
			return false;
		}
		book.setType(BookType.HEFTROMAN);
		book.setCycle(PerryRhodanCycles.getCycleName(book.getBookNumber()));
		log.debug("BookType Perry Rhodan Heftroman");
		bReader.mark(2048);
		if(book.getTitle().length()==0) {
			book.setTitle(extractTitle(bReader, true, 15));
		}else{
			String tLine = bReader.readLine();
			if(tLine!=null && tLine.trim().length()>0 && tLine.trim().length()<15) {
				book.setTitle(book.getTitle()+" "+tLine.trim());
			}else{
				bReader.reset();
			}
		}

		// Getting subtitle
		book.setSubtitle(extractSubtitle(bReader, false, 50));
		book.setAuthors(extractAuthor(bReader));
		log.debug(LoggingUtils.filteredObjectToString(book));
		state=State.INTRODUCTION_NEXT;
		return true;
	}
	
	/**
	 * Liefert den Titel oder SubTitel ein- oder zweizeilig. Die Zweite Zeile darf nur eine hï¿½chstanzahl Zeichen haben als zweite Zeile
	 * @param reader
	 * @param ignEmptyLines führende Leerzeilen ignorieren (nur bei Subtitel
	 * @param maxLenght maximale Länge der zweiten Zeile um als Umbruch zu gelten
	 * @return
	 * @throws IOException
	 */
	private String extractTitle(BufferedReader reader, boolean ignEmptyLines, int maxLenght) throws IOException {
		reader.mark(2048);
		String titel = "";
		String line;
		int lineCount = 0;
		while ((line = reader.readLine()) != null) {
			;
			if (line.isEmpty()) {
				if (lineCount > 0) {
					reader.mark(2048);
					break;
				} else {
					if (ignEmptyLines)
						continue;
					break;
				}
			}
			if (isAuthorTag(line)) {
				reader.reset();
				break;
			} else if (lineCount == 0 || line.trim().length() < maxLenght) {
				if (lineCount > 2) {
					reader.reset();
					titel = "";
					break;
				}

				titel = (titel + " " + line).trim();
				lineCount++;
			} else {
				// WICHTIG: ZURÜCKSETZEN AUS ANFANG, DAMIT DIE SUBTITLE-EXTRAKTION
				// AUCH DEN RICHTIGEN ANFANG HAT !!!
				reader.reset();
				break;
			}
		}
		return titel;
	}

	private String extractSubtitle(BufferedReader reader, boolean ignEmptyLines, int maxLenght) throws IOException {
		reader.mark(2048);
		String titel = "";
		String line;
		int lineCount = 0;
		while ((line = reader.readLine()) != null) {
			;
			if (line.isEmpty()) {
				if (lineCount > 0) {
					reader.mark(2048);
					break;
				} else {
					if (ignEmptyLines)
						continue;
					break;
				}
			}
			if (isAuthorTag(line)) {
				reader.reset();
				break;
			}
			// Filterung, ob der String den Titel des Romans reprï¿½sentiert.
			else if (!book.getTitle().isEmpty() && line.contains(book.getTitle())) {
				continue;
			} else if (lineCount == 0 || line.trim().length() < maxLenght) {
				if (lineCount > 2) {
					reader.reset();
					titel = "";
					break;
				}
				if (!line.trim().isEmpty()) {
					titel = (titel + " " + line).trim();
					lineCount++;
				}
			} else {
				break;
			}
		}
		return titel;
	}
	
	private String extractAuthor(BufferedReader reader) throws IOException {
		String line = getNextLine(reader, true, false, false);
		for (int index = 0; index < 10; index++) {
			if (isAuthorTag(line)) {
				reader.mark(2048);
				return capitalizeWords(line.substring(4));
			} else {
				line = getNextLine(reader, true, false, false);
			}
		}
		return "";
	}
	
	/**
	 * Extract the book.Introduction and set the file pointer at the begining of next chapter or character's.
	 * 
	 * @param reader
	 * @throws IOException
	 */
	private void extractIntroduction(BufferedReader reader) throws IOException {
		StringBuilder buffer = new StringBuilder();
		reader.mark(2048);
		String line = getNextLine(reader, true, true, true);
		while (line != null && !(isCharacterStartTag(line) || isChapterMark(line)) ) {
			if(isPreChapterMark(line)) {
				if(line.equalsIgnoreCase("ZEITTAFEL")) {	// skip ZEITtafel
					while( (line=getNextLine(reader,true, true, false))!=null ) {
						if(isPreChapterMark(line)) {
							break;
						}
						reader.mark(2048);
					}
					if(isChapterMark(line)) {
						break;	// nach ZEITTAFEL kein PROLOG ...
					}
				}
				if(line != null) {	// check nach ZEITTAFEL
					line=line.trim().toUpperCase();
					buffer.append("\n\t\t%").append(line);
					buffer.append("%\n");
				}
			}else{
				buffer.append(line).append("\n");
			}
			reader.mark(2048);
			line = reader.readLine();
		}
		book.setIntroduction(book.getIntroduction()+buffer.toString());
		if(line!=null && isCharacterStartTag(line)) {
			state = State.PERSONS_NEXT;
		}else if(line!=null && isChapterMark(line)) {
			log.trace("Intrduction end with start of chapter");
			state = State.BOOK_PRECHAPTER;
		}
		reader.reset();
	}
	
	/**
	 * Lesen der Hauptpersonen.
	 * Die Aufzählung startet mit der Einleitung 'Hauptpersonen des ...' {@link #isCharacterStartTag(String)}
	 * Leerzeilen zwischen der Einleitung und der ersten Person werden ignoriert.
	 * Die Beschreibung endet wenn kein ' - ' zwischen Namen und Beschreibung mehr gefunden wird.
	 * 
	 * @param reader
	 * @throws IOException
	 */
	private void extractPersons(BufferedReader reader) throws IOException {
		reader.mark(2048);
		String line=null;
		if(state!=State.PERSONS_PENDING) {
			line = getNextLine(reader, true, false, false);
		}
		if(state==State.PERSONS_PENDING || (line != null && isCharacterStartTag(line))) {
			if(state!=State.PERSONS_PENDING) {
				do {
					reader.mark(2048);
				}while( (line=reader.readLine()) != null && line.trim().length()==0);
				reader.reset(); // set marker to the first line of character
				state = State.PERSONS_PENDING;
			}
	
			// Schreibe alle Linien bis zum ersten Kapitel in den Buffer
			while ( (line=reader.readLine()) != null && !isChapterMark(line)) {
				// Die Zeile sollte ' Name - Beschreibung' sein
				// kommt kein ' - ' vor oder ist das mehrfach vorhanden wird angenommen dass die Aufzaehlung 
				// beendet ist.
				if (line.contains(" - ") && line.split(" - ").length < 4
						&& (!line.contains(",") || (line.indexOf(" - ") < line.indexOf(","))
								|| (line.indexOf(" - ") < (line.length()/2) )
						) ) {
					if (line.endsWith(".")) {
						book.getCharacters().add(line);
					} else {
						// additional lines follow?
						reader.mark(1024);
						String secondLine = reader.readLine();
						if(secondLine!=null && !secondLine.contains("-")) {
							String character = line.trim()+" "+secondLine.trim();
							book.getCharacters().add(character);
						}else{
							reader.reset();
							book.getCharacters().add(line);
						}
					}
				} else {
					// Ende der Character-Aufzählung
					state = State.BOOK_META_COMPLETE;
					break;
				}
				reader.mark(2048);
			}
			if(isChapterMark(line)) {
				state = State.BOOK_META_COMPLETE;
			}else if(line == null) {
				reader.mark(10);	// setzen auf Seitenende
			}
		}else{
			log.trace("Tag 'Hauptpersonen' not found");
		}
		reader.reset();
	}

	/**
	 * Heftroman Format, Seite beginnt mit Nr. xxx Titel Untertitel Author
	 * Kurzinhalt "Die Hauptpersonen des Romans" => pro Zeile eine Person "1." =>
	 * Start des ersten kapitel
	 */
	private boolean getMeta4Silberband(BufferedReader bReader) throws IOException {
		boolean found = false;
		String line;
		
		for (int i = 0; i < 10 && !found; i++) {
			if( (line = bReader.readLine())!=null) {
				if(line.trim().startsWith("Perry Rhodan Silberband")) {
					log.debug("BookType Perry Rhodan Silberband");
					found = true;
					try {
						book.setBookNumber(Integer.valueOf(line.trim().substring(24)));
					}catch(NumberFormatException e) {
						log.error("Line contains 'Silberband' but not a correct booknumber  >"+line);
					}catch(IndexOutOfBoundsException e) {
						log.error("Line contains 'Silberband' but not a correct booknumber  >"+line);
					}
					// search Subtitle
					for (int j = 0; j < 3 && book.getSubtitle().length()==0; j++) {
						line = bReader.readLine();
						if(line.trim().length()>0) {
							book.setSubtitle(line.trim());
						}
					}
				}else if(line.matches("Perry Rhodan, Band [0-9][0-9]*, .*")) {	// könnte 'Perry Rhodan, Band #, 'Titel' sein
					log.debug("Header Perry Rhodan, Band #, Titel  found, possible Silberband");
					found = true;
					for (String s : line.split(",")) {
						s = s.trim();
						if(s.matches("Band [0-9][0-9]*")) {
							book.setBookNumber(Integer.parseInt(s.substring(5)));
						}else if(!s.startsWith("Perry Rhodan")) {
							book.setSubtitle(s);
						}
					}
				} else if(line.trim().startsWith("Perry Rhodan")) {	// mal versuchen ....
					log.debug("possible BookType Perry Rhodan Silberband");
					
					for (int j = 0; j < 10 && line != null; j++) {
						line = bReader.readLine();
						if(line != null && line.trim().length()>0) {
							if(isChapterMark(line) || isChapterMark(line)) {
								i=20;	// äussere Schleife abbrechen
								break;
							}
							if(line.matches("Band.*[0-9]*[\\s]*")) {
								found = true;
								book.setBookNumber(extractNumber(line));
							}else {
								found = true;
								if(book.getSubtitle().length()>0) {
									book.setSubtitle(book.getSubtitle()+" "+line.trim());
								}else{
									book.setSubtitle(line.trim());
								}
							}
						}else if(book.getSubtitle().length()>0) {
							break;	//subtitel complete
						}
					}
				}
			}
		}
		if(found) {
			book.setType(BookType.SILBERBAND);
			if(book.getBookNumber()>0) {
				book.setCycle(PerryRhodanSilberbandCycles.getCycleName(book.getBookNumber()));
			}
			book.setTitle("Perry Rhodan Silberband"+(book.getBookNumber()!=0?" "+book.getBookNumber():""));
			
			state=State.INTRODUCTION_NEXT;
			bReader.mark(2048);
			// skip first page if no context
			while( (line = getNextLine(bReader, true, true, true))!=null) {
				if(isChapterMark(line) || isAuthorTag(line) || isPreChapterMark(line)) {
					break;
				}
				bReader.mark(2048);
			}
		}
		return found;
	}
	
	/**
	 * Read the next line that is not empty or filled with white-spaces.
	 * 
	 * @param reader the stream to read from
	 * @param skipEmptyLines TODO
	 * @param ignorePageNumber lines only whith a number are ignored
	 * @param ignoreKnownLines ignore lines that are known page header or footer
	 * @return <code>null</code> at EOF or the next line with text
	 * @throws IOException
	 */
	private String getNextLine(BufferedReader reader, boolean skipEmptyLines, boolean ignorePageNumber, boolean ignoreKnownLines) throws IOException {
		String line;
		while( (line=reader.readLine())!=null) {
			line = line.trim();
			if(line.length() > 0
					&& (!ignorePageNumber || !isPageNumber(line))
					&& (!ignoreKnownLines || !isLineToIgnore(line))) {
				return line;
			}else if(line.length()==0 && !skipEmptyLines) {
				return line;
			}
		}
		return null;
	}
	
	/**
	 * Prueft ob auf der Seite ein Image vorhanden ist und traegt es als Titelseite
	 * ein.
	 * 
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	private boolean extractTitleImage(BufferedReader reader) throws IOException {
		boolean found = false;
		List<?> pages = getDocument().getDocumentCatalog().getAllPages();
		PDPage firstPage = (PDPage) pages.get(0);
		PDResources resources = firstPage.getResources();
		Map<?, ?> images = resources.getImages();

		if (images != null) {
			Iterator<?> imageIter = images.keySet().iterator();
			while (imageIter.hasNext()) {
				String key = (String) imageIter.next();
				if (!imgPageFound) {
					book.setCover(((PDXObjectImage)images.get(key)).getRGBImage());
				} else {
					log.warn("more than one image found! " + key);
					warning=true;
				}
				found = true;
			}
		}
		return found;
	}
	
	/**
	 * Prüft auf bekannte Zeilen die ignoriert werden sollen.
	 */
	private boolean isLineToIgnore(String line) {
		line = line.trim();
		return line.matches("Perry Rhodan, Band [0-9]*, .*");
	}
	/**
	 * Check that the line is not a page number.
	 * 
	 * @param line text line from input
	 * @return <code>true</code> if the line contains only [WHITESPACE] and page number
	 */
	private boolean isPageNumber(String line) {
		return line.matches("[\\s]*[0-9][0-9]*") || line.matches("[\\s]*- [0-9][0-9]* -");
	}
	/**
	 * Prï¿½fen ob die Zeile eine Kopfzeile fï¿½r eine Einleitung ist.<br/>
	 * Gueltige Marker 'Einleitung' 'Vorspiel' 'Prolog'
	 * 
	 * @param line zu analysierende Zeile
	 * @return
	 */
	private boolean isPreChapterMark(String line) {
		if(line!=null) {
			line = line.trim();
			return line!=null && (line.equalsIgnoreCase("EINLEITUNG") || line.equalsIgnoreCase("VORSPIEL") || line.equalsIgnoreCase("PROLOG") || line.equalsIgnoreCase("ZEITTAFEL"));
		}
		return false;
	}
	
	/**
	 * Check that the line is a chapter marker
	 * @param line
	 * @return <code>true</code> if the line is a chapter start
	 */
	private boolean isChapterMark(String line) {
		// prüfen ob das ein Datum ist
		if(line.matches(".*(Januar|Februar|März|Maerz|April|Mai|Juni|Juli|August|September|Oktober|November|Dezember).*")) {
			return false;
		}
		
		if (this.ignoreChapterNumberWithText == null) {
			if (line.matches("[0-9][0-9]*[\\.]{1,2}[ ]+.{0,32}")) {
				this.ignoreChapterNumberWithText = Boolean.FALSE;
			} else if (line.matches("[0-9][0-9]*[\\.]{1,2}[ ]*")) {
				this.ignoreChapterNumberWithText = Boolean.TRUE;
			}
		}
		return line != null && (ApplicationContants.isChapterTitel(line) ||
				// Filterung nach Kapiteln welche zusaetzlich zu der Kapitelnummer eine
				// Ueberschrift besitzen.
				// Diese Filterung ist charakterisiert durch das zwingende Vorhandensein
				// eines Leerzeichens
				// nach dem Kapitel-Punkt '.' (siehe 1. RegEx )
				(this.ignoreChapterNumberWithText!=null && !this.ignoreChapterNumberWithText && line.matches("[0-9][0-9]*[\\.]{1,2}[ ]+.{0,32}")) ||
				// Ist die Filterung nach 1.) nicht erfolgreich, bleiben nur noch 2
				// Möglichkeiten offen
				// a.) Der String ist ganz anders als die RegEx., dann handelt es sich
				// auch nicht um ein Kapitel, oder
				// b.) Der String ist einfaches Kapitel ohne Überschrift und enthält
				// nach dem Kapitel-Punkt keine weiteren
				// Zeichen (dies wird mittels der 2. RegEx abgeprüft).
				line.matches("[0-9][0-9]*[\\.]{1,2}[ ]*"));
	}

	/**
	 * extract the chapters number.
	 * @param line the line from PDF
	 * @return The number of chapter 
	 * @throws IllegalStateException if the line contains no chapter mark
	 */
	private Integer getChapterNumber(String line) {
		if (!isChapterMark(line)) {
			throw new IllegalStateException("Line is not a chapter mark >" + line);
		}

		String number = "";

		for (int index = 0; index < line.length(); index++) {
			if (String.valueOf(line.charAt(index)).matches("[0-9]")) {
				number += line.charAt(index);
			} else {
				break;
			}
		}
		return number.length() > 0 ? Integer.parseInt(number) : null;
	}

	/**
	 * extract the chapters number.
	 * @param line the line from PDF
	 * @return The number of chapter 
	 * @throws IllegalStateException if the line contains no chapter mark
	 */
	private ChapterFormatInfo getChapterFormatInfo(String line)	{
		if (!isChapterMark(line))	{
			throw new IllegalStateException("Line is not a chapter mark >"	+ line);
		}

		String titel = null;
		
		for (int index = 0; index < line.length(); index++) {
			if(String.valueOf(line.charAt(index)).matches("[0-9]"))	{
				continue;
			}else{
				titel = line.substring(index, line.length());
				titel = titel.replaceAll("\\.", "").trim();
				break;
			}
		}
		if(titel!=null && titel.length()>0 && ApplicationContants.isChapterTitel(titel)) {
			return ApplicationContants.getChapterFormatInfo(titel);
		}
		return new ChapterFormatInfo(titel==null||titel.length()==0?null:titel,ChapterFormat.TEXT);
	}
	
	private boolean isCharacterStartTag(String line) {
		return line!=null && 
		(line.matches("Die Hautpersonen des Romans:[ ]*") || line.matches("Die Hauptpersonen des Romans:[ ]*") || line.matches("Hauptpersonen des Romans:[ ]*")
				|| line.matches("Die Hauptpersonen des Roman:[ ]*") || line.matches("Die Hauptpersonen des Romans[ ]*")
				|| line.matches("Die Hauptpersonen des Roman.*[ ]*"));
	}

	private boolean isAuthorTag(String line) {
		return line!=null && (line.startsWith("von ") || line.startsWith("Von "));
	}

	/**
	 * Prüft og auf der Seite (nur) Verlagsinformationen stehen und überliest die gesamte Seite.
	 * Sollte nur für die erste Seite angewendet werden.
	 * 
	 * @param reader
	 * @throws IOException
	 */
	private void skipUnconvertableCoverPage(BufferedReader reader) throws IOException {
		boolean skipPage=false;
		String line;
		
		while( (line = getNextLine(reader, true, true, false)) != null && !isChapterMark(line) && !isAuthorTag(line) && !isCharacterStartTag(line)) {
			if(line.contains("Alle Rechte vorbehalten") 
					|| line.contains("ISBN")
					){
				skipPage=true;
			}
		}
		
		if(skipPage) {
			if(line!=null) {
				throw new UnsupportedOperationException("Skip auf der Seite nur implementiert wenn keine Tags auf der gleichen Seite sind");
			}
			reader.mark(10);
		}else{
			reader.reset();
		}
	}
	
	private PDDocument getDocument() {
		return document;
	}

	private PDFTextStripper getStripper() {
		return stripper;
	}

	private int getEndPage() {
		return endPage;
	}

	private int getCurrentPage() {
		return currentPage;
	}

	private void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	private int getNextPage() {
		setCurrentPage(getCurrentPage() + 1);
		return getCurrentPage();
	}

	private boolean hasNextPage() {
		boolean retValue = false;
		if (getCurrentPage() <= getEndPage()) {
			retValue = true;
		}
		return retValue;
	}

	private enum State {
		/**
		 * Begin state or indicator that it looks not as a PerryRhodan Book
		 */
		UNDEFINED,
		/**
		 * The introduction chapter must follow, even if page is changed
		 */
		INTRODUCTION_NEXT,
		/**
		 * Expect that "Hauptpersonen" is next
		 */
		PERSONS_NEXT,
		/**
		 * "Hauptpersonen" might cross pages
		 */
		PERSONS_PENDING,
		/**
		 * Meta information complete, first chapter not found
		 */
		BOOK_META_COMPLETE,
		BOOK_PRECHAPTER,
		/**
		 * First chapter found, %BEGIN% is written
		 */
		BOOK_CHAPTERS
	}
}