package com.hangugi.command.server;

import io.netty.channel.ChannelHandlerContext;

import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import com.hangugi.CommandParserUtils;
import com.hangugi.Constants;

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
	public String executeCommand(ChannelHandlerContext ctx, List<String> commandList) {
		return null;
	}

	@Override
	public boolean isClose() {
		return false;
	}
}
