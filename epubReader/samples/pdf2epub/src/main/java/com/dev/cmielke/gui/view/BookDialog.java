package com.dev.cmielke.gui.view;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.dev.cmielke.beans.BookType;
import com.dev.cmielke.beans.PerryRhodanBook;
import com.dev.cmielke.gui.beans.Options;

/**
 * 
 * @author Rafthrasa
 * @deprecated This class is no longer used! Use MetaDataDialog of package com.dev.cmielke.gui.dialogs
 */

@Deprecated
@SuppressWarnings("serial")
public class BookDialog extends JDialog {
	private static final Logger LOG = Logger.getLogger(BookDialog.class);
	private static ResourceBundle rBundle = ResourceBundle.getBundle("pdf2epub", Locale.getDefault());
	/**
	 * remember the path where images are located
	 * TODO store/get Path from options
	 */
	private static File imgDirectory = null;
	private boolean update = false;
	private int closeStatus = -1;
	private JComboBox type = new JComboBox(new DefaultComboBoxModel(new Object[] { null, BookType.HEFTROMAN, BookType.SILBERBAND }));
	private JTextField nummer = new JTextField(20);
	private JTextField titel = new JTextField(50);
	private JTextField subTitel = new JTextField(50);
	private JTextField autoren = new JTextField(50);
	private JLabel titelImgView = new JLabel();
	private BufferedImage titelImg = null;
	private JButton okButton = new JButton("Fertig");
	private JButton abbButton = new JButton("Abbruch");
	private JButton chapterButton = new JButton("Kapitel");

	static {
		imgDirectory = Options.getCoverSearchPath();
	}

	private final PerryRhodanBook book;

	private BookDialog(Frame owner, String title, PerryRhodanBook book) {
		super(owner, title, true);
		init();
		setActions();
		this.book = book;
	}

	/**
	 * Show and edit the PerryRhodam Book META-DATA.
	 * 
	 * @param parent owner frame
	 * @param book the PerryRhodan Book-MetaData
	 * @return abbruch = -1;  OK = 0; Chapter = 1;
	 */
	public static int editBook(JFrame parent, PerryRhodanBook book, String orginalFilename) {
		BookDialog dialog = new BookDialog(parent, rBundle.getString("BookDialog.titel") + "  " + orginalFilename, book);
		dialog.type.setSelectedItem(book.getType());
		dialog.type.setEnabled(book.getType() == BookType.OTHER);

		dialog.nummer.setText(book.getBookNumber() > 0 ? String.valueOf(book.getBookNumber()) : "");
		dialog.setTitel();
		dialog.subTitel.setText(book.getSubtitle());
		dialog.autoren.setText(book.getAuthors());
		if (book.containsCover()) {
			dialog.titelImg = book.getCover();
			dialog.titelImgView.setIcon(new ImageIcon(book.getCover()));
		} else {
			dialog.titelImgView.setText(rBundle.getString("book.missingTitelBild"));
		}
		dialog.setVisible(true);
		if (dialog.update) {
			if (book.getType() == BookType.OTHER) {
				book.setType((BookType) dialog.type.getSelectedItem());
			}
			book.setBookNumber(Integer.parseInt(dialog.nummer.getText()));
			book.setTitle(dialog.titel.getText());
			book.setSubtitle(dialog.subTitel.getText());
			book.setAuthors(dialog.autoren.getText());
			if (dialog.titelImgView.getIcon() != null) {
				book.setCover(dialog.titelImg);
			} else {
				book.setCover(null);
			}
		}
		return dialog.closeStatus;
	}

