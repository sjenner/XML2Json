package tv.arte.utils;

/**
 * Cette classe fourni des m�thodes utilitaires pour la manipulation d'objets String
 * 
 * 
 * @author Arte / s-jenner
 * @date 31 october 2011
 *
 */

public class StringUtils {

	/**
	 * D�termine si une chaine est nulle ou vide (de longueur 0)
	 *  
	 * @param chaine � tester
	 * @return true si nul ou vide et false sinon
	 */
	public static boolean isNull(String chaine) {
		boolean estNul = false;
		if ( (chaine == null) || (chaine.length() == 0))
			estNul = true;
		return estNul;
	}
}
