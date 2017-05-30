package com.dev.cmielke.gui.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;

import com.dev.cmielke.gui.command.ActionCommand;
import com.dev.cmielke.gui.logging.TextAreaAppender;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 5344785772271228665L;
	private SpringLayout springLayout;
	
	private JButton addButton;
	private JButton clearButton;
	private JButton startButton;
	
	private	JTextArea console;
	private JProgressBar progressBar;
	private JList document_list;
	
	private static ResourceBundle rBundle = ResourceBundle.getBundle("pdf2epub", Locale.getDefault());
	
	
	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame
	 */
	public MainWindow() {
		super();
		setSize(660, 400);
		setTitle(rBundle.getString("windowTitle"));
		getContentPane().setLayout(new GridLayout(1, 0));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JPanel top_panel = new JPanel();
		springLayout = new SpringLayout();
		top_panel.setLayout(springLayout);
		getContentPane().add(top_panel);

		final JLabel title = new JLabel();
		title.setFont(new Font("Tahoma", Font.PLAIN, 20));
		title.setText(rBundle.getString("applicationTitle"));
		top_panel.add(title);
		springLayout.putConstraint(SpringLayout.SOUTH, title, 30, SpringLayout.NORTH, top_panel);
		springLayout.putConstraint(SpringLayout.NORTH, title, 5, SpringLayout.NORTH, top_panel);

		final JLabel listOfDocumentsLabel = new JLabel();
		top_panel.add(listOfDocumentsLabel);
		listOfDocumentsLabel.setText(rBundle.getString("documentListTitle"));

		this.document_list = new JList();
		document_list.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		top_panel.add(document_list);
		springLayout.putConstraint(SpringLayout.SOUTH, listOfDocumentsLabel, 0, SpringLayout.NORTH, document_list);
		springLayout.putConstraint(SpringLayout.NORTH, listOfDocumentsLabel, -16, SpringLayout.NORTH, document_list);
		springLayout.putConstraint(SpringLayout.EAST, document_list, 245, SpringLayout.WEST, top_panel);
		springLayout.putConstraint(SpringLayout.WEST, document_list, 10, SpringLayout.WEST, top_panel);
		springLayout.putConstraint(SpringLayout.EAST, title, 170, SpringLayout.EAST, document_list);
		springLayout.putConstraint(SpringLayout.WEST, title, 5, SpringLayout.EAST, document_list);

		this.addButton = new JButton();
		addButton.setActionCommand(ActionCommand.ADD_DOCUMENT.getCode());
		addButton.setText(rBundle.getString("addButtonText"));
		addButton.setToolTipText(rBundle.getString("tooltip.addButtonText"));
		top_panel.add(addButton);
		springLayout.putConstraint(SpringLayout.EAST, addButton, 65, SpringLayout.WEST, top_panel);
		springLayout.putConstraint(SpringLayout.WEST, addButton, 0, SpringLayout.WEST, document_list);
		springLayout.putConstraint(SpringLayout.SOUTH, addButton, 225, SpringLayout.NORTH, top_panel);

		this.clearButton = new JButton();
		clearButton.setActionCommand(ActionCommand.CLEAR_LIST.getCode());
		clearButton.setText(rBundle.getString("clearButtonText"));
		clearButton.setToolTipText(rBundle.getString("tooltip.clearButtonText"));
		top_panel.add(clearButton);
		springLayout.putConstraint(SpringLayout.EAST, clearButton, 160, SpringLayout.WEST, top_panel);
		springLayout.putConstraint(SpringLayout.WEST, clearButton, 5, SpringLayout.EAST, addButton);
		springLayout.putConstraint(SpringLayout.SOUTH, document_list, -5, SpringLayout.NORTH, clearButton);
		springLayout.putConstraint(SpringLayout.NORTH, document_list, 67, SpringLayout.NORTH, top_panel);
		springLayout.putConstraint(SpringLayout.SOUTH, clearButton, 225, SpringLayout.NORTH, top_panel);
		springLayout.putConstraint(SpringLayout.NORTH, clearButton, 0, SpringLayout.NORTH, addButton);

		this.startButton = new JButton();
		startButton.setText(rBundle.getString("startButtonText"));
		startButton.setToolTipText(rBundle.getString("tooltip.startButtonText"));
		startButton.setActionCommand(ActionCommand.START_PROCESSING.getCode());
		top_panel.add(startButton);
		springLayout.putConstraint(SpringLayout.EAST, startButton, 0, SpringLayout.EAST, document_list);
		springLayout.putConstraint(SpringLayout.WEST, startButton, 5, SpringLayout.EAST, clearButton);

		final JLabel processOutputLabel = new JLabel();
		processOutputLabel.setText(rBundle.getString("outputListTitle"));
		top_panel.add(processOutputLabel);
		springLayout.putConstraint(SpringLayout.EAST, processOutputLabel, 355, SpringLayout.WEST, top_panel);
		springLayout.putConstraint(SpringLayout.WEST, processOutputLabel, 5, SpringLayout.WEST, title);
		springLayout.putConstraint(SpringLayout.NORTH, processOutputLabel, 50, SpringLayout.NORTH, top_panel);

		final JScrollPane scrollPaneConsole = new JScrollPane();
		top_panel.add(scrollPaneConsole);
		springLayout.putConstraint(SpringLayout.SOUTH, startButton, 0, SpringLayout.SOUTH, scrollPaneConsole);
		springLayout.putConstraint(SpringLayout.NORTH, startButton, 0, SpringLayout.NORTH, clearButton);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPaneConsole, 0, SpringLayout.SOUTH, clearButton);
		springLayout.putConstraint(SpringLayout.NORTH, scrollPaneConsole, 0, SpringLayout.NORTH, document_list);
		springLayout.putConstraint(SpringLayout.WEST, scrollPaneConsole, 5, SpringLayout.WEST, title);

		this.console = new JTextArea();
		this.console.setFont(new Font("Monospaced", Font.PLAIN, 12));
		scrollPaneConsole.setViewportView(console);
		TextAreaAppender.setTextArea(this.console);

		final JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.GRAY);
		separator_1.setPreferredSize(new Dimension(100, 5));
		top_panel.add(separator_1);
		springLayout.putConstraint(SpringLayout.EAST, listOfDocumentsLabel, 0, SpringLayout.EAST, document_list);
		springLayout.putConstraint(SpringLayout.WEST, listOfDocumentsLabel, 0, SpringLayout.WEST, separator_1);
		springLayout.putConstraint(SpringLayout.SOUTH, separator_1, 45, SpringLayout.NORTH, top_panel);
		springLayout.putConstraint(SpringLayout.NORTH, separator_1, 5, SpringLayout.SOUTH, title);
		springLayout.putConstraint(SpringLayout.EAST, separator_1, 0, SpringLayout.EAST, scrollPaneConsole);
		springLayout.putConstraint(SpringLayout.WEST, separator_1, 0, SpringLayout.WEST, document_list);

		final JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(Color.GRAY);
		separator_2.setPreferredSize(new Dimension(100, 5));
		top_panel.add(separator_2);
		springLayout.putConstraint(SpringLayout.EAST, separator_2, 0, SpringLayout.EAST, scrollPaneConsole);
		springLayout.putConstraint(SpringLayout.WEST, separator_2, -625, SpringLayout.EAST, scrollPaneConsole);
		springLayout.putConstraint(SpringLayout.NORTH, separator_2, 235, SpringLayout.NORTH, top_panel);

		int southPosition = 260;
		
		final JLabel progressLabel = new JLabel();
		progressLabel.setVisible(false);
		progressLabel.setText("Progress");
		top_panel.add(progressLabel);
		springLayout.putConstraint(SpringLayout.SOUTH, progressLabel, southPosition, SpringLayout.NORTH, top_panel);
		springLayout.putConstraint(SpringLayout.EAST, progressLabel, 150, SpringLayout.WEST, top_panel);
		springLayout.putConstraint(SpringLayout.WEST, progressLabel, 0, SpringLayout.WEST, addButton);

		southPosition+=20;
		this.progressBar = new JProgressBar(0,1);
		progressBar.setVisible(false);
		top_panel.add(progressBar);
		springLayout.putConstraint(SpringLayout.SOUTH, progressBar, southPosition, SpringLayout.NORTH, top_panel);
		springLayout.putConstraint(SpringLayout.EAST, progressBar, 635, SpringLayout.WEST, top_panel);
		springLayout.putConstraint(SpringLayout.WEST, progressBar, 0, SpringLayout.WEST, addButton);
		springLayout.putConstraint(SpringLayout.EAST, scrollPaneConsole, 0, SpringLayout.EAST, progressBar);
		
		southPosition+=30;
		final JLabel optionsLabel = new JLabel();
		optionsLabel.setVisible(true);
		optionsLabel.setText(rBundle.getString("optionsLabel"));
		top_panel.add(optionsLabel);
		springLayout.putConstraint(SpringLayout.SOUTH, optionsLabel, southPosition, SpringLayout.NORTH, top_panel);
		springLayout.putConstraint(SpringLayout.EAST, optionsLabel, 150, SpringLayout.WEST, top_panel);
		springLayout.putConstraint(SpringLayout.WEST, optionsLabel, 0, SpringLayout.WEST, addButton);
		
		southPosition+=25;
		final JCheckBox showMetaDialog = new JCheckBox();
		showMetaDialog.setText(rBundle.getString("option.alwaysShowDialog"));
		top_panel.add(showMetaDialog);
		springLayout.putConstraint(SpringLayout.SOUTH, showMetaDialog, southPosition, SpringLayout.NORTH, top_panel);
		springLayout.putConstraint(SpringLayout.WEST, showMetaDialog, 10, SpringLayout.WEST, top_panel);
		
		southPosition+=25;
		final JCheckBox insertTitelPicture = new JCheckBox();
		insertTitelPicture.setText(rBundle.getString("option.setCover"));
		top_panel.add(insertTitelPicture);
		springLayout.putConstraint(SpringLayout.SOUTH, insertTitelPicture, southPosition, SpringLayout.NORTH, top_panel);
		springLayout.putConstraint(SpringLayout.WEST, insertTitelPicture, 10, SpringLayout.WEST, top_panel);
		
		this.setResizable(false);
	}

	public void centerWindow() {
		Toolkit tk = this.getToolkit();
		Dimension dim = tk.getScreenSize();
		int newWidth = (int) (dim.getWidth() - this.getWidth()) / 2;
		int newHeight = (int) (dim.getHeight() - this.getHeight()) / 2;
		this.setLocation(newWidth, newHeight);
	}
	
	public void registerActionListener(ActionListener listener) {
		this.addButton.addActionListener(listener);
		this.clearButton.addActionListener(listener);
		this.startButton.addActionListener(listener);
	}

	public void updateOutputConsole(String text) {
		this.console.setText(text);
	}
	
	public void updateDocumentList(Set<String> data) {
		this.document_list.setListData(data.toArray());
	}

	public void setProgressMaxmimum(int max) {
		this.progressBar.setMaximum(max);
	}

	public void setProgress(int value) {
		this.progressBar.setValue(value);
	}

	public void setWorkInProgress(boolean inProgress) {
		this.startButton.setEnabled(!inProgress);
		this.clearButton.setEnabled(!inProgress);
		this.addButton.setEnabled(!inProgress);
	}
}
