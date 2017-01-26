package com.hangugi.tma2.crawler.domino.util;

import java.io.File;

import com.hangugi.tma2.crawler.domino.config.DominoServerConfig;
import com.hangugi.tma2.crawler.domino.config.journaling.DominoJournalingServerConfig;
import com.hangugi.tma2.crawler.domino.envelope.EnvelopeGenerator;
import com.hangugi.tma2.crawler.domino.envelope.FileEnvelopeGenerator;
import com.hangugi.tma2.crawler.domino.envelope.HttpEnvelopeGenerator;
import com.hangugi.tma2.crawler.domino.envelope.ImapEnvelopeGenerator;
import com.hangugi.tma2.crawler.domino.envelope.SmtpEnvelopeGenerator;
import com.hangugi.tma2.crawler.domino.sender.HttpMimeHandler;
import com.hangugi.tma2.crawler.domino.sender.ImapMimeHandler;
import com.hangugi.tma2.crawler.domino.sender.MimeHandler;
import com.hangugi.tma2.crawler.domino.sender.SmtpMimeHandler;

public class DominoUtil {
	//private static final Logger logger = LoggerFactory.getLogger(DominoUtil.class);

	public static EnvelopeGenerator getEnvelopeGenerator(String targetProtocol) {
		if ("http".equalsIgnoreCase(targetProtocol)) {
			return new HttpEnvelopeGenerator();
		} else if ("imap".equalsIgnoreCase(targetProtocol)) {
			return new ImapEnvelopeGenerator();
		} else if ("file".equalsIgnoreCase(targetProtocol)) {
			return new FileEnvelopeGenerator();
		} else if ("smtp".equalsIgnoreCase(targetProtocol)) {
			return new SmtpEnvelopeGenerator();
		}

		return null;
	}

	public static MimeHandler getMimeHandler(String protocol) {
		if ("http".equalsIgnoreCase(protocol)) {
			return new HttpMimeHandler();
		} else if ("imap".equalsIgnoreCase(protocol)) {
			return new ImapMimeHandler();
		} else if ("smtp".equalsIgnoreCase(protocol)) {
			return new SmtpMimeHandler();
		}

		return null;
	}

	public static String getMetaPath(String documentUnid, String unidDirString) {
		return unidDirString + "/" + Long.toString(System.currentTimeMillis() % 1021) + "/" + documentUnid;
	}

	public static boolean isSend(DominoServerConfig dominoServerConfig) {
		if (dominoServerConfig instanceof DominoJournalingServerConfig) {
			return ((DominoJournalingServerConfig) dominoServerConfig).isSend();
		} else {
			return true;
		}
	}

	public static boolean isValid(String... list) {
		for (String str : list) {
			File file = new File(str);

			if (!file.exists()) {
				return false;
			}

			if (file.length() == 0) {
				return false;
			}
		}

		return true;
	}
}
