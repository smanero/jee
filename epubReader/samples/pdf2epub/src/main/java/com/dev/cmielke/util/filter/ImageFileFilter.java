package com.dev.cmielke.util.filter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.filechooser.FileFilter;

public class ImageFileFilter extends FileFilter
{
    public final static ArrayList<String> SUPPORTED_EXTENSIONS = new ArrayList<String>(Arrays.asList(new String[]{"jpg", "gif", "png", "bmp"}));

    private String getExtension(File f) 
    {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

	@Override
	public boolean accept(File f) 
	{
		if (f.isDirectory()) {
            return true;
        }

        String extension = getExtension(f);
        if (extension != null) 
        {
            if(SUPPORTED_EXTENSIONS.contains(getExtension(f))) 
            {
            	return true;
            } 
            else 
            {
                return false;
            }
        }

		return false;
	}

	@Override
	public String getDescription() 
	{
		return "Supported Image-Formats (*.jpg, *.gif, *.png, *.bmp)";
	}    
}
