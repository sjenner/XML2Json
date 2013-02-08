package tv.arte.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

public class HttpUtils {

//	private void getUrl(String url) throws Exception {
//		URL oracle = new URL(url);
//		BufferedReader in = new BufferedReader(new InputStreamReader(
//				oracle.openStream()));
//
//		String inputLine;
//
//		while ((inputLine = in.readLine()) != null)
//			System.out.println(inputLine);
//
//		in.close();
//	}

	/**
	 * Executes an HTTP-GET request and stores locally down to a file the content of the answer
	 * @param url the URL to post a GET to
	 * @param fileName the filename to store the result of the GET request locally
	 * @return true if the file was successfully created
	 * @throws Exception
	 */
	public static boolean getUrl(String url, String fileName) throws Exception {
		boolean succes = false;
		URL urlObj = new URL(url);
		InputStream inputStream = urlObj.openStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF8"));
		BufferedWriter sortie = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));

		String inputLine;
		String contenu = "";

		while ((inputLine = in.readLine()) != null)
			contenu += inputLine + "\n";

		sortie.write(contenu);
		sortie.close();

		in.close();

		try {
			File file = new File(fileName);
			if (file.exists())
				succes = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return succes;
	}

	/**
	 * Entry point to the program
	 * @param arg no args needed
	 */
//	public static void main(String arg[]) {
//		HttpUtils httpUtils = new HttpUtils();
//		try {
//			httpUtils.getUrl(URL_ARTE_XML, FICHIER_XML);
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//	}
}
