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
		Getopt getopt = new Getopt("encodeMailbox", args, "i:o:");

		int c;
		String input = null;
		String output = null;

		while((c = getopt.getopt()) != -1) {
			// System.out.println("c:" + c); 출력이 102이다. 왜일까?
			switch(c) {
				case 'i':
					input = getopt.getOptarg();
					break;
				case 'o':
					output = getopt.getOptarg();
					break;
				default:
					usage();
			}
		}

		if(input == null || output == null) {
			usage();
		}

		File inputFile = new File(input);
		File outputFile = new File(output);

		if(!inputFile.exists() || inputFile.isDirectory() || outputFile.isDirectory()) {
			usage();
		}

		encodeMailbox(inputFile, outputFile);
	}

	private static void usage() {
		System.out.println("/path/to/java -jar encodeMailbox.jar -i input_file_path -o output_file_path");
		System.exit(0);
	}

	private static void encodeMailbox(File inputFile, File outputFile) {
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;

		try {
			bufferedReader = new BufferedReader(new FileReader(inputFile));
			bufferedWriter = new BufferedWriter(new FileWriter(outputFile));

			String line = null;

			while((line = bufferedReader.readLine()) != null) {
				// TODO RFC2060를 읽어라
				String utf7 = BASE64MailboxEncoder.encode(line);
				bufferedWriter.write(utf7);
				bufferedWriter.newLine();
				//System.out.println(line + ":" + utf7);
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
