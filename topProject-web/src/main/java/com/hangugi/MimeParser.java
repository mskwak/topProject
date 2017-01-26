package com.hangugi;

import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MimeParser {
	private static final String HTML_BR_TAG = "<br>";

	private static final String HTML_SPACE_CHAR = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";

	private MimeMessage mimeMessage;

	private final int depth = 1;

	private final StringBuilder stringBuilder = new StringBuilder();

	public MimeParser() {
		super();
	}

	public MimeParser(MimeMessage mimeMessage) {
		this.mimeMessage = mimeMessage;
	}

	public MimeMessage getMimeMessage() {
		return this.mimeMessage;
	}

	public void setMimeMessage(MimeMessage mimeMessage) {
		this.mimeMessage = mimeMessage;
	}

	public String getBoundary() {
		this.getTopLevelContentType();
		try {
			Object object = this.mimeMessage.getContent();
			int myDepth = this.depth;
			this.parse(myDepth, object);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return this.stringBuilder.toString();
	}

	private void getTopLevelContentType() {
		try {
			this.stringBuilder.append("* " + this.mimeMessage.getContentType().replaceAll("(\r|\n|\t)", " "));
			this.stringBuilder.append(HTML_BR_TAG);
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
					this.getSubLevelContentType(myDepth, bodyPart.getContentType());
					this.parse(myDepth + 1, bodyPart.getContent());
				}
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getSubLevelContentType(int depth, String contentType) {
		String tab = "";

		for (int i = 0; i < depth; i++) {
			tab = tab + HTML_SPACE_CHAR;
		}

		this.stringBuilder.append(tab + contentType.replaceAll("(\r|\n|\t)", " "));
		this.stringBuilder.append(HTML_BR_TAG);
//		System.out.println(tab + contentType.replaceAll("(\r|\n|\t)", " "));
		//System.out.println(depth + ":" + contentType);
	}
}
