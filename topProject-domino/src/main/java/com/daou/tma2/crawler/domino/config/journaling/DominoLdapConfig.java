package com.daou.tma2.crawler.domino.config.journaling;

import java.io.BufferedReader;
import java.io.IOException;

public class DominoLdapConfig {
	private String ip;
	private String protocol;
	private int port;
	private String dn;
	private String password;

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getProtocol() {
		return this.protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDn() {
		return this.dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "DominoLdapConfig [ip=" + this.ip + ", protocol=" + this.protocol + ", port=" + this.port + ", dn=" + this.dn + ", password=" + this.password + "]";
	}

	public void readDominoLdapConfig(BufferedReader bufferedReader) throws IOException {
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
				this.setIp(val);
			} else if ("protocol".equalsIgnoreCase(key)) {
				this.setProtocol(val);
			} else if ("port".equalsIgnoreCase(key)) {
				this.setPort(Integer.parseInt(val));
			} else if ("dn".equalsIgnoreCase(key)) {
				this.setDn(val);
			} else if ("password".equalsIgnoreCase(key)) {
				this.setPassword(val);
			}
		}
	}
}
