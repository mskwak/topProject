package com.daou.tma2.crawler.domino.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.NotesFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.daou.tma2.crawler.domino.config.DominoServerConfig;
import com.daou.tma2.crawler.domino.config.journaling.DominoHttpTargetConfig;
import com.daou.tma2.crawler.domino.config.migration.DominoImapTargetConfig;
import com.daou.tma2.crawler.domino.service.AbstractDominoService;
import com.daou.tma2.crawler.domino.service.DominoJournalingService;
import com.daou.tma2.crawler.domino.service.DominoMigrationService;

public class DominoFactory {
	private static final Logger logger = LoggerFactory.getLogger(DominoFactory.class);

	public static Object getTargetAuthObject(Object targetAuthObject, DominoServerConfig dominoServerConfig) throws MessagingException { //NOPMD
		String protocol = dominoServerConfig.getDominoTargetConfig().getProtocol();
		if ("http".equalsIgnoreCase(protocol)) {
			targetAuthObject = getHttpAuthObject(dominoServerConfig);
		} else if ("imap".equalsIgnoreCase(protocol)) {
			targetAuthObject = getImapAuthObject(targetAuthObject, dominoServerConfig);
		} else if ("smtp".equalsIgnoreCase(protocol)) { //NOPMD
			targetAuthObject = dominoServerConfig.getDominoTargetConfig().getIp() + ":" + dominoServerConfig.getDominoTargetConfig().getPort();
		}

		return targetAuthObject;
	}

	private static Map<String, Object> getImapAuthObject(Object targetAuthObject, DominoServerConfig dominoServerConfig) throws MessagingException {
		closeImapAuthObject(targetAuthObject);

		String name = dominoServerConfig.getDominoTargetConfig().getProtocol();
		String host = dominoServerConfig.getDominoTargetConfig().getIp();
		int port = dominoServerConfig.getDominoTargetConfig().getPort();

		Properties properties = new Properties();
		properties.put("mail." + name + ".port", port);
		properties.put("mail.store.protocol", name);

		String userName = ((DominoImapTargetConfig) dominoServerConfig.getDominoTargetConfig()).getImapUser();
		String password = ((DominoImapTargetConfig) dominoServerConfig.getDominoTargetConfig()).getImapSuperPassword();

		Session session = Session.getDefaultInstance(properties, null);
		Store store = session.getStore(name);
		store.connect(host, userName, password);

		if (!store.isConnected()) {
			throw new MessagingException("invalid id or password.");
		}

		Map<String, Object> imapAuthObjectMap = new HashMap<String, Object>();

		imapAuthObjectMap.put("session", session);
		imapAuthObjectMap.put("store", store);

		return imapAuthObjectMap;
	}

	@SuppressWarnings("unchecked")
	private static void closeImapAuthObject(Object targetAuthObject) {
		if (targetAuthObject == null) {
			return;
		}

		Map<String, Object> imapAuthObjectMap = (Map<String, Object>) targetAuthObject;
		Session session = (Session) imapAuthObjectMap.get("session");
		Store store = (Store) imapAuthObjectMap.get("store");

		try {
			if (session != null) {
				session = null;
			}
			if (store.isConnected()) {
				store.close();
			}
		} catch (NoSuchProviderException e) {
		} catch (MessagingException e) {
		}
	}

	private static String getHttpAuthObject(DominoServerConfig dominoServerConfig) {
		DominoHttpTargetConfig dominoHttpTargetConfig = (DominoHttpTargetConfig) dominoServerConfig.getDominoTargetConfig();
		String targetAddress = dominoHttpTargetConfig.getProtocol() + "://" + dominoHttpTargetConfig.getIp() + ":" +
				dominoHttpTargetConfig.getPort() + "/" + dominoHttpTargetConfig.getUri();
		return targetAddress;
	}

	public static DirContext getDirContext(DirContext dirContext, DominoServerConfig dominoServerConfig) throws NamingException {
		closeDirContext(dirContext);

		String ip = dominoServerConfig.getDominoLdapConfig().getIp();
		String password = dominoServerConfig.getDominoLdapConfig().getPassword();
		String protocol = dominoServerConfig.getDominoLdapConfig().getProtocol();
		String dn = dominoServerConfig.getDominoLdapConfig().getDn();
		int port = dominoServerConfig.getDominoLdapConfig().getPort();

		Properties properties = new Properties();
		properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		properties.setProperty(Context.PROVIDER_URL, protocol + "://" + ip + ":" + port);
		properties.setProperty(Context.SECURITY_AUTHENTICATION, "simple");
		properties.setProperty(Context.SECURITY_PRINCIPAL, dn);
		properties.setProperty(Context.SECURITY_CREDENTIALS, password);

		System.getProperties().put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.getProperties().put("java.naming.provider.url", protocol + "://" + ip + ":" + port);

		return new InitialDirContext(properties);
	}

	public static void closeDirContext(DirContext dirContext) { //NOPMD
		try {
			if (dirContext != null) {
				dirContext.close();
			}
		} catch (NamingException e) {
			logger.error(e.toString(), e);
		} finally {
			dirContext = null;
		}
	}

	// TODO database를 close 하는 메소드를 아무리 찾아봐도 존재하지 않는다... 찾아야 한다. 
	public static Database getDataBase(Database database, DominoServerConfig dominoServerConfig) throws NotesException { //NOPMD
		try {
			if (database.isOpen()) {
				return database;
			}
		} catch (NotesException e) {
			database = null;
		} catch (NullPointerException e) {
			database = null;
		}

		String ip = dominoServerConfig.getDominoServerIpAddress();
		String user = dominoServerConfig.getUserName();
		String password = dominoServerConfig.getPassword();
		String journalDbName = dominoServerConfig.getJournalDbName();

		lotus.domino.Session session;
		try {
			session = NotesFactory.createSession(ip, user, password);
			// 2015.05.13 유안타증권. 장애발생
			// http://www-01.ibm.com/support/docview.wss?uid=swg21438386
			// 도미노 콘솔 상에서 아래와 같은 에러 발생
			//			LSXBE: ************************************
			//			LSXBE: ****** Out of Backend Memory *******
			//			LSXBE: ************************************

			if (database != null) {
				database.recycle();
			}

			database = session.getDatabase("", journalDbName);
			database.setFolderReferencesEnabled(true);
		} catch (NotesException e) {
			logger.error(e.toString() + " DS:check domino diiop user_name or password." +
					" ip:" + ip +
					" user:" + user +
					" password:" + password);
			throw e;
		}

		return database;
	}

	//database.setDocumentLockingEnabled(false);
	/*
		database.setDocumentLockingEnabled(true);
		if (!database.isDocumentLockingEnabled()) {
			return null;
		}
	*/


	//	private static void closeDatabase(Database database) {
	//		database = null;
	//	}

	public static AbstractDominoService getDominoServie(String type, ApplicationContext applicationContext) {
		AbstractDominoService abstractDominoService = null;

		if ("journaling".equalsIgnoreCase(type)) {
			abstractDominoService = applicationContext.getBean("dominoJournalingService", DominoJournalingService.class);
		} else if ("migration".equalsIgnoreCase(type)) {
			abstractDominoService = applicationContext.getBean("dominoMigrationService", DominoMigrationService.class);
		} else {
			return null;
		}

		return abstractDominoService;
	}
}
