package com.dev.cmielke.gui.util;

import java.awt.Font;

public class DialogConstants
{
	// Resource-Bundle Parameter-Names
	public static final String RB_PARAM_NAME_OK_BUTTON_TITLE     = "DialogBox.okButton.title";
	public static final String RB_PARAM_NAME_CANCEL_BUTTON_TITLE = "DialogBox.cancelButton.title";
	
	public static final String RB_PARAM_NAME_INTRODUCTION_BUTTON_TITLE = "MetaDataDialogBox.introductionButton.title";
	public static final String RB_PARAM_NAME_CHARACTERS_BUTTON_TITLE   = "MetaDataDialogBox.charactersButton.title";
	public static final String RB_PARAM_NAME_CHAPTER_EDIT_BUTTON_TITLE = "MetaDataDialogBox.chapterEditButton.title";
	
	public static final String RB_PARAM_NAME_BOOK_TYPE_LABEL    = "MetaDataDialogBox.bookType.label";
	public static final String RB_PARAM_NAME_TITLE_LABEL        = "MetaDataDialogBox.title.label";
	public static final String RB_PARAM_NAME_SUBTITLE_LABEL     = "MetaDataDialogBox.subtitle.label";
	public static final String RB_PARAM_NAME_NUMBER_LABEL       = "MetaDataDialogBox.number.label";
	public static final String RB_PARAM_NAME_CYCLE_LABEL        = "MetaDataDialogBox.cycle.label";
	public static final String RB_PARAM_NAME_AUTHOR_LABEL       = "MetaDataDialogBox.author.label";
	public static final String RB_PARAM_NAME_EDITOR_PANEL_LABEL = "MetaDataDialogBox.editorPanel.label";
	public static final String RB_PARAM_NAME_BUTTON_PANEL_LABEL = "MetaDataDialogBox.buttonPanel.label";
	
	public static final String RB_PARAM_NAME_MISSING_COVER_TEXT   = "ImagePanel.missingCover.text";
	public static final String RB_PARAM_NAME_COVER_DIMENSION_TEXT = "ImagePanel.coverDimension.text";
	
	public static final String RB_PARAM_NAME_LOAD_BUTTON_TITLE   = "ImageEditorPanel.loadButton.title";
	public static final String RB_PARAM_NAME_REMOVE_BUTTON_TITLE = "ImageEditorPanel.removeButton.title";
	
	public static final String RB_PARAM_NAME_EDIT_INTRO_DIALOG_INTRODUCTION_LABEL = "EditIntroductionDialog.introduction.label";
	
	public static final String RB_PARAM_NAME_EDIT_CHARS_DIALOG_COLUMN_PERSON_LABEL      = "EditMainCharacters.columnName.person";
	public static final String RB_PARAM_NAME_EDIT_CHARS_DIALOG_COLUMN_DESCRIPTION_LABEL = "EditMainCharacters.columnName.description";
	public static final String RB_PARAM_NAME_EDIT_CHARS_DIALOG_CHARACTER_TABLE_LABEL    = "EditMainCharacters.characterTable.label";
	public static final String RB_PARAM_NAME_EDIT_CHARS_DIALOG_ADD_ITEM_TITLE           = "EditMainCharacters.addMenuItem.title";
	public static final String RB_PARAM_NAME_EDIT_CHARS_DIALOG_REMOVE_ITEM_TITLE        = "EditMainCharacters.removeMenuItem.title";
	public static final String RB_PARAM_NAME_EDIT_CHARS_DIALOG_CLEAR_ITEM_TITLE         = "EditMainCharacters.clearMenuItem.title";
	
	public static final String RB_PARAM_NAME_CHAPTER_DIALOG_OK_BUTTON_TITLE     = "BookDialog.okButton.title";
	public static final String RB_PARAM_NAME_CHAPTER_DIALOG_CANCEL_BUTTON_TITLE = "BookDialog.cancelButton.title";
	public static final String RB_PARAM_NAME_CHAPTER_DIALOG_TITLE               = "BookDialog.title";
	
