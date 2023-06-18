package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.nio.file.Path;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import hr.fer.oprpp1.hw08.jnotepadpp.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizationProvider;

public class OpenDocumentAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	
	
	private DefaultMultipleDocumentModel multipleDocumentModel;
	private JFrame window;
	
	public OpenDocumentAction(DefaultMultipleDocumentModel multipleDocumentModel, JFrame window) {
		this.multipleDocumentModel = multipleDocumentModel;
		this.window = window;
		this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("openFile"));
		this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl O"));
		this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("openFileDesc"));
		
		LocalizationProvider.getInstance().addLocalizationListener(() -> {
			this.putValue(Action.NAME, LocalizationProvider.getInstance().getString("openFile"));
			this.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString("openFileDesc"));
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.enabled) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Open file");
			
			if(fileChooser.showOpenDialog(window) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			
			Path newPath = fileChooser.getSelectedFile().toPath();
			multipleDocumentModel.loadDocument(newPath);
		}
	}
}
