package com.daou.command.server;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

import com.daou.CommandParserUtils;
import com.daou.Constants;

public class UserEnv implements ServerCommand {
	private static final List<String> responseList = new ArrayList<String>();

	static {
		responseList.add("sendingdelaytime 5min");
		responseList.add("mailmessagestore /path/domain/seq/id");
		responseList.add("email id@domain.com");
		responseList.add("domain domain.com");
		responseList.add("mailhostid TWMjAwNTExMDUtMDAy");
		responseList.add("qrlanguage korean.euc-kr");
		responseList.add("attachencrypt off");
		responseList.add("sendingdelay off");
		responseList.add("applyadminrule on");
		responseList.add("qractivation system");
	}

	@Override
	public String executeCommand(List<String> commandList) {
		String tag = commandList.get(0);
		String protocolName = commandList.get(1);

		StringBuilder stringBuilder = new StringBuilder();

		for(String str: responseList) {
			stringBuilder.append(CommandParserUtils.getResponse(Constants.TAG.getValue(), str));
		}

		stringBuilder.append(CommandParserUtils.getResponse(tag, Constants.OK.getValue(), protocolName));

		return stringBuilder.toString();
	}

	@Override
	public String executeCommand(ChannelHandlerContext ctx, List<String> commandList) {
		return null;
	}

	@Override
	public boolean isClose() {
		return false;
	}
}