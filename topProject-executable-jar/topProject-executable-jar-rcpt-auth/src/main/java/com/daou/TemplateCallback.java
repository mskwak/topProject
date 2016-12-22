package com.daou;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.solr.common.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.daou.command.client.ClientCommand;

//재시도규칙
//* connection timeout 2초이고 실패시 재시도를 1회한다.
//* io timeout 5초이고 실패시 재시도는 하지 않는다.
//* Response의 에러코드가 201, 207, 217, 218인 경우에는 재시도를 1회한다.

@Component
public class TemplateCallback {
	private static final Logger logger = LoggerFactory.getLogger(TemplateCallback.class);
	@Autowired
	private String nateServerAddress;
	@Autowired
	private int nateServerPort;
	private static final int connectionTimeout = 2;
	private static final int retryCountForConnectionTimeout = 2;
	private static final int sleepTimeForRetry = 1;
	private static final int ioTimeout = 5;

	public String getAllowList(String senderEmailAddress, String recipientEmailAddress) {
		ClientCommand clientCommand = new ClientCommand() {
			@Override
			public String executeCommand(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter,
					String senderEmailAddress, String recipientEmailAddress) throws IOException {
				String line = null;
				List<String> allowList = new ArrayList<String>();

				try {
					bufferedWriter.write(CommandParserUtils.getCommand(senderEmailAddress, recipientEmailAddress));
					bufferedWriter.flush();

					while((line = bufferedReader.readLine()) != null) {
//						재시도 규칙에 의해 SocketTimeoutException()를 던진다.
						if(CommandParserUtils.isRetry(line)) {
							throw new SocketTimeoutException();
						}

						if(!CommandParserUtils.hasFilter(line)) {
							break;
						}

						String senderEmailAddressOrSenderDomain = CommandParserUtils.getAllowSenderEmailAddressOrSenderDomain(line);

						if(!StringUtils.isEmpty(senderEmailAddressOrSenderDomain)) {
							allowList.add(senderEmailAddressOrSenderDomain);
						}
					}
				} catch (IOException e) {
					throw new IOException();
				}

				return CommandParserUtils.convertRule(allowList);
			}
		};

		return this.templateMethod(clientCommand, senderEmailAddress, recipientEmailAddress);
	}

	private String templateMethod(ClientCommand clientCommand, String senderEmailAddress, String recipientEmailAddress) {
		Socket socket = null;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;

		for(int i = 0; i < retryCountForConnectionTimeout; i++) {
			try {
				SocketAddress socketAddress = new InetSocketAddress(this.nateServerAddress, this.nateServerPort);
				socket = new Socket();
				socket.connect(socketAddress, connectionTimeout * 1000);
				socket.setSoTimeout(ioTimeout * 1000);
				socket.setReuseAddress(true);

				bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

				return clientCommand.executeCommand(socket, bufferedReader, bufferedWriter, senderEmailAddress, recipientEmailAddress);
			} catch (UnknownHostException e) {
				logger.error("UnknownHostException", e);
			} catch (SocketTimeoutException e) { // 재시도규칙. connection timeout 2초이고 실패시 재시도를 1회한다.
				logger.error("SocketTimeoutException", e);

				try {
					Thread.sleep(sleepTimeForRetry * 1000);
				} catch (InterruptedException e1) {
					logger.error("InterruptedException", e1);
				}

				continue;
			} catch (SocketException e) {
				logger.error("SocketException", e);
			} catch (IOException e) {
				logger.error("IOException", e);
			} finally {
				try {
					bufferedWriter.write("quit");
					bufferedWriter.flush();
				} catch (IOException e) {
				}
				IOUtils.closeQuietly(bufferedReader);
				IOUtils.closeQuietly(bufferedWriter);
				IOUtils.closeQuietly(socket);
			}
		}

		return null;
	}
}