package com.daou.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.io.IOUtils;

public class SmtpTest {
	private static final int IOTIMEOUT = 600 * 1000;
	private static final String CRLF = "\r\n";
	//	private static final String DATA = "DATA" + CRLF;
	private static final String QUIT = "QUIT" + CRLF;



	public static void main(String[] args) {
		String smtpServer = "tmadev.terracetech.co.kr";
		Socket socket = null;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;

		try {
			socket = new Socket(smtpServer, 25);
			socket.setSoTimeout(IOTIMEOUT);

			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			System.out.println(bufferedReader.readLine());


			bufferedWriter.write("ESIP 1.1.1.1\r\n");
			bufferedWriter.flush();
			System.out.println(bufferedReader.readLine());


			bufferedWriter.write("EDATE 20140210101615\r\n");
			bufferedWriter.flush();
			System.out.println(bufferedReader.readLine());




		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (bufferedWriter != null) {
					bufferedWriter.write(QUIT);
					bufferedWriter.flush();
					bufferedWriter.close();
				}
			} catch (IOException e) {
				//NOPMD
			}

			IOUtils.closeQuietly(bufferedReader);
			IOUtils.closeQuietly(socket);
		}
	}

	//	private Socket connect(String smtpServer, int port) throws IOException {
	//		Socket socket = null;
	//
	//		try {
	//			socket = new Socket(smtpServer, port);
	//			socket.setSoTimeout(IOTIMEOUT);
	//		} catch (UnknownHostException e) {
	//			throw e;
	//		} catch (IOException e) {
	//			throw e;
	//		}
	//
	//		return socket;
	//	}
}
