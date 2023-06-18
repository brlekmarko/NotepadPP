package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import hr.fer.oprpp1.hw08.jnotepadpp.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.interfaces.SingleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizationProvider;

public class StatsDocumentAction extends AbstractAction{

	private static final long serialVersionUID = 1L;

	
	private DefaultMultipleDocumentModel multipleDocumentModel;
	private JFrame window;
	
	public StatsDocumentAction(DefaultMultipleDocumentModel multipleDocumentModel, JFrame window) {
		this.multipleDocumentModel = multipleDocumentModel;
		this.window = window;
		this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("stats"));
		this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl T"));
		this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("statsDesc"));
		
		LocalizationProvider.getInstance().addLocalizationListener(() -> {
			this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("stats"));
			this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("statsDesc"));
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.enabled) {
			SingleDocumentModel singleDocument = multipleDocumentModel.getCurrentDocument();
			
			String currentText = singleDocument.getTextComponent().getText();
			String currentTextNoWhitespaces = currentText.replaceAll("\\s", "");
			
			int characters = currentText.length();
			int charactersNoWhitespaces = currentTextNoWhitespaces.length();
			int rows = currentText.split("\n").length;
			
			JOptionPane.showMessageDialog(window, "This document has:\n" + characters + " characters\n"
										+ charactersNoWhitespaces + " non-whitespace characters\n"
										+ rows + " rows.");
		}
	}
}

