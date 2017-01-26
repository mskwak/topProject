package com.hangugi.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.internet.MailDateFormat;

public class MailDateFormatTest {

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		//		MailDateFormat mailDateFormat = new MailDateFormat();
		//System.out.println(mailDateFormat.format(new Date())); // Tue, 15 Oct 2013 16:35:27 +0900 (KST)

		String httpFormatDate = "Mon, 8 Jul 2013 12:38:48 +0900";
		DateFormat dateFormat = new MailDateFormat();
		Date date = dateFormat.parse(httpFormatDate);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

		System.out.println(simpleDateFormat.format(date));


		//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
		//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'XXXXX' (z)");
		//Date date = simpleDateFormat.parse(httpFormatDate);
	}
}
