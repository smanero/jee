package com.dev.cmielke.gui.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class LayoutHelper
{
	public static void addComponent(Container con, GridBagLayout gbl, Component c,
			int x, int y, int width, int height, double weightx, double weighty)
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbl.setConstraints(c, gbc);
		con.add( c );
	}
}
