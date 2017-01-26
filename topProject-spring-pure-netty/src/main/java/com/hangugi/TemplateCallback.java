package com.hangugi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hangugi.command.client.ClientCommand;


public class TemplateCallback {
	private static final Logger logger = LoggerFactory.getLogger(TemplateCallback.class);

	public String getAllowList(String emailAddress) {
		ClientCommand clientCommand = new ClientCommand() {
			@Override
			public String executeCommand(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter, String emailAddress) throws IOException {
				String response = null;

				try {
					System.out.println(bufferedReader.readLine());

					String[] strs = { "MAIL FROM: " + emailAddress + "\r\n", "QUIT" + "\r\n" };

					for(String str: strs) {
						bufferedWriter.write(str);
						bufferedWriter.flush();
						response = bufferedReader.readLine();
						System.out.println(response);
					}
				} catch (IOException e) {
					logger.error("", e.getStackTrace());
					throw new IOException();
				}

				return response;
			}
		};

		return this.templateMethod(clientCommand, emailAddress);
	}

	private String templateMethod(ClientCommand clientCommand, String emailAddress) {
		Socket socket = null;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;

		try {
			socket = new Socket("inbound.daou.co.kr", 25);
			socket.setSoTimeout(10 * 1000);
			socket.setReuseAddress(true);

			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

			return clientCommand.executeCommand(socket, bufferedReader, bufferedWriter, emailAddress);
		} catch (UnknownHostException e) {
			logger.error("", e.getStackTrace());
		} catch (IOException e) {
			logger.error("", e.getStackTrace());
		} finally {
			IOUtils.closeQuietly(bufferedReader);
			IOUtils.closeQuietly(bufferedWriter);
			IOUtils.closeQuietly(socket);
		}

		return null;
	}
}