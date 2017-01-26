package com.hangugi.tma2.crawler.domino.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MailDateFormat;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.NotesException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DominoDocumentUtil {
	private static final Logger logger = LoggerFactory.getLogger(DominoDocumentUtil.class);

	public static String getEnvelopeEdate(Document document) throws NotesException {
		String[] stringArray = { "DeliveredDate", "Received", "PostedDate" };

		for (String fieldName : stringArray) {
			String edate = getEnvelopeEdateFromDeliveredDateOrReceivedOrPostedDate(document, fieldName);
			if (edate != null) {
				return edate;
			}
		}

		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

	private static String getEnvelopeEdateFromDeliveredDateOrReceivedOrPostedDate(Document document, String fieldName) throws NotesException {
		String edate = null;

		if ("Received".equalsIgnoreCase(fieldName)) {
			edate = getEnvelopeEdateFromReceived(document, fieldName);
		}

		if (edate == null) {
			edate = getEnvelopeEdateFromDeliveredDateOrPostedDate(document, fieldName);
		}

		return edate;
	}

	@SuppressWarnings("rawtypes")
	private static String getEnvelopeEdateFromReceived(Document document, String fieldName) throws NotesException { //NOPMD
		List received = document.getItemValue(fieldName);
		String edate = null;

		if (received.isEmpty()) {
			return edate;
		}

		// receivedString is like
		// from  ([127.0.0.1]) by domino702.terracetech.co.kr (Lotus Domino Release 8.5.3) with SMTP id 2013070812384800-1 ; Mon, 8 Jul 2013 12:38:48 +0900
		String receivedString = (String) received.get(0);
		String[] args = receivedString.split(";");

		if (args[1] != null) {
			String httpFormatDate = args[1].trim();
			DateFormat dateFormat = new MailDateFormat();
			Date date;
			try {
				date = dateFormat.parse(httpFormatDate);
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				edate = simpleDateFormat.format(date);
			} catch (ParseException e) {
				logger.error(e.toString() + ":" + httpFormatDate, e);
			}
		}

		return edate;
	}

	@SuppressWarnings("rawtypes")
	private static String getEnvelopeEdateFromDeliveredDateOrPostedDate(Document document, String fieldName) throws NotesException {
		List edateList = document.getItemValue(fieldName);

		if (edateList.isEmpty()) {
			return null;
		}

		DateTime dateTime = (DateTime) edateList.get(0);
		Date date = dateTime.toJavaDate();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

		return simpleDateFormat.format(date);
	}

	/*
	ESIP
	- 첫번째 Received 필드로 부터 송신 아이피를 구한다.
	- 저널 DB가 존재하는 도미노 서버의 주소를 설정한다.
	 */
	@SuppressWarnings("rawtypes")
	public static String getEnvelopeEsip(Document document, String defaultEsip) throws NotesException {
		List received = document.getItemValue("Received");

		if (received.isEmpty()) {
			return defaultEsip;
		}

		// receivedString is like
		// from  ([127.0.0.1]) by domino702.terracetech.co.kr (Lotus Domino Release 8.5.3) with SMTP id 2013070812384800-1 ; Mon, 8 Jul 2013 12:38:48 +0900
		String receivedString = (String) received.get(0);

		if (receivedString == null) {
			return defaultEsip;
		}

		String[] args = receivedString.split("\\[");

		if (args.length < 2) {
			return defaultEsip;
		}

		String[] brgs = args[1].split("\\]");

		// brgs[0]가 127.0.0.1일 경우 저널DB가 존재하는 공인(사설) 아이피를 리턴한다.
		if ("127.0.0.1".equals(brgs[0])) {
			return defaultEsip;
		}

		// brgs[0]가 IPv4 형식이 아닐 경우 저널DB가 존재하는 공인(사설) 아이피를 리턴한다.
		InetAddressValidator inetAddressValidator = new InetAddressValidator();

		if (!inetAddressValidator.isValidInet4Address(brgs[0])) {
			logger.info("invalid ipv4 format: " + brgs[0] + " received header: " + receivedString);
			return defaultEsip;
		}

		return /*defaultEsip = */brgs[0];
	}

	@SuppressWarnings("rawtypes")
	public static String getEnvelopeFrom(Document document, DirContext dirContext, SearchControls searchControls) throws NotesException, NamingException {
		String envFrom = "";
		String[] itemNameList = { "SMTPOriginator", "From" };
		List envFromList = new ArrayList();

		for (String itemName : itemNameList) {
			envFromList = document.getItemValue(itemName);

			if (!envFromList.isEmpty()) {
				envFrom = (String) envFromList.get(0);
				break;
			}
		}

		// 이메일 주소 검사, ldap 인지? 그룹인지? 아이디만 있는지?
		if (!StringUtils.isEmpty(envFrom)) {
			envFrom = getEmailAddress(envFrom, dirContext, searchControls);
		}

		if (StringUtils.isEmpty(envFrom)) {
			envFrom = "";
		}

		return envFrom;
	}

	@SuppressWarnings("unchecked")
	public static List<String> getEnvelopeRcpt(Document document, String alternativeRecipient, DirContext dirContext, SearchControls searchControls) throws NotesException, NamingException {
		List<String> envRcptList = new ArrayList<String>();
		List<String> previousConversionRcptList = document.getItemValue("$JournalRecipients");

		// SendTo, CopyTo, BlindCopyTo 에서 수신자 뽑기
		if (previousConversionRcptList.isEmpty()) {
			envRcptList = getEnvelopeRcptFromToCcBcc(document, dirContext, searchControls);
		}
		// $JournalRecipients 에서 수신짜 뽑기 domino 7.0.2 부터는 $JournalRecipients 설정이 가능하다
		else {
			envRcptList = getEnvelopeRcptFromJournalRecipients(document, previousConversionRcptList, dirContext, searchControls);
		}

		if (envRcptList.isEmpty()) {
			envRcptList.add(alternativeRecipient);
		}

		return envRcptList;
	}

	@SuppressWarnings("unchecked")
	private static List<String> getEnvelopeRcptFromJournalRecipients(Document document, List<String> previousConversionRcptList,
			DirContext dirContext, SearchControls searchControls) throws NamingException, NotesException {
		List<String> envRcptList = new ArrayList<String>();

		// previousConversionRcptList에는 $JournalRecipients 필드로 부터 뽑은 값들이 들어 있다.
		getEmailAddressList(previousConversionRcptList, envRcptList, dirContext, searchControls);

		// $JournalRecipientsExpand_n 에서 수신자 뽑기
		int i = 0;

		for (;;) {
			String itemName = "$JournalRecipientsExpanded_" + Integer.toString(++i);
			List<String> previousConversionRcptLists = document.getItemValue(itemName);

			if (previousConversionRcptLists.isEmpty()) {
				break;
			}

			getEmailAddressList(previousConversionRcptLists, envRcptList, dirContext, searchControls);
		}

		return envRcptList;
	}

	@SuppressWarnings("unchecked")
	private static List<String> getEnvelopeRcptFromToCcBcc(Document document, DirContext dirContext, SearchControls searchControls) throws NotesException, NamingException {
		List<String> envRcptList = new ArrayList<String>();
		String[] toCcBcc = { "SendTo", "CopyTo", "BlindCopyTo" };

		for (String field : toCcBcc) {
			List<String> previousConversionRcptList = document.getItemValue(field);
			getEmailAddressList(previousConversionRcptList, envRcptList, dirContext, searchControls);
		}

		return envRcptList;
	}

	private static void getEmailAddressList(List<String> previousConversionRcptList, List<String> envRcptList,
			DirContext dirContext, SearchControls searchControls) throws NamingException {
		for (String previousConversionRcpt : previousConversionRcptList) {
			String recipient = getEmailAddress(previousConversionRcpt, dirContext, searchControls);

			if (!StringUtils.isEmpty(recipient)) {
				envRcptList.add(recipient);
			}
		}
	}

	private static String getEmailAddress(String address, DirContext dirContext, SearchControls searchControls) throws NamingException {
		String recipient = null;
		boolean existCn = address.toLowerCase().startsWith("cn=");
		boolean existAt = address.contains("@");
		boolean existDot = address.contains(".");

		// DN 으로 판단
		if (existCn) {
			String[] cnNameArray = address.split("[,/]");
			recipient = getEmailAddressFromLdap(cnNameArray[0], dirContext, searchControls);
		}
		// 이메일 주소로 판단
		else if (!existCn && existAt && existDot) {
			InternetAddress internetAddress = null;
			try {
				internetAddress = new InternetAddress(address);

				if (internetAddress != null) {
					recipient = internetAddress.getAddress();
				}
			} catch (AddressException e) {
				logger.error(e.toString(), e);
			}
		}
		// 그룹으로 판단
		else {
			recipient = getEmailAddressFromDominoGroup(address, dirContext, searchControls);
		}

		logger.debug("get mail address from domino property." + " ADDRESS:" + address + " EMAIL:" + recipient);
		if (recipient == null || !EmailValidator.isOnlyAscii(recipient)) {
			return null;
		}

		return recipient;
	}

	private static String getEmailAddressFromLdap(String cnName, DirContext dirContext, SearchControls searchControls) throws NamingException {
		String emailAddress = null;
		String searchFilter = "(" + cnName + ")";

		logger.debug("DN:" + cnName + " FILTER:" + searchFilter);

		NamingEnumeration<SearchResult> result = dirContext.search("", searchFilter, searchControls);

		if (result.hasMore()) {
			//emailAddress = (String) result.next().getAttributes().get("mail").get();
			Attribute attribute = result.next().getAttributes().get("mail");

			if (attribute != null) {
				emailAddress = (String) attribute.get();
			}
		}

		logger.debug("DN:" + cnName + " FILTER:" + searchFilter + " EMAIL:" + emailAddress);

		return emailAddress;
	}

	// 도미노 그룹도 메일 주소를 가질 수 있다. 만약 도미노 그룹에 메일 주소가 설정되어 있을 경우 그 메일 주소를 리턴한다.
	// 도미노 그룹에 메일 주소가 설정되어 있지 않을 경우 그룹 이름 자체를 리턴한다.
	// 이는 그룹 이름을 이메일 주소로 하는 계정에도 메일을 아카이빙 하기 위함이다.
	private static String getEmailAddressFromDominoGroup(String rcpt, DirContext dirContext, SearchControls searchControls) throws NamingException {
		String dnName = "CN=" + rcpt;
		String emailAddress = DominoDocumentUtil.getEmailAddressFromLdap(dnName, dirContext, searchControls);

		if (emailAddress == null) {
			emailAddress = rcpt;
		}

		return emailAddress;
	}
}
