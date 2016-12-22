package com.daou;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class CommandParserUtils {
//	private static final Logger logger = LoggerFactory.getLogger(CommandParserUtils.class);
	private static final String filterType = "10";
	private static final String subFilterType = "5";
	private static final char delimiter = (char)0x02;
	private CommandParserUtils() {}

	public static List<String> getCommandList(String request) {
		List<String> list = new ArrayList<String>();

		if(StringUtils.isEmpty(request)) {
			return list;
		}

		String[] requestArray = StringUtils.split(request);
		int argsLength = requestArray.length;

		if(argsLength < 2) {
			return list;
		}

		String protocolName = requestArray[1];
		String fullClassName = Constants.getMap().get(protocolName.toLowerCase());

		if(StringUtils.isEmpty(fullClassName)) {
			return list;
		}

		CollectionUtils.addAll(list, requestArray);

		return list;
	}

	public static String getResponse(String... args) {
		StringBuilder stringBuilder = new StringBuilder();

		for(String str: args) {
			stringBuilder.append(str);
			stringBuilder.append(" ");
		}

		return stringBuilder.append(Constants.CRLF.getValue()).toString();
	}

	public static String getResponse(Constants... args) {
		List<String> list = new ArrayList<String>();

		for(Constants constant: args) {
			list.add(constant.getValue());
		}

		return getResponse(list.toArray(new String[list.size()]));
	}

	// 네이트 응답 파서
	public static boolean isRetry(String line) {
		boolean retry = false;

		if(line.toUpperCase().startsWith("STP/1.0") &&
				(line.contains("201") || line.contains("207") || line.contains("217") || line.contains("218"))) {
			retry = true;
		}

		return retry;
	}

	public static boolean hasFilter(String line) {
		boolean hasFilter = false;

		if(line.toUpperCase().startsWith("STP/1.0 100 OK")) {
			hasFilter = true;
		}

		return hasFilter;
	}

	public static final String getCommand(String senderEmailAddress, String recipientEmailAddress) {
		StringBuilder stringBuilder = new StringBuilder();

		// Header
		stringBuilder.append("ulocator_filter_select_exact");
		stringBuilder.append("\n");
		stringBuilder.append("\n");

		stringBuilder.append("email");
		stringBuilder.append(delimiter);
		stringBuilder.append(recipientEmailAddress);
		stringBuilder.append(delimiter);
		stringBuilder.append("\n");

		stringBuilder.append("type");
		stringBuilder.append(delimiter);
		stringBuilder.append(filterType);
		stringBuilder.append(delimiter);
		stringBuilder.append("\n");

		stringBuilder.append("fromEmail");
		stringBuilder.append(delimiter);
		stringBuilder.append(senderEmailAddress);
		stringBuilder.append(delimiter);
		stringBuilder.append("\n");

		stringBuilder.append("\n");
		stringBuilder.append(0);
		stringBuilder.append("\n");

		return stringBuilder.toString();
	}

	private static String getAllowRuleString(int size) {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("apply");
		stringBuilder.append(" ");
		stringBuilder.append("on");
		stringBuilder.append("\n");

		stringBuilder.append("cond_no");
		stringBuilder.append(" ");
		stringBuilder.append(size);
		stringBuilder.append("\n");

		return stringBuilder.toString();
	}

	private static String getAllowRuleLength(String s, int l) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("*");
		stringBuilder.append(" ");
		stringBuilder.append(s);
		stringBuilder.append("{");
		stringBuilder.append(l);
		stringBuilder.append("}");
		stringBuilder.append("\n");

		return stringBuilder.toString();
	}

	public static String convertRule(List<String> allowList) {
		StringBuilder stringBuilder = new StringBuilder();
		int allowListSize = allowList.size();

		if(allowList.isEmpty()) {
			String allowRuleString = getAllowRuleString(allowListSize);
			String allowRuleLength = getAllowRuleLength("ALLOW", allowRuleString.length());

			stringBuilder.append(allowRuleLength);
			stringBuilder.append(allowRuleString);
			stringBuilder.append("* BLOCK {19}\n");
			stringBuilder.append("apply on\n");
			stringBuilder.append("cond_no 0\n");

			return stringBuilder.toString();
		}

		for(int i = 0; i < allowListSize; i++) {
			stringBuilder.append("condv2");
			stringBuilder.append(" ");
//			stringBuilder.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			stringBuilder.append(UUID.randomUUID().toString());
			stringBuilder.append("\n");

				stringBuilder.append("subcond_no 1\n");
				stringBuilder.append("apply on\n");
				stringBuilder.append("op AND\n");
				stringBuilder.append("policy normal\n");

				stringBuilder.append("tagxheader off\n");
				stringBuilder.append("tagxheaderstr X-TERRACESPAM : YES\n");

				stringBuilder.append("tagprependsbj off\n");
				stringBuilder.append("tagprependsbjstr [SPAM]\n");

				stringBuilder.append("tagappendsbj off\n");
				stringBuilder.append("tagappendsbjstr [SPAM]\n");

				stringBuilder.append("subcond\n");
					stringBuilder.append("field from\n");
					stringBuilder.append("checkmethod matches\n");

					stringBuilder.append("patternmv\n");
						stringBuilder.append("*");
						stringBuilder.append(" ");
						stringBuilder.append(allowList.get(i));
						stringBuilder.append("\n");
					stringBuilder.append("end\n");

				stringBuilder.append("end\n");

			stringBuilder.append("end\n");
		}

		stringBuilder.append("* BLOCK {19}\n");
		stringBuilder.append("apply on\n");
		stringBuilder.append("cond_no 0\n");

		return stringBuilder.toString();
	}

	public static String getAllowSenderEmailAddressOrSenderDomain(String line) {
		// 네이트 스펙 문서에서 정의되어 있는 filter_type. 값이 10일 경우 수신 허용
		if(!line.startsWith(filterType)) {
			return "";
		}

		char[] delimiters = { delimiter };
		String[] lines = line.split(new String(delimiters));
		int size = lines.length;

		if(size < 2) {
			return "";
		}

		// 네이트 스펙 문서에서 정의되어 있는 filter_subtype 은 무조건 5이다.
		String filter_subtype = lines[1];

		if(!subFilterType.equalsIgnoreCase(filter_subtype)) {
			return "";
		}

		// 네이트 스펙 문서에서 0x02로 구분되는 배열에서 2번째 값은 송신자 이메일 주소 또는 도메인이다.
		return lines[2];
	}
//	public static <T> String getResponse(T[] elements) {
//		List<T> list = new ArrayList<T>();
//
//		return null;
//	}
}