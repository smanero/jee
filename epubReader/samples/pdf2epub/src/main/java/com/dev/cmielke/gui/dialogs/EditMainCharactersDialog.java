package com.dev.cmielke.gui.dialogs;

import static com.dev.cmielke.gui.util.DialogConstants.ACTION_CMD_CODE_BUTTON_CANCEL;
import static com.dev.cmielke.gui.util.DialogConstants.ACTION_CMD_CODE_BUTTON_OK;
import static com.dev.cmielke.gui.util.DialogConstants.ACTION_CMD_CODE_MI_ADD;
import static com.dev.cmielke.gui.util.DialogConstants.ACTION_CMD_CODE_MI_CLEAR;
import static com.dev.cmielke.gui.util.DialogConstants.ACTION_CMD_CODE_MI_REMOVE;
import static com.dev.cmielke.gui.util.DialogConstants.ADD_PARAM_NAME_PERRY_RHODAN_BOOK;
import static com.dev.cmielke.gui.util.DialogConstants.DEFAULT_FONT;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_EDIT_CHARS_DIALOG_ADD_ITEM_TITLE;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_EDIT_CHARS_DIALOG_CHARACTER_TABLE_LABEL;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_EDIT_CHARS_DIALOG_CLEAR_ITEM_TITLE;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_EDIT_CHARS_DIALOG_COLUMN_DESCRIPTION_LABEL;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_EDIT_CHARS_DIALOG_COLUMN_PERSON_LABEL;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_EDIT_CHARS_DIALOG_REMOVE_ITEM_TITLE;
import static com.dev.cmielke.gui.util.DialogConstants.RETURN_CODE_CANCEL;
import static com.dev.cmielke.gui.util.DialogConstants.RETURN_CODE_OK;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.dev.cmielke.beans.PerryRhodanBook;
import com.dev.cmielke.gui.dialogs.components.ComplexComponent;
import com.dev.cmielke.gui.dialogs.components.DialogBox;
import com.dev.cmielke.gui.dialogs.components.PopupMenuMouseListener;
import com.dev.cmielke.gui.util.LayoutHelper;
import com.dev.cmielke.gui.util.UIUtils;

public class EditMainCharactersDialog extends DialogBox
{
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(EditMainCharactersDialog.class);
	
	private static final String SEPARATOR = " - ";

	private static String[] columnNames = new String[]{rBundle.getString(RB_PARAM_NAME_EDIT_CHARS_DIALOG_COLUMN_PERSON_LABEL),
													   rBundle.getString(RB_PARAM_NAME_EDIT_CHARS_DIALOG_COLUMN_DESCRIPTION_LABEL)};
	
	private JMenuItem addItem, removeItem, clearItem;
	
	private JScrollPane scrollPane;
	
	private JTable characterTable;
	
	private DefaultTableModel data;
	
	private PerryRhodanBook book;
	
	public EditMainCharactersDialog(String title, String message, Map<String, Object> additionalParameters)
	{
		super(title, message, additionalParameters);
		this.book = (PerryRhodanBook) additionalParameters.get(ADD_PARAM_NAME_PERRY_RHODAN_BOOK);
	}
	
