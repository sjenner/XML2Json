package tv.arte.utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtils {

//	public static void main(String args[]) {
//		try {
//			String smtpServer = "badiane.mail.strg.arte";
//			String to = "stephane.jenner@arte.tv";
//			String from = "stephane.jenner@arte.tv";
//			String subject = "Daemon JSON : erreur lors d'un traitement automatique";
//			String body = "Ceci est un test d'envoi de mail depuis une application cliente java";
//			sendMail(smtpServer, to, from, subject, body);
//		} catch (Exception ex) {
//			System.out.println("Usage: java com.lotontech.mail.SimpleSender"
//					+ " smtpServer toAddress fromAddress subjectText bodyText");
//		}
//		System.exit(0);
//	}

	/**
	 * Envoi d'un mail sans authentification mais en se servant des paramètres existants
	 * Ne fonctionne pas si l'on fourni un e-mail non déclaré au niveau du serveur SMTP
	 * @param smtpServer le serveur auqeul se connecter
	 * @param to l'adresse destinataire
	 * @param from l'adresse émettrice
	 * @param subject sujet de l'e-mail
	 * @param body le corps du message
	 */
	public static void sendMail(String envoyerMail, String smtpServer, String to, String from, String subject, String body, String nomFlux) {		
		Properties props = new Properties();
		props.put("mail.smtp.host", smtpServer);
		props.put("mail.debug", "true");

		if (! ConstantesUtils.estActive(envoyerMail) ) {
			return;
		}
		
		Session session = Session.getInstance(props);

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			InternetAddress[] address = { new InternetAddress(to) };
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(subject);
			msg.setSentDate(new Date());
			msg.setText(body + " pour le FLUX : " + nomFlux);

			Transport.send(msg);
		} catch (MessagingException e) {
			System.out.println("Problème lors de l'envoi d'un mail :" + e.getMessage());
		}
	}
}
