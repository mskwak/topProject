package com.daou.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class MimeBodyPartTest {

	/**
	 * @param args
	 */
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		FileInputStream fileInputStream = null;
		MimeBodyPart mimeBodyPart;
		try {
			fileInputStream = new FileInputStream(new File("/2.eml"));
			mimeBodyPart = new MimeBodyPart(fileInputStream);
			Enumeration enumeration = mimeBodyPart.getAllHeaderLines();

			while (enumeration.hasMoreElements()) {
				System.out.println(enumeration.nextElement());
			}

			System.out.println(mimeBodyPart.getContentType());

			Object object = mimeBodyPart.getContent();
			System.out.println(object.getClass().getName());

			if (object instanceof MimeMultipart) {
				MimeMultipart mimeMultipart = (MimeMultipart) object;
				System.out.println(mimeMultipart.getCount());

				BodyPart bodyPart = mimeMultipart.getBodyPart(0);

				if (bodyPart == null) {
					System.out.println("NULL");
				}

				System.out.println(bodyPart.getContentType());

			}



		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
