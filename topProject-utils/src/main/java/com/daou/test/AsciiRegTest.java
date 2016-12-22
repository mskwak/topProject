package com.daou.test;

public class AsciiRegTest {

	public static void main(String[] args) {
		String str = "ddd";

		if (str.matches("\\p{ASCII}*")) {
			System.out.println("true");
		} else {
			System.out.println("false");
		}

	}

}
