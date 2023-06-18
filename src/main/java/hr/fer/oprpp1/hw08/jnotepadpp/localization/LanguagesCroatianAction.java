package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

public class LanguagesCroatianAction extends AbstractAction{
	
	private static final long serialVersionUID = 1L;
	

	
	public LanguagesCroatianAction() {
		
		this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("croatian"));
		this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("croatianDesc"));
		
		LocalizationProvider.getInstance().addLocalizationListener(() -> {
			this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("croatian"));
			this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("croatianDesc"));
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		LocalizationProvider.getInstance().setLanguage("hr");
	}
}
