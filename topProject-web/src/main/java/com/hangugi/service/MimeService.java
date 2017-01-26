package com.hangugi.service;

import java.io.InputStream;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import com.hangugi.MimeParser;

@Service
public class MimeService {

	public String getMimeStructure(InputStream inputStream) throws MessagingException {
		MimeMessage mimeMessage = new MimeMessage(null, inputStream);
		MimeParser mimeParser = new MimeParser(mimeMessage);
		return mimeParser.getBoundary();
	}
}
