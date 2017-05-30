package com.dev.cmielke.gui.dialogs;

import static com.dev.cmielke.gui.util.DialogConstants.ACTION_CMD_CODE_BUTTON_CANCEL;
import static com.dev.cmielke.gui.util.DialogConstants.ACTION_CMD_CODE_BUTTON_OK;
import static com.dev.cmielke.gui.util.DialogConstants.ACTION_CMD_CODE_MI_ADD_AS_CHARACTER;
import static com.dev.cmielke.gui.util.DialogConstants.ACTION_CMD_CODE_MI_COPY;
import static com.dev.cmielke.gui.util.DialogConstants.ACTION_CMD_CODE_MI_CUT;
import static com.dev.cmielke.gui.util.DialogConstants.ACTION_CMD_CODE_MI_PASTE;
import static com.dev.cmielke.gui.util.DialogConstants.ADD_PARAM_NAME_PERRY_RHODAN_BOOK;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_EDIT_INTRO_DIALOG_INTRODUCTION_LABEL;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_MENU_ITEM_ADD_AS_CHARACTER;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_OPTION_MENU_ADD_AS_CHARACTER_FAILURE_MESSAGE;
import static com.dev.cmielke.gui.util.DialogConstants.RETURN_CODE_CANCEL;
import static com.dev.cmielke.gui.util.DialogConstants.RETURN_CODE_OK;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import com.dev.cmielke.beans.PerryRhodanBook;
import com.dev.cmielke.gui.dialogs.components.ComplexComponent;
import com.dev.cmielke.gui.dialogs.components.CopyPastePopupMenu;
import com.dev.cmielke.gui.dialogs.components.DialogBox;
import com.dev.cmielke.gui.dialogs.components.PopupMenuMouseListener;
import com.dev.cmielke.gui.util.CopyPasteBuffer;
import com.dev.cmielke.gui.util.UIUtils;
import com.inet.jortho.SpellChecker;

public class EditIntroductionDialog extends DialogBox
{
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(EditIntroductionDialog.class);

	private JTextArea introduction;
	
	private PerryRhodanBook book;
	
	private CopyPastePopupMenu menu;
	
	private CopyPasteBuffer buffer = CopyPasteBuffer.getInstance();
	
	public EditIntroductionDialog(String title, String message, Map<String,Object> additionalParameters)
	{
		super(title, message, additionalParameters);
		this.book = (PerryRhodanBook) additionalParameters.get(ADD_PARAM_NAME_PERRY_RHODAN_BOOK);
		
		UIUtils.centerWindowHorizontal(this);
		pack();
	}
	
	@Override
	protected void initComponents(Map<String,Object> additionalParameters)
	{
		PerryRhodanBook book = (PerryRhodanBook) additionalParameters.get(ADD_PARAM_NAME_PERRY_RHODAN_BOOK);
		
		introduction = new JTextArea();
		introduction.setLineWrap(true);
		introduction.setWrapStyleWord(true);
		introduction.setColumns(64);
		introduction.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		introduction.setName(rBundle.getString(RB_PARAM_NAME_EDIT_INTRO_DIALOG_INTRODUCTION_LABEL));
		introduction.setText(book.getIntroduction());
		introduction.setBorder(BorderFactory.createEtchedBorder());
		ComplexComponent comp = new ComplexComponent(introduction, rBundle.getString(RB_PARAM_NAME_EDIT_INTRO_DIALOG_INTRODUCTION_LABEL));
		getDialogComponents().add(comp);
	}
	
