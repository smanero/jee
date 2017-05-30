package com.dev.cmielke.gui.util;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

public class UIUtils
{
	public static void centerWindow(Container c)
	{
		Toolkit tk = c.getToolkit();
        Dimension dim = tk.getScreenSize();
        int newWidth = (int)(dim.getWidth() - c.getWidth()) /2;
        int newHeight = (int)(dim.getHeight() - c.getHeight()) /2;
        c.setLocation(newWidth, newHeight);
	}
	
	public static void centerWindowHorizontal(Container c)
	{
		Toolkit tk = c.getToolkit();
        Dimension dim = tk.getScreenSize();
        int newWidth = (int)(dim.getWidth() - c.getWidth()) /2; 
        c.setLocation(newWidth, 0);
	}
	
	public static void centerWindowVertical(Container c)
	{
		Toolkit tk = c.getToolkit();
        Dimension dim = tk.getScreenSize();
        int newHeight = (int)(dim.getHeight() - c.getHeight()) /2;
        c.setLocation(0, newHeight);
	}
}
