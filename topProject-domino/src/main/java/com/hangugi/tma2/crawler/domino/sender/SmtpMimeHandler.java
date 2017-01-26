package com.hangugi.tma2.crawler.domino.sender;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hangugi.tma2.crawler.domino.envelope.Envelope;
import com.hangugi.tma2.crawler.domino.envelope.SmtpEnvelope;
import com.hangugi.tma2.crawler.domino.util.EmailValidator;



public class SmtpMimeHandler extends MimeHandler {
	private static final Logger logger = LoggerFactory.getLogger(SmtpMimeHandler.class);
	private static final int IOTIMEOUT = 600 * 1000;
	private static final String CRLF = "\r\n";
	private static final String DATA = "DATA";
	private static final String QUIT = "QUIT";
	private static final String EOF = ".";
	private Socket socket = null;
	private BufferedReader bufferedReader = null;
	private BufferedWriter bufferedWriter = null;

	@Override
	public boolean send(String documentUnid, Envelope envelope, Object targetAuthObject) throws Exception {
		SmtpEnvelope smtpEnvelope = (SmtpEnvelope) envelope;
		boolean isSuccess = false;
		String edate = smtpEnvelope.getEdate();
		String esip = smtpEnvelope.getEsip();
		String envFrom = smtpEnvelope.getEnvFrom();
		List<String> envRcptList = smtpEnvelope.getRcptList();

		String smtpServer = (String) targetAuthObject;
		String[] hostAndPort = smtpServer.split(":");
		String host = hostAndPort[0];
		int port = Integer.parseInt(hostAndPort[1]);
		boolean continueJob = false;

		try {
			this.connect(host, port);

			this.write("ESIP " + esip);
			this.write("EDATE " + edate);
			this.writeMailFrom("MAIL FROM: " + envFrom);

			for (String recipient : envRcptList) {
				if (EmailValidator.isOnlyAscii(recipient)) {
					if (this.writeRcptTo("RCPT TO: " + recipient)) {
						continueJob = true;
					}
				} else {
					logger.error("skip recipient. invalid character: " + recipient);
				}
			}

			if (continueJob) {
				this.write(DATA);
				this.writeData(smtpEnvelope.getEmlPath());
				this.write(CRLF + EOF);

				isSuccess = true;
			}
		} catch (UnknownHostException e) {
			logger.error(e.toString() + " DS:failed to send mail.", e);
			throw new UnknownHostException(e.toString() + " DS:failed to send mail.");
		} catch (IOException e) {
			logger.error(e.toString() + " DS:failed to send mail.", e);
			throw new IOException(e.toString() + " DS:failed to send mail.");
		} finally {
			this.write(QUIT);
			IOUtils.closeQuietly(this.bufferedWriter);
			IOUtils.closeQuietly(this.bufferedReader);
			IOUtils.closeQuietly(this.socket);

			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("UNID:" + documentUnid + " ");
			if (isSuccess) {
				stringBuffer.append("DS:send success");
			} else {
				stringBuffer.append("DS:send failed");
			}

			logger.info(stringBuffer.toString());
		}

		return isSuccess;
	}

	private void connect(String smtpServer, int port) throws IOException {
		try {
			this.socket = new Socket(smtpServer, port);
			this.socket.setSoTimeout(IOTIMEOUT);
			this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
			String response = this.bufferedReader.readLine();

			if (response == null || (response != null && !response.startsWith("2"))) {
				throw new IOException("failed to connect the server:" + smtpServer);
			}
		} catch (UnknownHostException e) {
			throw new UnknownHostException("failed to connect the server:" + smtpServer);
		} catch (IOException e) {
			throw new IOException("failed to connect the server:" + smtpServer, e);
		}
	}

	private void write(String command) throws IOException {
		try {
			this.bufferedWriter.write(command + CRLF);
			this.bufferedWriter.flush();
			String response = this.bufferedReader.readLine();

			logger.info("COMMAND:" + command.trim() + " RESPONSE:" + response.trim());

			if (response == null || (response != null && !response.startsWith("2") && !response.startsWith("3"))) {
				throw new IOException("failed to execute command: " + command);
			}

		} catch (IOException e) {
			throw new IOException("failed to execute command: " + command);
		}
	}

	private void writeMailFrom(String command) throws IOException {
		String[] mailFroms = { command, "MAIL FROM: <>" };

		for (String mailFrom : mailFroms)
		{
			try {
				this.bufferedWriter.write(mailFrom + CRLF);
				this.bufferedWriter.flush();
				String response = this.bufferedReader.readLine();

				logger.info("COMMAND:" + mailFrom.trim() + " RESPONSE:" + response.trim());

				if (response.startsWith("2")) {
					break;
				} else if (response.startsWith("5")) {
					continue;
				} else {
					throw new IOException("failed to execute command: " + mailFrom);
				}
			} catch (IOException e) {
				throw new IOException("failed to execute command: " + mailFrom);
			}
		}
	}

