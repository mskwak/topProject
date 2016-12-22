package com.daou.tma2.crawler.domino.sender;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daou.tma2.crawler.domino.config.Constants;
import com.daou.tma2.crawler.domino.envelope.Envelope;
import com.daou.tma2.crawler.domino.envelope.ImapEnvelope;

public class ImapMimeHandler extends MimeHandler {
	private static final Logger logger = LoggerFactory.getLogger(ImapMimeHandler.class);

	@SuppressWarnings("unchecked")
	@Override
	public boolean send(String documentUnid, Envelope envelope, Object targetAuthObject) throws Exception {
		Map<String, Object> imapAuthObjectMap = (Map<String, Object>) targetAuthObject;
		Session session = (Session) imapAuthObjectMap.get("session");
		Store store = (Store) imapAuthObjectMap.get("store");
		ImapEnvelope imapEnvelope = (ImapEnvelope) envelope;
		File emlFile = new File(envelope.getEmlPath());
		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream(emlFile);

			MimeMessage message = new MimeMessage(session, inputStream);

			message.setSentDate(imapEnvelope.getInternalDate());
			message.setFlags(imapEnvelope.getFlags(), true);

			Folder folder = store.getFolder(imapEnvelope.getMailBoxName());

			if (!folder.exists()) {
				folder.create(Folder.READ_WRITE);
			}

			folder.appendMessages(new Message[] { message });
		} catch (FileNotFoundException e) {
			logger.info("FILE:" + emlFile.getAbsolutePath() + " " + "DS:append failed.");
			throw e;
		} catch (MessagingException e) {
			logger.info("FILE:" + emlFile.getAbsolutePath() + " " + "DS:append failed.");
			throw e;
		} finally {
			IOUtils.closeQuietly(inputStream);
		}

		logger.info("UNID:" + documentUnid + " " + "FILE:" + emlFile.getAbsolutePath() + " " + "DS:append success.");

		return true;
	}

	@Override
	public void recordUnidHistory(String documentUnid, String historyDirString) throws IOException {
		String fullPathString = historyDirString + "/" + Constants.DOMINO_HISTORY_FILE_BY_UNID;
		File fullPathFile = new File(fullPathString);
		BufferedWriter bufferedWriter = null;

		try {
			bufferedWriter = new BufferedWriter(new FileWriter(fullPathFile, true));
			bufferedWriter.write(documentUnid);
			bufferedWriter.newLine();
			bufferedWriter.flush();
		} catch (IOException e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(bufferedWriter);
		}
	}
}