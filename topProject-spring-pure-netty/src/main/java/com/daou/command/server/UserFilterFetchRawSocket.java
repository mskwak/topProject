package com.daou.command.server;

import io.netty.channel.ChannelHandlerContext;

import java.util.List;

import org.apache.commons.validator.routines.EmailValidator;

import com.daou.CommandParserUtils;
import com.daou.Constants;
import com.daou.TemplateCallback;

public class UserFilterFetchRawSocket implements ServerCommand {
	@Override
	public String executeCommand(List<String> commandList) {
		String tag = commandList.get(0);
		String protocolName = commandList.get(1);
		StringBuilder stringBuilder = new StringBuilder();

		if(commandList.size() < 3) {
			stringBuilder.append(CommandParserUtils.getResponse(tag, Constants.OK.getValue(), protocolName));
			return stringBuilder.toString();
		}

		String emailAddress = commandList.get(2);
		EmailValidator emailValidator = EmailValidator.getInstance();

		if(!emailValidator.isValid(emailAddress)) {
			stringBuilder.append(CommandParserUtils.getResponse(tag, Constants.OK.getValue(), protocolName));
			return stringBuilder.toString();
		}

		TemplateCallback templateCallback = new TemplateCallback();

		return templateCallback.getAllowList(emailAddress);
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
