package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import hr.fer.oprpp1.hw08.jnotepadpp.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.interfaces.SingleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizationProvider;

public class ToUpperCaseAction extends AbstractAction{

	private static final long serialVersionUID = 1L;

	
	private DefaultMultipleDocumentModel multipleDocumentModel;
	
	public ToUpperCaseAction(DefaultMultipleDocumentModel multipleDocumentModel) {
		this.multipleDocumentModel = multipleDocumentModel;
		this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("upperCase"));
		this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl U"));
		this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("upperCaseDesc"));
		
		LocalizationProvider.getInstance().addLocalizationListener(() -> {
			this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("upperCase"));
			this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("upperCaseDesc"));
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.enabled) {
			SingleDocumentModel singleDocument = multipleDocumentModel.getCurrentDocument();
			
			JTextArea textArea = singleDocument.getTextComponent();
			
			String allText = textArea.getText();
			
			String selected = textArea.getSelectedText();
			String selectedUpper = selected.toUpperCase();
			int selectedStartIndex = textArea.getSelectionStart();
			int selectedEndIndex = textArea.getSelectionEnd();
			
			textArea.setText(allText.substring(0, selectedStartIndex) + selectedUpper + allText.substring(selectedEndIndex));
		}
		
	}
}
