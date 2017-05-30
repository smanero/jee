package com.dev.cmielke.gui.dialogs.components;

import static com.dev.cmielke.gui.util.DialogConstants.*;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.dev.cmielke.gui.beans.Options;
import com.dev.cmielke.gui.util.ImageHelper;
import com.dev.cmielke.util.LoggingUtils;
import com.dev.cmielke.util.filter.ImageFileFilter;

@SuppressWarnings("serial")
public class ImageEditorPanel extends JPanel implements ActionListener
{
	private static File imgDirectory = Options.getCoverSearchPath();
	
	private static final Logger log = Logger.getLogger(ImageEditorPanel.class);
	
	private static ResourceBundle rBundle = ResourceBundle.getBundle("pdf2epub");
	
	private Window window;
	
	private ImagePanel imagePanel;
	private JPanel buttonPanel;
	
	private LayoutManager layout;
	
	private JButton loadButton, removeButton;
	
	private boolean imageLoaded = false;
	
	public ImageEditorPanel(Window window)
	{
		this.window = window;
		initLayout();
		initImagePanel();		
		initLocalComponents();
		addActionListener(this);
	}
	
	public ImageEditorPanel(BufferedImage image, Window window)
	{
		this.window = window;
		initLayout();
		initImagePanel(image);		
		initLocalComponents();
		addActionListener(this);
	}
	
	public ImageEditorPanel(File imageFile, Window window)
	{		
		this.window = window;
		initLayout();
		initImagePanel(imageFile);		
		initLocalComponents();
		addActionListener(this);
	}
	
	public BufferedImage getBufferedImage()
	{
		return ImageHelper.toBufferedImage(getImage());
	}
	
	public Image getImage()
	{
		return imagePanel.getImage(); 
	}
	
	public void setImage(Image image)
	{
		imagePanel.setImage(image);
	}
	
	public void setImage(File image)
	{
		imagePanel.setImage(image);
	}
	
	private void initLayout()
	{
		layout = new BorderLayout();
		setLayout(layout);
	}
	
	private void initImagePanel()
	{
		imagePanel = new ImagePanel();
		add(imagePanel, BorderLayout.CENTER);
	}
	
	private void initImagePanel(Image image)
	{
		imagePanel = new ImagePanel(image);
		add(imagePanel, BorderLayout.CENTER);
	}
	
	private void initImagePanel(File imageFile)
	{
		imagePanel = new ImagePanel(imageFile);
		add(imagePanel, BorderLayout.CENTER);
	}
	
	private void initLocalComponents()
	{
		buttonPanel = new JPanel();
		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);
		buttonPanel.setLayout(layout);
		
		URL url = ImageEditorPanel.class.getResource( ICON_IMAGE_ADD );		
		loadButton = new JButton(rBundle.getString(RB_PARAM_NAME_LOAD_BUTTON_TITLE));
		loadButton.setActionCommand(Integer.toString(ACTION_CMD_CODE_BT_ADD));
		if(url != null)
		{
			loadButton.setIcon(new ImageIcon(url));
		}
		buttonPanel.add(loadButton);

		url = ImageEditorPanel.class.getResource( ICON_IMAGE_REMOVE );
		removeButton = new JButton(rBundle.getString(RB_PARAM_NAME_REMOVE_BUTTON_TITLE));
		removeButton.setActionCommand(Integer.toString(ACTION_CMD_CODE_BT_REMOVE));
		if( url != null)
		{
			removeButton.setIcon(new ImageIcon(url));
		}
		buttonPanel.add(removeButton);
		
		add(buttonPanel, BorderLayout.SOUTH);
		
		if(!isImageSet())
		{
			removeButton.setEnabled(false);
		}
	}
	
	private void addActionListener(ActionListener listener)
	{
		loadButton.addActionListener(listener);
		removeButton.addActionListener(listener);
	}
	
	private boolean isImageSet()
	{
		return (imagePanel.getImage() != null);
	}
	
	public boolean hasNewImage()
	{
		return imageLoaded;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{	
		Integer actionCode = Integer.parseInt(e.getActionCommand());
		switch(actionCode.intValue())
		{
			case ACTION_CMD_CODE_BT_ADD:
			{
				log.debug("> ACTION: load");
				JFileChooser c = new JFileChooser(imgDirectory);
				c.setFileFilter(new ImageFileFilter());
				int code = c.showOpenDialog(this);
				if(code == JFileChooser.APPROVE_OPTION)
				{
					File img = c.getSelectedFile();
					log.debug("Loading File: ["+ img.getName() +"]");
					imagePanel.setImage(img);
					
					if(isImageSet())
					{
						imageLoaded = true;
						removeButton.setEnabled(true);
					}
					window.pack();
				}
				else
				{
					log.debug("Loading canceled !");
				}
				break;
			}
			case ACTION_CMD_CODE_BT_REMOVE:
			{
				log.debug("> ACTION: remove");
				imagePanel.removeImage();
				break;
			}
			default:
			{
				log.error("Unrecognized ActionCommand !!!");
				break;
			}
		}
	}
	
	public static void main(String[] args)
	{
		LoggingUtils.configureLog4J();
		JFrame frame = new JFrame("Test");
		JFileChooser c = new JFileChooser();
		c.setCurrentDirectory(new File("/home/cmielke/workspace/PDF2Epub-Converter/src/main/resources"));
		int code = c.showOpenDialog(frame);
		if( code == JFileChooser.APPROVE_OPTION)
		{
			ImageEditorPanel panel = new ImageEditorPanel(c.getSelectedFile(), frame);
			
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
