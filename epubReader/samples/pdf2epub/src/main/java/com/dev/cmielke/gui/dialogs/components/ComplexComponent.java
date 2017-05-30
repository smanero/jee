package com.dev.cmielke.gui.dialogs.components;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;

public class ComplexComponent extends JComponent
{
	private static final long serialVersionUID = -7239307021829347705L;
	
	private JComponent component;
	private JLabel label;
	private Border border;
	
	public ComplexComponent(JComponent component, JLabel label)
	{
		this.component = component;
		this.label = label;
	}
	
	public ComplexComponent(JComponent component, JLabel label, Border border)
	{
		this(component, label);
		this.border = border;
	}
	
	public ComplexComponent(JComponent component, String labelText)
	{
		this(component, new JLabel(labelText));
	}

	public boolean hasBorder()
	{
		return (getBorder() != null);
	}
	
	public JComponent getComponent()
	{
		return component;
	}

	public void setComponent(JComponent component)
	{
		this.component = component;
	}

	public JLabel getLabel()
	{
		return label;
	}

	public void setLabel(JLabel label)
	{
		this.label = label;
	}

	public Border getBorder()
	{
		return border;
	}

	public void setBorder(Border border)
	{
		this.border = border;
	}
}
