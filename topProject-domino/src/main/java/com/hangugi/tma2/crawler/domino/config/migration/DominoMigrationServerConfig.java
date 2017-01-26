package com.hangugi.tma2.crawler.domino.config.migration;

import java.io.BufferedReader;
import java.io.IOException;

import com.hangugi.tma2.crawler.domino.config.DominoServerConfig;
import com.hangugi.tma2.crawler.domino.config.DominoTargetConfig;
import com.hangugi.tma2.crawler.domino.config.journaling.DominoLdapConfig;

public class DominoMigrationServerConfig extends DominoServerConfig {
	private final String imapUser;
	private DominoExcludeBoxConfig dominoExcludeBoxConfig;
	private DominoReplaceBoxConfig dominoReplaceBoxConfig;
	private DominoPeriodConfig dominoPeriodConfig;

	public String getImapUser() {
		return this.imapUser;
	}

	public DominoExcludeBoxConfig getDominoExcludeBoxConfig() {
		return this.dominoExcludeBoxConfig;
	}

	public void setDominoExcludeBoxConfig(DominoExcludeBoxConfig dominoExcludeBoxConfig) {
		this.dominoExcludeBoxConfig = dominoExcludeBoxConfig;
	}

	public DominoReplaceBoxConfig getDominoReplaceBoxConfig() {
		return this.dominoReplaceBoxConfig;
	}

	public void setDominoReplaceBoxConfig(DominoReplaceBoxConfig dominoReplaceBoxConfig) {
		this.dominoReplaceBoxConfig = dominoReplaceBoxConfig;
	}

	public DominoPeriodConfig getDominoPeriodConfig() {
		return this.dominoPeriodConfig;
	}

	public void setDominoPeriodConfig(DominoPeriodConfig dominoPeriodConfig) {
		this.dominoPeriodConfig = dominoPeriodConfig;
	}

	public DominoMigrationServerConfig(String imapUser, String journalDbName) {
		super(journalDbName);
		this.imapUser = imapUser;
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
			} else if ("target".equalsIgnoreCase(key)) {
				DominoTargetConfig dominoTargetConfig = new DominoImapTargetConfig(this.getImapUser());
				dominoTargetConfig.readDominoTargetConfig(bufferedReader);
				this.setDominoTargetConfig(dominoTargetConfig);
			} else if ("ldap".equalsIgnoreCase(key)) {
				DominoLdapConfig dominoLdapConfig = new DominoLdapConfig();
				dominoLdapConfig.readDominoLdapConfig(bufferedReader);
				this.setDominoLdapConfig(dominoLdapConfig);
			} else if ("excludebox".equalsIgnoreCase(key)) {
				DominoExcludeBoxConfig dominoExcludeBoxConfig = new DominoExcludeBoxConfig();
				dominoExcludeBoxConfig.readDominoExcludeBoxConfig(bufferedReader);
				this.setDominoExcludeBoxConfig(dominoExcludeBoxConfig);
			} else if ("replacebox".equalsIgnoreCase(key)) {
				DominoReplaceBoxConfig dominoReplaceBoxConfig = new DominoReplaceBoxConfig();
				dominoReplaceBoxConfig.readDominoReplaceBoxConfig(bufferedReader);
				this.setDominoReplaceBoxConfig(dominoReplaceBoxConfig);
			} else if ("period".equalsIgnoreCase(key)) {
				DominoPeriodConfig dominoPeriodConfig = new DominoPeriodConfig();
				dominoPeriodConfig.readDominoPeriodConfig(bufferedReader);
				this.setDominoPeriodConfig(dominoPeriodConfig);
			}
		}
	}
}
