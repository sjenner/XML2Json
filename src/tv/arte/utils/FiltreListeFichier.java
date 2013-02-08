package tv.arte.utils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Permet de d�finir un filtre de recherche sur les fichiers contenus dans un r�pertoire pr�cis
 * @author Arte
 *
 */
public class FiltreListeFichier implements FilenameFilter {

	private String fichier;

	private String extension;

	/**
	 * Constructeur
	 * @param nom le pr�fixe que doit comporter le nom de fichier pour satisfaire le filtre
	 * @param extension l'extension que doit porter le nom de fichier pour satisfaire le filtre
	 */
	public FiltreListeFichier(String nom, String extension) {
		this.fichier = nom;
		this.extension = extension;
	}

	/**
	 * @param rep le r�pertoire � tester
	 * @param nom le nom du fichier � tester
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
