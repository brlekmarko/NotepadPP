package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;


public class LanguagesEnglishAction extends AbstractAction{
	
	private static final long serialVersionUID = 1L;
	

	
	public LanguagesEnglishAction() {
		
		this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("english"));
		this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
		this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("englishDesc"));
		
		LocalizationProvider.getInstance().addLocalizationListener(() -> {
			this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("english"));
			this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("englishDesc"));
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		LocalizationProvider.getInstance().setLanguage("en");
	}
}
