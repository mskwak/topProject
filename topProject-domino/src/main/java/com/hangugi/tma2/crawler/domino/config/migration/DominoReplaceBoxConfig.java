package com.hangugi.tma2.crawler.domino.config.migration;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DominoReplaceBoxConfig {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<String, String>();

	public void readDominoReplaceBoxConfig(BufferedReader bufferedReader) throws IOException {
		String line = null;

		while ((line = bufferedReader.readLine()) != null) {
			line = line.trim();

			if (line.length() == 0 || line.startsWith("#")) {
				continue;
			}

			if ("end".equalsIgnoreCase(line)) {
				break;
			}

			String[] keyValuePair = line.split("\\t+");
			int size = keyValuePair.length;

			if (size == 2) {
				this.concurrentHashMap.put(keyValuePair[0].trim(), keyValuePair[1].trim());
			} else {
				this.log.error("line is invalid! line size : " + size + ", line value=" + line);
			}
		}
	}
}
