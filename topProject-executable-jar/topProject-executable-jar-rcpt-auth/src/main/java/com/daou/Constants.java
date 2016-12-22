package com.daou;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum Constants {
	TAG("*"),
	OK("OK"),
	NO("NO"),
	BAD("BAD"),
	CRLF("\r\n"),
	VERSION("1.0"),
	GREETING("sf_ladmd dbagt service ready protocol-version:"),
	TIMEOUT_MESSAGE("request timeout. server die."),
	INVALID_COMMAND("invalid command.");

	private String value;

	private static final Map<String, String> map = new ConcurrentHashMap<String, String>();

	static {
		map.put("help", "com.daou.command.server.Help");
		map.put("quit", "com.daou.command.server.Quit");
		map.put("logout", "com.daou.command.server.Logout");
		map.put("user_env", "com.daou.command.server.UserEnv");
		map.put("user_filter_fetch", "com.daou.command.server.UserFilterFetch");
	}

	public String getValue() {
		return this.value;
	}

	public static Map<String, String> getMap() {
		return map;
	}

	private Constants(String value) {
		this.value = value;
	}
}
