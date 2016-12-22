package com.daou.tma2.crawler.domino.config.migration;

import java.io.BufferedReader;

import com.daou.tma2.crawler.domino.config.DominoTargetConfig;

public class DominoImapTargetConfig extends DominoTargetConfig {
	private String imapUser;
	private String imapSuperPassword;

	public String getImapUser() {
		return this.imapUser;
	}

	public void setImapUser(String imapUser) {
		this.imapUser = imapUser;
	}

	public String getImapSuperPassword() {
		return this.imapSuperPassword;
	}

	public void setImapSuperPassword(String imapSuperPassword) {
		this.imapSuperPassword = imapSuperPassword;
	}

	public DominoImapTargetConfig(String imapUser) {
		this.imapUser = imapUser;
	}

	@Override
	public void readDominoTargetConfig(BufferedReader bufferedReader) throws Exception {
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

			if ("protocol".equalsIgnoreCase(key)) {
				this.setProtocol(val);
			} else if ("ip".equalsIgnoreCase(key)) {
				this.setIp(val);
			} else if ("port".equalsIgnoreCase(key)) {
				this.setPort(Integer.parseInt(val));
			} else if ("super_password".equalsIgnoreCase(key)) {
				this.setImapSuperPassword(val);
			}
		}
	}
}
