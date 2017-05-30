package com.dev.cmielke.gui.dialogs;

import static com.dev.cmielke.gui.util.DialogConstants.ACTION_CMD_CODE_BT_CHAPTER_EDIT;
import static com.dev.cmielke.gui.util.DialogConstants.ACTION_CMD_CODE_BT_CHARACTERS;
import static com.dev.cmielke.gui.util.DialogConstants.ACTION_CMD_CODE_BT_INTRODUCTION;
import static com.dev.cmielke.gui.util.DialogConstants.ACTION_CMD_CODE_BUTTON_CANCEL;
import static com.dev.cmielke.gui.util.DialogConstants.ACTION_CMD_CODE_BUTTON_OK;
import static com.dev.cmielke.gui.util.DialogConstants.ACTION_CMD_CODE_CB_BOOK_TYPE;
import static com.dev.cmielke.gui.util.DialogConstants.ACTION_CMD_CODE_TF_AUTHOR;
import static com.dev.cmielke.gui.util.DialogConstants.ACTION_CMD_CODE_TF_CYCLE;
import static com.dev.cmielke.gui.util.DialogConstants.ACTION_CMD_CODE_TF_NUMBER;
import static com.dev.cmielke.gui.util.DialogConstants.ACTION_CMD_CODE_TF_SUBTITLE;
import static com.dev.cmielke.gui.util.DialogConstants.ACTION_CMD_CODE_TF_TITLE;
import static com.dev.cmielke.gui.util.DialogConstants.ADD_PARAM_NAME_PERRY_RHODAN_BOOK;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_AUTHOR_LABEL;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_BOOK_TYPE_LABEL;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_BUTTON_PANEL_LABEL;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_CHAPTER_EDIT_BUTTON_TITLE;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_CHARACTERS_BUTTON_TITLE;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_CYCLE_LABEL;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_EDITOR_PANEL_LABEL;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_INTRODUCTION_BUTTON_TITLE;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_NUMBER_LABEL;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_SUBTITLE_LABEL;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_TITLE_LABEL;
import static com.dev.cmielke.gui.util.DialogConstants.RB_TOOLTIP_ADD_CHARACTERS;
import static com.dev.cmielke.gui.util.DialogConstants.RB_TOOLTIP_ADD_INTRODUCTION;
import static com.dev.cmielke.gui.util.DialogConstants.RETURN_CODE_CANCEL;
import static com.dev.cmielke.gui.util.DialogConstants.RETURN_CODE_OK;
import static com.dev.cmielke.gui.util.DialogConstants.RETURN_CODE_OK_AND_EDIT_CHAPTERS;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.dev.cmielke.beans.BookType;
import com.dev.cmielke.beans.PerryRhodanBook;
import com.dev.cmielke.gui.dialogs.components.ComplexComponent;
import com.dev.cmielke.gui.dialogs.components.DialogBox;
import com.dev.cmielke.gui.dialogs.components.ImageEditorPanel;
import com.dev.cmielke.gui.util.UIUtils;
import com.dev.cmielke.gui.util.ValidationHelper;

@SuppressWarnings("serial")
public class MetaDataDialog extends DialogBox
{
	private static final Logger log = Logger.getLogger(MetaDataDialog.class);
	
	private JComboBox bookType;
	
	private JTextField title, subtitle, number, cycle, author;
	
	private JButton introduction, characters, chapterEdit, charactersAdd, introductionAdd;
	
	private ImageEditorPanel editorPanel;
	
	private JPanel buttonPanel;
	
	private PerryRhodanBook book;
	
	public MetaDataDialog(String title, String message, Map<String,Object> additionalParameters)
	{
		super(title, message, additionalParameters);
		this.book = (PerryRhodanBook) additionalParameters.get(ADD_PARAM_NAME_PERRY_RHODAN_BOOK);
		
		UIUtils.centerWindowHorizontal(this);
	}
	
