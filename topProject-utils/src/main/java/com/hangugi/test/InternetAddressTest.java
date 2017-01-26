package com.hangugi.test;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class InternetAddressTest {

	public static void main(String[] args) {

		InternetAddress internetAddress = null;
		try {
			internetAddress = new InternetAddress("곽면순 <mskw@daou.co.kr>");
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(internetAddress.getAddress());
		System.out.println(internetAddress.getPersonal());
	}
}
