package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import hr.fer.oprpp1.hw08.jnotepadpp.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizationProvider;

public class CloseDocumentAction extends AbstractAction{
	
	private static final long serialVersionUID = 1L;
	
	
	private DefaultMultipleDocumentModel multipleDocumentModel;
	
	public CloseDocumentAction(DefaultMultipleDocumentModel multipleDocumentModel) {
		this.multipleDocumentModel = multipleDocumentModel;
		
		this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("close"));
		this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control W"));
		this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("closeDesc"));
		
		LocalizationProvider.getInstance().addLocalizationListener(() -> {
			this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("close"));
			this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("closeDesc"));
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.enabled) {
			multipleDocumentModel.closeDocument(multipleDocumentModel.getCurrentDocument());
		}
	}
}