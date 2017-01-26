package com.hangugi.tma2.crawler.domino.config.journaling;

import java.io.BufferedReader;
import java.io.IOException;

import com.hangugi.tma2.crawler.domino.config.DominoServerConfig;
import com.hangugi.tma2.crawler.domino.config.DominoTargetConfig;

public class DominoJournalingServerConfig extends DominoServerConfig {
	private String alternativeRecipient;
	private boolean documentDeletion;
	private int workerThread = 100;
	private boolean send;
	private DominoScheduleConfig dominoScheduleConfig;

	public String getAlternativeRecipient() {
		return this.alternativeRecipient;
	}

	public void setAlternativeRecipient(String alternativeRecipient) {
		this.alternativeRecipient = alternativeRecipient;
	}

	public boolean isDocumentDeletion() {
		return this.documentDeletion;
	}

	public void setDocumentDeletion(boolean documentDeletion) {
		this.documentDeletion = documentDeletion;
	}

	public int getWorkerThread() {
		return this.workerThread;
	}

	public void setWorkerThread(int workerThread) {
		this.workerThread = workerThread;
	}

	public DominoScheduleConfig getDominoScheduleConfig() {
		return this.dominoScheduleConfig;
	}

	public void setDominoScheduleConfig(DominoScheduleConfig dominoScheduleConfig) {
		this.dominoScheduleConfig = dominoScheduleConfig;
	}

	public boolean isSend() {
		return this.send;
	}

	public void setSend(boolean send) {
		this.send = send;
	}

	public DominoJournalingServerConfig() {
		super();
	}

	@Override
	public void readDominoServerConfig(BufferedReader bufferedReader) throws NumberFormatException, IOException, Exception {
		String line = null;

		while ((line = bufferedReader.readLine()) != null) {
			line = line.trim();

			if (line.length() == 0 || line.startsWith("#")) {
				continue;
			}

			if ("end".equalsIgnoreCase(line)) {
				break;
			}

			String[] args = line.split("\\s+");
			String key = null;
			String val = null;
			int argsLength = args.length;

			if (argsLength == 0) {
				continue;
			} else if (argsLength == 1) {
				key = args[0].trim();
			} else if (argsLength == 2) {
				key = args[0].trim();
				val = args[1].trim();
			} else {
				continue;
			}

			if ("ip".equalsIgnoreCase(key)) {
				this.setDominoServerIpAddress(val);
			} else if ("user_name".equalsIgnoreCase(key)) {
				this.setUserName(val);
			} else if ("password".equalsIgnoreCase(key)) {
				this.setPassword(val);
			} else if ("journal_db_name".equalsIgnoreCase(key)) {
				this.setJournalDbName(val);
			} else if ("http_protocol".equalsIgnoreCase(key)) {
				this.setHttpProtocol(val);
			} else if ("http_port".equalsIgnoreCase(key)) {
				this.setHttpPort(Integer.parseInt(val));
			} else if ("alternative_recipient".equalsIgnoreCase(key)) {
				this.setAlternativeRecipient(val);
			} else if ("worker_thread".equalsIgnoreCase(key)) {
				this.setWorkerThread(Integer.parseInt(val));
			} else if ("document_deletion".equalsIgnoreCase(key)) {
				this.setDocumentDeletion(("on".equalsIgnoreCase(val) ? true : false));
			} else if ("send".equalsIgnoreCase(key)) {
				this.setSend(("on".equalsIgnoreCase(val) ? true : false));
			} else if ("ldap".equalsIgnoreCase(key)) {
				DominoLdapConfig dominoLdapConfig = new DominoLdapConfig();
				dominoLdapConfig.readDominoLdapConfig(bufferedReader);
				this.setDominoLdapConfig(dominoLdapConfig);
			} else if ("schedule".equalsIgnoreCase(key)) {
				DominoScheduleConfig dominoScheduleConfig = new DominoScheduleConfig();
				dominoScheduleConfig.readDominoScheduleConfig(bufferedReader);
				this.setDominoScheduleConfig(dominoScheduleConfig);
			} else if ("target".equalsIgnoreCase(key)) {
				DominoTargetConfig dominoTargetConfig = new DominoHttpTargetConfig();
				dominoTargetConfig.readDominoTargetConfig(bufferedReader);
				this.setDominoTargetConfig(dominoTargetConfig);
			}
		}
	}
}
