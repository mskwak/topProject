package com.hangugi.tma2.crawler.domino.envelope;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.naming.directory.DirContext;

import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.NotesException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hangugi.tma2.crawler.domino.config.DominoServerConfig;
import com.hangugi.tma2.crawler.domino.config.migration.DominoMigrationServerConfig;
import com.hangugi.tma2.crawler.domino.util.MailBox;

public class ImapEnvelopeGenerator implements EnvelopeGenerator {
	private static final Logger logger = LoggerFactory.getLogger(ImapEnvelopeGenerator.class);

	@Override
	public Envelope generateEnvelope(Document document, String metaPath, String emlPath, DirContext dirContext, DominoServerConfig dominoServerConfig) throws Exception {
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		ImapEnvelope imapEnvelope = new ImapEnvelope();

		try {
			Date internalDate = this.getInternalDate();
			String internalDateString = this.getInternalDateString();
			String mailBoxName = this.getUtf7MailBoxName(document);
			String recipient = ((DominoMigrationServerConfig) dominoServerConfig).getImapUser();
			List<String> envRcptList = new ArrayList<String>();

			fileWriter = new FileWriter(metaPath);
			bufferedWriter = new BufferedWriter(fileWriter);

			bufferedWriter.write("INTERNALDATE:" + internalDateString + "\r\n");
			bufferedWriter.write("TO:" + recipient + "\r\n");
			bufferedWriter.write("BOX:" + mailBoxName + "\r\n");

			envRcptList.add(recipient);

			imapEnvelope.setInternalDate(internalDate);
			imapEnvelope.setRcptList(envRcptList);
			imapEnvelope.setMailBoxName(mailBoxName);
			imapEnvelope.setEmlPath(emlPath);
		} catch (IOException e) {
			logger.error(e.toString(), e);
			throw e;
		} catch (NotesException e) {
			logger.error(e.toString(), e);
			throw e;
		} finally {
			IOUtils.closeQuietly(bufferedWriter);
			IOUtils.closeQuietly(fileWriter);
		}

		return imapEnvelope;
	}

	private String getInternalDateString() throws NotesException {
		Date date = this.getInternalDate();
		// 16-Sep-2013 14:17:59 +0900
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-EEE-yyyy HH:mm:ss Z", Locale.ENGLISH);

		return simpleDateFormat.format(date);
	}

	private Date getInternalDate() throws NotesException {
		String[] stringArray = { "DeliveredDate", "Received", "PostedDate" };

		for (String fieldName : stringArray) {
			Date edate = this.getInternalDateFromDeliveredDateOrReceivedOrPostedDate(fieldName);
			if (edate != null) {
				return edate;
			}
		}

		return new Date();
	}

	private Date getInternalDateFromDeliveredDateOrReceivedOrPostedDate(String fieldName) throws NotesException {
		Date edate = null;

		if ("Received".equalsIgnoreCase(fieldName)) {
			edate = this.getInternalDateFromReceived(fieldName);
		} else {
			edate = this.getInternalDateFromDeliveredDateOrPostedDate(fieldName);
		}

		return edate;
	}

	//TODO
	private Date getInternalDateFromReceived(String fieldName) { //NOPMD
		return null;
	}

	// TODO
	@SuppressWarnings("rawtypes")
	private Date getInternalDateFromDeliveredDateOrPostedDate(String fieldName) throws NotesException { //NOPMD
		//List edateList = this.dominoDocumentSet.getDocument().getItemValue(fieldName);
		List edateList = new ArrayList();

		if (edateList.isEmpty()) {
			return null;
		}

		DateTime dateTime = (DateTime) edateList.get(0);
		Date date = dateTime.toJavaDate();

		return date;
	}

	@SuppressWarnings("rawtypes")
	private String getUtf7MailBoxName(Document document) throws NotesException {
		Vector vector = document.getFolderReferences();
		int size = vector.size();
		String mailBoxName = null;

		// TODO 메일함 참조가 2개 이상일때는 어떻게 하냐?
		for (int j = 0; j < size; j++) {
			mailBoxName = (String) vector.elementAt(j);
		}

		// TODO Inbox로 넣을지 정책적으로 결정 필요
		if (StringUtils.isEmpty(mailBoxName)) {
			return "Inbox";
		}

		return MailBox.getUtf7MailBoxName(mailBoxName);
	}
}
