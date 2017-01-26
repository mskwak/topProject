package com.hangugi.smtp;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Producer {
	private static final Logger logger = LoggerFactory.getLogger(Producer.class);
	// TODO new Object 객체에서 스프링 빈을 사용 가능하도록 수정 필요
	private static final Properties properties = new Properties();

	@Autowired
	private SmtpConfig smtpConfig;

	public void start() {
		ExecutorService executorService = Executors.newFixedThreadPool(this.smtpConfig.getSmtpSenderCount());
		File file = new File(this.smtpConfig.getEmlDirectory());
		File[] fileList = file.listFiles();
		this.setProperties();

		for(;;) {
			for(int i = 0, size = fileList.length; i < size; i++) {
				SmtpSender smtpSender = new SmtpSender(properties, fileList[i]);
				executorService.execute(smtpSender);

				try {
					Thread.sleep(this.smtpConfig.getSleepInterval());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void stop() {
		logger.info("producer stop...");
//		executorService.shutdown();
	}

	// TODO new Object 객체에서 스프링 빈을 사용 가능하도록 수정 필요
	private void setProperties() {
		properties.put("mail.smtp.protocol", "smtp");
		properties.put("mail.smtp.ehlo", true);
		properties.put("mail.smtp.port", this.smtpConfig.getSmtpPort());
		properties.put("mail.smtp.host", this.smtpConfig.getHost());
		properties.put("mail.smtp.auth", this.smtpConfig.isAuth());
		properties.put("mail.smtp.auth.id", this.smtpConfig.getAuthId());
		properties.put("mail.smtp.auth.password", this.smtpConfig.getAuthPassword());
		properties.put("mail.smtp.mail.from", this.smtpConfig.getMailFrom());
		properties.put("mail.smtp.rcpt.to", this.smtpConfig.getRcptTo());
		properties.put("mail.debug", String.valueOf(this.smtpConfig.isDebug()));
		properties.put("mail.smtp.ssl.enable", this.smtpConfig.isSsl());
		properties.put("mail.smtp.starttls.enable", this.smtpConfig.isStarttls());
	}
}