package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import hr.fer.oprpp1.hw08.jnotepadpp.interfaces.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.interfaces.SingleDocumentModel;


/**
 * Predstavlja jedan tab u notepadu
 * 
 * @author Marko Brlek
 *
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel{

	private Path file;
	private String textContent;
	private JTextArea textArea;
	private boolean modified;
	private List<SingleDocumentListener> listeners;
	private ImageIcon icon;
	
	
	
	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}
	

	/**
	 * Kreira novi tab, "otvara" jedan dokument
	 * Postavlja na njega novi document listener.
	 * 
	 * @param filePath gdje se file nalazi na disku
	 * @param textContent sto pise u datoteci
	 */
	public DefaultSingleDocumentModel(Path filePath, String textContent){
		this.file = filePath;
		this.textContent = textContent;
		this.modified = false;
		this.listeners = new ArrayList<>();
		try {
			setIcon("saved");
		} catch (IOException ignorable) {}
				
		this.textArea = new JTextArea(textContent);
		this.textArea.getDocument().addDocumentListener(new DocumentListener() {

			public void insertUpdate(DocumentEvent e) {
				setModified(true);
			}

			public void removeUpdate(DocumentEvent e) {
				setModified(true);
			}

			public void changedUpdate(DocumentEvent e) {
				setModified(true);
			}
			
		});
		
		this.addSingleDocumentListener(new DefaultSingleDocumentListener());
	}
	
	public JTextArea getTextComponent() {
		return this.textArea;
	}

	public Path getFilePath() {
		return this.file;
	}

	public void setFilePath(Path path) {
		this.file = path;
		this.listeners.forEach(listener -> listener.documentFilePathUpdated(this));
	}

	public boolean isModified() {
		return this.modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;	
		this.listeners.forEach(listener -> listener.documentModifyStatusUpdated(this));
	}

	public void addSingleDocumentListener(SingleDocumentListener l) {
		this.listeners.add(l);
	}

	public void removeSingleDocumentListener(SingleDocumentListener l) {
		this.listeners.remove(l);
	}
	
	public Icon getIcon() {
		return this.icon;
	}
	
	/**
	 * Postavlja ikonu na crvenu ili zelenu, ovisno o parametru status.
	 * 
	 * @param status saved ili unsaved
	 * @throws IOException
	 */
	public void setIcon(String status) throws IOException {
		if(status.equals("saved")) {
			InputStream is = this.getClass().getResourceAsStream("icons/saved.png");
			if(is == null) {
				throw new IOException("Icon does not exist");
			}
			byte[] bytes = is.readAllBytes();
			this.icon = new ImageIcon(bytes);
			Image image = icon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH);
			this.icon = new ImageIcon(newimg);
		}
		else if(status.equals("unsaved")) {
			InputStream is = this.getClass().getResourceAsStream("icons/unsaved.png");
			if(is == null) {
				throw new IOException("Icon does not exist");
			}
			byte[] bytes = is.readAllBytes();
			this.icon = new ImageIcon(bytes);
			Image image = icon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH);
			this.icon = new ImageIcon(newimg);
		}
		else {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Predstavlja document listener, kada se modificira tekst mjenja se ikonica.
	 * 
	 * @author Marko Brlek
	 *
	 */
	private static class DefaultSingleDocumentListener implements SingleDocumentListener{

		@Override
		public void documentModifyStatusUpdated(SingleDocumentModel model) {
			DefaultSingleDocumentModel defaultModel = (DefaultSingleDocumentModel) model;
			try {
				if(model.isModified()) {
					defaultModel.setIcon("unsaved");
				}else{
					defaultModel.setIcon("saved");
				}
			} catch(Exception ignorable) {}
			
		}

		@Override
		public void documentFilePathUpdated(SingleDocumentModel model) {
			//funkcionira i bez ovog, vjerojatno je zato kod ružan
			
		}

	}

}
