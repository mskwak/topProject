package com.daou.command.server;

import java.util.List;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.daou.CommandParserUtils;
import com.daou.Constants;
import com.daou.TemplateCallback;

//value는 Constants Enum 클래스에 정의되어 있는 맵의 키이름과 일치해야 한다.
@Component(value = "user_filter_fetch")
public class UserFilterFetch implements ServerCommand {
	@Autowired
	private TemplateCallback templateCallback;

	@Override
	public String executeCommand(List<String> commandList) {
		String tag = commandList.get(0);
		String protocolName = commandList.get(1);
		StringBuilder stringBuilder = new StringBuilder();

		if(commandList.size() < 4) {
			stringBuilder.append(CommandParserUtils.getResponse(tag, Constants.OK.getValue(), protocolName));
			return stringBuilder.toString();
		}

		String senderEmailAddress = commandList.get(2);
		
		String recipientEmailAddress = commandList.get(3);
		EmailValidator emailValidator = EmailValidator.getInstance();

		if(!emailValidator.isValid(recipientEmailAddress)) {
			stringBuilder.append(CommandParserUtils.getResponse(tag, Constants.OK.getValue(), protocolName));
			return stringBuilder.toString();
		}

		return this.templateCallback.getAllowList(senderEmailAddress, recipientEmailAddress);
//		TemplateCallback templateCallback = new TemplateCallback();
//		return templateCallback.getAllowList(emailAddress);
	}

	@Override
	public boolean isClose() {
		return false;
	}
}
