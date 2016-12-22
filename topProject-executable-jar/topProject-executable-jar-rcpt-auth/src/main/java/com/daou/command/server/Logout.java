package com.daou.command.server;

import java.util.List;

import org.springframework.stereotype.Component;

import com.daou.CommandParserUtils;
import com.daou.Constants;

//value는 Constants Enum 클래스에 정의되어 있는 맵의 키이름과 일치해야 한다.
@Component(value = "logout")
public class Logout implements ServerCommand {
	@Override
	public String executeCommand(List<String> commandList) {
		String tag = commandList.get(0);
		String protocolName = commandList.get(1);

		return CommandParserUtils.getResponse(tag, Constants.OK.getValue(), protocolName);
	}

	@Override
	public boolean isClose() {
		return true;
	}
}
