package tv.arte.utils;

/**
 * Classe utilitaire sur des Constantes 
 * @author Arte
 *
 */

public class ConstantesUtils {

	/**
	 * A partir de la chaine de caract�res fournie, d�termine si elle doit �tre consid�r�e comme un "Oui" ou comme un "Non"
	 * (YES, yes, Y, y sont des valeurs qui sont interpr�t�es comme "Oui")
	 * 
	 * @param valeur la chaine � tester
	 * @return true si la chaine doit �tre interpr�t�e comme un "Oui"
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
