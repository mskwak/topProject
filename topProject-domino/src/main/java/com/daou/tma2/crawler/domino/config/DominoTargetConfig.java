package com.daou.tma2.crawler.domino.config;

import java.io.BufferedReader;

public abstract class DominoTargetConfig {
	private String protocol;
	private int port;
	private String ip;

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

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public abstract void readDominoTargetConfig(BufferedReader bufferedReader) throws Exception;
}