	@Override
	protected JPanel buildContentPanel(JPanel contentPanel)
	{
		contentPanel = super.buildContentPanel(contentPanel);
		
		//Auto-Popup-Menu disablen
		SpellChecker.enablePopup(introduction, false);
		
		//Creating own menu
		menu = new CopyPastePopupMenu(this);
		
		//Adding SpellChecker-Menu at first
		menu.add(SpellChecker.createCheckerMenu(), 0);
		menu.add(SpellChecker.createLanguagesMenu(), 1);
		menu.addSeparatorAt(2);
		menu.enablePasteOption(false);
		
		//Adding special add-Character-Menu
		menu.addSeparator();
		JMenuItem addAsCharacterItem = new JMenuItem(rBundle.getString(RB_PARAM_NAME_MENU_ITEM_ADD_AS_CHARACTER));
		addAsCharacterItem.setActionCommand(Integer.toString(ACTION_CMD_CODE_MI_ADD_AS_CHARACTER));
		addAsCharacterItem.addActionListener(this);
		menu.add(addAsCharacterItem);
		
		//registering PopupMenu
		introduction.addMouseListener(new PopupMenuMouseListener(menu));
		
		return contentPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		boolean closeDialog = false;
		Integer code = Integer.parseInt(e.getActionCommand());
		switch(code.intValue())
		{
			//Menu-Items
			case ACTION_CMD_CODE_MI_COPY:
			{
				log.debug("> ACTION: MenuItem COPY was pressed.");
				String text = introduction.getSelectedText();
				log.debug("Text-To-Copy: ["+ text +"]");
				buffer.set(text);
				break;
			}
			case ACTION_CMD_CODE_MI_PASTE:
			{
				log.debug("> ACTION: MenuItem PASTE was pressed.");
				
				if(!buffer.isEmpty())
				{
					int pos = introduction.getCaretPosition();
					String bufferText = buffer.get();
					int bufferLength = bufferText.length();
					
					String text  = introduction.getText();
					String start = text.substring(0, pos);
					String end   = text.substring(pos, text.length());					
					text = start + bufferText + end;
					
					introduction.setText(text);
					introduction.setCaretPosition(pos + bufferLength);
				}
				break;
			}
			case ACTION_CMD_CODE_MI_CUT:
			{
				log.debug("> ACTION: MenuItem CUT was pressed.");
				buffer.set(introduction.getSelectedText());
				
				introduction.setText(cutSelection());
				
				break;
			}
			case ACTION_CMD_CODE_MI_ADD_AS_CHARACTER:
			{
				log.debug("> ACTION: MenuItem 'AddAsCharacter' was pressed.");
				String text = introduction.getSelectedText().trim();
				if (text.contains(" - ") && text.split(" - ").length < 4
					&& (!text.contains(",") || (text.indexOf(" - ") < text.indexOf(","))
					|| (text.indexOf(" - ") < (text.length()/2) ) ) ) 
				{
					log.debug("Füge Hauptperson hinzu: ["+ text +"]");
					book.getCharacters().add(text);
					introduction.setText(cutSelection());
				}
				else
				{
					JOptionPane.showMessageDialog(this, rBundle.getString(RB_PARAM_NAME_OPTION_MENU_ADD_AS_CHARACTER_FAILURE_MESSAGE));
				}
				break;
			}
			//Action-Buttons
			case ACTION_CMD_CODE_BUTTON_OK:
			{
				log.debug("> ACTION: Button OK was pressed.");
				closeDialog = true;
				break;
			}
			case ACTION_CMD_CODE_BUTTON_CANCEL:
			{
				log.debug("> ACTION: Button CANCEL was pressed.");
				setReturnValue(RETURN_CODE_CANCEL);
				close();
				break;
			}
			default:
			{
				log.debug("> ACTION: CODE WAS UNRECOGNIZED !!!");
				break;
			}
		}
		
		enableFields();
		
		if(hasActionErrors())
		{
			setFocusOnFirstErrorComponent();
		}
		else
		{
			if(closeDialog)
			{
				setBookIntroduction();
				setReturnValue(RETURN_CODE_OK);
				close();
			}
		}
	}
	
	private String cutSelection()
	{
		int startPos = introduction.getSelectionStart();
		int endPos   = introduction.getSelectionEnd();							
		return cutRegion(introduction.getText(), startPos, endPos);
	}
	
	private String cutRegion(String text, int startPos, int endPos)
	{				
		String start = text.substring(0, startPos);
		String end   = text.substring(endPos, text.length());
		return (start+end);
	}
	
	private void enableFields()
	{
		if(buffer.isEmpty())
		{
			menu.enablePasteOption(false);
		}
		else
		{
			menu.enablePasteOption(true);
		}
	}
	
	private void setBookIntroduction()
	{
		book.setIntroduction(introduction.getText());
	}
}
