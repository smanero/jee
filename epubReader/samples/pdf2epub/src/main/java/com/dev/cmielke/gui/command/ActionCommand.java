package com.dev.cmielke.gui.command;

public enum ActionCommand 
{
	ADD_DOCUMENT("1000", "Add Document to list"),
	CLEAR_LIST("1001", "Remove all documents from list"),
	START_PROCESSING("1002", "Start unlocking of documents");
	
	private String code;
	private String description;
	
	private ActionCommand(String code, String description)
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
	
	public static ActionCommand getCommand(String cmd)
	{
		if(cmd.equals(ADD_DOCUMENT.getCode()))return ActionCommand.ADD_DOCUMENT;
		if(cmd.equals(CLEAR_LIST.getCode()))return ActionCommand.CLEAR_LIST;
		if(cmd.equals(START_PROCESSING.getCode()))return ActionCommand.START_PROCESSING;
		return null;
	}
}
