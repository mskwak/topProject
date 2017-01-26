package com.hangugi;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class CommandParserUtils {

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
	
//	public static <T> String getResponse(T[] elements) {
//		List<T> list = new ArrayList<T>();
//		
//		return null;
//	}
}