package com.daou.smtp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.mail.DefaultAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.mail.smtp.SMTPMessage;
import com.sun.mail.smtp.SMTPTransport;

public class SmtpSender implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(SmtpSender.class);
	private final File file;
	private final Properties properties;

	public SmtpSender(final Properties properties, final File file) {
		this.properties = properties;
		this.file = file;
	}

	@Override
	public void run() {
		// TODO new Object 객체에서 스프링 빈을 사용 가능하도록 수정 필요
		String smtpAuthId = this.properties.getProperty("mail.smtp.auth.id");
		String smtpAuthPassword = this.properties.getProperty("mail.smtp.auth.password");
		boolean isDebug = "true".equalsIgnoreCase(this.properties.getProperty("mail.debug"));
		String smtpProtocol = this.properties.getProperty("mail.smtp.protocol");
		String smtpMailFrom = this.properties.getProperty("mail.smtp.mail.from");
		String smtpRcptTo = this.properties.getProperty("mail.smtp.rcpt.to");

		Session session = Session.getDefaultInstance(this.properties, new DefaultAuthenticator(smtpAuthId, smtpAuthPassword));
		session.setDebug(isDebug);
		SMTPTransport smtpTransport = null;
		InputStream inputStream;
		SMTPMessage smtpMessage;

		try {
			smtpTransport = (SMTPTransport) session.getTransport(smtpProtocol);
			inputStream = new FileInputStream(this.file);
			smtpMessage = new SMTPMessage(session, inputStream);

			smtpMessage.setEnvelopeFrom(smtpMailFrom);

			String[] rcptToArray = smtpRcptTo.split(",");
			int rcptToArraySize = rcptToArray.length;
			Address[] addresses = new Address[rcptToArraySize];

			for(int i = 0; i < rcptToArraySize; i++) {
				addresses[i] = new InternetAddress(rcptToArray[i]);
			}

			smtpTransport.connect();
			smtpTransport.sendMessage(smtpMessage, addresses);
			logger.info("sending success. " + this.file.getAbsolutePath());
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} finally {
			try {
				smtpTransport.close();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

//    private class SMTPAuthenticator extends javax.mail.Authenticator {
//        @Override
//		public PasswordAuthentication getPasswordAuthentication() {
//        	String userName = "mskw@daou.co.kr";
//        	String password = "1qaz@WSX";
//    		return new PasswordAuthentication(userName, password);
//        }
//    }
}
