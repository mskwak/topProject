package com.daou.test;

import java.util.Locale;

public class LocaleTest {

	public static void main(String[] args) {
		System.out.println("user.language:" + System.getProperty("user.language"));
		System.out.println("user.region:" + System.getProperty("user.region"));
		System.out.println("file.encoding:" + System.getProperty("file.encoding"));

		Locale loc = Locale.getDefault();
		System.out.println("loc:" + loc);
		System.out.println("DisplayLanguage:" + loc.getDisplayLanguage());
		System.out.println("DisplayCountry:" + loc.getDisplayCountry());
		System.out.println(loc.getDisplayLanguage(Locale.US));
		System.out.println(loc.getDisplayCountry(Locale.US));
		String enc = new java.io.OutputStreamWriter(System.out).getEncoding();
		System.out.println("default encoding = " + enc);
	}
}
