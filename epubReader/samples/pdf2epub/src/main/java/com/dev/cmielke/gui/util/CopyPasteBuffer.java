package com.dev.cmielke.gui.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import org.apache.log4j.Logger;

public class CopyPasteBuffer implements ClipboardOwner
{
	private static Logger log = Logger.getLogger(CopyPasteBuffer.class);
	
	private static CopyPasteBuffer instance;
	
	private Clipboard systemClipboard;
	
	private CopyPasteBuffer()
	{
		this.systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	}
	
	public static CopyPasteBuffer getInstance()
	{
		if(instance == null)
		{
			instance = new CopyPasteBuffer();
		}
		return instance;
	}
	
	public String get()
	{
		String value = "";
		if(!isEmpty())
		{
			Transferable object = systemClipboard.getContents(this);
			try
			{
				value = (String) object.getTransferData(DataFlavor.stringFlavor);
			}
			catch (UnsupportedFlavorException e)
			{
				log.debug("UnsupportedException occured!");
			}
			catch (IOException e)
			{
				log.debug("Inhalt der Zwischenablage ist kein STRING-Object, returning Leer-String!");
			}
		}
		return value;	
	}
	
	public void set(String text)
	{
		StringSelection container = new StringSelection(text);
		systemClipboard.setContents(container, this);
	}
	
	public boolean isEmpty()
	{
		boolean isEmpty = true;
		if(systemClipboard.getContents(this) != null)
		{
			isEmpty = false;
		}
		return isEmpty;
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents)
	{
		log.debug("CopyPasteBuffer -> Inahlt der Zwischenablage hat sich geändert!");
	}
}
