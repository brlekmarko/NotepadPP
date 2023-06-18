package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import hr.fer.oprpp1.hw08.jnotepadpp.actions.CloseDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.CopyDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.CutDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.ExitDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.InvertCaseAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.NewDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.OpenDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.PasteDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.SaveAsDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.SaveDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.SortAscendingAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.SortDescendingAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.StatsDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.ToLowerCaseAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.ToUpperCaseAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.UniqueAction;
import hr.fer.oprpp1.hw08.jnotepadpp.interfaces.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.interfaces.SingleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LanguagesCroatianAction;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LanguagesEnglishAction;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LanguagesGermanAction;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizationProvider;


/**
 * Predstavlja finalnu notepad aplikaciju.
 * 
 * @author Marko Brlek
 *
 */
public class JNotepadPP extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private JPanel topPanel;
	private JPanel bottomPanel;
	private DefaultMultipleDocumentModel multipleDocumentModel;
	private boolean gotovo;
	
	private JLabel length;
	private JLabel row;
	private JLabel col;
	private JLabel sel;
	private JLabel time;
	
	private Action newDocumentAction;
	private Action closeDocumentAction;
	private Action exitDocumentAction;
	private Action openDocumentAction;
	private Action saveDocumentAction;
	private Action saveAsDocumentAction;
	private Action statsDocumentAction;
	private Action copyDocumentAction;
	private Action pasteDocumentAction;
	private Action cutDocumentAction;
	private Action toLowerCaseAction;	
	private Action toUpperCaseAction;
	private Action invertCaseAction;
	private Action languagesEnglishAction;
	private Action languagesGermanAction;
	private Action languagesCroatianAction;
	private Action sortAscendingAction;
	private Action sortDescendingAction;
	private Action uniqueAction;
	
	private JButton buttonNew;
	private JButton buttonOpen;
	private JButton buttonSave;
	private JButton buttonSaveAs;
	private JButton buttonStats;
	private JButton buttonClose;
	private JButton buttonCut;
	private JButton buttonCopy;
	private JButton buttonPaste;
	
	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenu toolsMenu;
	private JMenu changeCaseMenu;
	private JMenu sortMenu;
	private JMenu languagesMenu;
	
	
	public JNotepadPP() {
		setLocation(100,100);
		setSize(1000, 800);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(closeWindow);
		setTitle("JNotepad++");
		
		initGUI();
	}
	
	
	/**
	 * Kreira menuBar, toolBar te glavni dio zamotan u JScrollPane
	 * Stavlja listener na multipledocumentmodel tako da može
	 * stavljati caretListener na textarea koji se trenutno prikazuje
	 */
	private void initGUI() {
		
		Container pane = this.getContentPane();
		pane.setLayout(new BorderLayout());
		
		topPanel = new JPanel(new BorderLayout());
		
		multipleDocumentModel = new DefaultMultipleDocumentModel();
		
		MyCaretListener myCaretListener = new MyCaretListener();
		
		
		//ovdje ima čudne manevre jer se caretListener ne aktivira na promjenu taba
		multipleDocumentModel.addMultipleDocumentListener(new MultipleDocumentListener() {
			/**
			 * Miče caretListener sa prošlog dokumenta i stavlja ga na novi.
			 * Treba updateati donji status bar što napravim tako da pomaknem caret naprijed natrag.
			 * Provjerava treba li ugasiti save, save as... opcije
			 */
			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				JNotepadPP.this.setTitle(multipleDocumentModel.getTitle());
				previousModel.getTextComponent().removeCaretListener(myCaretListener);
				currentModel.getTextComponent().addCaretListener(myCaretListener);
				
				currentModel.getTextComponent().requestFocus();
				int previousPosition = currentModel.getTextComponent().getCaretPosition();
				
				currentModel.getTextComponent().setCaretPosition(currentModel.getTextComponent().getText().length());
				
				if(currentModel.getTextComponent().getText().length() == 0) {
					JNotepadPP.this.length.setText("Length: 0");
					JNotepadPP.this.row.setText("Ln: 1");
					JNotepadPP.this.col.setText("Col: 1");
					JNotepadPP.this.sel.setText("Sel: 0");
				}else {
					currentModel.getTextComponent().setCaretPosition(currentModel.getTextComponent().getText().length()-1);
					currentModel.getTextComponent().setCaretPosition(previousPosition); //izforsiram pomak careta, odvratno
				}
				
				enableTools(multipleDocumentModel.getEnabledTools());
			}
			/**
			 * Dodaje caretListener na novi dokument.
			 * Pomiče caret naprijed natrag da ga natjera da se aktivira.
			 * Upali save, save as... opcije
			 */
			@Override
			public void documentAdded(SingleDocumentModel model) {
				JNotepadPP.this.setTitle(multipleDocumentModel.getTitle());
				model.getTextComponent().addCaretListener(myCaretListener);
				
				model.getTextComponent().requestFocus();
				
				int previousPosition = model.getTextComponent().getCaretPosition();
				
				model.getTextComponent().setCaretPosition(model.getTextComponent().getText().length());
				
				if(model.getTextComponent().getText().length() == 0) {
					JNotepadPP.this.length.setText("Length: 0");
					JNotepadPP.this.row.setText("Ln: 1");
					JNotepadPP.this.col.setText("Col: 1");
					JNotepadPP.this.sel.setText("Sel: 0");
				}else {
					model.getTextComponent().setCaretPosition(model.getTextComponent().getText().length()-1);
					model.getTextComponent().setCaretPosition(previousPosition); //izforsiram pomak, odvratno
				}
				
				enableTools(multipleDocumentModel.getEnabledTools());
			}
			/**
			 * Miče caretListener
			 * Provjerava treba li ugasiti save, save as... opcije
			 */
			@Override
			public void documentRemoved(SingleDocumentModel model) {
				JNotepadPP.this.setTitle(multipleDocumentModel.getTitle());
				model.getTextComponent().removeCaretListener(myCaretListener);
				enableTools(multipleDocumentModel.getEnabledTools());
			}			
		});
		
		
		
		
		JScrollPane scrollPane = new JScrollPane(multipleDocumentModel);
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		pane.add(scrollPane, BorderLayout.CENTER);
		
		createActions();
		createMenuBar();
		createToolBar();
		
		pane.add(topPanel, BorderLayout.NORTH);
		
		
		//donji status bar
		bottomPanel = new JPanel(new GridLayout(1, 2));
		
		length = new JLabel("Length: 0");
		row = new JLabel("Ln: 0");
		col = new JLabel("Col: 0");
		sel = new JLabel("Sel: 0");
		time = new JLabel(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));

		length.setAlignmentX(LEFT_ALIGNMENT);
		
		JPanel rightPanel = new JPanel(new GridLayout(1,0));
		rightPanel.add(row);
		rightPanel.add(col);
		rightPanel.add(sel);
		rightPanel.add(time);
		////
		
		
		
		JNotepadPP.this.bottomPanel.add(length);
		JNotepadPP.this.bottomPanel.add(rightPanel);
		pane.add(bottomPanel, BorderLayout.SOUTH);
		
		
		this.gotovo = false;
		clockUpdate.start(); //pokreće sat dretvu
	}
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JNotepadPP notepad = new JNotepadPP();
			notepad.setVisible(true);
		});
	}
	
	
	
	private void createActions() {
		this.newDocumentAction = new NewDocumentAction(multipleDocumentModel);
		this.closeDocumentAction = new CloseDocumentAction(multipleDocumentModel);
		this.exitDocumentAction = new ExitDocumentAction(this);
		this.openDocumentAction = new OpenDocumentAction(multipleDocumentModel, this);
		this.saveDocumentAction = new SaveDocumentAction(multipleDocumentModel);
		this.saveAsDocumentAction = new SaveAsDocumentAction(multipleDocumentModel);
		this.statsDocumentAction = new StatsDocumentAction(multipleDocumentModel, this);
		this.copyDocumentAction = new CopyDocumentAction(multipleDocumentModel);
		this.pasteDocumentAction = new PasteDocumentAction(multipleDocumentModel);
		this.cutDocumentAction = new CutDocumentAction(multipleDocumentModel);
		this.toLowerCaseAction = new ToLowerCaseAction(multipleDocumentModel);
		this.toUpperCaseAction = new ToUpperCaseAction(multipleDocumentModel);
		this.invertCaseAction = new InvertCaseAction(multipleDocumentModel);
		this.languagesEnglishAction = new LanguagesEnglishAction();
		this.languagesCroatianAction = new LanguagesCroatianAction();
		this.languagesGermanAction = new LanguagesGermanAction();
		this.sortAscendingAction = new SortAscendingAction(multipleDocumentModel);
		this.sortDescendingAction = new SortDescendingAction(multipleDocumentModel);
		this.uniqueAction = new UniqueAction(multipleDocumentModel);
		enableTools(false);
		enableCopyCut(false);
	}
	
	
	
	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		fileMenu = new JMenu("File");
		fileMenu.add(new JMenuItem(this.newDocumentAction));
		fileMenu.add(new JMenuItem(this.openDocumentAction));
		fileMenu.add(new JMenuItem(this.saveDocumentAction));
		fileMenu.add(new JMenuItem(this.saveAsDocumentAction));
		fileMenu.add(new JMenuItem(this.statsDocumentAction));
		fileMenu.add(new JMenuItem(this.closeDocumentAction));
		fileMenu.add(new JMenuItem(this.exitDocumentAction));
		
		menuBar.add(fileMenu);
		
		
		
		editMenu = new JMenu("Edit");
		editMenu.add(new JMenuItem(this.cutDocumentAction));
		editMenu.add(new JMenuItem(this.copyDocumentAction));
		editMenu.add(new JMenuItem(this.pasteDocumentAction));
		
		menuBar.add(editMenu);
		
		
		
		toolsMenu = new JMenu("Tools");
		
		changeCaseMenu = new JMenu("Change case");
		changeCaseMenu.add(new JMenuItem(this.toLowerCaseAction));
		changeCaseMenu.add(new JMenuItem(this.toUpperCaseAction));
		changeCaseMenu.add(new JMenuItem(this.invertCaseAction));
		toolsMenu.add(changeCaseMenu);
		
		sortMenu = new JMenu("Sort");
		sortMenu.add(this.sortAscendingAction);
		sortMenu.add(this.sortDescendingAction);
		toolsMenu.add(sortMenu);
		
		toolsMenu.add(this.uniqueAction);
		
		menuBar.add(toolsMenu);
		
		
		languagesMenu = new JMenu("Languages");
		languagesMenu.add(new JMenuItem(this.languagesEnglishAction));
		languagesMenu.add(new JMenuItem(this.languagesGermanAction));
		languagesMenu.add(new JMenuItem(this.languagesCroatianAction));
		
		menuBar.add(languagesMenu);
		
		topPanel.add(menuBar, BorderLayout.NORTH);
	}
	
	private ImageIcon getIcon(String name) {
		InputStream is = this.getClass().getResourceAsStream("icons/" + name + ".png");
		if(is == null) {
			return null;
		}
		try {
			byte[] bytes = is.readAllBytes();
			ImageIcon icon = new ImageIcon(bytes);
			Image image = icon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH);
			return new ImageIcon(newimg);
		} catch (IOException e) {
			return null;
		}
		
	}
	private void createToolBar() {
		JToolBar toolBar = new JToolBar("Tools");
		toolBar.setFloatable(true);
		
		buttonNew = new JButton(getIcon("new"));
		buttonNew.addActionListener(newDocumentAction);
		toolBar.add(buttonNew);
		
		buttonOpen = new JButton(getIcon("open"));
		buttonOpen.addActionListener(openDocumentAction);
		toolBar.add(buttonOpen);
		
		buttonSave = new JButton(getIcon("save"));
		buttonSave.addActionListener(saveDocumentAction);
		toolBar.add(buttonSave);
		
		buttonSaveAs = new JButton(getIcon("saveAs"));
		buttonSaveAs.addActionListener(saveAsDocumentAction);
		toolBar.add(buttonSaveAs);
		
		buttonStats = new JButton(getIcon("stats"));
		buttonStats.addActionListener(statsDocumentAction);
		toolBar.add(buttonStats);
		
		buttonClose = new JButton(getIcon("close"));
		buttonClose.addActionListener(closeDocumentAction);
		toolBar.add(buttonClose);
		
		toolBar.addSeparator();
		
		buttonCut = new JButton(getIcon("cut"));
		buttonCut.addActionListener(cutDocumentAction);
		toolBar.add(buttonCut);
		
		buttonCopy = new JButton(getIcon("copy"));
		buttonCopy.addActionListener(copyDocumentAction);
		toolBar.add(buttonCopy);
		
		buttonPaste = new JButton(getIcon("paste"));
		buttonPaste.addActionListener(pasteDocumentAction);
		toolBar.add(buttonPaste);
		
		updateTexts();
		topPanel.add(toolBar, BorderLayout.CENTER);
		
		
		LocalizationProvider.getInstance().addLocalizationListener(() -> updateTexts());
	}

	
	
	/**
	 * Stavlja akciju koja se obavi kada kliknemo X na prozoru. 
	 */
	public WindowAdapter closeWindow = new WindowAdapter(){
		public void windowClosing(WindowEvent e){
			closeWindow();
		}
	};
	
	
	/**
	 * Gasi sve tabove, provjerava jesu li spremljeni
	 */
	public void closeWindow() {
		if(multipleDocumentModel.closeAll()) {
			this.gotovo = true;
			dispose();
		};
	}
	
	public void updateTexts() {
		buttonNew.setToolTipText((String) this.newDocumentAction.getValue(Action.SHORT_DESCRIPTION));
		buttonOpen.setToolTipText((String) this.openDocumentAction.getValue(Action.SHORT_DESCRIPTION));
		buttonSave.setToolTipText((String) this.saveDocumentAction.getValue(Action.SHORT_DESCRIPTION));
		buttonSaveAs.setToolTipText((String) this.saveAsDocumentAction.getValue(Action.SHORT_DESCRIPTION));
		buttonStats.setToolTipText((String) this.statsDocumentAction.getValue(Action.SHORT_DESCRIPTION));
		buttonClose.setToolTipText((String) this.closeDocumentAction.getValue(Action.SHORT_DESCRIPTION));
		buttonCut.setToolTipText((String) this.cutDocumentAction.getValue(Action.SHORT_DESCRIPTION));
		buttonCopy.setToolTipText((String) this.copyDocumentAction.getValue(Action.SHORT_DESCRIPTION));
		buttonPaste.setToolTipText((String) this.pasteDocumentAction.getValue(Action.SHORT_DESCRIPTION));
		fileMenu.setText(LocalizationProvider.getInstance().getString("file"));
		editMenu.setText(LocalizationProvider.getInstance().getString("edit"));
		toolsMenu.setText(LocalizationProvider.getInstance().getString("tools"));
		changeCaseMenu.setText(LocalizationProvider.getInstance().getString("changeCase"));
		sortMenu.setText(LocalizationProvider.getInstance().getString("sort"));
		languagesMenu.setText(LocalizationProvider.getInstance().getString("languages"));
	}
	
	public void enableTools(boolean enabled) {
		this.saveDocumentAction.setEnabled(enabled);
		this.saveAsDocumentAction.setEnabled(enabled);
		this.statsDocumentAction.setEnabled(enabled);
		this.closeDocumentAction.setEnabled(enabled);
		this.pasteDocumentAction.setEnabled(enabled);
	}
	
	public void enableCopyCut(boolean enabled) {
		this.copyDocumentAction.setEnabled(enabled);
		this.cutDocumentAction.setEnabled(enabled);
		this.toLowerCaseAction.setEnabled(enabled);
		this.toUpperCaseAction.setEnabled(enabled);
		this.invertCaseAction.setEnabled(enabled);
		this.sortAscendingAction.setEnabled(enabled);
		this.sortDescendingAction.setEnabled(enabled);
		this.uniqueAction.setEnabled(enabled);
	}
	
	/**
	 * 
	 * Caret listener, provjerava jeli išta selectano
	 * te prema tome određuje jesu li akcije upaljene.
	 * 
	 * Mjenja vrijednosti u status baru.
	 * 
	 * @author Marko Brlek
	 *
	 */
	private class MyCaretListener implements CaretListener{

		@Override
		public void caretUpdate(CaretEvent e) {
			int dot = e.getDot(); //dot == position
			int mark = e.getMark();
			
			if(dot != mark) {
				enableCopyCut(true); //omoguci akcije
			}else {
				enableCopyCut(false); //nemoj omoguciti
			}
			
			JTextArea textArea = (JTextArea) e.getSource();
			int length = textArea.getText().length();
			
			int row = 1;
			for(char c : textArea.getText().substring(0, dot).toCharArray()) {
				if(c == '\n') {
					row++; //broji linije do mjesta gdje je caret
				}
			}
			
			List<Character> currentRow = new ArrayList<>();
			for(char c : textArea.getText().substring(0, dot).toCharArray()) {
				if(c == '\n') {
					currentRow.clear();
				}else {
					currentRow.add(c);
				}
			}
			//na kraju ovog for-a je su u listi svi znakovi od reda gdje se nalazi caret
			int col = currentRow.size()+1;
			
			JNotepadPP.this.length.setText("Length: " + length);
			JNotepadPP.this.row.setText("Ln: " + row);
			JNotepadPP.this.col.setText("Col: " + col);
			JNotepadPP.this.sel.setText("Sel: " + Math.abs(dot-mark));
			
			JNotepadPP.this.revalidate();
		}
	}
	
	/**
	 * Radi dretvu koja svaku sekundu ponovno crta JLabel koji prikazuje vrijeme.
	 * Provjerava jeli ugasen prozor da ne drzi EDT dretvu zivom.
	 */
	private Thread clockUpdate = new Thread(new Runnable(){

		@Override
		public void run() {
			while(!JNotepadPP.this.gotovo) {
				JNotepadPP.this.time.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
				JNotepadPP.this.revalidate();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ignorable) {}
			}
			
		}
	});
	

}