	public static final String RB_TOOLTIP_ADD_CHARACTERS   = "MetaDataDialog.tooltip.charactersAdd.text";
	public static final String RB_TOOLTIP_ADD_INTRODUCTION = "MetaDataDialog.tooltip.introductionAdd.text";
	
	public static final String RB_PARAM_NAME_MENU_ITEM_COPY  = "CopyPastePopupMenu.copyItem.title";
	public static final String RB_PARAM_NAME_MENU_ITEM_PASTE = "CopyPastePopupMenu.pasteItem.title";
	public static final String RB_PARAM_NAME_MENU_ITEM_CUT   = "CopyPastePopupMenu.cutItem.title";
	public static final String RB_PARAM_NAME_MENU_ITEM_ADD_AS_CHARACTER = "CopyPastePopupMenu.addAsCharacterItem.title";
	
	public static final String RB_PARAM_NAME_OPTION_MENU_ADD_AS_CHARACTER_FAILURE_MESSAGE = "CopyPastePopupMenu.optionPane.addAsAcharacter.message";
	
	// Icons
	public static final String ICON_IMAGE_ADD    = "/images/picture_add.png";
	public static final String ICON_IMAGE_REMOVE = "/images/picture_delete.png";
	
	// Additional-Parameter-Names
	public static final String ADD_PARAM_NAME_PERRY_RHODAN_BOOK = "book";
	
	// Basic Font for all Text-Components
	public static final Font DEFAULT_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);
	
	// Basic Return-Codes
	public static final int RETURN_CODE_OK     = 0;
	public static final int RETURN_CODE_CANCEL = -1;
	public static final int RETURN_CODE_ERROR  = -15;
	
	// Return-Code
	public static final int RETURN_CODE_OK_AND_EDIT_CHAPTERS = 1;
	
	// Standard Action-Command-IDs 
	public static final int ACTION_CMD_CODE_BUTTON_OK     = 10000;
	public static final int ACTION_CMD_CODE_BUTTON_CANCEL = 10001;
	
	// Action-Command-IDs for BookNumberCallbackDialog
	public static final int ACTION_CMD_CODE_BUTTON_A  = 1001;
	public static final int ACTION_CMD_CODE_BUTTON_B  = 1002;
	public static final int ACTION_CMD_CODE_TEXTFIELD = 1003;
	
	// Action-Command-IDs for MetaDataCallbackDialog
	public static final int ACTION_CMD_CODE_TF_TITLE     = 1001;
	public static final int ACTION_CMD_CODE_TF_SUBTITLE  = 1002;
	public static final int ACTION_CMD_CODE_TF_NUMBER    = 1003;
	public static final int ACTION_CMD_CODE_TF_CYCLE     = 1004;
	public static final int ACTION_CMD_CODE_TF_AUTHOR    = 1005;
	public static final int ACTION_CMD_CODE_CB_BOOK_TYPE = 1006;
	public static final int ACTION_CMD_CODE_BT_INTRODUCTION = 1007;
	public static final int ACTION_CMD_CODE_BT_CHARACTERS   = 1008;
	public static final int ACTION_CMD_CODE_BT_CHAPTER_EDIT = 1009;
	
	// Action-Command-IDs for EditMainCharactersDialog
	public static final int ACTION_CMD_CODE_MI_ADD     = 1001;
	public static final int ACTION_CMD_CODE_MI_REMOVE  = 1002;
	public static final int ACTION_CMD_CODE_MI_CLEAR   = 1003;
	
	// Action-Command-IDs for ImageEditorPanel
	public static final int ACTION_CMD_CODE_BT_ADD     = 1001;
	public static final int ACTION_CMD_CODE_BT_REMOVE  = 1002;
	
	// Action-Command-IDs for CopyPastePopupMenu
	public static final int ACTION_CMD_CODE_MI_COPY  = 1001;
	public static final int ACTION_CMD_CODE_MI_PASTE = 1002;
	public static final int ACTION_CMD_CODE_MI_CUT   = 1003;
	public static final int ACTION_CMD_CODE_MI_ADD_AS_CHARACTER = 1004;
}
