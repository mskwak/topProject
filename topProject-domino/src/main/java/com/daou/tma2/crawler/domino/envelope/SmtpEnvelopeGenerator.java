package com.daou.tma2.crawler.domino.envelope;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;

import lotus.domino.Document;
import lotus.domino.NotesException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daou.tma2.crawler.domino.config.DominoServerConfig;
import com.daou.tma2.crawler.domino.config.journaling.DominoJournalingServerConfig;
import com.daou.tma2.crawler.domino.util.DominoDocumentUtil;

public class SmtpEnvelopeGenerator implements EnvelopeGenerator {
	private static final Logger logger = LoggerFactory.getLogger(SmtpEnvelopeGenerator.class);
	private static final int timeout = 60 * 1000;

	@Override
	public Envelope generateEnvelope(Document document, String metaPath, String emlPath, DirContext dirContext, DominoServerConfig dominoServerConfig) throws Exception {
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		SmtpEnvelope smtpEnvelope = new SmtpEnvelope();

		String defaultEsip = dominoServerConfig.getDominoServerIpAddress();
		String alternativeRecipient = ((DominoJournalingServerConfig) dominoServerConfig).getAlternativeRecipient();
		//TODO ehlo 구하는 로직 필요
		String ehlo = null;

		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		try {
			String edate = DominoDocumentUtil.getEnvelopeEdate(document);
			String esip = DominoDocumentUtil.getEnvelopeEsip(document, defaultEsip);
			String envFrom = DominoDocumentUtil.getEnvelopeFrom(document, dirContext, searchControls);
			List<String> envRcptList = DominoDocumentUtil.getEnvelopeRcpt(document, alternativeRecipient, dirContext, searchControls);

			String host = dominoServerConfig.getDominoTargetConfig().getIp();
			int port = dominoServerConfig.getDominoTargetConfig().getPort();

			Properties properties = new Properties();
			properties.put("mail.smtp.port", port);
			properties.put("mail.smtp.host", host);
			properties.put("mail.smtp.quitwait", false);
			properties.put("mail.smtp.timeout", timeout);
			properties.put("mail.smtp.connectiontimeout", timeout);
			properties.put("mail.smtp.writetimeout", timeout);

			Session session = Session.getInstance(properties);

			smtpEnvelope.setEhlo(ehlo);
			smtpEnvelope.setEdate(edate);
			smtpEnvelope.setEsip(esip);
			smtpEnvelope.setEnvFrom(envFrom);
			smtpEnvelope.setRcptList(envRcptList);
			smtpEnvelope.setEmlPath(emlPath);
			smtpEnvelope.setSession(session);

			fileWriter = new FileWriter(metaPath);
			bufferedWriter = new BufferedWriter(fileWriter);

			bufferedWriter.write("EDATE:" + edate + "\r\n");
			bufferedWriter.write("ESIP:" + esip + "\r\n");
			bufferedWriter.write("FROM:" + envFrom + "\r\n");
			bufferedWriter.write("TIMEOUT:" + timeout + "\r\n");
			bufferedWriter.write("TARGETIP:" + host + "\r\n");
			bufferedWriter.write("PORT:" + port + "\r\n");

			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("EDATE:" + edate + " ");
			stringBuffer.append("ESIP:" + esip + " ");
			stringBuffer.append("EF:" + envFrom + " ");
			stringBuffer.append("ER:");

			int envRcptSize = envRcptList.size();

			for (int i = 0; i < envRcptSize; i++) {
				bufferedWriter.write("TO:" + envRcptList.get(i) + "\r\n");

				stringBuffer.append(envRcptList.get(i));
				if (i == (envRcptSize - 1)) {
					stringBuffer.append(" ");
				} else {
					stringBuffer.append(",");
				}
			}

			stringBuffer.append("FILE:" + emlPath + " ");
			logger.info(stringBuffer.toString());
		} catch (NotesException e) {
			logger.error(e.toString(), e);
			throw e;
		} catch (NamingException e) {
			logger.error(e.toString(), e);
			throw e;
		} catch (IOException e) {
			logger.error(e.toString(), e);
			throw e;
		} finally {
			IOUtils.closeQuietly(bufferedWriter);
			IOUtils.closeQuietly(fileWriter);
		}

		return smtpEnvelope;
	}
}
