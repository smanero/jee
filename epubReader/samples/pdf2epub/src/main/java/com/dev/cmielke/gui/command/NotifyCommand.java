package com.dev.cmielke.gui.command;

public enum NotifyCommand 
{
	UPDATE_DOCUMENT_LIST("2000", "Contents of the document list has changed, update the document list");
	
	private String code;
	private String description;
	
	private NotifyCommand(String code, String description)
	{
		this.code = code;
		this.description = description;
	}
	
	public String getCode()
	{
		return code;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public static NotifyCommand getCommand(String cmd)
	{
		if(cmd.equals(UPDATE_DOCUMENT_LIST.getCode()))return NotifyCommand.UPDATE_DOCUMENT_LIST;
		return null;
	}
}
