package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import hr.fer.oprpp1.hw08.jnotepadpp.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.interfaces.SingleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizationProvider;

public class CutDocumentAction extends AbstractAction{

	private static final long serialVersionUID = 1L;

	
	private DefaultMultipleDocumentModel multipleDocumentModel;
	
	public CutDocumentAction(DefaultMultipleDocumentModel multipleDocumentModel) {
		this.multipleDocumentModel = multipleDocumentModel;
		this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("cut"));
		this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl X"));
		this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("cutDesc"));
		
		LocalizationProvider.getInstance().addLocalizationListener(() -> {
			this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("cut"));
			this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("cutDesc"));
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.enabled) {
			SingleDocumentModel singleDocument = multipleDocumentModel.getCurrentDocument();
			singleDocument.getTextComponent().cut();
		}
		
	}
}