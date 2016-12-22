package com.daou.tma2.crawler.domino.config.journaling;

import java.io.BufferedReader;

import com.daou.tma2.crawler.domino.config.DominoTargetConfig;

public class DominoHttpTargetConfig extends DominoTargetConfig {
	private String uri;

	public String getUri() {
		return this.uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
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
			} else if ("uri".equalsIgnoreCase(key)) {
				this.setUri(val);
			}
		}
	}
}
