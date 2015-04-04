package test;

import java.security.spec.PSSParameterSpec;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import com.sun.mail.imap.protocol.BODY;
import com.sun.mail.util.MailConnectException;

public class MailTest {
	private static String USER_NAME = "chocfactory2015";
	private static String PASSWORD = "madoverchocolates";
	private static String RECIPIENT = "maharshigor@gmail.com";
	private static String SUBJECT = "Verification";
	private static String BODY_LINE = "Thank you for registering at Chocoloate Factory.\n To continue shopping at Chocolate Factory, click the below verification link :";

	public static void main(String[] args) {
		String from = USER_NAME;
		String pass = PASSWORD;
		String[] to = { RECIPIENT }; // list of recipient email addresses
		String subject = "Sample Subject!!";
		String body = "This mail is sent using Java API for GMAIL :D";
		System.out.println("sending mail...");
		sendFromGMail(from, pass, to, subject, body);
	}

	public static void sendVerificationMail(String recipent, String link) {
		String[] recipents = { recipent };
		sendFromGMail(USER_NAME, PASSWORD, recipents, SUBJECT, BODY_LINE + "\n"
				+ link);
	}

	private static void sendFromGMail(String from, String pass, String[] to,
			String subject, String body) {
		
		Properties props = System.getProperties();
		
		String proxyHost = "172.31.16.10";           
		String proxyPort = "8080";
		String host = "smtp.gmail.com";
		
		props.setProperty("proxySet","true");
		props.put("socksProxyHost",proxyHost);
		props.put("socksProxyPort",proxyPort);
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.auth", "true");

		SmtpAuthenticator authenticator = new SmtpAuthenticator();
		Session session = Session.getDefaultInstance(props,authenticator); 
		Message message = new MimeMessage(session);

		try {
			message.setFrom(new InternetAddress(from));
			InternetAddress[] toAddress = new InternetAddress[to.length];

			// To get the array of addresses
			for (int i = 0; i < to.length; i++) {
				toAddress[i] = new InternetAddress(to[i]);
			}

			for (int i = 0; i < toAddress.length; i++) {
				message.addRecipient(Message.RecipientType.TO, toAddress[i]);
			}

			message.setSubject(subject);
			message.setText(body);
			// Transport transport = session.getTransport("smtp");
			// transport.connect(host, from, pass);
			// transport.sendMessage(message, message.getAllRecipients());
			Transport.send(message);
			System.out.println("MAIL sent!!");
			// transport.close();
		} catch (MailConnectException mce) {
			mce.printStackTrace();
		} catch (AddressException ae) {
			ae.printStackTrace();
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}
}

class SmtpAuthenticator extends Authenticator {
	public SmtpAuthenticator() {

		super();
	}

	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		String username = "user";
		String password = "password";
		if ((username != null) && (username.length() > 0) && (password != null)
				&& (password.length() > 0)) {

			return new PasswordAuthentication(username, password);
		}

		return null;
	}
}
