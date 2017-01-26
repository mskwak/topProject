package com.hangugi.tma2.crawler.domino.sender;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hangugi.tma2.crawler.domino.envelope.Envelope;

public abstract class MimeHandler {
	private static final Logger logger = LoggerFactory.getLogger(MimeHandler.class);

	public abstract boolean send(String documentUnid, Envelope envelope, Object targetAuthObject) throws Exception;

	public void deleteLocalDocument(String documentUnid, String... fileList) {
		for (String file : fileList) {
			File list = new File(file);
			if (list.exists()) {
				if (list.delete()) {
					logger.debug("FILE:" + list.getAbsolutePath() + " " + "DS:delete local document.");
				} else {
					logger.error("FILE:" + list.getAbsolutePath() + " " + "DS:failed to delete local document.");
				}
			} else {
				logger.info("FILE:" + list.getAbsolutePath() + " " + "DS:document does not exist.");
			}
		}
	}

	public void moveLocalDocument(String documentUnid, String retryDirString, String metaPath, String mimePath, String emlPath) {
		File[] srcList = { new File(metaPath), new File(mimePath), new File(emlPath) };

		for (int i = 0; i < srcList.length; i++) {
			if (srcList[i].exists()) {
				String[] args = srcList[i].toString().split("[/\\\\]");
				String fileNameStr = args[args.length - 1];
				File dst = new File(retryDirString + "/" + fileNameStr);
				srcList[i].renameTo(dst);

				logger.info("UNID:" + documentUnid + " " + "SRC:" + srcList[i] + " " + "DST:" + dst + " " + "DS:move local document.");
			}
		}
	}

	public abstract void recordUnidHistory(String documentUnid, String historyDirString) throws IOException;
}
