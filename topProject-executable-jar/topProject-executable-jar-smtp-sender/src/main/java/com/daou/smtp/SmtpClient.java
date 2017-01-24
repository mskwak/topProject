package com.daou.smtp;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SmtpClient {
	public static void main(String[] args) {
		String propertiesPath = System.getProperty("properties.file.path");
		String logbackPath = System.getProperty("logback.configurationFile");

		if(StringUtils.isEmpty(propertiesPath) || StringUtils.isEmpty(logbackPath)) {
			usage();
			System.exit(0);
		}

		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SmtpConfig.class);
		final Producer producer = annotationConfigApplicationContext.getBean(Producer.class);

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				producer.stop();
			}
		};

		Runtime.getRuntime().addShutdownHook(new Thread(runnable));

		producer.start();
		annotationConfigApplicationContext.close();
	}

	private static void usage() {
		System.out.println("/path/to/java -jar -Dproperties.file.path=/path/to/file.properties -Dlogback.configurationFile=/path/to/logback.xml tool.jar");
	}
}
