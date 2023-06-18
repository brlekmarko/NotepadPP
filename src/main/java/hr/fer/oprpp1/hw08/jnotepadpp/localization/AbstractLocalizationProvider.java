package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import java.util.ArrayList;
import java.util.List;

public class AbstractLocalizationProvider implements ILocalizationProvider{
	
	private List<ILocalizationListener> listeners;
	
	public AbstractLocalizationProvider() {
		listeners = new ArrayList<>();
	}
	
	@Override
	public void addLocalizationListener(ILocalizationListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener listener) {
		listeners.remove(listener);
		
	}
	
	public void fire() {
		for(ILocalizationListener listener : listeners) {
			listener.localizationChanged();
		}
	}

	@Override
	public String getString(String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
