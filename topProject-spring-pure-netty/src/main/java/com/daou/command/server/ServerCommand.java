package com.daou.command.server;

import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public interface ServerCommand {
	public String executeCommand(List<String> commandList);

	public String executeCommand(ChannelHandlerContext ctx, List<String> commandList);

	public boolean isClose();
}
