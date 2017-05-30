package com.dev.cmielke.gui.dialogs.components;

import static com.dev.cmielke.gui.util.DialogConstants.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.dev.cmielke.util.LoggingUtils;

public class ImagePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(ImagePanel.class);
	
	private static final int DEFAULT_WIDTH  = 350;
	private static final int DEFAULT_HEIGHT = 510;
	
	private static ResourceBundle rBundle = ResourceBundle.getBundle("pdf2epub");
	
	private Image image;
	
	public ImagePanel() 
	{
		Dimension d = new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setSize(d);
		setPreferredSize(d);
		setBorder(BorderFactory.createEtchedBorder());
	}
	
	public ImagePanel(File imageFile)
	{		
		this();
		setImage(imageFile);
	}
	
	public ImagePanel(Image image)
	{
		this();
		setImage(image);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		if (image != null)
		{
			g.drawImage(image, 0, 0, this);
		}
		else
		{
			g.clearRect(getX(), getY(), getWidth(), getHeight());
			g.setColor(Color.RED);
			g.drawString(rBundle.getString(RB_PARAM_NAME_MISSING_COVER_TEXT), 5, 15);
			g.setColor(Color.BLACK);
			g.drawString(rBundle.getString(RB_PARAM_NAME_COVER_DIMENSION_TEXT), DEFAULT_WIDTH/4, DEFAULT_HEIGHT/2);
		}
	}
	
	public Image getImage()
	{
		return image;
	}
	
	public void removeImage()
	{
		log.debug("> removeImage");
		this.image = null;
		getGraphics().clearRect(getX(), getY(), getWidth(), getHeight());
		log.debug("< removeImage");
	}
	
	public void setImage(File file)
	{
		log.debug("> setImage");
		if(file != null)
		{
			image = Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath());
			setImage(image);
		}
		else
		{
			log.debug("File-object is NULL, nothing to do here!");
		}
		log.debug("< setImage");
	}

	public void setImage(Image image)
	{
		this.image = image;	
		if(image != null)
		{
			MediaTracker mt = new MediaTracker(this);
	        mt.addImage(this.image, 0);
	        try 
	        {
	        	mt.waitForAll();
	        }
	        catch (InterruptedException e) 
	        {
	        	log.warn("InterruptedException - MediaTracker");
	        }
	
			int width = this.image.getWidth(this);
			int height = this.image.getHeight(this);
			log.debug("Image properties - WIDTH: ["+ width +"] HEIGHT: ["+ height +"]");
			
			this.setSize(width, height);
			this.setPreferredSize(new Dimension(width, height));
			
			if (this.image != null)
			{
				repaint();
			}
		}
	}
	
	public static void main(String[] args)
	{
		LoggingUtils.configureLog4J();
		JFrame frame = new JFrame("Test");
		JFileChooser c = new JFileChooser();
		int code = c.showOpenDialog(frame);
		if( code == JFileChooser.APPROVE_OPTION)
		{
			ImagePanel panel = new ImagePanel(c.getSelectedFile());
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			frame.setVisible(true);
			
			frame.getContentPane().add(panel);
			frame.pack();
		}
		else
		{
			System.exit(1);
		}
	}	
}
