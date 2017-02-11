package com.hangugi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.sun.mail.imap.protocol.BASE64MailboxEncoder;

import gnu.getopt.Getopt;

public class EncodeMailbox {
	public static void main(String[] args) {
		Getopt getopt = new Getopt("encodeMailbox", args, "f:");

		int c;
		String arg = null;

		while((c = getopt.getopt()) != -1) {
			// System.out.println("c:" + c); 출력이 102이다. 왜일까?
			switch(c) {
				case 'f':
					arg = getopt.getOptarg();
					break;
				default:
					System.out.println("/path/to/java -jar encodeMailbox.jar -f file");
					System.exit(0);
			}
		}

		File file = new File(arg);

		if(!file.exists() || file.isDirectory()) {
			System.out.println("/path/to/java -jar encodeMailbox.jar -f file");
			System.out.println("-f file does not exist. file must be text file.");
			System.exit(0);
		}

		encodeMailbox(file);
	}

	private static void encodeMailbox(File file) {
		String resultFile = file.getParent() + "/encodedMailBoxList.txt";
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;

		try {
			bufferedReader = new BufferedReader(new FileReader(file));
			bufferedWriter = new BufferedWriter(new FileWriter(resultFile));

			String line = null;

			while((line = bufferedReader.readLine()) != null) {
				// TODO RFC2060를 읽어라
				String utf7 = BASE64MailboxEncoder.encode(line);
				bufferedWriter.write(utf7);
				bufferedWriter.newLine();
				System.out.println(line + ":" + utf7);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(bufferedWriter);
			IOUtils.closeQuietly(bufferedReader);
		}
	}
}
