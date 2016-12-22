package com.daou.tma2.crawler.domino.document;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.naming.directory.DirContext;

import lotus.domino.Document;
import lotus.domino.NotesException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daou.tma2.crawler.domino.config.DominoServerConfig;
import com.daou.tma2.crawler.domino.config.journaling.DominoJournalingServerConfig;
import com.daou.tma2.crawler.domino.envelope.Envelope;
import com.daou.tma2.crawler.domino.envelope.EnvelopeGenerator;
import com.daou.tma2.crawler.domino.util.DominoUtil;

public class DominoDocument {
	private static final Logger logger = LoggerFactory.getLogger(DominoDocument.class);
	private final String documentUnid;
	private final Document document;

	public DominoDocument(String documentUnid, Document document) {
		this.documentUnid = documentUnid;
		this.document = document;
	}

	public synchronized boolean isArchivingTarget() throws NotesException {
		if (this.isDeletedDocument()) {
			return false;
		}

		if (!this.isValidDocument()) {
			return false;
		}

		if (this.isValidDocumentByForm()) {
			return true;
		}

		// TODO 타입이 저널링일 때, Memo도 아니고 Reply도 아닌 문서는 삭제해야 한다. 보존 시 계속 저널링 DB에 쌓이게 된다.

		return false;
	}

	private synchronized boolean isDeletedDocument() throws NotesException {
		if (this.document.isDeleted()) {
			logger.info("UNID:" + this.documentUnid + " " + "DS:document is already deleted.");
			return true;
		}
		return false;
	}

	private synchronized boolean isValidDocument() throws NotesException {
		if (this.document.isValid()) {
			return true;
		}
		logger.info("UNID:" + this.documentUnid + " " + "DS:document is invalid.");
		return false;
	}

	private synchronized boolean isValidDocumentByForm() throws NotesException {
		String formValue = this.document.getItemValueString("Form");
		if ("Memo".equals(formValue) || "Reply".equals(formValue)) {
			return true;
		}

		if (this.document.remove(true)) {
			logger.info("UNID:" + this.documentUnid + " " + "FORM:" + formValue + " " + "DS:form is not mail. delete server document.");
		}

		return false;
	}

	public void deleteServerDocument(String metaPath, String emlPath, DominoServerConfig dominoServerConfig) throws NotesException {
		if (dominoServerConfig instanceof DominoJournalingServerConfig) {
			if (((DominoJournalingServerConfig) dominoServerConfig).isDocumentDeletion()) {
				try {
					if (this.document.remove(true)) {
						logger.info("UNID:" + this.documentUnid + " DS:delete server document.");
					} else {
						logger.info("UNID:" + this.documentUnid + " DS:failed to delete server document.");
					}
				} catch (NotesException e) {
					logger.info("UNID:" + this.documentUnid + " DS:failed to delete server document.", e);
				}
			}
		}
	}

	public Envelope generateEnvelope(String targetProtocol, String metaPath, String emlPath, DirContext dirContext, DominoServerConfig dominoServerConfig) throws Exception {
		EnvelopeGenerator envelopeGenerator = DominoUtil.getEnvelopeGenerator(targetProtocol);
		Envelope envelope = envelopeGenerator.generateEnvelope(this.document, metaPath, emlPath, dirContext, dominoServerConfig);
		return envelope;
	}

	public void generateEml(String mimePath, String emlPath, DominoServerConfig dominoServerConfig) throws IOException {
		this.generateMimeFile(mimePath, dominoServerConfig);
		this.generateEmlFile(mimePath, emlPath);
	}

	private void generateMimeFile(String mimePath, DominoServerConfig dominoServerConfig) throws IOException {
		String ip = dominoServerConfig.getDominoServerIpAddress();
		int port = dominoServerConfig.getHttpPort();
		String protocol = dominoServerConfig.getHttpProtocol();
		String journalDbName = dominoServerConfig.getJournalDbName();
		String userName = dominoServerConfig.getUserName();
		String password = dominoServerConfig.getPassword();

		HttpHost httpHost = new HttpHost(ip, port, protocol);
		HttpGet httpGet = new HttpGet("/" + journalDbName + "/0/" + this.documentUnid + "/?OpenDocument&Form=l_MailMessageHeader&PresetFields=FullMessage;1");
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
		defaultHttpClient.getCredentialsProvider().setCredentials(new AuthScope(ip, port), new UsernamePasswordCredentials(userName, password));

		HttpResponse responsee;
		OutputStream outputStream = null;
		InputStream inputStream = null;

		try {
			responsee = defaultHttpClient.execute(httpHost, httpGet);

			if (responsee.getStatusLine().getStatusCode() != 200) {
				logger.info("UNID:" + this.documentUnid + " " + "RE:" + responsee.getStatusLine().toString().trim());
				throw new IOException();
			}

			HttpEntity entity = responsee.getEntity();

			byte[] buffer = new byte[1024 * 8];
			outputStream = new FileOutputStream(mimePath);
			inputStream = entity.getContent();

			for (;;) {
				int i = inputStream.read(buffer);

				if (i == -1) {
					break;
				}

				outputStream.write(buffer, 0, i);
			}

			EntityUtils.consume(entity);
		} catch (IOException e) {
			logger.error(e.toString(), e);
			throw e;
		} finally {
			defaultHttpClient.getConnectionManager().shutdown();
			IOUtils.closeQuietly(outputStream);
			IOUtils.closeQuietly(inputStream);
		}
	}

	private void generateEmlFile(String mimePath, String emlPath) throws IOException {
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;

		try {
			bufferedReader = new BufferedReader(new FileReader(mimePath));
			bufferedWriter = new BufferedWriter(new FileWriter(emlPath));
			String line = null;

			while ((line = bufferedReader.readLine()) != null) {
				line = line.replaceAll("<br>$", "\r\n");
				line = line.replaceAll("&quot;", "\"");
				line = line.replaceAll("&lt;", "<");
				line = line.replaceAll("&gt;", ">");
				line = line.replaceAll("&amp;", "&");
				line = line.replaceAll("&nbsp;", " ");

				bufferedWriter.write(line);
			}
		} catch (IOException e) {
			logger.error(e.toString(), e);
			throw e;
		} finally {
			IOUtils.closeQuietly(bufferedReader);
			IOUtils.closeQuietly(bufferedWriter);
		}
	}
}