	@Override
	protected void setWindowConstraints()
	{
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(2*DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
		UIUtils.centerWindow(this);
		pack();
	}
	
	@Override
	protected void initComponents(Map<String,Object> additionalParameters)
	{
		this.book = (PerryRhodanBook) additionalParameters.get(ADD_PARAM_NAME_PERRY_RHODAN_BOOK); 
		
		bookType = new JComboBox(new DefaultComboBoxModel(new Object[] { BookType.OTHER, BookType.HEFTROMAN, BookType.SILBERBAND }));
		bookType.setName(rBundle.getString(RB_PARAM_NAME_BOOK_TYPE_LABEL));
		bookType.setSelectedItem(book.getType());
		bookType.setActionCommand(Integer.toString(ACTION_CMD_CODE_CB_BOOK_TYPE));
		ComplexComponent comp = new ComplexComponent(bookType, rBundle.getString(RB_PARAM_NAME_BOOK_TYPE_LABEL));
		getDialogComponents().add(comp);
		
		title = new JTextField();
		title.setName(rBundle.getString(RB_PARAM_NAME_TITLE_LABEL));
		title.setText(book.getTitle());
		title.setActionCommand(Integer.toString(ACTION_CMD_CODE_TF_TITLE));
		comp = new ComplexComponent(title, rBundle.getString(RB_PARAM_NAME_TITLE_LABEL));
		getDialogComponents().add(comp);
		
		subtitle = new JTextField();
		subtitle.setName(rBundle.getString(RB_PARAM_NAME_SUBTITLE_LABEL));
		subtitle.setText(book.getSubtitle());
		subtitle.setActionCommand(Integer.toString(ACTION_CMD_CODE_TF_SUBTITLE));
		comp = new ComplexComponent(subtitle, rBundle.getString(RB_PARAM_NAME_SUBTITLE_LABEL));
		getDialogComponents().add(comp);
		
		number = new JTextField();
		number.setName(rBundle.getString(RB_PARAM_NAME_NUMBER_LABEL));
		number.setText(book.getBookNumber()>0 ? Integer.toString(book.getBookNumber()) : "");
		number.setActionCommand(Integer.toString(ACTION_CMD_CODE_TF_NUMBER));
		comp = new ComplexComponent(number, rBundle.getString(RB_PARAM_NAME_NUMBER_LABEL));
		getDialogComponents().add(comp);
		
		cycle = new JTextField();
		cycle.setName(rBundle.getString(RB_PARAM_NAME_CYCLE_LABEL));
		cycle.setText(book.getCycle());
		cycle.setActionCommand(Integer.toString(ACTION_CMD_CODE_TF_CYCLE));
		comp = new ComplexComponent(cycle, rBundle.getString(RB_PARAM_NAME_CYCLE_LABEL));
		getDialogComponents().add(comp);
		
		author = new JTextField();
		author.setName(rBundle.getString(RB_PARAM_NAME_AUTHOR_LABEL));
		author.setText(book.getAuthors());
		author.setActionCommand(Integer.toString(ACTION_CMD_CODE_TF_AUTHOR));
		comp = new ComplexComponent(author, rBundle.getString(RB_PARAM_NAME_AUTHOR_LABEL));
		getDialogComponents().add(comp);
		
		editorPanel = new ImageEditorPanel(this);
		editorPanel.setImage(book.getCover());
		comp = new ComplexComponent(editorPanel, rBundle.getString(RB_PARAM_NAME_EDITOR_PANEL_LABEL));
		getDialogComponents().add(comp);	
		
		JSeparator separator1 = new JSeparator();
		getDialogComponents().add(separator1);
		
		buttonPanel = new JPanel();
		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);
		buttonPanel.setLayout(layout);
		introduction = new JButton(rBundle.getString(RB_PARAM_NAME_INTRODUCTION_BUTTON_TITLE));
		introduction.setActionCommand(Integer.toString(ACTION_CMD_CODE_BT_INTRODUCTION));
		introduction.addActionListener(this);
		buttonPanel.add(introduction);
		
		introductionAdd = new JButton("+");
		introductionAdd.setActionCommand(Integer.toString(ACTION_CMD_CODE_BT_INTRODUCTION));
		introductionAdd.addActionListener(this);
		introductionAdd.setToolTipText(rBundle.getString(RB_TOOLTIP_ADD_INTRODUCTION));
		if(book.getType() == BookType.HEFTROMAN)
		{
			buttonPanel.add(introductionAdd);
		}
		
		if(book.getType() == BookType.HEFTROMAN)
		{
			JSeparator verticalSeparator = new JSeparator(JSeparator.VERTICAL);
			buttonPanel.add(verticalSeparator);
		}
		
		characters = new JButton(rBundle.getString(RB_PARAM_NAME_CHARACTERS_BUTTON_TITLE));
		characters.setActionCommand(Integer.toString(ACTION_CMD_CODE_BT_CHARACTERS));
		characters.addActionListener(this);
		buttonPanel.add(characters);
		comp = new ComplexComponent(buttonPanel, rBundle.getString(RB_PARAM_NAME_BUTTON_PANEL_LABEL));
		getDialogComponents().add(comp);
		
		charactersAdd = new JButton("+");
		charactersAdd.setActionCommand(Integer.toString(ACTION_CMD_CODE_BT_CHARACTERS));
		charactersAdd.addActionListener(this);
		charactersAdd.setToolTipText(rBundle.getString(RB_TOOLTIP_ADD_CHARACTERS));
		if(book.getType() == BookType.HEFTROMAN)
		{
			buttonPanel.add(charactersAdd);
		}
		
		enableFields();
	}
	
	private void enableFields()
	{
		introduction.setEnabled(book.containsIntroduction());
		introductionAdd.setEnabled(!book.containsIntroduction());
		characters.setEnabled(book.containsCharacters());
		charactersAdd.setEnabled(!book.containsCharacters());
	}
	
