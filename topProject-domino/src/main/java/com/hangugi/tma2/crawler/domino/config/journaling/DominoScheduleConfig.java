package com.hangugi.tma2.crawler.domino.config.journaling;

import java.io.BufferedReader;
import java.io.IOException;

public class DominoScheduleConfig {
	private String type;
	private String batchStartTime;
	private String batchEndTime;

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBatchStartTime() {
		return this.batchStartTime;
	}

	public void setBatchStartTime(String batchStartTime) {
		this.batchStartTime = batchStartTime;
	}

	public String getBatchEndTime() {
		return this.batchEndTime;
	}

	public void setBatchEndTime(String batchEndTime) {
		this.batchEndTime = batchEndTime;
	}

	public void readDominoScheduleConfig(BufferedReader bufferedReader) throws IOException {
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

			if ("type".equalsIgnoreCase(key)) {
				this.setType(val);
			} else if ("batch_start_time".equalsIgnoreCase(key)) {
				this.setBatchStartTime(val);
			} else if ("batch_end_time".equalsIgnoreCase(key)) {
				this.setBatchEndTime(val);
			}
		}
	}
}
