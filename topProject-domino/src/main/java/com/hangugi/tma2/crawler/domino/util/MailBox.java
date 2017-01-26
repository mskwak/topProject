package com.hangugi.tma2.crawler.domino.util;

import org.apache.commons.lang3.StringUtils;

import com.sun.mail.imap.protocol.BASE64MailboxEncoder;

public class MailBox {
	private static final String DOMINO_IMAP_NAMESPACE = "\\\\";
	private static final String DOMINO_INBOX_NAME = "($Inbox)";
	private static final String DOMINO_JUNK_MAILBOX_NAME = "($JunkMail)|JunkMail";
	private static final String TMS_IMAP_NAMESPACE = "\\.";
	private static final String TMS_MAILBOX_REPLACE_STRING = "_";
	private final static String[] UNUSED_CHARACTER = { "\\.", "/", "%", "\"", "\\\\", "'", "`", "\\*" };

	public static String getUtf7MailBoxName(String rawMailBoxName) {
		String replaceMailBoxName = null;

		if (StringUtils.isEmpty(rawMailBoxName)) {
			return replaceMailBoxName;
		}

		replaceMailBoxName = renameMailBoxName(rawMailBoxName);
		replaceMailBoxName = replaceSpecialCharacter(replaceMailBoxName);

		return BASE64MailboxEncoder.encode(replaceMailBoxName);
	}

	private static String replaceSpecialCharacter(String rawMailBoxName) {
		String replaceMailBoxName = replaceTmsImapNameSpace(rawMailBoxName);
		replaceMailBoxName = replaceDominoImapNameSpace(replaceMailBoxName);
		return replaceNotAvailableCharacter(replaceMailBoxName);
	}

	private static String replaceTmsImapNameSpace(String rawMailBoxName) {
		String replaceMailBoxName = rawMailBoxName;
		return replaceMailBoxName.replaceAll(TMS_IMAP_NAMESPACE, TMS_MAILBOX_REPLACE_STRING);
	}

	private static String replaceDominoImapNameSpace(String replaceMailBoxName) {
		return replaceMailBoxName.replaceAll(DOMINO_IMAP_NAMESPACE, TMS_IMAP_NAMESPACE);
	}

	private static String replaceNotAvailableCharacter(String replaceMailBoxName) {
		String replaceString = replaceMailBoxName;
		for (String string : UNUSED_CHARACTER) {
			if (replaceString.contains(string)) {
				replaceString = replaceString.replaceAll(string, TMS_MAILBOX_REPLACE_STRING);
			}
		}

		return replaceString;
	}

	/*
	 * ($Inbox)
	 * ($JunkMail)|JunkMail
	 * 
	 */
	private static String renameMailBoxName(String rawMailBoxName) {
		String replaceMailBoxName = rawMailBoxName;

		if (DOMINO_INBOX_NAME.equals(replaceMailBoxName)) {
			return "Inbox";
		} else if (DOMINO_JUNK_MAILBOX_NAME.equals(replaceMailBoxName)) {
			return "JunkMail";
		}

		return replaceMailBoxName;
	}
}
