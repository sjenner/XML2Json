package tv.arte.utils;

public class FTPUtils {

	public static boolean transfereFichier(String host, String user, String password, String path, String ancienNom, String nouveauNom) {
		FTPClient client = new FTPClient();
		client.connect(host, user, password, nouveauNom);
		return client.uploadFile(path, ancienNom, nouveauNom);
	}
}
