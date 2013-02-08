package tv.arte.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TempsUtils {
	
	static Locale locale = Locale.getDefault();
	static Date actuelle = new Date();
	static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
	static DateFormat dateFormatCourt = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * Renvoie la date et l'heure actuelle au format : YYYY-MM-DD HH:MM:SS AM/PM
	 * 
	 * @return
	 */
	public static String getTempsActuel() {
		String dat = dateFormat.format(actuelle);
		return dat;
	}
	
	/**
	 * Rnevoie la date du jour au format : YYYY-MM-DD
	 * 
	 * @return
	 */
	public static String getJourActuel() {
		String dat = dateFormatCourt.format(actuelle);
		return dat;
	}
}
