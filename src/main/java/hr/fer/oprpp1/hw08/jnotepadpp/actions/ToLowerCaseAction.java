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

public class ToLowerCaseAction extends AbstractAction{

	private static final long serialVersionUID = 1L;

	
	private DefaultMultipleDocumentModel multipleDocumentModel;
	
	public ToLowerCaseAction(DefaultMultipleDocumentModel multipleDocumentModel) {
		this.multipleDocumentModel = multipleDocumentModel;
		this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("lowerCase"));
		this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl L"));
		this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("lowerCaseDesc"));
		
		LocalizationProvider.getInstance().addLocalizationListener(() -> {
			this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("lowerCase"));
			this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("lowerCaseDesc"));
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.enabled) {
			SingleDocumentModel singleDocument = multipleDocumentModel.getCurrentDocument();
			
			JTextArea textArea = singleDocument.getTextComponent();
			
			String allText = textArea.getText();
			
			String selected = textArea.getSelectedText();
			String selectedLower = selected.toLowerCase();
			int selectedStartIndex = textArea.getSelectionStart();
			int selectedEndIndex = textArea.getSelectionEnd();
			
			textArea.setText(allText.substring(0, selectedStartIndex) + selectedLower + allText.substring(selectedEndIndex));
		}
		
	}
}