	private boolean writeRcptTo(String command) throws IOException {
		try {
			this.bufferedWriter.write(command + CRLF);
			this.bufferedWriter.flush();
			String response = this.bufferedReader.readLine();

			logger.info("COMMAND:" + command.trim() + " RESPONSE:" + response.trim());

			if (response.startsWith("2")) {
				return true;
			} else if (response.startsWith("5")) {
				return false;
			} else {
				throw new IOException("failed to execute command: " + command);
			}
		} catch (IOException e) {
			throw new IOException("failed to execute command: " + command);
		}
	}

	private void writeData(String emlPath) throws IOException {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(new File(emlPath)));
			String line = null;

			while ((line = bufferedReader.readLine()) != null) {
				if (".".equals(line)) {
					line = "..";
				}

				this.bufferedWriter.write(line + CRLF);
			}
		} catch (FileNotFoundException e) {
			logger.error(e.toString() + ":" + emlPath, e);
			throw e;
		} catch (IOException e) {
			logger.error(e.toString() + ":" + emlPath, e);
			throw e;
		} finally {
			IOUtils.closeQuietly(bufferedReader);
		}
	}

	@SuppressWarnings("unused")
	private void writeDataDeprecated(String emlPath) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(emlPath);
		byte[] buffer = new byte[1024 * 8];
		try {
			for (;;) {
				int i = fileInputStream.read(buffer);

				if (i == -1) {
					break;
				}

				this.socket.getOutputStream().write(buffer, 0, i);
			}
		} catch (IOException e) {
			logger.error(e.toString(), e);
			throw e;
		} finally {
			IOUtils.closeQuietly(fileInputStream);
		}
	}

	@Override
	public void recordUnidHistory(String documentUnid, String historyDirString) {
		return;
	}
}


/*
public class SmtpMimeHandler extends MimeHandler {
	private static final Logger logger = LoggerFactory.getLogger(SmtpMimeHandler.class);

	@Override
	public boolean send(String documentUnid, Envelope envelope, Object targetAuthObject) throws Exception {
		SmtpEnvelope smtpEnvelope = (SmtpEnvelope) envelope;
		Session session = smtpEnvelope.getSession();
		SMTPTransport smtpTransport = null;
		InputStream inputStream = null;
		boolean isSuccess = false;

		try {
			smtpTransport = (SMTPTransport) session.getTransport("smtp");
			smtpTransport.connect();

			// set from
			InternetAddress envFrom = new InternetAddress(smtpEnvelope.getEnvFrom());
			// set rcpt 
			List<String> envRcptList = smtpEnvelope.getRcptList();
			int envRcptSize = envRcptList.size();
			InternetAddress[] envRcptArray = new InternetAddress[envRcptSize];

			for (int i = 0; i < envRcptSize; i++) {
				envRcptArray[i] = new InternetAddress(envRcptList.get(i));
			}

			// set data
			inputStream = new FileInputStream(smtpEnvelope.getEmlPath());
			// send esip
			smtpTransport.simpleCommand("ESIP " + smtpEnvelope.getEsip());
			// send edate
			smtpTransport.simpleCommand("EDATE " + smtpEnvelope.getEdate());

			SMTPMessage smtpMessage = new SMTPMessage(session, inputStream);
			smtpMessage.setFrom(envFrom);
			smtpMessage.setRecipients(RecipientType.TO, envRcptArray);

			Transport.send(smtpMessage);
			isSuccess = true;
		} catch (NoSuchProviderException e) {
			logger.error("NoSuchProviderException Occurs: " + smtpEnvelope.getEmlPath(), e);
			throw e;
		} catch (MessagingException e) {
			logger.error("MessagingException Occurs: " + smtpEnvelope.getEmlPath(), e);
			throw e;
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException Occurs: " + smtpEnvelope.getEmlPath(), e);
			throw e;
		} finally {
			IOUtils.closeQuietly(inputStream);

			try {
				smtpTransport.close();
			} catch (MessagingException e) {
				//NOPMD
			}

			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("UNID:" + documentUnid + " ");
			if (isSuccess) {
				stringBuffer.append("DS:send success");
			} else {
				stringBuffer.append("DS:send failed");
			}

			logger.info(stringBuffer.toString());
		}

		return isSuccess;
	}

	@Override
	public void recordUnidHistory(String documentUnid, String historyDirString) {
		return;
	}
}
*/