package com.hangugi.tma2.crawler.domino.config.migration;

import java.io.BufferedReader;
import java.io.IOException;

public class DominoPeriodConfig {
	private String startTime;
	private String endTime;

	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return this.endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void readDominoPeriodConfig(BufferedReader bufferedReader) throws IOException {
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

			if ("start".equalsIgnoreCase(key)) {
				this.setStartTime(val);
			} else if ("end".equalsIgnoreCase(key)) {
				this.setEndTime(val);
			}
		}
	}
}
