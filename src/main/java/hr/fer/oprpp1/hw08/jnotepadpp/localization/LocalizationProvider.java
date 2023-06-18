package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import java.text.Collator;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationProvider extends AbstractLocalizationProvider{
	private String language;
	private ResourceBundle bundle;
	private Collator collator;
	private static final LocalizationProvider provider = new LocalizationProvider();
	
	private LocalizationProvider() {
		this.language = "en";
		bundle = ResourceBundle.getBundle("hr.fer.oprpp1.hw08.jnotepadpp.translations.translations", Locale.forLanguageTag("en"));
		collator = Collator.getInstance(bundle.getLocale());
	};
	
	public static LocalizationProvider getInstance() {
		return provider;
	}
	
	public void setLanguage(String lang) {
		this.language = lang;
		this.bundle = ResourceBundle.getBundle("hr.fer.oprpp1.hw08.jnotepadpp.translations.translations", Locale.forLanguageTag(lang));
		collator = Collator.getInstance(bundle.getLocale());
		super.fire();
	}
	
	public String getLanguage() {
		return this.language;
	}
	
	public String getString(String key) {
		return this.bundle.getString(key);
	}
	
	public Collator getCollator() {
		return this.collator;
	}
}
