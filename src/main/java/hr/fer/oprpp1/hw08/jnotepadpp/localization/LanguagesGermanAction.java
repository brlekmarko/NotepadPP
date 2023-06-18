package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

public class LanguagesGermanAction extends AbstractAction{
	
	private static final long serialVersionUID = 1L;
	

	
	public LanguagesGermanAction() {
		
		this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("german"));
		this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_G);
		this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("germanDesc"));
		
		LocalizationProvider.getInstance().addLocalizationListener(() -> {
			this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("german"));
			this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("germanDesc"));
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		LocalizationProvider.getInstance().setLanguage("de");
	}
}
