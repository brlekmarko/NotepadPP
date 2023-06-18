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

public class InvertCaseAction extends AbstractAction{

	private static final long serialVersionUID = 1L;

	
	private DefaultMultipleDocumentModel multipleDocumentModel;
	
	public InvertCaseAction(DefaultMultipleDocumentModel multipleDocumentModel) {
		this.multipleDocumentModel = multipleDocumentModel;
		this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("invertCase"));
		this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl I"));
		this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("invertCaseDesc"));
		
		LocalizationProvider.getInstance().addLocalizationListener(() -> {
			this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("invertCase"));
			this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("invertCaseDesc"));
		});
	}
	
	
	//kod sa stackoverflowa, izgleda dobro
	private static String reverseCase(String text){
	    char[] chars = text.toCharArray();
	    for (int i = 0; i < chars.length; i++)
	    {
	        char c = chars[i];
	        if (Character.isUpperCase(c))
	        {
	            chars[i] = Character.toLowerCase(c);
	        }
	        else if (Character.isLowerCase(c))
	        {
	            chars[i] = Character.toUpperCase(c);
	        }
	    }
	    return new String(chars);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.enabled) {
			SingleDocumentModel singleDocument = multipleDocumentModel.getCurrentDocument();
			
			JTextArea textArea = singleDocument.getTextComponent();
			
			String allText = textArea.getText();
			
			String selected = textArea.getSelectedText();
			String selectedInvert = reverseCase(selected);
			int selectedStartIndex = textArea.getSelectionStart();
			int selectedEndIndex = textArea.getSelectionEnd();
			
			textArea.setText(allText.substring(0, selectedStartIndex) + selectedInvert + allText.substring(selectedEndIndex));
		}
		
	}
}
