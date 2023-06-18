package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import hr.fer.oprpp1.hw08.jnotepadpp.JNotepadPP;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizationProvider;

public class ExitDocumentAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	
	
	private JNotepadPP window;
	
	public ExitDocumentAction(JNotepadPP window) {
		this.window = window;
		this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("exit"));
		this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt F4"));
		this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("exitDesc"));
		
		LocalizationProvider.getInstance().addLocalizationListener(() -> {
			this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("exit"));
			this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("exitDesc"));
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.enabled) {
			window.closeWindow();
		}
	}
}

