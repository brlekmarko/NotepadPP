package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import hr.fer.oprpp1.hw08.jnotepadpp.interfaces.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.interfaces.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.interfaces.SingleDocumentModel;

/**
 * 
 * Predstavlja tabbed pane na notepadu, sadrži više dokumenata u sebi.
 * 
 * @author Marko Brlek
 *
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel{


	private static final long serialVersionUID = 1L;
	
	private List<SingleDocumentModel> singleModels;
	private SingleDocumentModel currentSingleModel;
	private List<MultipleDocumentListener> listeners;
	private String title;
	private boolean enabledTools; //jesu li upaljene opcije save, save as...
	
	
	/**
	 * Dodaje change listener koji reagira na promjenu tabova.
	 */
	public DefaultMultipleDocumentModel() {
		this.singleModels = new ArrayList<>();
		this.listeners = new ArrayList<>();
		this.enabledTools = false;
		
		this.addMultipleDocumentListener(new DefaultMultipleDocumentListener(this));
		this.addChangeListener((event) -> {
			if(this.getSelectedIndex() == -1) {
				return;
			}
			this.listeners.forEach(listener -> listener.currentDocumentChanged(currentSingleModel, singleModels.get(this.getSelectedIndex())));
		});
	}
	
	
	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return singleModels.iterator();
	}

	@Override
	public JComponent getVisualComponent() {
		return this;
	}

	/**
	 * Novi dokument još nema path i tekst je prazan.
	 */
	@Override
	public SingleDocumentModel createNewDocument() {
		DefaultSingleDocumentModel newDocument = new DefaultSingleDocumentModel(null, "");
		
		this.listeners.forEach(listener -> listener.documentAdded(newDocument));

		return newDocument;
	}

	@Override
	public SingleDocumentModel getCurrentDocument() {
		return currentSingleModel;
	}

	/**
	 * Čita file koji poslali kao parametar te ako je file valjan stvara
	 * novi tab(SingleDocumentModel), postavlja mu path na ovaj path i 
	 * tekst na tekst dokumenta koji se nalazi na tom pathu 
	 */
	@Override
	public SingleDocumentModel loadDocument(Path path) {
		if(path == null) throw new IllegalArgumentException("Path cant be null");
		
		String text = "";
		
		if(!path.toFile().canRead()) {
			JOptionPane.showMessageDialog(DefaultMultipleDocumentModel.this, "File does not exist", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}else {
			
			if(findForPath(path) != null) {
				JOptionPane.showMessageDialog(DefaultMultipleDocumentModel.this, "File is already open, close it first.", "Error", JOptionPane.ERROR_MESSAGE);
				return null;
			}
			
			try {
				byte[] bytes = Files.readAllBytes(path);
				text = new String(bytes, StandardCharsets.UTF_8);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(DefaultMultipleDocumentModel.this, "Error reading file", "Error", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}
		
		DefaultSingleDocumentModel newDocument = new DefaultSingleDocumentModel(path, text);
		
		this.listeners.forEach(listener -> listener.documentAdded(newDocument));
		
		return newDocument;
	}

	/**
	 * Otvara file i omogucava odabir mjesta gdje ce se dokument spremiti.
	 * Ako se odabire postojeći file onda baca upozorenje, pita za nastavak.
	 * Ako se odabire file koji je već otvoren u drugom tabu baca upozorenje.
	 */
	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		
		File newFile = null;
		
		if(newPath == null) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Save file");
			if(fileChooser.showSaveDialog(DefaultMultipleDocumentModel.this) == JFileChooser.APPROVE_OPTION) {
				newFile = fileChooser.getSelectedFile();
				
				if(findForPath(newFile.toPath()) != null) {
					JOptionPane.showMessageDialog(DefaultMultipleDocumentModel.this, "File is already open, close it first.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(newFile.exists()) {
					int result = JOptionPane.showConfirmDialog(DefaultMultipleDocumentModel.this, "File already exists. Override?", "Warning", JOptionPane.WARNING_MESSAGE);
					if(result != 0) {
						return;
					}else {
						newPath = newFile.toPath();
					}
				}else {
					newPath = newFile.toPath();
				}
			}else {
				return;
			}
		}
			
		
		String text = model.getTextComponent().getText();
		
		try {
			Files.write(newPath, text.getBytes(StandardCharsets.UTF_8));
		}catch (IOException ex){
			JOptionPane.showMessageDialog(DefaultMultipleDocumentModel.this, "Error saving file", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		model.setFilePath(newPath);
		this.setTitleAt(this.getSelectedIndex(), newPath.getFileName().toString());
		this.setToolTipTextAt(this.getSelectedIndex(), newPath.toString());
		
		this.setNewModel(model, false);
		
		this.listeners.forEach(listener -> {
			listener.currentDocumentChanged(model, model);
		});
		
	}

	/**
	 * Zatvara trenutno otvoreni dokument.
	 * Ako nije spremljen baca upozorenje i pita za nastavak.
	 */
	@Override
	public void closeDocument(SingleDocumentModel model) {
		
		if(model.isModified()) {
			int result = JOptionPane.showConfirmDialog(DefaultMultipleDocumentModel.this, "File not saved. Continue?", "Warning", JOptionPane.WARNING_MESSAGE);
			if(result != 0) {
				return;
			}
		}
		
		this.listeners.forEach(listener -> {
			listener.documentRemoved(model);
		});
	}
	

	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		this.listeners.add(l);
	}

	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		this.listeners.remove(l);
	}

	@Override
	public int getNumberOfDocuments() {
		return this.singleModels.size();
	}

	@Override
	public SingleDocumentModel getDocument(int index) {
		return this.singleModels.get(index);
	}

	
	@Override
	public SingleDocumentModel findForPath(Path path) {
		if(path == null) throw new IllegalArgumentException("Path cant be null");
		
		for(SingleDocumentModel model : this.singleModels) {
			if(model.getFilePath() != null && model.getFilePath().equals(path)) {
				return model;
			}
		}

		return null;
	}
	

	@Override
	public int getIndexOfDocument(SingleDocumentModel doc) {
		return this.singleModels.indexOf(doc);
	}
	
	
	
	
	
	public String getTitle() {
		return this.title;
	}
	
	
	/**
	 * Stavlja naziv prozora na {imeDatoteke} - JNotepad++
	 * Ako datoteka još nema naziv, onda unnamed
	 */
	public void setTitle() {
		if(currentSingleModel != null) {
			if(currentSingleModel.getFilePath() != null) {
				this.title = currentSingleModel.getFilePath().getFileName().toString() + " - JNotepad++";
			}else {
				this.title = "(unnamed) - JNotepad++";
			}
		}else {
			this.title = "JNotepad++";
		}
	}
	
	
	
	public boolean getEnabledTools() {
		return this.enabledTools;
	}
	
	
	
	public void setEnabledTools(boolean enabledTools) {
		this.enabledTools = enabledTools;
	}
	
	
	
	/**
	 * Zatvara sve dokumente.
	 * Ako neki dokumenti nisu spremljeni onda baca upozorenje i 
	 * pita za nastavak jedan po jedan
	 * 
	 * @return true ako dozvoljeno gašenje, false inace
	 */
	public boolean closeAll() {
		
		if(this.currentSingleModel != null) {
			for(MultipleDocumentListener listener : listeners) {
				listener.currentDocumentChanged(singleModels.get(this.getSelectedIndex()), singleModels.get(0));
			}
		}
		
		SingleDocumentModel model;
		
		while(singleModels.size() > 0) {
			model = singleModels.get(0);
			if(model.isModified()) {
				int result = JOptionPane.showConfirmDialog(DefaultMultipleDocumentModel.this, "File not saved. Continue?", "Warning", JOptionPane.WARNING_MESSAGE);
				if(result != 0) {
					return false;
				}
			}
			for(MultipleDocumentListener listener : listeners) {
				listener.documentRemoved(model);
			}
		}
		return true;
	}
	
	
	
	/**
	 * Postavlja ikonicu taba na ikonicu dokumenta
	 */
	private void setIcon() {
		if(this.getIconAt(singleModels.indexOf(currentSingleModel)) != ((DefaultSingleDocumentModel) currentSingleModel).getIcon()) {
			DefaultMultipleDocumentModel.this.setIconAt(singleModels.indexOf(currentSingleModel), ((DefaultSingleDocumentModel) currentSingleModel).getIcon());
		}
	}
	
	
	
	
	/**
	 * Otvara novi tab, miče listenere sa prošlog i stavlja ih na novi
	 * Postavlja novi naslov dokumenta i ikonicu.
	 * 
	 * @param newDocument
	 * @param modified
	 */
	private void setNewModel(SingleDocumentModel newDocument, boolean modified) {
		removeCurrentListeners();
		currentSingleModel = newDocument;
		setTitle();
		addCurrentListeners();
		newDocument.setModified(modified);
		this.setIcon();
		
		this.setSelectedIndex(this.getIndexOfDocument(newDocument));
	}
	
	
	//novi KeyListener, na svaki klik provjerava treba li mjenjati ikonicu
	//ovo vjerojatno može bolje
	private KeyListener defaultKeyListener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) {
			setIcon();
		}
		@Override
		public void keyPressed(KeyEvent e) {
			setIcon();
		}
		@Override
		public void keyReleased(KeyEvent e) {
			setIcon();
		}	
	};
	
	
	private void removeCurrentListeners() {
		if(currentSingleModel != null) {
			currentSingleModel.getTextComponent().removeKeyListener(defaultKeyListener);
		}
	}
	
	private void addCurrentListeners() {
		if(currentSingleModel != null) {
			currentSingleModel.getTextComponent().addKeyListener(defaultKeyListener);
		}
	}
	
	
	
	
	
	
	/**
	 * Listener koji reagira na mjenjanje, dodavanje i micanje tabova.
	 * 
	 * @author Marko Brlek
	 *
	 */
	private static class DefaultMultipleDocumentListener implements MultipleDocumentListener{
		
		private DefaultMultipleDocumentModel multipleDocumentModel;
		
		public DefaultMultipleDocumentListener(DefaultMultipleDocumentModel multipleDocumentModel) {
			this.multipleDocumentModel = multipleDocumentModel;
		}

		/**
		 * Miče listenere sa prošlog, stavlja ih na novi i otvara tab, mjenja naslov
		 */
		@Override
		public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
			multipleDocumentModel.removeCurrentListeners();
			multipleDocumentModel.currentSingleModel = currentModel;
			multipleDocumentModel.addCurrentListeners();
			multipleDocumentModel.setSelectedIndex(multipleDocumentModel.getIndexOfDocument(currentModel));
			multipleDocumentModel.setTitle();
		}

		/**
		 * Mjenja enabledTools u true, omogućuje spremanje datoteke itd.
		 * Postavlja naslov i zove setNewModel metodu.
		 * 
		 */
		@Override
		public void documentAdded(SingleDocumentModel model) {
			multipleDocumentModel.singleModels.add(model);
			multipleDocumentModel.setEnabledTools(true);
			
			if(model.getFilePath() == null) {
				multipleDocumentModel.addTab("(unnamed)", model.getTextComponent());
				multipleDocumentModel.setToolTipTextAt(multipleDocumentModel.getIndexOfDocument(model), "File does not have a name yet");
				multipleDocumentModel.setNewModel(model, true);
			}else {
				multipleDocumentModel.addTab(model.getFilePath().getFileName().toString(), model.getTextComponent());
				multipleDocumentModel.setToolTipTextAt(multipleDocumentModel.getIndexOfDocument(model), model.getFilePath().toString());
				multipleDocumentModel.setNewModel(model, false);
			}
		}

		/**
		 * Zatvara trenutni tab te provjerava koji treba otvoriti umjesto njega
		 */
		@Override
		public void documentRemoved(SingleDocumentModel model) {
			
			multipleDocumentModel.removeTabAt(multipleDocumentModel.getSelectedIndex());
			multipleDocumentModel.singleModels.remove(model);
			
			if(model == multipleDocumentModel.currentSingleModel) {
				if(multipleDocumentModel.singleModels.size() == 0) {
					multipleDocumentModel.currentSingleModel = null;
					multipleDocumentModel.setTitle();
					multipleDocumentModel.setEnabledTools(false);
				}
				else {
					for(MultipleDocumentListener listener : multipleDocumentModel.listeners) {
						listener.currentDocumentChanged(model, multipleDocumentModel.singleModels.get(0));
					}
				}
			}
			
		}

	}
}
