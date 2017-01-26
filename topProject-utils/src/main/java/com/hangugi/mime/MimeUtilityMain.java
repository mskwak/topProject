package com.hangugi.mime;

import java.io.UnsupportedEncodingException;

import javax.mail.internet.MimeUtility;

public class MimeUtilityMain {

	public static void main(String[] args) {
		try {
			String text = MimeUtility.encodeText("[외부메일알림", "EUC-KR", "B");
			System.out.println(text);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
