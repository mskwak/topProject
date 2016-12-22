package com.daou.tma2.crawler.domino.config;

import java.io.BufferedReader;

import com.daou.tma2.crawler.domino.config.journaling.DominoLdapConfig;


public abstract class DominoServerConfig {
	protected String dominoServerIpAddress;
	protected String userName;
	protected String password;
	protected int httpPort;
	protected String httpProtocol;
	protected String journalDbName;
	protected DominoTargetConfig dominoTargetConfig;
	protected DominoLdapConfig dominoLdapConfig;

	// target protocol
	protected String targetProtocol;

	// queue
	protected String unidDir;
	protected String historyDir;
	protected String retryDir;

	public String getDominoServerIpAddress() {
		return this.dominoServerIpAddress;
	}

	public void setDominoServerIpAddress(String dominoServerIpAddress) {
		this.dominoServerIpAddress = dominoServerIpAddress;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getHttpPort() {
		return this.httpPort;
	}

	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	public String getHttpProtocol() {
		return this.httpProtocol;
	}

	public void setHttpProtocol(String httpProtocol) {
		this.httpProtocol = httpProtocol;
	}

	public String getJournalDbName() {
		return this.journalDbName;
	}

	public void setJournalDbName(String journalDbName) {
		this.journalDbName = journalDbName;
	}

	public String getTargetProtocol() {
		return this.targetProtocol;
	}

	public void setTargetProtocol(String targetProtocol) {
		this.targetProtocol = targetProtocol;
	}

	public String getUnidDir() {
		return this.unidDir;
	}

	public void setUnidDir(String unidDir) {
		this.unidDir = unidDir;
	}

	public String getHistoryDir() {
		return this.historyDir;
	}

	public void setHistoryDir(String historyDir) {
		this.historyDir = historyDir;
	}

	public String getRetryDir() {
		return this.retryDir;
	}

	public void setRetryDir(String retryDir) {
		this.retryDir = retryDir;
	}

	public DominoTargetConfig getDominoTargetConfig() {
		return this.dominoTargetConfig;
	}

	public void setDominoTargetConfig(DominoTargetConfig dominoTargetConfig) {
		this.dominoTargetConfig = dominoTargetConfig;
	}

	public DominoLdapConfig getDominoLdapConfig() {
		return this.dominoLdapConfig;
	}

	public void setDominoLdapConfig(DominoLdapConfig dominoLdapConfig) {
		this.dominoLdapConfig = dominoLdapConfig;
	}

	public DominoServerConfig(String journalDbName) {
		this.journalDbName = journalDbName;
	}

	public DominoServerConfig() {
	}

	public abstract void readDominoServerConfig(BufferedReader bufferedReader) throws Exception;
}
