package com.daou.tma2.crawler.domino.config.migration;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class DominoExcludeBoxConfig {
	private final ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<String, String>();

	public void readDominoExcludeBoxConfig(BufferedReader bufferedReader) throws IOException {
		String line = null;

		while ((line = bufferedReader.readLine()) != null) {
			line = line.trim();

			if (line.length() == 0 || line.startsWith("#")) {
				continue;
			}

			if ("end".equalsIgnoreCase(line)) {
				break;
			}

			this.concurrentHashMap.put(line, line);
		}
	}
}
