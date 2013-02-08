package tv.arte.utils;

/**
 * Classe utilitaire sur des Constantes 
 * @author Arte
 *
 */

public class ConstantesUtils {

	/**
	 * A partir de la chaine de caractères fournie, détermine si elle doit être considérée comme un "Oui" ou comme un "Non"
	 * (YES, yes, Y, y sont des valeurs qui sont interprétées comme "Oui")
	 * 
	 * @param valeur la chaine à tester
	 * @return true si la chaine doit être interprétée comme un "Oui"
	 */
	public static boolean estActive(String valeur) {
		boolean estActive = true;
		String STRING_YES = "yes";
		String STRING_Y = "y";

		if ( (! STRING_Y.equalsIgnoreCase(valeur)) && (!STRING_YES.equalsIgnoreCase(valeur)) ) {
			estActive = false;
		}
		return estActive;
	}
}
