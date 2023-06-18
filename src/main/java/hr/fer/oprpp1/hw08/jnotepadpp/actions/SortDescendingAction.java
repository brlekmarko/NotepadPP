package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.Collator;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;

import hr.fer.oprpp1.hw08.jnotepadpp.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.interfaces.SingleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizationProvider;

public class SortDescendingAction  extends AbstractAction{
	
	private static final long serialVersionUID = 1L;

	
	private DefaultMultipleDocumentModel multipleDocumentModel;
	private Collator collator;
	
	public SortDescendingAction(DefaultMultipleDocumentModel multipleDocumentModel) {
		this.multipleDocumentModel = multipleDocumentModel;
		
		this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("sortDescending"));
		this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl alt D"));
		this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("sortDescendingDesc"));
		this.collator = LocalizationProvider.getInstance().getCollator();
		
		LocalizationProvider.getInstance().addLocalizationListener(() -> {
			this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("sortDescending"));
			this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("sortDescendingDesc"));
			this.collator = LocalizationProvider.getInstance().getCollator();
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.enabled) {
			SingleDocumentModel singleDocument = multipleDocumentModel.getCurrentDocument();
			
			JTextArea textArea = singleDocument.getTextComponent();
			
			String allText = textArea.getText();
			int startIndex = 0;
			int endIndex = 0;
			try {
				startIndex = textArea.getLineStartOffset(textArea.getLineOfOffset(textArea.getSelectionStart()));
				endIndex = textArea.getLineEndOffset(textArea.getLineOfOffset(textArea.getSelectionEnd()));
			} catch (BadLocationException ignorable) {
			}
			
			String selected = allText.substring(startIndex, endIndex);
			List<String> selectedSplitted = Arrays.asList(selected.split("\n"));
			selectedSplitted.sort(collator.reversed());
			
			String selectedSorted = "";
			for(String s : selectedSplitted) {
				selectedSorted+=s;
				selectedSorted+='\n';
			}
			
			textArea.setText(allText.substring(0, startIndex) + selectedSorted + allText.substring(endIndex));
		}
		
	}
}
