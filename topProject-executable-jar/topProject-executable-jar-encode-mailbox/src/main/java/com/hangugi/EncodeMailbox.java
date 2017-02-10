package com.hangugi;

import com.sun.mail.imap.protocol.BASE64MailboxEncoder;

public class EncodeMailbox {
	public static void main(String[] args) {
/*		Getopt getopt = new Getopt("EncodeMailbox", args, "f");
		String baseDir = getopt.getOptarg();

		int ccc;

		while((ccc = getopt.getopt()) != -1) {
			System.out.println(ccc);
		}

		System.out.println(baseDir);

		File file = new File(baseDir);

		if(file.exists()) {
			System.out.println("exist");
		} else {
			System.out.println("not exist");
		}*/

		// TODO RFC2060를 읽어라
		String x = BASE64MailboxEncoder.encode("곽면순");
		System.out.println(x);
	}
}
