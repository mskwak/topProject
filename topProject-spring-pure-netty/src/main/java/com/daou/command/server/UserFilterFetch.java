package com.daou.command.server;

import io.netty.channel.ChannelHandlerContext;

import java.util.List;

import org.apache.commons.validator.routines.EmailValidator;

import com.daou.CommandParserUtils;
import com.daou.Constants;
import com.daou.NettyClient;

public class UserFilterFetch implements ServerCommand {
	@Override
	public String executeCommand(List<String> commandList) {
		return null;
	}

	@Override
	public String executeCommand(ChannelHandlerContext ctx, List<String> commandList) {
		String tag = commandList.get(0);
		String protocolName = commandList.get(1);
		String emailAddress = commandList.get(2);

		StringBuilder stringBuilder = new StringBuilder();
		EmailValidator emailValidator = EmailValidator.getInstance();

		if(!emailValidator.isValid(emailAddress)) {
			stringBuilder.append(CommandParserUtils.getResponse(tag, Constants.OK.getValue(), protocolName));
			return stringBuilder.toString();
		}

		NettyClient nettyClient = new NettyClient();
		String reponse = nettyClient.doJob(ctx, emailAddress);
		return "xxxxxxxxxxxxxxxxxxxxxxxxxxxx";
	}

	@Override
	public boolean isClose() {
		return false;
	}
}
