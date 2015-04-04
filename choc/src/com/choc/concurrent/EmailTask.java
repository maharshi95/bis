package com.choc.concurrent;

import java.security.Security;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.tomcat.util.threads.ResizableExecutor;

import com.sun.mail.util.MailConnectException;

public class EmailTask implements Runnable {

	private static final String USERNAME = "chocfactory2015@gmail.com";
	private static final String PASSWORD = "madoverchocolates";

	private ArrayList<String> recipentList;
	private String subject;
	private String message;

	public EmailTask(String recipent, String subject, String message) {
		recipentList = new ArrayList<String>();
		recipentList.add(recipent);
		this.subject = subject;
		this.message = message;
	}
	
	public EmailTask(String[] recipents, String subject, String message) {
		recipentList = new ArrayList<String>();
		for(String recipent : recipents) {
			recipentList.add(recipent);
		}
		this.subject = subject;
		this.message = message;
	}
	

	@Override
	public void run() {
		post("Sending email to " + recipentList);
		sendFromGMail(USERNAME, PASSWORD, recipentList, subject, message);
		post("Email sent to " + recipentList);
	}

	private void post(String message) {
		System.out.println(Thread.currentThread().getName() + ": " + message);
	}
	
	private void sendFromGMail(String from, String pass, ArrayList<String> to,
			String subject, String body) {
		
		//Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        
		Properties props = System.getProperties();
		String host = "smtp.gmail.com";
		
//		  props.setProperty("proxySet","true");
//        props.setProperty("socksProxyHost","172.31.16.10");
//        props.setProperty("socksProxyPort","8080");
		
        props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.auth", "true");
		
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		
		// props.put("mail.debug", "true");
         props.put("mail.store.protocol", "pop3");
         props.put("mail.transport.protocol", "smtp");
		

		Session session = GmailSession.getSession(props, from, pass);
		MimeMessage message = new MimeMessage(session);

		try {
			message.setFrom(new InternetAddress(from));
			InternetAddress[] toAddress = new InternetAddress[to.size()];

			for (int i = 0; i < to.size(); i++) {
				toAddress[i] = new InternetAddress(to.get(i));
				message.addRecipient(Message.RecipientType.TO, toAddress[i]);
			}

			message.setSubject(subject);
			message.setText(body);
			Transport.send(message);
//			Transport transport = GmailSession.getTransport();
//			post("Connecting to " + host);
//			if(!transport.isConnected()) {
//				transport.connect(host, from, pass);
//			}	
//			post("Connected, Preparing message");
//			transport.sendMessage(message, message.getAllRecipients());
//			System.out.println("trying again...");
//			transport.sendMessage(message, message.getAllRecipients());
			post("Message sent");
//			//transport.close();
			
		} catch (MailConnectException mce) {
			mce.printStackTrace();
		} catch (AddressException ae) {
			ae.printStackTrace();
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}

	public ArrayList<String> getRecipents() {
		return recipentList;
	}

	public void setRecipent(ArrayList<String> recipents) {
		this.recipentList = recipents;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

class GmailSession {
	
	private static boolean init = false;
	private static Session session;
	private static Transport transport;
	
	public static Session getSession(Properties props, String from, String pass){
		if(init == false) {
			init = true;
			session =  Session.getInstance(props,
					new Authenticator() {
				protected PasswordAuthentication getPasswwordAuthentification() {
					return new PasswordAuthentication(from, pass);
				}
			});
		}
		return session;
	}
	
	public static Transport getTransport() throws NoSuchProviderException {
		if(transport == null)
			transport = session.getTransport("smtp");
		return transport;
	}
}
