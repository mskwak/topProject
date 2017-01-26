package com.hangugi.tma2.crawler.domino.envelope;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;

import lotus.domino.Document;
import lotus.domino.NotesException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hangugi.tma2.crawler.domino.config.DominoServerConfig;
import com.hangugi.tma2.crawler.domino.config.journaling.DominoJournalingServerConfig;
import com.hangugi.tma2.crawler.domino.util.DominoDocumentUtil;

public class HttpEnvelopeGenerator implements EnvelopeGenerator {
	private static final Logger logger = LoggerFactory.getLogger(HttpEnvelopeGenerator.class);

	@Override
	public Envelope generateEnvelope(Document document, String metaPath, String emlPath, DirContext dirContext, DominoServerConfig dominoServerConfig) throws Exception {
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		HttpEnvelope httpEnvelope = new HttpEnvelope();

		String defaultEsip = dominoServerConfig.getDominoServerIpAddress();
		String alternativeRecipient = ((DominoJournalingServerConfig) dominoServerConfig).getAlternativeRecipient();

		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		try {
			String edate = DominoDocumentUtil.getEnvelopeEdate(document);
			String esip = DominoDocumentUtil.getEnvelopeEsip(document, defaultEsip);
			String envFrom = DominoDocumentUtil.getEnvelopeFrom(document, dirContext, searchControls);
			List<String> envRcptList = DominoDocumentUtil.getEnvelopeRcpt(document, alternativeRecipient, dirContext, searchControls);

			httpEnvelope.setEsip(esip);
			httpEnvelope.setEdate(edate);
			httpEnvelope.setEnvFrom(envFrom);
			httpEnvelope.setRcptList(envRcptList);
			httpEnvelope.setEmlPath(emlPath);

			fileWriter = new FileWriter(metaPath);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write("EDATE:" + edate + "\r\n");
			bufferedWriter.write("ESIP:" + esip + "\r\n");
			bufferedWriter.write("FROM:" + envFrom + "\r\n");

			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("EDATE:" + edate + " ");
			stringBuffer.append("ESIP:" + esip + " ");
			stringBuffer.append("EF:" + envFrom + " ");
			stringBuffer.append("ER:");

			int rcptSize = envRcptList.size();

			for (int i = 0; i < rcptSize; i++) {
				bufferedWriter.write("TO:" + envRcptList.get(i) + "\r\n");

				stringBuffer.append(envRcptList.get(i));
				if (i == (rcptSize - 1)) {
					stringBuffer.append(" ");
				} else {
					stringBuffer.append(",");
				}
			}

			stringBuffer.append("FILE:" + emlPath + " ");
			logger.info(stringBuffer.toString());
		} catch (IOException e) {
			logger.error(e.toString(), e);
			throw e;
		} catch (NotesException e) {
			logger.error(e.toString(), e);
			throw e;
		} catch (NamingException e) {
			logger.error(e.toString(), e);
			throw e;
		} finally {
			IOUtils.closeQuietly(bufferedWriter);
			IOUtils.closeQuietly(fileWriter);
		}

		return httpEnvelope;
	}
}