	@Override
	protected void setWindowConstraints()
	{
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(2*DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
		UIUtils.centerWindow(this);
	}
	
	@Override
	protected void initComponents(Map<String,Object> additionalParameters)
	{				
		PerryRhodanBook book = (PerryRhodanBook) additionalParameters.get(ADD_PARAM_NAME_PERRY_RHODAN_BOOK);
		String[][] characterData = new String[book.getCharacters().size()][2];
		
		int index = 0;
		for(String sData : book.getCharacters())
		{
			characterData[index] = sData.split(SEPARATOR);
			index++;
		}
		
		data = new DefaultTableModel(characterData, columnNames);
		characterTable = new JTable(data);
		characterTable.setName(rBundle.getString(RB_PARAM_NAME_EDIT_CHARS_DIALOG_CHARACTER_TABLE_LABEL));
		characterTable.setFont(DEFAULT_FONT);
		scrollPane = new JScrollPane(characterTable);
		characterTable.setFillsViewportHeight(true);
		ComplexComponent comp = new ComplexComponent(scrollPane, rBundle.getString(RB_PARAM_NAME_EDIT_CHARS_DIALOG_CHARACTER_TABLE_LABEL));
		getDialogComponents().add(comp);
		
		JPopupMenu contextMenu = new JPopupMenu();
		addItem = new JMenuItem(rBundle.getString(RB_PARAM_NAME_EDIT_CHARS_DIALOG_ADD_ITEM_TITLE));
		addItem.setActionCommand(Integer.toString(ACTION_CMD_CODE_MI_ADD));
		addItem.addActionListener(this);
		contextMenu.add(addItem);
		removeItem = new JMenuItem(rBundle.getString(RB_PARAM_NAME_EDIT_CHARS_DIALOG_REMOVE_ITEM_TITLE));
		removeItem.setActionCommand(Integer.toString(ACTION_CMD_CODE_MI_REMOVE));
		removeItem.addActionListener(this);
		contextMenu.add(removeItem);
		clearItem = new JMenuItem(rBundle.getString(RB_PARAM_NAME_EDIT_CHARS_DIALOG_CLEAR_ITEM_TITLE));
		clearItem.setActionCommand(Integer.toString(ACTION_CMD_CODE_MI_CLEAR));
		clearItem.addActionListener(this);
		contextMenu.add(clearItem);
		
		enableFields();
		
		characterTable.addMouseListener(new PopupMenuMouseListener(contextMenu));
	}
	
	private void enableFields()
	{
		if(characterTable.getRowCount() > 0)
		{
			removeItem.setEnabled(true);
			clearItem.setEnabled(true);
		}
		else
		{
			clearItem.setEnabled(false);
			removeItem.setEnabled(false);
		}
	}

	@Override
	protected JPanel buildContentPanel(JPanel contentPanel)
	{
		GridBagLayout compGrid = new GridBagLayout();
		
		contentPanel.setLayout(compGrid);
		
		int y = 0;
		for(JComponent comp : getDialogComponents())
		{
			if(comp instanceof ComplexComponent)
			{
				ComplexComponent lComp = (ComplexComponent)comp;
				lComp.getLabel().setVerticalAlignment(JLabel.TOP);
				LayoutHelper.addComponent(contentPanel, compGrid, lComp.getLabel()    , 0, y, 1, 1, 0.0, 0.0 );
				LayoutHelper.addComponent(contentPanel, compGrid, lComp.getComponent(), 1, y, 1, 1, 1.0, 1.0);
			}
			else if(comp instanceof JSeparator)
			{
				LayoutHelper.addComponent(contentPanel, compGrid, comp, 0, y, 2, 1, 0.0, 0.0);
			}
			else
			{
				LayoutHelper.addComponent(contentPanel, compGrid, comp, 1, y, 1, 1, 0.0, 0.0);
			}
			y++;
		}
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.gray);
		LayoutHelper.addComponent(contentPanel, compGrid, separator, 0, y, 2, 1, 0.0, 0.0);
		
		return contentPanel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		boolean closeDialog = false;
		Integer code = Integer.parseInt(e.getActionCommand());
		switch(code.intValue())
		{
			case ACTION_CMD_CODE_MI_ADD:
			{
				log.debug("> ACTION: Context-Menu Item ADD-Row was pressed.");
				data.addRow(new String[]{"",""});					
				setFocusOnCell(characterTable.getRowCount()-1, 0);
				break;
			}
			case ACTION_CMD_CODE_MI_REMOVE:
			{
				log.debug("> ACTION: Context-Menu Item Remove-Row was pressed.");
				deleteTableRow(characterTable.getSelectedRow());				
				break;
			}
			case ACTION_CMD_CODE_MI_CLEAR:
			{
				log.debug("> ACTION: Context-Menu Item CLEAR-List was pressed.");
				deleteAllTableRows();
				break;
			}
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
				setMainCharacters();
				setReturnValue(RETURN_CODE_OK);
				close();
			}
		}
	}
	
	private void deleteTableRow(int index)
	{
		if(index >= 0)
		{
			data.removeRow(index);
		}
	}
	
	private void deleteAllTableRows()
	{
		int rows = characterTable.getRowCount();
		for(int index = rows-1; index >= 0; index--)
		{
			deleteTableRow(index);
		}
	}
	
	private void setFocusOnCell(int row, int col)
	{
		characterTable.setColumnSelectionAllowed(false);
		characterTable.setRowSelectionAllowed(true);
	    boolean success = characterTable.editCellAt(row, col);
	    if (success) {
	      boolean toggle = false;
	      boolean extend = false;
	      characterTable.changeSelection(row, col, toggle, extend);
	    }
	}
	
	private void setMainCharacters()
	{
		ArrayList<String> mainCharacters = new ArrayList<String>();
		for(int index = 0; index < data.getRowCount(); index++)
		{
			String characters  = (String) data.getValueAt(index, 0);
			String description = (String) data.getValueAt(index, 1);
			if(!characters.isEmpty() && !description.isEmpty())
			{
				mainCharacters.add(characters + SEPARATOR + description);
			}
		}
		log.debug("COUNT: "+ mainCharacters.size());
		book.getCharacters().clear();
		book.getCharacters().addAll(mainCharacters);
	}
}
