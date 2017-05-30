package com.dev.cmielke.gui.dialogs.components;

import static com.dev.cmielke.gui.util.DialogConstants.ACTION_CMD_CODE_MI_COPY;
import static com.dev.cmielke.gui.util.DialogConstants.ACTION_CMD_CODE_MI_CUT;
import static com.dev.cmielke.gui.util.DialogConstants.ACTION_CMD_CODE_MI_PASTE;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_MENU_ITEM_COPY;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_MENU_ITEM_CUT;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_MENU_ITEM_PASTE;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

@SuppressWarnings("serial")
public class CopyPastePopupMenu extends JPopupMenu
{
	protected static ResourceBundle rBundle = ResourceBundle.getBundle("pdf2epub");
	
	private JMenuItem copyItem, pasteItem, cutItem;
	
	public CopyPastePopupMenu(ActionListener listener)
	{
		initMenu(listener);
	}
	
	public void addMenuItemAt(int index, JMenuItem item)
	{
		add(item, index);
	}
	
	public void addMenuItem(JMenuItem item)
	{
		add(item);
	}
	
	public void removeCopyOption()
	{
		remove(copyItem);
	}
	
	public void removePasteOption()
	{
		remove(pasteItem);
	}
	
	public void removeCutOption()
	{
		remove(cutItem);
	}
	
	public void enableCopyOption(boolean b)
	{
		copyItem.setEnabled(b);
	}
	
	public void enablePasteOption(boolean b)
	{
		pasteItem.setEnabled(b);
	}
	
	public void enableCutOption(boolean b)
	{
		cutItem.setEnabled(b);
	}
	
	public void addSeparator()
	{
		addSeparatorAt(-1);
	}
	
	public void appendMenu(JMenu menu)
	{
		Component[] components = menu.getComponents();
		if(components.length > 0)
		{	
			addSeparator();
			for(Component comp : components)
			{
				add(comp);
			}
		}
	}
	
	public void addSeparatorAt(int index)
	{
		JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
		if(index > 0)
		{
			add(separator, index);
		}
		else
		{
			add(separator);
		}
	}
	
	private void initMenu(ActionListener listener)
	{
		copyItem = new JMenuItem(rBundle.getString(RB_PARAM_NAME_MENU_ITEM_COPY));
		copyItem.setActionCommand(Integer.toString(ACTION_CMD_CODE_MI_COPY));
		copyItem.addActionListener(listener);
		add(copyItem);
		
		pasteItem = new JMenuItem(rBundle.getString(RB_PARAM_NAME_MENU_ITEM_PASTE));
		pasteItem.setActionCommand(Integer.toString(ACTION_CMD_CODE_MI_PASTE));
		pasteItem.addActionListener(listener);
		add(pasteItem);
		
		cutItem = new JMenuItem(rBundle.getString(RB_PARAM_NAME_MENU_ITEM_CUT));
		cutItem.setActionCommand(Integer.toString(ACTION_CMD_CODE_MI_CUT));
		cutItem.addActionListener(listener);
		add(cutItem);
	}
	
}
