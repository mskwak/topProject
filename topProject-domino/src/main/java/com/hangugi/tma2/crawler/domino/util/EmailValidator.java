package com.hangugi.tma2.crawler.domino.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
	private final Pattern pattern;
	private Matcher matcher;
	private static final String EMAIL_PATTERN =
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public EmailValidator() {
		this.pattern = Pattern.compile(EMAIL_PATTERN);
	}

	/**
	 * Validate hex with regular expression
	 * 
	 * @param hex
	 *            hex for validation
	 * @return true valid hex, false invalid hex
	 */
	public boolean validate(final String hex) {
		this.matcher = this.pattern.matcher(hex);
		return this.matcher.matches();
	}

	public String removeAngleBracket(String rcpt) {
		String emailAddress = null;

		if (rcpt.contains("<") && rcpt.contains(">") && rcpt.contains("@")) {
			String r = null;

			r = rcpt.replaceAll("<{1,}", "<");
			r = rcpt.replaceAll(">{1,}", ">");

			String[] args = r.split("<");
			String[] brgs = args[1].split(">");

			emailAddress = brgs[0];
		} else {
			return rcpt;
		}

		return emailAddress;
	}

	public static boolean isOnlyAscii(String address) {
		return address.matches("\\p{ASCII}*");
	}
}
