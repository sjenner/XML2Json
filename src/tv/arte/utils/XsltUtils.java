package tv.arte.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XsltUtils {
	
	private static final boolean DEBUG_ENABLED = false;

	private static final String BALISE_OUVRANTE_DESC = "\"description\":\"";

	private static final String BALISE_FERMANTE_DESC = "FINDESCRIPTION";
	
	private static final String BALISE_OUVRANTE_TITLE = "\"title\":\"";

	private static final String BALISE_FERMANTE_TITLE = "FINTITLE";

	// cette constante ne sert que pour implémenter une astuce de codage !! 
	private static final String DOUBLE_QUOTE = "\"";
	
	// cette constante ne sert que pour implémenter une astuce de codage !!
	private static final String VIRGULE = ",";
	
	private static final String MOTIF_A_REMPLACER = "\"";
	
	private static final String MOTIF_DE_REMPLACEMENT = "\\\"";
	
	private static final String DIRECTIVE_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	
	private static final String VIRGULE_FIN_DE_FICHIER = "},]}" ;
	
	private static final String FIN_DE_FICHIER_CORRECTE = "} ]}" ;

	private static final String BALISE_ITEM = "\"Item\": {";
	
	private static final String CR = "\n";
	

//	public static void main(String[] argv) {
//		// File ficherXml = new File(RSS_FEED_FILE_PATH);
//		try {
//			// XsltUtils.verifieFichiers();
//			XsltUtils xsltUtils = new XsltUtils();
//			xsltUtils.traitementXslt();
//			xsltUtils.regulariseUrl();
//			xsltUtils.regulariseFichier();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println("Terminé");
//	}

//	/**
//	 * Permet de vérifier l'existence d'un fichier
//	 */
//	private void verifieFichiers(String fichierXslt) {
//		try {
//			System.out.println("Test d'existence du fichier :" + fichierXslt);
//			File file = new File(fichierXslt);
//			if (file.exists())
//				System.out.println("Le fichier existe bel et bien");
//			else
//				System.out.println("Le fichier n'existe pas");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * Generation du fichier Json a partir d'un fichier XML présent sur la machine
	 * @param fichierXslt fichier contenant les transformations XSLT à appliquer
	 * @param fichierXml fichier XML source
	 * @param fichierJson fichier produit intermédiaire
	 * @param fichierJsonFinal ficheir JSON final 
	 * @throws TransformerConfigurationException
	 * @throws TransformerException
	 */
	public static void generationFichierJson(String fichierXslt, String fichierXml, String fichierJson) throws TransformerConfigurationException, TransformerException {
		XsltUtils xsltUtils = new XsltUtils();
		xsltUtils.traitementXslt(fichierXslt, fichierXml, fichierJson);
		xsltUtils.regulariseUrl(fichierJson);
		xsltUtils.regulariseFichier(fichierJson);
	}

	/**
	 * Réalise une transformation XSL sur un fichier XML afin de produire un fichier JSON ayant la même
	 * structure et les mêmes informations
	 * 
	 * @throws TransformerException
	 * @throws TransformerConfigurationException
	 */
	private void traitementXslt(String fichierXslt, String fichierXml, String fichierJson) throws TransformerException,
			TransformerConfigurationException {

		// transformation du flux XML en texte
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer(new StreamSource(fichierXslt));
		Properties properties = transformer.getOutputProperties();
		properties.put(OutputKeys.METHOD, "xml");
		transformer.transform(new StreamSource(fichierXml),	new StreamResult(fichierJson));
	}

	/**
     * Parcourt un fichier JSON ligne par ligne pour réaliser "l'escapation" des doubles-quotes
     */
	private void regulariseUrl(String fichierJson) {
		String read;
		String write = "";
		if (DEBUG_ENABLED) {
			System.out.println("<XsltUtils>--regulariseUrl(...)-- [IN]");
		}
		try {
			BufferedReader entree = new BufferedReader(new FileReader(fichierJson));
			read = entree.readLine();

			while (read != null) {
				if (read.indexOf(BALISE_OUVRANTE_DESC) >= 0)
					read = regulariseLigne(read, BALISE_OUVRANTE_DESC, BALISE_FERMANTE_DESC);				
				if (read.indexOf(BALISE_OUVRANTE_TITLE) >= 0)
					read = regulariseLigne(read, BALISE_OUVRANTE_TITLE, BALISE_FERMANTE_TITLE);
				write += read + "\n";
				read = entree.readLine();
			}
			entree.close();
			BufferedWriter sortie = new BufferedWriter(new FileWriter(
					fichierJson));
			sortie.write(write);
			sortie.close();

		} catch (Exception e) {
			System.out.println("<XsltUtils>--regulariseUrl(...)"
					+ e.getMessage());
		}
		if (DEBUG_ENABLED) {
			System.out.println("<XsltUtils>--regulariseUrl(...)-- [OUT]");
		}
	}

	/**
	 * Extrait les données contenus entre deux balises : <TRAVAILLEURL> et </TRAVAILLEURL> afin d'en "escaper"
	 * toutes les occurences de doubles-quotes
	 *  
	 * ATTENTION : ne supprime pas les balises qui ont servi à réaliser les transformations 
	 *  
	 * @param read une chaine de caractère (une ligne d'un fichier par ex)
	 * @param baliseOuvrante la balise ouvrante à rechercher pour le début des régularisations à appliquer
	 * @param baliseFermante la balise fermante à rechercher pour la fin des régularisations à appliquer
	 * @return la chaîne dont toutes les doubles-quotes ont été "escapées"
	 */
	private String regulariseLigne(String read, String baliseOuvrante, String baliseFermante) {
		int debutBalise = 0;
		int debutChaine = 0;
		int finChaine = 0;
		boolean doubleQuote = false;
		String nouvelleLigne = "";

		// astuce de codage
		if (read.endsWith(DOUBLE_QUOTE)) {
			read = read.substring(0, read.length() - 1);
			doubleQuote = true;
		}
		
		debutBalise = read.indexOf(baliseOuvrante);
		debutChaine = debutBalise + baliseOuvrante.length();
		if (read.indexOf(baliseFermante) >= 0)
			finChaine = read.indexOf(baliseFermante) - 1;
		else			
			finChaine = read.length();
		
		if (debutBalise < 0) {
			debutBalise = 0;
			debutChaine = 0;
		}

		try {
			String chaine = read.substring(debutChaine, finChaine);
			chaine = escapeDoubleQuotes(chaine);
			if (finChaine == read.length()) {
				nouvelleLigne = read.substring(0, debutChaine)
						+ chaine;
			}
			else {
				nouvelleLigne = read.substring(0, debutChaine)
					+ chaine
					+ read.substring(finChaine + baliseFermante.length(),
							read.length());
			}
			// si on a supprimé la double quote avant il faut la remettre
			if (doubleQuote)
				nouvelleLigne += DOUBLE_QUOTE;
			nouvelleLigne += CR;
		} catch (Exception e) {
			System.out.println("<XsltUtils>--regulariseUrl(chaine) : " + e.getMessage());
		}

		return nouvelleLigne;
	}

	/**
	 * Réalise "l'escapation" (escape) d'une chaîne entière délimitée par des doubles quotes : 
	 * ("..." devient : \"...\" ) pour être compatible json (limitations jscript)
	 * 
	 * @param chaine la chaine dans laquelle escaper les doubles-quotes
	 * @return la chaine précédente dans laquelle des backslashes ont été placés avant chaque occurence de double-quotes
	 */
	private String escapeDoubleQuotes(String chaine) {
		int indice1 = chaine.indexOf(MOTIF_A_REMPLACER);
		String nouvelleChaine = "";		
		
		if (indice1 >= 0) {
			nouvelleChaine = chaine.substring(0, indice1) + 
							MOTIF_DE_REMPLACEMENT + 
							escapeDoubleQuotes(chaine.substring(indice1 + MOTIF_A_REMPLACER.length(), chaine.length()));
		} else {
				nouvelleChaine = chaine;
		}

		return nouvelleChaine;
	}
	
	/*
	 * Permet de supprimer les en-têtes de fichier (déclaration XML) en trop et les caractères induits par la 
	 * transformation XSL (une virgule pour le dernier item et la chaine de caractères : FINDESCRIPTION remplacée par un retour à la ligne)
	 */
	private void regulariseFichier(String fichierJsonFinal) {
		String read;
		String write = "";
		int indiceBaliseItem = 0;
		int indiceBaliseFermante = 0;
		if (DEBUG_ENABLED) {
			System.out.println("<XsltUtils>--regulariseFichier(...)-- [IN]");
		}
		try {
			BufferedReader entree = new BufferedReader(new FileReader(fichierJsonFinal));
			read = entree.readLine();

			// traitement de début de fichier : on supprime la directive XML
			if ((read != null) && (read.indexOf(DIRECTIVE_XML) >= 0)) 
				read = read.substring(DIRECTIVE_XML.length(), read.length());
			
			while (read != null) {
				// traitement de confort de lecture : on met tous les nouveaux Items en début de ligne
				if ((indiceBaliseItem = read.indexOf(BALISE_ITEM)) >= 0) {
					read = read.substring(0, indiceBaliseItem) + "\n\n" + read.substring(indiceBaliseItem, read.length());
				}
				// remplace la balise texte : FINDESCRIPTION par un retour charriot
				if ((indiceBaliseFermante = read.indexOf(BALISE_FERMANTE_DESC)) >= 0) {
					read = read.substring(0, indiceBaliseFermante) + "\n" + read.substring(indiceBaliseFermante + BALISE_FERMANTE_DESC.length(), read.length());
				}
				// remplace la balise texte : FINTITLE par un retour charriot
				if ((indiceBaliseFermante = read.indexOf(BALISE_FERMANTE_TITLE)) >= 0) {
					read = read.substring(0, indiceBaliseFermante) + "\n" + read.substring(indiceBaliseFermante + BALISE_FERMANTE_TITLE.length(), read.length());
				}
				// traitement de fin de fichier : on supprime la virgule en trop
				if (read.indexOf(VIRGULE_FIN_DE_FICHIER) >= 0)
					read = read.substring(0, read.indexOf(VIRGULE_FIN_DE_FICHIER)) + FIN_DE_FICHIER_CORRECTE;
				write += read;
				read = entree.readLine();
			}
			entree.close();
			BufferedWriter sortie = new BufferedWriter(new FileWriter(
					fichierJsonFinal));
			sortie.write(write);
			sortie.close();

		} catch (Exception e) {
			System.out.println("<XsltUtils>--regulariseFichier(...)"
					+ e.getMessage());
		}
		if (DEBUG_ENABLED) {
			System.out.println("<XsltUtils>--regulariseFichier(...)-- [OUT]");
		}					
	}
}
