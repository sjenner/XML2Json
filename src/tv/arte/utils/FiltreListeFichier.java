package tv.arte.utils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Permet de définir un filtre de recherche sur les fichiers contenus dans un répertoire précis
 * @author Arte
 *
 */
public class FiltreListeFichier implements FilenameFilter {

	private String fichier;

	private String extension;

	/**
	 * Constructeur
	 * @param nom le préfixe que doit comporter le nom de fichier pour satisfaire le filtre
	 * @param extension l'extension que doit porter le nom de fichier pour satisfaire le filtre
	 */
	public FiltreListeFichier(String nom, String extension) {
		this.fichier = nom;
		this.extension = extension;
	}

	/**
	 * @param rep le répertoire à tester
	 * @param nom le nom du fichier à tester
	 */
	@Override
	public boolean accept(File rep, String nom) {
		boolean fileOK = true;

		if ((nom != null) && (nom.length() > 0)) {
			fileOK &= nom.startsWith(fichier);
		}

		if (extension != null) {
			fileOK &= nom.endsWith('.' + extension);
		}
		return fileOK;
	}
}
