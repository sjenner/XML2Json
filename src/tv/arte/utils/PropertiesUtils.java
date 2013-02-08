package tv.arte.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtils {
	/**
	 * Charge les propriétés contenues dans le fichier de paramétrage dont le
	 * nom est founi en paramètre
	 * 
	 * @param nomFichier
	 * @return
	 */
	public static Properties lectureProperties(boolean DEBUG, String nomFichier) {
		// Read properties file.
		Properties properties = new Properties();
		try {
			// DEBUG = true si on est en environnement de développement et false
			// si on est en environnement de production
			if (DEBUG) {
				// File file = new File("test.txt");
				// file.createNewFile();
				properties.load(new FileInputStream("src" + File.separator + "fr" + File.separator + "arte" + File.separator + "utils" + File.separator + nomFichier));
			} else
				properties.load(new FileInputStream("params" + File.separator + nomFichier));
		} catch (IOException e) {
			System.out.println("Problème de lecture du fichier de propriétés : " + e.getMessage());
			System.out.println("L'application va se terminer !!");
			System.exit(1);
		}
		return properties;
	}
}
