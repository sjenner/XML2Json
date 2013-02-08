package tv.arte.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FichierUtils {
	
	public static String CARIAGE_RETURN = "\n";
	public static String SEPARATEUR = "===========================================================================================" + CARIAGE_RETURN;
	public static String ESPACE = "\t";
	public static int SANS_SEPARATEUR = 0;
	public static int SEPARATEUR_AVANT = 1;
	public static int SEPARATEUR_APRES = 2;

	/**
	 * Copie d'un fichier 
	 * 
	 * @param ancien nom
	 * @param nouveau nom
	 * @return true si tout s'est bien passé et false sinon
	 * @throws Exception
	 */
	public static boolean copieFichier(String ancien, String nouveau)
			throws Exception {
		boolean succes = false;
		String read;
		String write = "";
		BufferedReader entree = new BufferedReader(new FileReader(ancien));

		while ((read = entree.readLine()) != null) {
			write += read + "\n";
		}
		entree.close();
		BufferedWriter sortie = new BufferedWriter(new FileWriter(nouveau));
		sortie.write(write);
		sortie.close();

		try {
			File file = new File(nouveau);
			if (file.exists())
				succes = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return succes;
	}

	/**
	 * Ajoute du texte à la suite d'un fichier
	 * 
	 * @param nomFichier nom du fichier
	 * @param message texte à ajouter
	 * @param separateur valeur qui détermine la présence ou non et la position d'un éventuel séparateur tel que :
	 * 		- SANS_SEPARATEUR : pas de déparateur
	 * 	 	- SEPARATEUR_AVANT : séparateur avant le texte à ajouter
	 * 		- SEPARATEUR_APRES : séparateur après le texte à ajouter
	 */
	public static void append2Fichier(String nomFichier, String message, int separateur) {
		append2Fichier(nomFichier, message, null, null, separateur);
	}	

	/**
	 * Ajoute du texte à la suite d'un fichier et spécifie l'exception qui est survenue et qu'il faut stocker dans le fichier
	 * 
	 * @param nomFichier nom du fichier
	 * @param message texte à ajouter
	 * @param exception exception à ajouter
	 * @param separateur valeur qui détermine la présence ou non et la position d'un éventuel séparateur tel que :
	 * 		- SANS_SEPARATEUR : pas de déparateur
	 * 	 	- SEPARATEUR_AVANT : séparateur avant le texte à ajouter
	 * 		- SEPARATEUR_APRES : séparateur après le texte à ajouter
	 */
	public static void append2Fichier(String nomFichier, String message, Exception exception, int separateur) {
		append2Fichier(nomFichier, message, null, exception, separateur);
	}
	
	/*
	 * Methode privée qui réalise effectivement le tracage dans le fichier spécifié
	 * 
	 * @param nomFichier le nom du fichier
	 * @param message le message
	 * @param nomFlux le nom du type de flux lorsqu'il y en a un
	 * @param exception l'exception lorsqu'il y en a une
	 * @param separateur valeur qui détermine la présence ou non et la position d'un éventuel séparateur tel que :
	 * 		- SANS_SEPARATEUR : pas de déparateur
	 * 	 	- SEPARATEUR_AVANT : séparateur avant le texte à ajouter
	 * 		- SEPARATEUR_APRES : séparateur après le texte à ajouter
	 */
	private static void append2Fichier(String nomFichier, String message, String nomFlux, Exception exception, int separateur) {
		try {
			FileWriter fstream = new FileWriter(nomFichier, true);
			BufferedWriter out = new BufferedWriter(fstream);
			if (separateur == FichierUtils.SEPARATEUR_AVANT)
				out.write(SEPARATEUR);
			String time = TempsUtils.getTempsActuel();			
			out.write("" + time + " : " + message + CARIAGE_RETURN);
			if (exception != null)
				out.write(CARIAGE_RETURN + exception.getMessage() + CARIAGE_RETURN);
			if (separateur == FichierUtils.SEPARATEUR_APRES)
				out.write(SEPARATEUR);
			out.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	/**
	 * Supprime physiquement le fichier d'erreurs 
	 * @param nomFichier
	 */
	public static void supprimeFichierErreur(String nomFichier) {
		File fichier = new File(nomFichier);
		if (fichier.exists()) {
			fichier.delete();
		}
	}

	/**
	 * Realise l'extraction de données contenues dans la balise Item encadrées par la balise Date 
	 * 
	 * @param absPath le path absolu vers le fichier à traiter
	 * @param baliseItem la balise de niveau "Item" qui contient la balise de date
	 * @param baliseDate la balise contenant la dte à comparer
	 */
	public static boolean extractionDonneesJourCourant(String absPath, String baliseItem, String baliseDate) {
		boolean extraites = false;
		
		
		
		return extraites;
	}
	
	/**
	 * Renomme le fichier d'erreurs pour conserver l'historique des problèmes survenus
	 * @param nomFichier
	 */
//	public static void renommeFichierErreur(String nomFichier) {
//		File fichier = new File(nomFichier);
//		if (fichier.exists()) {
//			if (fichier.renameTo(new File(nomFichier + TempsUtils.getTempsActuel())))
//				System.out.println("fichier:" + fichier.getAbsolutePath());
//			else
//				System.out.println(" cha a pas marché ...");
//		}	
//	}	
}
