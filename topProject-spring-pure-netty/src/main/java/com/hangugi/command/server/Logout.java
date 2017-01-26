package com.hangugi.command.server;

import io.netty.channel.ChannelHandlerContext;

import java.util.List;

import com.hangugi.CommandParserUtils;
import com.hangugi.Constants;

public class Logout implements ServerCommand {
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
