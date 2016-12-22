package com.daou.command.server;

import io.netty.channel.ChannelHandlerContext;

import java.util.List;

import com.daou.CommandParserUtils;
import com.daou.Constants;

public class Quit implements ServerCommand {
	@Override
	public String executeCommand(List<String> commandList) {
		String tag = commandList.get(0);
		String protocolName = commandList.get(1);

		return CommandParserUtils.getResponse(tag, Constants.OK.getValue(), protocolName);
	}

	@Override
	public String executeCommand(ChannelHandlerContext ctx, List<String> commandList) {
		return null;
	}

	@Override
	public boolean isClose() {
		return true;
	}
}
