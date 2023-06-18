package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import hr.fer.oprpp1.hw08.jnotepadpp.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizationProvider;

public class NewDocumentAction extends AbstractAction{
	
	private static final long serialVersionUID = 1L;
	
	
	private DefaultMultipleDocumentModel multipleDocumentModel;
	
	public NewDocumentAction(DefaultMultipleDocumentModel multipleDocumentModel) {
		this.multipleDocumentModel = multipleDocumentModel;
		this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("newFile"));
		this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
		this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("newFileDesc"));
		
		LocalizationProvider.getInstance().addLocalizationListener(() -> {
			this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("newFile"));
			this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("newFileDesc"));
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.enabled) {
			multipleDocumentModel.createNewDocument();
		}
	}
}