	void init() {
		setSize(600, 700);
		setLayout(new GridBagLayout());
		GridBagConstraints gbcLabel = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 5), 0, 0);
		GridBagConstraints gbcFeld = new GridBagConstraints(1, 0, 1, 1, 1.7, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0);

		final JLabel lType = new JLabel(rBundle.getString("book.Typ"));
		add(lType, gbcLabel);
		add(type, gbcFeld);

		final JLabel lNummer = new JLabel(rBundle.getString("book.Nummer"));
		gbcLabel.gridy++;
		gbcFeld.gridy++;
		add(lNummer, gbcLabel);
		add(nummer, gbcFeld);

		final JLabel lTitel = new JLabel(rBundle.getString("book.Titel"));
		gbcLabel.gridy++;
		gbcFeld.gridy++;
		add(lTitel, gbcLabel);
		add(titel, gbcFeld);

		final JLabel lSubTitel = new JLabel(rBundle.getString("book.Untertitel"));
		gbcLabel.gridy++;
		gbcFeld.gridy++;
		add(lSubTitel, gbcLabel);
		add(subTitel, gbcFeld);

		final JLabel lAutor = new JLabel(rBundle.getString("book.Autoren"));
		gbcLabel.gridy++;
		gbcFeld.gridy++;
		add(lAutor, gbcLabel);
		add(autoren, gbcFeld);

		final JLabel lImg = new JLabel(rBundle.getString("book.Titelbild"));
		gbcLabel.gridy++;
		gbcFeld.gridy++;
		add(lImg, gbcLabel);
		add(this.titelImgView, new GridBagConstraints(1, gbcFeld.gridy, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH,
				new Insets(5, 5, 5, 5), 0, 0));

		// Button Panel
		JPanel buttonPanel = new JPanel();
		gbcLabel.gridy++;
		gbcFeld.gridy++;
		add(buttonPanel, new GridBagConstraints(0, gbcFeld.gridy, 2, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		buttonPanel.add(chapterButton);
		buttonPanel.add(abbButton);
		buttonPanel.add(okButton);
	}

	private void setActions() {
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkComplete()) {
					BookDialog.this.update = true;
					BookDialog.this.closeStatus = 0;
					BookDialog.this.setVisible(false);
				}
			}
		});

		abbButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BookDialog.this.update = false;
				BookDialog.this.closeStatus = -1;
				BookDialog.this.setVisible(false);
			}
		});

		chapterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkComplete()) {
					BookDialog.this.update = true;
					BookDialog.this.closeStatus = 1;
					BookDialog.this.setVisible(false);
				}
			}
		});

		this.type.addItemListener(new ItemListener() {
			/**
			 * Titel bei Silberband nicht verfügbar.
			 * Bei Silberband Titel setzen
			 */
			@Override
			public void itemStateChanged(ItemEvent e) {
				BookDialog.this.setTitel();
			}
		});
		this.nummer.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				JTextField f = (JTextField) e.getComponent();
				String x = f.getText();
				try {
					int i = Integer.parseInt(x);
					f.setText(String.valueOf(i));
					if (BookDialog.this.type.getSelectedItem() == BookType.SILBERBAND) {
						BookDialog.this.setTitel();
					}
				} catch (NumberFormatException ex) {
					f.setText(""); // TODO besseren check
				}
			}
		});
		this.titelImgView.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser chooser = new JFileChooser(BookDialog.imgDirectory);
				if (chooser.showOpenDialog(BookDialog.this) == JFileChooser.APPROVE_OPTION) {
					try {
						BookDialog.this.titelImg = ImageIO.read(chooser.getSelectedFile());
						BookDialog.imgDirectory = chooser.getSelectedFile().getParentFile();
					} catch (IOException ex) {
						LOG.error("Could not read Image '" + chooser.getSelectedFile() + "'", ex);
						BookDialog.this.titelImg = null;
					}
					if (BookDialog.this.titelImg != null) {
						BookDialog.this.titelImgView.setIcon(new ImageIcon(BookDialog.this.titelImg));
						BookDialog.this.titelImgView.setText(null);
					} else {
						BookDialog.this.titelImgView.setIcon(null);
						BookDialog.this.titelImgView.setText(rBundle.getString("book.missingTitelBild"));
					}
				}
			}
		});
	}

	private boolean checkComplete() {
		return BookDialog.this.type.getSelectedItem() != null
				&& BookDialog.this.type.getSelectedItem() != BookType.OTHER
				&& BookDialog.this.nummer.getText().length() > 0
				&& ((BookDialog.this.type.getSelectedItem() == BookType.SILBERBAND && BookDialog.this.subTitel.getText().length() > 0) || (BookDialog.this.type
						.getSelectedItem() != BookType.SILBERBAND && BookDialog.this.titel.getText().length() > 0));
	}

	private void setTitel() {
		if (this.type.getSelectedItem() != null) {
			switch ((BookType) this.type.getSelectedItem()) {
			case SILBERBAND:
				this.titel.setText("Perry Rhodan Silberband" + (this.nummer.getText().length() > 0 ? " " + this.nummer.getText() : ""));
				this.titel.setEnabled(false);
				break;
			default:
				this.titel.setText(book.getTitle());
				this.titel.setEnabled(true);
				break;
			}
		}
	}

	/**
	 * Test the application
	 * @param args not needed
	 */
	public static void main(String args[]) {
		try {
			PerryRhodanBook book = new PerryRhodanBook();
			book.setType(BookType.SILBERBAND);
			book.setBookNumber(5);
			book.setTitle("Titel");
			book.setSubtitle("Subtitel");
			int ok = BookDialog.editBook(null, book, "");
			if (ok > -1) {
				System.out.println("Status=" + ok + "\n" + book);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.exit(0);
	}

}
