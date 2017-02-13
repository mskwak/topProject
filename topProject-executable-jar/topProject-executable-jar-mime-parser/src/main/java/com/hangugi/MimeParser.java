package com.hangugi;

import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MimeParser {
	private MimeMessage mimeMessage;

	private int depth = 1;

	public MimeMessage getMimeMessage() {
		return this.mimeMessage;
	}

	public void setMimeMessage(MimeMessage mimeMessage) {
		this.mimeMessage = mimeMessage;
	}

	public void printStructure() {
		this.printTopLevelContentType();
		try {
			Object object = this.mimeMessage.getContent();
			int myDepth = this.depth;
			this.parse(myDepth, object);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	private void printTopLevelContentType() {
		try {
			System.out.println("* " + this.mimeMessage.getContentType().replaceAll("(\r|\n|\t)", " "));
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	private void parse(int myDepth, Object object) {
		try {
			if (object instanceof MimeMultipart) {
				MimeMultipart mimeMultipart = (MimeMultipart) object;
				int partCount = mimeMultipart.getCount();

				for (int i = 0; i < partCount; i++) {
					BodyPart bodyPart = mimeMultipart.getBodyPart(i);
					this.printSubLevelContentType(myDepth, bodyPart.getContentType());
					this.parse(myDepth + 1, bodyPart.getContent());
				}
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void printSubLevelContentType(int depth, String contentType) {
		String tab = "";

		for (int i = 0; i < depth; i++) {
			tab = tab + "\t";
		}

		System.out.println(tab + contentType.replaceAll("(\r|\n|\t)", " "));
		//System.out.println(depth + ":" + contentType);
	}
}
