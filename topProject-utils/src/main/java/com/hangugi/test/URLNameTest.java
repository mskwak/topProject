package com.hangugi.test;

import javax.mail.URLName;

public class URLNameTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String url = "http://1.1.1.1:8080/yyy=123";
		URLName urlName = new URLName(url);

		System.out.println(urlName.getHost());
		System.out.println(urlName.getFile());
		System.out.println(urlName.getPort());
		System.out.println(urlName.getProtocol());
		System.out.println(urlName.toString());
	}

}
