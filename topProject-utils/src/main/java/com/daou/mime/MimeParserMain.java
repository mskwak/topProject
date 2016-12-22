package com.daou.mime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class MimeParserMain {

	public static void main(String[] args) {
		try {
			File file = new File("D:\\nate");
			//File file = new File("D:\\2011.���Ϲ��");
			File[] files = file.listFiles();

			for (File f : files) {
				if(f.isFile()) {
					MimeMessage mimeMessage = new MimeMessage(null, new FileInputStream(f.getAbsolutePath()));
					MimeParser mimeParser = new MimeParser();
					mimeParser.setMimeMessage(mimeMessage);
					mimeParser.printStructure();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
