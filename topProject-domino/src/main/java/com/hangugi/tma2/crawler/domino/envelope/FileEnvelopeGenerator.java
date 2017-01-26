package com.hangugi.tma2.crawler.domino.envelope;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Session;
import javax.naming.directory.DirContext;

import lotus.domino.Document;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hangugi.tma2.crawler.domino.config.DominoServerConfig;

public class FileEnvelopeGenerator implements EnvelopeGenerator {
	private static final Logger logger = LoggerFactory.getLogger(FileEnvelopeGenerator.class);

	@Override
	public Envelope generateEnvelope(Document document, String metaPath, String emlPath, DirContext dirContext, DominoServerConfig dominoServerConfig) throws Exception {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		Envelope envelope = null;
		try {
			fileReader = new FileReader(metaPath);
			bufferedReader = new BufferedReader(fileReader);
			String protocol = dominoServerConfig.getDominoTargetConfig().getProtocol();

			if ("http".equalsIgnoreCase(protocol)) {
				envelope = this.generateHttpEnvelope(bufferedReader, emlPath);
			} else if ("imap".equalsIgnoreCase(protocol)) {
				envelope = this.generateImapEnvelope(bufferedReader, emlPath);
			} else if ("smtp".equalsIgnoreCase(protocol)) {
				envelope = this.generateSmtpEnvelope(bufferedReader, emlPath);
			}
		} catch (IOException e) {
			logger.error(e.toString(), e);
			throw e;
		} finally {
			IOUtils.closeQuietly(fileReader);
			IOUtils.closeQuietly(bufferedReader);
		}

		return envelope;
	}

	private Envelope generateHttpEnvelope(BufferedReader bufferedReader, String emlPath) throws IOException {
		HttpEnvelope httpEnvelope = new HttpEnvelope();
		String line = null;
		List<String> envRcptList = new ArrayList<String>();

		while ((line = bufferedReader.readLine()) != null) {
			line = line.trim();

			if (line.length() == 0 || line.startsWith("#")) {
				continue;
			}

			String[] args = line.split(":", 2);

			if (args.length < 2) {
				continue;
			}

			String key = args[0];
			String val = args[1];

			if (key.toUpperCase().startsWith("EDATE")) {
				httpEnvelope.setEdate(val);
			} else if (key.toUpperCase().startsWith("ESIP")) {
				httpEnvelope.setEsip(val);
			} else if (key.toUpperCase().startsWith("FROM")) {
				httpEnvelope.setEnvFrom(val);
			} else if (key.toUpperCase().startsWith("TO")) {
				envRcptList.add(val);
			}
		}

		httpEnvelope.setRcptList(envRcptList);
		httpEnvelope.setEmlPath(emlPath);

		return httpEnvelope;
	}

	private Envelope generateSmtpEnvelope(BufferedReader bufferedReader, String emlPath) throws IOException {
		SmtpEnvelope smtpEnvelope = new SmtpEnvelope();
		Properties properties = new Properties();
		properties.put("mail.smtp.quitwait", false);

		String line = null;
		List<String> envRcptList = new ArrayList<String>();

		while ((line = bufferedReader.readLine()) != null) {
			line = line.trim();

			if (line.length() == 0 || line.startsWith("#")) {
				continue;
			}

			String[] args = line.split(":", 2);

			if (args.length < 2) {
				continue;
			}

			String key = args[0].trim();
			String val = args[1].trim();

			if (key.toUpperCase().startsWith("EDATE")) {
				smtpEnvelope.setEdate(val);
			} else if (key.toUpperCase().startsWith("ESIP")) {
				smtpEnvelope.setEsip(val);
			} else if (key.toUpperCase().startsWith("FROM")) {
				smtpEnvelope.setEnvFrom(val);
			} else if (key.toUpperCase().startsWith("TO")) {
				envRcptList.add(val);
			} else if (key.toUpperCase().startsWith("TARGETIP")) {
				properties.put("mail.smtp.host", val);
			} else if (key.toUpperCase().startsWith("PORT")) {
				properties.put("mail.smtp.port", val);
			} else if (key.toUpperCase().startsWith("TIMEOUT")) {
				properties.put("mail.smtp.timeout", val);
				properties.put("mail.smtp.connectiontimeout", val);
				properties.put("mail.smtp.writetimeout", val);
			}
		}

		Session session = Session.getInstance(properties);

		smtpEnvelope.setRcptList(envRcptList);
		smtpEnvelope.setEmlPath(emlPath);
		smtpEnvelope.setSession(session);

		return smtpEnvelope;
	}

	private Envelope generateImapEnvelope(BufferedReader bufferedReader, String emlPath) throws IOException, ParseException {
		ImapEnvelope imapEnvelope = new ImapEnvelope();
		String line = null;
		List<String> envRcptList = new ArrayList<String>();

		while ((line = bufferedReader.readLine()) != null) {
			line = line.trim();

			if (line.length() == 0 || line.startsWith("#")) {
				continue;
			}

			String[] args = line.split(":", 2);

			if (args.length < 2) {
				continue;
			}

			String key = args[0];
			String val = args[1];

			if (key.toUpperCase().startsWith("INTERNALDATE")) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-EEE-yyyy HH:mm:ss Z", Locale.ENGLISH);
				Date date = simpleDateFormat.parse(val);
				imapEnvelope.setInternalDate(date);
			} else if (key.toUpperCase().startsWith("BOX")) {
				imapEnvelope.setMailBoxName(val);
			} else if (key.toUpperCase().startsWith("TO")) {
				envRcptList.add(val);
			}
		}

		imapEnvelope.setRcptList(envRcptList);
		imapEnvelope.setEmlPath(emlPath);

		return imapEnvelope;
	}
}
