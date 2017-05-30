package com.dev.cmielke.gui.beans;

import java.io.File;
import java.util.Observable;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.dev.cmielke.gui.command.NotifyCommand;

public class DocumentList extends Observable
{
	private static Logger log = Logger.getLogger(DocumentList.class);
	private TreeSet<File> documents;
	
	public DocumentList()
	{
		documents = new TreeSet<File>();
	}
	
	public int getSize()
	{
		return this.documents.size();
	}
	
	public void addDocument(File file)
	{
		log.debug("> addDocument()");
		log.debug("Adding to list: ["+ file.getPath() +"]");
		documents.add(file);
		setChanged();
		notifyObservers(NotifyCommand.UPDATE_DOCUMENT_LIST);
		log.debug("< addDocument()");
	}
	
	public void addDocuments(File[] files)
	{
		log.debug("> addDocuments()");
		for (File file : files) 
		{
			log.debug("Adding to list: ["+ file.getName() +", "+ file.getAbsolutePath() +"]");
			documents.add(file);
		}
		setChanged();
		notifyObservers(NotifyCommand.UPDATE_DOCUMENT_LIST);
		log.debug("< addDocuments()");
	}
	
	public void removeDocument(File doc)
	{
		log.debug("> removeDocument()");
		log.debug("Removing document: ["+ doc +"]");
		log.debug("Result: [" + documents.remove(doc) + "]");
		setChanged();
		notifyObservers(NotifyCommand.UPDATE_DOCUMENT_LIST);
		log.debug("< removeDocument()");
	}
	
	public void removeAllDocuments()
	{
		log.debug("> removeAllDocuments()");
		documents.clear();
		setChanged();
		notifyObservers(NotifyCommand.UPDATE_DOCUMENT_LIST);
		log.debug("< removeAllDocuments()");
	}
	
	public Set<String> getDocumentNames()
	{
		TreeSet<String> names = new TreeSet<String>();
		for(File file : documents)
		{
			names.add(file.getName());
		}
		return names;
	}
	
	public Set<File> getFiles()
	{
		return documents;
	}
}
