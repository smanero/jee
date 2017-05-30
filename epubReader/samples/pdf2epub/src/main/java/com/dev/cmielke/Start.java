package com.dev.cmielke;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import org.apache.log4j.Logger;

import com.dev.cmielke.beans.PerryRhodanBook;
import com.dev.cmielke.build.EPubBuilder;
import com.dev.cmielke.generator.HTMLGenerator;
import com.dev.cmielke.generator.PDFTextExtractor;
import com.dev.cmielke.gui.controller.MainController;
import com.dev.cmielke.gui.view.MainWindow;
import com.dev.cmielke.util.FileSystemUtils;
import com.dev.cmielke.util.LoggingUtils;
import com.dev.cmielke.util.filter.PortableDocumentFilter;

public class Start
{
    private static Logger log = Logger.getLogger(Start.class);
	
	public static enum ApplicationMode {TEXT, GUI};
	
	public static void main(String[] args) throws IOException
	{
		LoggingUtils.configureLog4J();
		ApplicationMode mode = ApplicationMode.valueOf(args.length==0?"GUI":args[0]);
		switch(mode)
		{
			case TEXT:
			{
				JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(false);
				chooser.setCurrentDirectory(new File("C:\\Users\\Rafthrasa\\Documents\\eBooks - Romane\\Perry Rhodan - Romanzyklen\\Perry Rhodan - Romanzyklus - 0400-0499 - Die Cappins"));
		        chooser.setFileFilter(new PortableDocumentFilter());
		        
		        int option = chooser.showOpenDialog(null);
		        
		        switch(option)
		        {
		        	case JFileChooser.APPROVE_OPTION:
		        	{
		        		File file = chooser.getSelectedFile();
		        		
		        		log.debug("Processing PDF-File: [" + file.getName() + "]");
		        		PDFTextExtractor reader = new PDFTextExtractor(file);
		        		reader.processDocument();
		        		PerryRhodanBook book = reader.getBook();
		        		
		  					if(book.isMetaComplete() || book.getBookNumber()>0 || book.getTitle().length()>0) {
			  					// Generation of EPub-Files
			  					HTMLGenerator generator = new HTMLGenerator(book);
			  					generator.generateEPub();
	
			  					// Building EPub-Document
			  					EPubBuilder builder = new EPubBuilder(book);
			  					builder.generateEPub();

			  					// Checking validity of created epub-file
			  					// EpubChecker.validateEPubFile(ApplicationContants.EPUB_OUTPUT_DIRECTORY +
			  					// book.getBookFilename() + ".epub");
		  					}else{
		  						System.err.println("Book metadata are not complete, please use GUI mode!");
		  					}

		        		FileSystemUtils.waitForDeletion();
		        		FileSystemUtils.cleanOutputDirectory();
		        		
		        		break;
		        	}
		        	case JFileChooser.CANCEL_OPTION:
		        	{
		        		System.exit(0);
		        	}
		        }
		        break;
			}
			case GUI:
			{
				MainWindow window = new MainWindow();
				new MainController(window);
				break;
			}
		}		
	}
}
