package tv.arte;

import java.io.File;
import java.io.ObjectInputStream.GetField;
import java.util.Properties;
import java.util.StringTokenizer;

import tv.arte.utils.ConstantesUtils;
import tv.arte.utils.FTPUtils;
import tv.arte.utils.FichierUtils;
import tv.arte.utils.HttpUtils;
import tv.arte.utils.MailUtils;
import tv.arte.utils.PropertiesUtils;
import tv.arte.utils.XsltUtils;


/**
 * 
 * @author Arte/s-jenner
 * 
 * Modifications :
 *  - 2012-01-06 : Demande de limitation du fichier JSON � la journ�e courante pour diminuer la taille du flux JSON (question de performances)
 *
 */

public class JsonMain {

	/**
	 * DEBUG = true si on est en environnement de d�veloppement et false si on
	 * est en environnement de production
	 */
	private static final boolean DEBUG = false;

	/**
	 * Point d'entr�e g�n�ral de l'application.
	 * @param args aucun param n'est n�cessaire
	 *    Voir le fichier de properties : json.properties
	 *    
	 *    Exemple de valeurs possibles pour le fichier de propri�t�s :
	 *    
	 *      PATH	C:\\Projets\\JSon_CM\\exemple_fichier
	 *		FILE	programme.xml
	 *		URL		http://cehtml.arte.tv/fr/3720858,templateId=renderXml,xmlKind=mediarss.xml
	 *		MAIL	stephane.jenner@arte.tv
	 *		FICHIER_XSLT	json.xsl
	 *		FICHIER_XML	rssFeed.xml
	 *		FICHIER_JSON	jsonFeed.json
	 *		FICHIER_JSON_FINAL	programmeArte.json
	 *		NOUVEAU_FICHIER	nouveau PATH + nouveau nom de fichier
	 *		SMTP_SERVER	badiane.mail.strg.arte
	 *		FROM_EMAIL	stephane.jenner@arte.tv
	 *		TO_EMAIL	stephane.jenner@arte.tv
	 *		SUBJECT	Daemon JSON : 
	 *		MESSAGE_1	Il est impossible de r�cup�rer le contenu XML � l'URL fournie
	 *		MESSAGE_2	Il est impossible de transformer le fichier XML trouv� en une version JSON
	 *		MESSAGE_3	Il est impossible de transf�rer le fichier JSON sur el serveur Web statique
	 *		MESSAGE_OK	Tout s'est bien d�roul�
	 *		ENVOYER_MAIL	YES
	 *    
	 *    
	 */
	public static void main(String[] args) {
		// permet de donner une conclusion lorsque tout s'est bien pass�
		boolean sansProbleme = true;
		// le fichier des propri�t�s g�n�rales
		Properties propGenerales = PropertiesUtils.lectureProperties(DEBUG, "json.properties");
		// le fichier contenant la liste des fichiers � traiter (= la liste des flux � r�cup�rer)
		Properties listeFeeds = PropertiesUtils.lectureProperties(DEBUG, "feedList.properties");
		String listeFichiers = listeFeeds.getProperty("FEEDS");
		StringTokenizer st = new StringTokenizer(listeFichiers, ",");

		// les proprietes g�n�rales valables pour tous les flux XML
		String SMTP_SERVER = propGenerales.getProperty("SMTP_SERVER");
		String MAIL_TO = propGenerales.getProperty("TO_EMAIL");
		String MAIL_FROM = propGenerales.getProperty("FROM_EMAIL");
		String SUBJECT = propGenerales.getProperty("SUBJECT");
		String MESSAGE_KO_1 = propGenerales.getProperty("MESSAGE_KO_1");
		String MESSAGE_KO_2 = propGenerales.getProperty("MESSAGE_KO_2");
		String MESSAGE_KO_3 = propGenerales.getProperty("MESSAGE_KO_3");
		String MESSAGE_KO_4 = propGenerales.getProperty("MESSAGE_KO_4");
		String MESSAGE_OK_1 = propGenerales.getProperty("MESSAGE_OK_1");
		String MESSAGE_OK_2 = propGenerales.getProperty("MESSAGE_OK_2");
		String MESSAGE_OK_3 = propGenerales.getProperty("MESSAGE_OK_3");
		String MESSAGE_OK_4 = propGenerales.getProperty("MESSAGE_OK_4");
		String MESSAGE_PAS_HTTP = propGenerales.getProperty("MESSAGE_PAS_HTTP");
		String MESSAGE_OK = propGenerales.getProperty("MESSAGE_OK");
		String ENVOYER_MAIL = propGenerales.getProperty("ENVOYER_MAIL");
		String FICHIER_LOG = propGenerales.getProperty("FICHIER_LOG");
		String HOST = propGenerales.getProperty("HOST");
		String USERNAME = propGenerales.getProperty("USERNAME");
		String PASSWORD = propGenerales.getProperty("PASSWORD");
		String PURGER_FICHIERS_ERREURS = propGenerales.getProperty("PURGER_FICHIERS_ERREURS");

		// les propri�t�s qui ne sont valables que pour un flux XML donn�
		String PATH = "";
		String FICHIER_XML = "";
		String URL = "";
		String FICHIER_XSLT = "";
		String FICHIER_JSON = "";
		String NOUVEAU_FICHIER = "";
		Properties proprietes = null;

		while (st.hasMoreTokens()) {
			sansProbleme = true;
			String nomFichier = (String) st.nextToken();
			proprietes = PropertiesUtils.lectureProperties(DEBUG, nomFichier);

			PATH = proprietes.getProperty("PATH");
			FICHIER_XML = proprietes.getProperty("FICHIER_XML");
			URL = proprietes.getProperty("URL");
			FICHIER_XSLT = proprietes.getProperty("FICHIER_XSLT");
			FICHIER_JSON = proprietes.getProperty("FICHIER_JSON");
			NOUVEAU_FICHIER = proprietes.getProperty("NOUVEAU_FICHIER");
			String FICHIER_ERREURS = proprietes.getProperty("FICHIER_ERREURS");
			String UNIQUEMENT_JOUR_COURANT = proprietes.getProperty("EXTRAIRE_DONNEES_JOUR_COURANT");

			// R�cup�ration par HTTP get du fichier XML
//			try {
//				if ((URL != null) && (!URL.equalsIgnoreCase("NONE"))) {
//					HttpUtils.getUrl(URL, PATH + File.separator + FICHIER_XML);
//					FichierUtils.append2Fichier(FICHIER_LOG, SUBJECT + FichierUtils.ESPACE + FICHIER_XML + FichierUtils.ESPACE + MESSAGE_OK_1, FichierUtils.SEPARATEUR_AVANT);
//				} else {
//					FichierUtils.append2Fichier(FICHIER_LOG, SUBJECT + FichierUtils.ESPACE + FICHIER_XML + FichierUtils.ESPACE + MESSAGE_PAS_HTTP, FichierUtils.SEPARATEUR_AVANT);
//				}
//			} catch (Exception e) {
//				sansProbleme = false;
//				MailUtils.sendMail(ENVOYER_MAIL, SMTP_SERVER, MAIL_TO, MAIL_FROM, SUBJECT + MESSAGE_KO_1, e.getMessage(), FICHIER_XML);
//				FichierUtils.append2Fichier(FICHIER_ERREURS, SUBJECT + MESSAGE_KO_1, e, FichierUtils.SANS_SEPARATEUR);
//				FichierUtils.append2Fichier(FICHIER_LOG, SUBJECT + MESSAGE_KO_1, e, FichierUtils.SANS_SEPARATEUR);
//				if (DEBUG)
//					System.out.println("Probl�me dans la r�cup�ration du fichier par requ�te HTTP :" + e.getMessage());
//			}

			// Extraction des donn�es pour le jour courant uniquement (question de performances : on limite la taille du flux json final)
			// un param�tre de configuration permet de dire si on prend le flux tel quel ou s'il faut 
			try {
				if (sansProbleme && (ConstantesUtils.estActive(UNIQUEMENT_JOUR_COURANT))) {
					String BALISE_ITEM = proprietes.getProperty("BALISE_ITEM");
					String BALISE_DATE = proprietes.getProperty("BALISE_DATE");
					sansProbleme = FichierUtils.extractionDonneesJourCourant(PATH + File.separator + FICHIER_XML, BALISE_ITEM, BALISE_DATE);
					if (sansProbleme) {
							FichierUtils.append2Fichier(FICHIER_LOG, SUBJECT + FichierUtils.ESPACE + FICHIER_XML + FichierUtils.ESPACE + MESSAGE_OK_4, FichierUtils.SANS_SEPARATEUR);
					}
					else {
						FichierUtils.append2Fichier(FICHIER_ERREURS, SUBJECT + MESSAGE_KO_4, null, FichierUtils.SANS_SEPARATEUR);
						FichierUtils.append2Fichier(FICHIER_LOG, SUBJECT + MESSAGE_KO_4, null, FichierUtils.SANS_SEPARATEUR);
					}
				}
			} catch (Exception e) {
				sansProbleme = false;
				MailUtils.sendMail(ENVOYER_MAIL, SMTP_SERVER, MAIL_TO, MAIL_FROM, SUBJECT + MESSAGE_KO_4, e.getMessage(), FICHIER_XML);
				FichierUtils.append2Fichier(FICHIER_ERREURS, SUBJECT + MESSAGE_KO_4, e, FichierUtils.SANS_SEPARATEUR);
				FichierUtils.append2Fichier(FICHIER_LOG, SUBJECT + MESSAGE_KO_4, e, FichierUtils.SANS_SEPARATEUR);
				if (DEBUG)
					System.out.println("Probl�me dans l'extraction des donn�es de la journ�e courante du fichier XML :" + e.getMessage());
			}
						
			// Transformation du fichier XML en Json
			try {
				if (sansProbleme) {
					XsltUtils.generationFichierJson(PATH + File.separator + FICHIER_XSLT, PATH + File.separator + FICHIER_XML, PATH + File.separator + FICHIER_JSON);
					FichierUtils.append2Fichier(FICHIER_LOG, SUBJECT + FichierUtils.ESPACE + FICHIER_XML + FichierUtils.ESPACE + MESSAGE_OK_2, FichierUtils.SANS_SEPARATEUR);
				}
			} catch (Exception e) {
				sansProbleme = false;
				MailUtils.sendMail(ENVOYER_MAIL, SMTP_SERVER, MAIL_TO, MAIL_FROM, SUBJECT + MESSAGE_KO_2, e.getMessage(), FICHIER_XML);
				FichierUtils.append2Fichier(FICHIER_ERREURS, SUBJECT + MESSAGE_KO_2, e, FichierUtils.SANS_SEPARATEUR);
				FichierUtils.append2Fichier(FICHIER_LOG, SUBJECT + MESSAGE_KO_2, e, FichierUtils.SANS_SEPARATEUR);
				if (DEBUG)
					System.out.println("Probl�me dans la transformation du fichier XML en JSON :" + e.getMessage());
			}

			// D�p�t du fichier Json sur le serveur statique
//			try {
//				if (sansProbleme) {
//					 sansProbleme = FTPUtils.transfereFichier(HOST, USERNAME, PASSWORD, PATH, FICHIER_JSON, NOUVEAU_FICHIER);
//					FichierUtils.append2Fichier(FICHIER_LOG, SUBJECT + FichierUtils.ESPACE + FICHIER_XML + FichierUtils.ESPACE + MESSAGE_OK_3, FichierUtils.SANS_SEPARATEUR);
//				}
//			} catch (Exception e) {
//				sansProbleme = false;
//				MailUtils.sendMail(ENVOYER_MAIL, SMTP_SERVER, MAIL_TO, MAIL_FROM, SUBJECT + MESSAGE_KO_3, e.getMessage(), FICHIER_XML);
//				FichierUtils.append2Fichier(FICHIER_ERREURS, SUBJECT + FichierUtils.ESPACE + MESSAGE_KO_3, e, FichierUtils.SANS_SEPARATEUR);
//				FichierUtils.append2Fichier(FICHIER_LOG, SUBJECT + MESSAGE_KO_3, e, FichierUtils.SANS_SEPARATEUR);
//				if (DEBUG)
//					System.out.println("Probl�me du transfert du fichier JSON sur le serveur web :" + e.getMessage());
//			}

			if (sansProbleme) {
				MailUtils.sendMail(ENVOYER_MAIL, SMTP_SERVER, MAIL_TO, MAIL_FROM, SUBJECT + MESSAGE_OK, MESSAGE_OK, FICHIER_XML);
				FichierUtils.append2Fichier(FICHIER_LOG, SUBJECT + FichierUtils.ESPACE + FICHIER_XML + FichierUtils.ESPACE + MESSAGE_OK, FichierUtils.SANS_SEPARATEUR);
				// Si une purge est demand�e le fichier est supprim� sinon il
				// est simplement renomm�
				if (ConstantesUtils.estActive(PURGER_FICHIERS_ERREURS))
					FichierUtils.supprimeFichierErreur(FICHIER_ERREURS);
				// else
				// FichierUtils.renommeFichierErreur(FICHIER_ERREURS);
			}
		}
	}

	
}
