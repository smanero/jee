package com.dev.cmielke.gui.controller;

import static com.dev.cmielke.util.ApplicationContants.OUTPUT_DIRECTORY;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import org.apache.log4j.Logger;

import com.dev.cmielke.beans.PerryRhodanBook;
import com.dev.cmielke.build.EPubBuilder;
import com.dev.cmielke.generator.HTMLGenerator;
import com.dev.cmielke.generator.PDFTextExtractor;
import com.dev.cmielke.gui.beans.DocumentList;
import com.dev.cmielke.gui.beans.Options;
import com.dev.cmielke.gui.command.ActionCommand;
import com.dev.cmielke.gui.command.NotifyCommand;
import com.dev.cmielke.gui.dialogs.DialogFactory;
import com.dev.cmielke.gui.dialogs.components.DialogBox;
import com.dev.cmielke.gui.dialogs.BookChapterDialog;
import com.dev.cmielke.gui.util.DialogConstants;
import com.dev.cmielke.gui.view.MainWindow;
import com.dev.cmielke.util.FileSystemUtils;
import com.dev.cmielke.util.filter.PortableDocumentFilter;

public class MainController implements ActionListener, Observer {
	private static Logger log = Logger.getLogger(MainController.class);

	private DocumentList list;
	private MainWindow window;

	public MainController(MainWindow window) {
		this.window = window;

		list = new DocumentList();
		list.addObserver(this);

		setWindowPrefenrences();
	}

	private void setWindowPrefenrences() {
		window.registerActionListener(this);
		window.centerWindow();
		window.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		ActionCommand cmd = ActionCommand.getCommand(e.getActionCommand());
		if (cmd != null) {
			switch (cmd) {
			case ADD_DOCUMENT: {
				log.debug("> ACTION: [" + cmd.name() + "," + cmd.getCode() + "]");
				JFileChooser chooser = new JFileChooser(Options.getInputPath());
				chooser.setMultiSelectionEnabled(true);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setFileFilter(new PortableDocumentFilter());
				int result = chooser.showOpenDialog(window);
				if (result == JFileChooser.APPROVE_OPTION) {
					list.addDocuments(chooser.getSelectedFiles());
				} else {
					log.debug("No files selected, dialog aborted.");
				}
				log.debug("< ACTION: [" + cmd.name() + "," + cmd.getCode() + "]");
				break;
			}
			case CLEAR_LIST:
				log.debug("> ACTION: [" + cmd.name() + "," + cmd.getCode() + "]");
				list.removeAllDocuments();
				log.debug("< ACTION: [" + cmd.name() + "," + cmd.getCode() + "]");
				break;
			case START_PROCESSING:
				log.debug("> ACTION: [" + cmd.name() + "," + cmd.getCode() + "]");
				
				this.window.setWorkInProgress(true);
				this.window.updateOutputConsole("");	// clear progress output
				Converter c = new Converter();
				c.start();
				log.debug("< ACTION: [" + cmd.name() + "," + cmd.getCode() + "]");
				break;
			}
		} else {
			log.error("Could not parse ActionCommand Object!");
		}
	}

	public void update(Observable o, Object arg) {
		try {
			NotifyCommand cmd = (NotifyCommand) arg;
			switch (cmd) {
			case UPDATE_DOCUMENT_LIST: {
				log.debug("Notify-Command:[" + NotifyCommand.UPDATE_DOCUMENT_LIST.getCode() + ", value:"
						+ NotifyCommand.UPDATE_DOCUMENT_LIST.name() + "]");
				window.updateDocumentList(list.getDocumentNames());
				break;
			}
			}
		} catch (Exception e) {
			log.error("got an exception!", e);
		}
	}
	
	private class Converter extends Thread {
		public void run() {
			int convertCount=0;
			int errorCount=0;
			int warningCount=0;
			
			TreeSet<File> converterList = new TreeSet<File>(list.getFiles());
			for (File file : converterList) {
				log.info("Processing PDF-File: [" + file.getName() + "]");
				String errorReason = "PDF convert failed";
				try {
					PDFTextExtractor reader = new PDFTextExtractor(file);
					reader.processDocument();
					PerryRhodanBook book = reader.getBook();
					if(reader.hasWarnings()) {
						warningCount++;
					}
					
					if(!book.containsCover() && book.getBookNumber()!=0 && Options.isCoverAutoSearch() && Options.getCoverSearchPath() != null) {
						File img = new File(Options.getCoverSearchPath(),book.getBookNumber()+".jpg");
						if(img.exists()) {
							try {
								book.setCover(ImageIO.read(img));
							}catch(IOException e) {
								// ignore if the image can not loaded
								log.debug("Image "+img.getCanonicalPath()+" can not loaded!",e);
							}
						}
					}
					boolean writeEPub = true;
					if(!book.isMetaComplete() || Options.showBookMetaDataDialog() || (!Options.ignoreMissingCoverImage() && !book.containsCover())) 
					{
						DialogBox dialog = DialogFactory.getMetaDataDialog(book);
						dialog.setTitle(dialog.getTitle()+" "+file.getName());
						int code = dialog.showDialog();
						switch(code)
						{
							case DialogConstants.RETURN_CODE_CANCEL:
							case DialogConstants.RETURN_CODE_ERROR:	
								writeEPub = false;
								break;
							case DialogConstants.RETURN_CODE_OK_AND_EDIT_CHAPTERS:
								writeEPub = BookChapterDialog.editChapter(window, book, file.getName());
								break;
						}
					}
					if(writeEPub) {
						errorReason = "EPub building failed";
						// Generation of EPub-Files
						HTMLGenerator generator = new HTMLGenerator(book);
						generator.generateEPub();
	
						// Building EPub-Document
						EPubBuilder builder = new EPubBuilder(book);
						builder.generateEPub();
						
						// Checking validity of created epub-file
						// EpubChecker.validateEPubFile(ApplicationContants.EPUB_OUTPUT_DIRECTORY +
						// book.getBookFilename() + ".epub");
	
						log.info("PDF converted ["+file.getName()+"]");
						list.removeDocument(file);
						convertCount++;

						try {
							log.trace("Deleting directory [" + OUTPUT_DIRECTORY + "]");
							FileSystemUtils.waitForDeletion();
							FileSystemUtils.cleanOutputDirectory();
							log.debug("Deletion complete!");
						} catch (IOException e) {
							log.fatal("Could not delete directory [" + OUTPUT_DIRECTORY + "] stop converting, please check directory", e);
							break;
						}
					}
				}catch(Exception e) {
					log.error(errorReason+" PDF ["+file.getName()+"] => "+e.getMessage(),e);
					errorCount++;
				}
			}
			log.info("Convertion complete "+convertCount+" converted  "+warningCount+" warnings  "+errorCount+" failures");
			MainController.this.window.setWorkInProgress(false);
		}
	}
}
