package com.hangugi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	private static Logger logger = LoggerFactory.getLogger(Main.class);

	private static ApplicationContext applicationContext;

	public static void main(String[] args) {
		logger.info("xxx");
		applicationContext = new ClassPathXmlApplicationContext("classpath:META-INF/spring.xml");
		Test test = applicationContext.getBean("test", Test.class);
		test.hello();

		//Map<String, String> hash = new HashMap<String, String>();
	}
}