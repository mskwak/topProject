package com.hangugi.test;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;

import com.sun.mail.smtp.SMTPTransport;


public class SMTPTransportTest {
	public static void main(String[] args) {
		Properties properties = new Properties();
		String host = "175.115.94.175";

		properties.put("mail.smtp.protocol", "smtp");
		properties.put("mail.smtp.port", 25);
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.quitwait", false);
		properties.put("mail.smtp.ehlo", true);
		Session session = Session.getInstance(properties);
		SMTPTransport transport = null;

		try {
			transport = (SMTPTransport) session.getTransport("smtp");
			transport.connect();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				transport.close();
			} catch (MessagingException e) {
			}
		}
	}

	/*
	public static void main(String[] args) {
		String host = "175.115.94.175";
		Properties properties = new Properties();
		properties.put("mail.smtp.port", 25);
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.quitwait", false);
		properties.put("mail.smtp.ehlo", false);
		Session session = Session.getInstance(properties);
		//		Transport transport = session.getTransport();
		URLName urlname = new URLName(host);

		SMTPTransport smtpTransport = new SMTPTransport(session, urlname);

		Socket socket = null;
		try {
			socket = new Socket(host, 25);
			socket.setSoTimeout(60 * 1000);
			smtpTransport.connect(socket);
			smtpTransport.simpleCommand("ESIP 1.1.1.1");
			smtpTransport.simpleCommand("MAIL FROM: sssssssssssssssssssssssss");

			String filename = "/JavaTestData/01.eml";
			InputStream inputStream = new FileInputStream(filename);
			SMTPMessage smtpMessage = new SMTPMessage(session, inputStream);

			for (int i = 0; i < 10; i++) {
				//smtpTransport.simpleCommand("RCPT TO: " + i);
				//System.out.println(smtpTransport.getLastServerResponse());
				InternetAddress internetAddress = new InternetAddress();
				internetAddress.setAddress(Integer.toString(i));
				smtpMessage.setRecipient(RecipientType.TO, internetAddress);
			}

			Transport.send(smtpMessage);

		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				smtpTransport.close();
				socket.close();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	*/
}
