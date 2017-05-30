package com.dev.cmielke.gui.dialogs.components;

import static com.dev.cmielke.gui.util.DialogConstants.DEFAULT_FONT;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_CANCEL_BUTTON_TITLE;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_OK_BUTTON_TITLE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import org.apache.log4j.Logger;

import com.dev.cmielke.gui.util.DialogConstants;
import com.dev.cmielke.gui.util.LayoutHelper;
import com.dev.cmielke.gui.util.UIUtils;
import com.dev.cmielke.util.SpellCheckHelper;

public abstract class DialogBox extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(DialogBox.class);
	
	public static final Font HEADER_FONT = new Font("arial", Font.PLAIN, 18);
	public static final int DEFAULT_FRAME_WIDTH  = 500;
	public static final int DEFAULT_FRAME_HEIGHT = 500;
	
	protected static ResourceBundle rBundle = ResourceBundle.getBundle("pdf2epub");
	
	private String dialogTitle  = "Title";
	private String message      = "message";
	
	private ArrayList<JComponent> dialogComponents = new ArrayList<JComponent>();
	
	private JPanel mainPanel         = new JPanel();
	private JPanel contentPanel      = new JPanel();
	private JPanel buttonPanel       = new JPanel();
	
	private LayoutManager mainLayout = new BorderLayout();
	private LayoutManager contentLayout;
	
	private JButton okButton     = new JButton(rBundle.getString(RB_PARAM_NAME_OK_BUTTON_TITLE));
	private JButton cancelButton = new JButton(rBundle.getString(RB_PARAM_NAME_CANCEL_BUTTON_TITLE));
	
	private JTextArea errorMessageBox = new JTextArea();
	private HashMap<Component, String> errors = new HashMap<Component, String>();
	
	protected HashMap<String, Object> additionalParameters;
	
	protected int returnValue;
	
	public DialogBox(String title, String message)
	{
		this(title, message, new HashMap<String,Object>());
	}
	
	public DialogBox(String title, String message, Map<String, Object> additionalParameters)
	{
		this.setModal(true);
		this.setTitle(title);
		this.dialogTitle = title;
		this.message = message;
		this.additionalParameters = new HashMap<String, Object>(additionalParameters);
		
		initLayout();
		initStandardComponents();
		initComponents(this.additionalParameters);
		registerCompontents();
		
		buildDialog();
		
		setWindowConstraints();
	}
	
	public int showDialog()
	{
		this.setVisible(true);
		return this.getReturnValue();
	}
	
	protected void setWindowConstraints()
	{
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
		UIUtils.centerWindow(this);
		pack();
	}
	
	/**
	 * Hook zur Anpassung des ContentLayouts. Als Standard wird hier
	 * ein GridLayout verwendet, welches eine Column besitzt und
	 * soviele Reihen hat, wie es DialogComponents gibt.
	 */
	protected void initLayout()
	{
		contentLayout = new GridLayout(dialogComponents.size(),1);
		contentPanel.setLayout(contentLayout);
		GridLayout gLayout = (GridLayout)contentLayout;
		gLayout.setVgap(5);
	}
	
	/**
	 * Main-Hook für alle implementierenden Klassen. Hier wird der Content-Bereich der
	 * Dialog-Box erzeugt / bzw. festgelegt welche Components der Content-Bereich 
	 * enthält. Alle Components die in dem Content-Bereich erscheinen sollen, müssen
	 * in die Component-Liste <code>getDialogComponents</code> hinzugefügt werden. 
	 * Diese Liste wird bei der Generierung des Content-Bereichs ausgewertet und die
	 * einzelnen Components dem Content-Bereich zugeordnet.
	 * 
	 * Es ist jeder belibieger Component erlaubt. Hinzu kommt eine Spezial-Component-Klasse
	 * <code>ComplexComponent</code> welche sich aus einem Label und einem beliebigem Component
	 * zusammensetzt. Dadurch sind Components mit führendem Label realisierbar.
	 */
	protected abstract void initComponents(Map<String,Object> additionalParameters);
	
	/**
	 * Methode zur Initialisierung und Konfiguration der Standard-Komponenten.
	 */
	protected void initStandardComponents()
	{
		okButton.setActionCommand(Integer.toString(DialogConstants.ACTION_CMD_CODE_BUTTON_OK));
		okButton.addActionListener(this);
		cancelButton.setActionCommand(Integer.toString(DialogConstants.ACTION_CMD_CODE_BUTTON_CANCEL));
		cancelButton.addActionListener(this);
	}
	
	/**
	 * Methode zur Generierung des Dialogs
	 */
	protected void buildDialog()
	{
		mainPanel.setLayout(mainLayout);
	
		mainPanel.add(buildHeader(), BorderLayout.NORTH);
		
		mainPanel.add(buildContentPanel(contentPanel), BorderLayout.CENTER);
		
		mainPanel.add(buildButtonPanel(buttonPanel), BorderLayout.SOUTH);
		
		getContentPane().add(mainPanel);
	}
	
	/**
	 * Hook zur Anpassung der Header-Generierung. Als Standard wird ein GridLayout
	 * verwendet. Die Überschrift ist ein Label und die Message ist eine nicht
	 * editierbare Textarea. Die Überschrift ist spezielle formatiert.
	 * 
	 * @return Panel-Component, welche die Header-Elemente enthält.
	 */
	protected JPanel buildHeader()
	{
		GridBagLayout headerLayout = new GridBagLayout();
		JPanel headPanel  = new JPanel(headerLayout);
		JLabel titleLabel = new JLabel(dialogTitle);
		titleLabel.setFont(HEADER_FONT);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		LayoutHelper.addComponent(headPanel, headerLayout, titleLabel, 0, 0, 1, 1, 1.0, 0.0);
		
		if(message != null && !message.isEmpty())
		{
			JTextArea messageArea = new JTextArea(message);
			messageArea.setEditable(false);
			messageArea.setRows(5);
			messageArea.setLineWrap(true);
			messageArea.setWrapStyleWord(true);
			messageArea.setBackground(null);
			messageArea.setBorder(BorderFactory.createEmptyBorder(5,15,5,15));
			LayoutHelper.addComponent(headPanel, headerLayout, messageArea, 0, 1, 1, 1, 1.0, 0.0);
		}
		
		errorMessageBox.setEditable(false);
		errorMessageBox.setRows(3);
		errorMessageBox.setLineWrap(true);
		errorMessageBox.setBackground(new Color(255, 223, 223));
		errorMessageBox.setForeground(Color.red);
		errorMessageBox.setBorder(BorderFactory.createEtchedBorder());
		if(errorMessageBox.getText().isEmpty())
		{
			errorMessageBox.setVisible(false);
		}
		LayoutHelper.addComponent(headPanel, headerLayout, errorMessageBox, 0, 2, 1, 1, 1.0, 0.0);
		
		return headPanel;
	}
	
	/**
	 * Hook zur Anpassung der Generierung des Content-Bereichs.
	 * 
	 * @param contentPanel - Panel des Content-Bereichs
	 * @return Panel, welches die Elemente des Content-Bereichs enthält.
	 */
	protected JPanel buildContentPanel(JPanel contentPanel)
	{
		GridBagLayout compGrid = new GridBagLayout();
		
		contentPanel.setLayout(compGrid);
		
		int y = 0;
		for(JComponent comp : dialogComponents)
		{
			if(comp instanceof ComplexComponent)
			{
				ComplexComponent lComp = (ComplexComponent)comp;
				lComp.getLabel().setVerticalAlignment(JLabel.TOP);
				LayoutHelper.addComponent(contentPanel, compGrid, lComp.getLabel()    , 0, y, 1, 1, 0.0, 0.0);
				LayoutHelper.addComponent(contentPanel, compGrid, lComp.getComponent(), 1, y, 1, 1, 1.0, 1.0);
				
				lComp.getComponent().setFont(DEFAULT_FONT);
				if(lComp.getComponent() instanceof JTextComponent)
				{
					SpellCheckHelper.registerComponent((JTextComponent) lComp.getComponent());
				}
			}
			else if(comp instanceof JSeparator)
			{
				comp.setForeground(Color.gray);
				LayoutHelper.addComponent(contentPanel, compGrid, comp, 0, y, 2, 1, 1.0, 1.0);
			}
			else
			{
				comp.setFont(DEFAULT_FONT);
				if(comp instanceof JTextComponent)
				{
					SpellCheckHelper.registerComponent((JTextComponent) comp);
				}
				LayoutHelper.addComponent(contentPanel, compGrid, comp, 1, y, 1, 1, 1.0, 1.0);
			}
			y++;
		}
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.gray);
		LayoutHelper.addComponent(contentPanel, compGrid, separator, 0, y, 2, 1, 1.0, 1.0);
		
		return contentPanel;
	}
	
	/**
	 * Hook zur Anpassung der Generierung der Button-Leiste im Main-Layout
	 * 
	 * @param buttonPanel - Panel der Button-Leiste
	 * @return Panel, welches die Button-Leisten-Elemente enthält.
	 */
	protected JPanel buildButtonPanel(JPanel buttonPanel)
	{
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		return buttonPanel;
	}
	
	protected void registerCompontents()
	{
		for(JComponent comp : dialogComponents)
		{
			try
			{
				if(comp instanceof ComplexComponent)
				{
					comp = ((ComplexComponent)comp).getComponent();
				}
				
				Class<?> clazz = comp.getClass();
				Class<?> partypes[] = new Class[1];
				partypes[0] = ActionListener.class;
				
				Method meth = clazz.getMethod("addActionListener", partypes);

				Object arglist[] = new Object[1];
				arglist[0] = this;
				meth.invoke(comp, arglist);
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (SecurityException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				e.printStackTrace();
			}
			catch (NoSuchMethodException e)
			{
				log.debug("Component ["+ comp.getClass() +"] has no 'addActionListener'-method, ignoring it!");
			}
		}
	}
	
	public void close()
	{
		this.setVisible(false);
		this.dispose();
	}
	
	public void addError(Component comp, String message)
	{
		errors.put(comp, message);
		displayErrorMessages();
	}
	
	public void removeError(Component comp)
	{
		errors.remove(comp);
		displayErrorMessages();
	}
	
	private void displayErrorMessages()
	{
		StringBuilder builder = new StringBuilder("");
		for(Component comp : errors.keySet())
		{
			String value = errors.get(comp);
			builder.append("\u2022 " + value +"\n");
		}
		if(!builder.toString().isEmpty())
		{
			errorMessageBox.setVisible(true);
		}
		else
		{
			errorMessageBox.setVisible(false);
		}
		errorMessageBox.setText(builder.toString());
		update(getGraphics());
		pack();
	}
	
	public void setFocusOnFirstErrorComponent()
	{
		Component comp = getErrors().keySet().iterator().next();
		comp.requestFocus();
	}
	
	public boolean hasActionErrors()
	{
		boolean hasErrors = false;
		if(!errors.isEmpty())
		{
			hasErrors = true;
		}
		return hasErrors;
	}

/* *************************************************************************************************************
 * 
 * GETTER / SETTER
 * 
 * ************************************************************************************************************/
	
	public String getDialogTitle()
	{
		return dialogTitle;
	}

	public void setDialogTitle(String title)
	{
		this.dialogTitle = title;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public ArrayList<JComponent> getDialogComponents()
	{
		return dialogComponents;
	}

	public void setDialogComponents(ArrayList<JComponent> dialogComponents)
	{
		this.dialogComponents = dialogComponents;
	}

	public JPanel getMainPanel()
	{
		return mainPanel;
	}

	public void setMainPanel(JPanel mainPanel)
	{
		this.mainPanel = mainPanel;
	}

	public JPanel getButtonPanel()
	{
		return buttonPanel;
	}

	public void setButtonPanel(JPanel buttonPanel)
	{
		this.buttonPanel = buttonPanel;
	}

	public LayoutManager getMainLayout()
	{
		return mainLayout;
	}

	public void setMainLayout(LayoutManager mainLayout)
	{
		this.mainLayout = mainLayout;
	}

	public int getReturnValue()
	{
		return returnValue;
	}

	public void setReturnValue(int returnValue)
	{
		this.returnValue = returnValue;
	}

	public HashMap<Component, String> getErrors()
	{
		return errors;
	}

	public void setErrors(HashMap<Component, String> errors)
	{
		this.errors = errors;
	}

	public JPanel getContentPanel()
	{
		return contentPanel;
	}

	public void setContentPanel(JPanel contentPanel)
	{
		this.contentPanel = contentPanel;
	}
}