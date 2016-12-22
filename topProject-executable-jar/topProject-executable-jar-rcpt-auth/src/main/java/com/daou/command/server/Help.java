package com.daou.command.server;

import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

import com.daou.CommandParserUtils;
import com.daou.Constants;

// value는 Constants Enum 클래스에 정의되어 있는 맵의 키이름과 일치해야 한다.
@Component(value = "help")
public class Help implements ServerCommand {
	@Override
	public String executeCommand(List<String> commandList) {
		String tag = commandList.get(0);
		String protocolName = commandList.get(1);

		TreeMap<String, String> treeMap = new TreeMap<String, String>(Constants.getMap());
		Iterator<String> iterator = treeMap.keySet().iterator();
		StringBuilder stringBuilder = new StringBuilder();

		while(iterator.hasNext()) {
			stringBuilder.append(CommandParserUtils.getResponse(Constants.TAG.getValue(), iterator.next()));
		}

		stringBuilder.append(CommandParserUtils.getResponse(tag, Constants.OK.getValue(), protocolName));

		return stringBuilder.toString();
	}

	@Override
	public boolean isClose() {
		return false;
	}
}
