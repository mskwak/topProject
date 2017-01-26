package com.hangugi.command.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hangugi.CommandParserUtils;
import com.hangugi.Constants;

//value는 Constants Enum 클래스에 정의되어 있는 맵의 키이름과 일치해야 한다.
@Component(value = "user_env")
public class UserEnv implements ServerCommand {
	private static final Logger logger = LoggerFactory.getLogger(UserEnv.class);

	@Override
	public String executeCommand(List<String> commandList) {
		String tag = commandList.get(0);
		String protocolName = commandList.get(1);
		
		StringBuilder stringBuilder = new StringBuilder();
		
		if(commandList.size() < 4) {
			stringBuilder.append(CommandParserUtils.getResponse(tag, Constants.NO.getValue(), protocolName));
			return stringBuilder.toString();
		}

		String createString = commandList.get(2);
		
		if(!"create".equalsIgnoreCase(createString)) {
			stringBuilder.append(CommandParserUtils.getResponse(tag, Constants.NO.getValue(), protocolName));
			return stringBuilder.toString();
		}

		EmailValidator emailValidator = EmailValidator.getInstance();
		String emailAddress = commandList.get(3);

		if(!emailValidator.isValid(emailAddress)) {
			stringBuilder.append(CommandParserUtils.getResponse(tag, Constants.NO.getValue(), protocolName));
			return stringBuilder.toString();
		}

		List<String> responseList = new ArrayList<String>();
		String id = getId(emailAddress);
		String domain = getDomain(emailAddress);
		int seq = getSeq(id);
		
		responseList.add("sendingdelaytime 5min");
		responseList.add("mailmessagestore " + "/quarantine/" + domain + "/" + seq + "/" + id);
		responseList.add("email " + emailAddress);
		responseList.add("domain " + domain);
		responseList.add("mailhostid " + getHostid());
		responseList.add("qrlanguage korean.euc-kr");
		responseList.add("attachencrypt off");
		responseList.add("sendingdelay off");
		responseList.add("applyadminrule on");
		responseList.add("qractivation system");
		
		for(String str: responseList) {
			stringBuilder.append(CommandParserUtils.getResponse(Constants.TAG.getValue(), str));
		}

		stringBuilder.append(CommandParserUtils.getResponse(tag, Constants.OK.getValue(), protocolName));

		return stringBuilder.toString();
	}
	
	private static String getId(String emailAddress) {
		return (emailAddress.split("@"))[0];
	}
	
	private static String getDomain(String emailAddress) {
		return (emailAddress.split("@"))[1];
	}
	
	private static int getSeq(String id) {
		int leng = id.length();
		int sum = 0;
		
		for(int i = 0, j = 11; i < leng; i++, j++) {
			sum += id.codePointAt(i) * j;
		}
		
		return sum % 1021;
	}
	
	private static String getHostid() {
		File hostIdFile = new File("/opt/TerraceWatcher/config/hostid.config");
		BufferedReader bufferedReader = null;
		
		try {
			bufferedReader = new BufferedReader(new FileReader(hostIdFile));
			String line = null;
			
			while((line = bufferedReader.readLine()) != null) {
				String trimLine = line.toLowerCase().trim();
				
				if(trimLine.startsWith("hostid")) {
					String[] hostidArray = line.split("\\s+");
					return StringUtils.defaultString(hostidArray[1], "hostid");
				}
			}
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		} finally {
			IOUtils.closeQuietly(bufferedReader);
		}
		
		return "hostid";
	}
	
	@Override
	public boolean isClose() {
		return false;
	}
}