package com.dev.cmielke.gui.dialogs;

import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_CHAPTER_DIALOG_CANCEL_BUTTON_TITLE;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_CHAPTER_DIALOG_OK_BUTTON_TITLE;
import static com.dev.cmielke.gui.util.DialogConstants.RB_PARAM_NAME_CHAPTER_DIALOG_TITLE;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.dev.cmielke.beans.PerryRhodanBook;
import com.dev.cmielke.beans.PerryRhodanBook.Chapter;
import com.dev.cmielke.gui.util.DialogConstants;
import com.dev.cmielke.gui.util.UIUtils;
import com.dev.cmielke.util.SpellCheckHelper;
import com.dev.cmielke.util.ApplicationContants.ChapterFormat;
import com.dev.cmielke.util.ApplicationContants.ChapterFormatInfo;

@SuppressWarnings("serial")
public class BookChapterDialog extends JDialog 
{
	private static ResourceBundle rBundle = ResourceBundle.getBundle("pdf2epub", Locale.getDefault());
	
	private boolean closeOk;

	private JComboBox formatType = new JComboBox(new DefaultComboBoxModel(new Object[] { ChapterFormat.TEXT,
																						 ChapterFormat.PREFORMATED,
																						 ChapterFormat.ZEITTAFEL,
																						 ChapterFormat.GLOSSAR }));
	
	private JButton okButton  = new JButton(rBundle.getString(RB_PARAM_NAME_CHAPTER_DIALOG_OK_BUTTON_TITLE));
	private JButton abbButton = new JButton(rBundle.getString(RB_PARAM_NAME_CHAPTER_DIALOG_CANCEL_BUTTON_TITLE));
	
	JEditorPane editor = new JEditorPane();
	JScrollPane editorScrollPane = new JScrollPane(editor);
	JList chapterList = new JList();

	private final PerryRhodanBook book;
	private Chapter currentChapter;

	private BookChapterDialog(Frame owner, String title, PerryRhodanBook book) {
		super(owner, title, true);
		init();
		this.book = book;
		setView();
		setActions();
		
		UIUtils.centerWindowHorizontal(this);
		SpellCheckHelper.registerComponent(editor);
		editor.setFont(DialogConstants.DEFAULT_FONT);
	}
	
	public static boolean editChapter(JFrame parent, PerryRhodanBook book, String orginalFilename) {
		BookChapterDialog dialog = new BookChapterDialog(parent, rBundle.getString(RB_PARAM_NAME_CHAPTER_DIALOG_TITLE) + "  " + orginalFilename, book);
		dialog.setVisible(true);
		
		return dialog.closeOk;
	}
	
	void init() {
		setSize(800, 700);
		setLayout(new GridBagLayout());
		
		JScrollPane listScrollPane = new JScrollPane(chapterList);
		JPanel editorPanel = new JPanel(new GridBagLayout());
		editorPanel.add(formatType,new GridBagConstraints(0, 0, GridBagConstraints.REMAINDER, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 10, 2), 0, 0));
		editorPanel.add(editorScrollPane,new GridBagConstraints(0, 1, GridBagConstraints.REMAINDER, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		JSplitPane splittPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,listScrollPane,editorPanel);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(splittPane,BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(abbButton);
		buttonPanel.add(okButton);
		add(mainPanel,new GridBagConstraints(0, 0, GridBagConstraints.REMAINDER, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 5), 0, 0));
		add(buttonPanel,new GridBagConstraints(0, 1, GridBagConstraints.REMAINDER, 1, 1.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(10, 0, 5, 10), 0, 0));
	}
	
	void setView() {
		DefaultListModel model = new DefaultListModel();
		
		for (Chapter chapter : book.getChapters()) {
			model.addElement(new ChapterListDecorator(chapter));
		}
		this.chapterList.setModel(model);
	}
	
	void setActions() {
		
		this.chapterList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent evt) {
				saveChapterText();
				JList list = (JList)evt.getSource();
				BookChapterDialog.this.currentChapter = ((ChapterListDecorator)list.getSelectedValue()).chapter;
				setChapterEditor();
			}
		});
		this.chapterList.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar()==KeyEvent.VK_DELETE && e.isShiftDown()) {
					JList list = (JList)e.getSource();
					
					if(list.getSelectedValue()!=null) {
						BookChapterDialog.this.book.removeChapter(((ChapterListDecorator)list.getSelectedValue()).chapter);
						setView();
					}
				}
			}
		});
		this.abbButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BookChapterDialog.this.setVisible(false);
			}
		});
		this.okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveChapterText();
				BookChapterDialog.this.closeOk=true;
				BookChapterDialog.this.setVisible(false);
			}
		});
	}
	
	private void setChapterEditor() {
		if(this.currentChapter!=null) {
			this.editor.setText(this.currentChapter.getText());
			this.formatType.setSelectedItem(this.currentChapter.getFormat());
			this.editor.setCaretPosition(0);
		}else{
			this.editor.setText("");
			this.formatType.setSelectedItem(ChapterFormat.TEXT);
		}
	}
	private void saveChapterText() {
		if(currentChapter != null) {	// speichere Kapitel
			this.currentChapter.setText(this.editor.getText());
			this.currentChapter.setFormat((ChapterFormat)this.formatType.getSelectedItem());
		}
	}
	
	
	private static class ChapterListDecorator {
		private final Chapter chapter;
		public ChapterListDecorator(Chapter chapter) {
			this.chapter = chapter;
		}
		
		@Override
		public String toString() {
			String chapterTitel;
			if(chapter.hasChapterNumber()) {
				if(chapter.getTitel()!=null && chapter.getTitel().length()>0) {
					chapterTitel = chapter.getChapterNumber()+". "+chapter.getTitel();
				}else{
					chapterTitel = chapter.getChapterNumber()+". Kapitel";
				}
			}else{
				chapterTitel = chapter.getTitel();
			}
			return chapterTitel;
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			PerryRhodanBook book = new PerryRhodanBook();
			book.setBookNumber(5);
			book.setTitle("Titel");
			book.setSubtitle("Subtitel");
			book.newChapter(null, new ChapterFormatInfo("Einleitung", ChapterFormat.TEXT));
			book.getCurrentChapter().appendToText("Das ist die Einführung mit etwas Text zum editieren");
			book.newChapter(1, new ChapterFormatInfo(null, ChapterFormat.TEXT));
			book.getCurrentChapter().appendToText("Das ist das Kapitel1");
			book.newChapter(2, new ChapterFormatInfo(null, ChapterFormat.TEXT));
			book.getCurrentChapter().appendToText("Das ist das Kapitel 2.");
			book.newChapter(null, new ChapterFormatInfo("Glossar", ChapterFormat.GLOSSAR));
			book.getCurrentChapter().appendToText("begriff-\nDas ist die Erklärung zum Begriff\nbegriff 1-\nDas ist die Erklärung zum Begriff 2");
			boolean ok = BookChapterDialog.editChapter(null, book, "");
			if(ok) {
				System.out.println(book);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.exit(0);
	}

}