	protected void registerCompontents()
	{
		//Registering Standard-Components and all Components within DialogComponents
		super.registerCompontents();
		
		//Adding Item-Listener
		this.bookType.addItemListener(new ItemListener() {
			/**
			 * Titel bei Silberband nicht verfügbar.
			 * Bei Silberband Titel setzen
			 */
			@Override
			public void itemStateChanged(ItemEvent e) {
				MetaDataDialog.this.setTitel();
			}
		});
		
		//Adding Focus-Listener
		this.number.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				JTextField f = (JTextField)e.getComponent();
				String x = f.getText();
				try {
					int i = Integer.parseInt(x);
					f.setText(String.valueOf(i));
					if(MetaDataDialog.this.bookType.getSelectedItem()==BookType.SILBERBAND) {
						MetaDataDialog.this.setTitel();
					}
				}catch(NumberFormatException ex) {
					f.setText("");	// TODO besseren check
				}
			}
		});
	}
	
	@Override
	protected JPanel buildButtonPanel(JPanel buttonPanel)
	{
		super.buildButtonPanel(buttonPanel);
		
		chapterEdit = new JButton(rBundle.getString(RB_PARAM_NAME_CHAPTER_EDIT_BUTTON_TITLE));
		chapterEdit.setActionCommand(Integer.toString(ACTION_CMD_CODE_BT_CHAPTER_EDIT));
		chapterEdit.addActionListener(this);
		
		buttonPanel.add(chapterEdit, 0);
		return buttonPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		boolean closeDialog = false;
		Integer code = Integer.parseInt(e.getActionCommand());
		switch(code.intValue())
		{
			case ACTION_CMD_CODE_BT_CHAPTER_EDIT:
			{
				log.debug("> ACTION: Button Kapitel-Bearbeitung was activated.");
				if(!hasActionErrors())
				{
					setMetaData();
					setReturnValue(RETURN_CODE_OK_AND_EDIT_CHAPTERS);
					close();
				}
				break;
			}
			case ACTION_CMD_CODE_BT_INTRODUCTION:
			{
				log.debug("> ACTION: Button Introduction was activated.");
				EditIntroductionDialog dialog = new EditIntroductionDialog("Einleitung bearbeiten", null, additionalParameters);
				dialog.showDialog();	
				break;
			}
			case ACTION_CMD_CODE_BT_CHARACTERS:
			{
				log.debug("> ACTION: Button Hauptpersonen was activated.");				
				EditMainCharactersDialog dialog = new EditMainCharactersDialog("Hauptpersonen bearbeiten", null, additionalParameters);
				dialog.showDialog();
				break;
			}
			case ACTION_CMD_CODE_CB_BOOK_TYPE:
			{
				log.debug("> ACTION: ComboBox BookType was activated.");
				bookType.transferFocus();
				break;
			}
			case ACTION_CMD_CODE_TF_TITLE:
			{
				log.debug("> ACTION: TF Title was activated.");
				ValidationHelper.validateString(this, title);
				break;
			}
			case ACTION_CMD_CODE_TF_SUBTITLE:
			{
				log.debug("> ACTION: TF subtitle was activated.");
				ValidationHelper.validateString(this, subtitle);
				break;
			}
			case ACTION_CMD_CODE_TF_NUMBER:
			{
				log.debug("> ACTION: TF Number was activated.");
				ValidationHelper.validateNumber(this, number);
				break;
			}
			case ACTION_CMD_CODE_TF_CYCLE:
			{
				log.debug("> ACTION: TF cycle was activated.");
				ValidationHelper.validateString(this, cycle);
				break;
			}
			case ACTION_CMD_CODE_TF_AUTHOR:
			{
				log.debug("> ACTION: TF author was activated.");
				ValidationHelper.validateString(this, author);
				break;
			}
			case ACTION_CMD_CODE_BUTTON_OK:
			{
				log.debug("> ACTION: Button OK was pressed.");
				ValidationHelper.validateString(this, title);
				ValidationHelper.validateString(this, subtitle);
				ValidationHelper.validateNumber(this, number);
				ValidationHelper.validateString(this, cycle);
				ValidationHelper.validateString(this, author);
				if(this.bookType.getSelectedItem() == BookType.SILBERBAND) {
					ValidationHelper.validateMandatory(this, subtitle);
				}else{
					ValidationHelper.validateMandatory(this, title);
				}
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
				setMetaData();
				setReturnValue(RETURN_CODE_OK);
				close();
			}
		}
	}
	
	private void setMetaData()
	{
		this.book.setType((BookType)bookType.getSelectedItem());
		this.book.setTitle(title.getText());
		this.book.setSubtitle(subtitle.getText());
		this.book.setBookNumber(Integer.valueOf(number.getText()));
		this.book.setCycle(cycle.getText());
		this.book.setAuthors(author.getText());
		if(editorPanel.hasNewImage())
		{
			this.book.setCover(editorPanel.getBufferedImage());
		}
	}
	
	private void setTitel() {
		if(this.bookType.getSelectedItem()!=null) {
			switch((BookType)this.bookType.getSelectedItem()) {
			case SILBERBAND:
				this.title.setText("Perry Rhodan Silberband"+(this.number.getText().length()>0?" "+this.number.getText():""));
				this.title.setEnabled(false);
				break;
			default:
				this.title.setText(book.getTitle());
				this.title.setEnabled(true);
				break;
			}
		}
	}

}
