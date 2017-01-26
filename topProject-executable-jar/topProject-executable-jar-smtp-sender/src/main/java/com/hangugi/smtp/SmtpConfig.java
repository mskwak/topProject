package com.hangugi.smtp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan("com.hangugi")
@PropertySource("file:${properties.file.path}")
public class SmtpConfig {
	@Value("${send.interval.second}") private double sleepInterval;
	@Value("${send.thread.count}") private int smtpSenderCount;
	@Value("${eml.directory}") private String emlDirectory;
	@Value("${mail.smtp.host}") private String host;
	@Value("${mail.smtp.auth}") private boolean isAuth;
	@Value("${mail.smtp.auth.id}") private String authId;
	@Value("${mail.smtp.auth.password}") private String authPassword;
	@Value("${mail.smtp.port}") private int smtpPort;
	@Value("${mail.smtp.mail.from}") private String mailFrom;
	@Value("${mail.smtp.rcpt.to}") private String rcptTo;
	@Value("${mail.debug}") private boolean isDebug;
	@Value("${mail.smtp.ssl.enable}") private boolean isSsl;
	@Value("${mail.smtp.starttls.enable}") private boolean isStarttls;

	@Bean
	public long getSleepInterval() {
		return (long) this.sleepInterval * 1000;
	}

	@Bean
	public int getSmtpSenderCount() {
		return this.smtpSenderCount;
	}

	@Bean
	public String getEmlDirectory() {
		return this.emlDirectory;
	}

	@Bean
	public String getHost() {
		return this.host;
	}

	@Bean
	public boolean isAuth() {
		return this.isAuth;
	}

	@Bean
	public String getAuthId() {
		return this.authId;
	}

	@Bean
	public String getAuthPassword() {
		return this.authPassword;
	}

	@Bean
	public int getSmtpPort() {
		return this.smtpPort;
	}

	@Bean
	public String getMailFrom() {
		return this.mailFrom;
	}

	@Bean
	public String getRcptTo() {
		return this.rcptTo;
	}

	@Bean
	public boolean isDebug() {
		return this.isDebug;
	}

	@Bean
	public boolean isSsl() {
		return this.isSsl;
	}

	@Bean
	public boolean isStarttls() {
		return this.isStarttls;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
