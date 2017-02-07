package com.hangugi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class NettyMain {
	private static final Logger logger = LoggerFactory.getLogger(NettyMain.class);

    public static void main(String[] args) {
    	String configLocation = "classpath:netty.xml";
    	ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext(configLocation);
    	NettyServerLaunchService nettyServerLaunchService = classPathXmlApplicationContext.getBean(NettyServerLaunchService.class);

    	// TODO 이 로직이 이곳에 위치해야만 하는 건가?
		Runnable shutdownHook = new Runnable() {
			@Override
			public void run() {
				logger.info("Stopping server...");
				nettyServerLaunchService.stop();
				classPathXmlApplicationContext.close();
			}
		};

		// hook: (갈)고리, 걸이
    	Runtime.getRuntime().addShutdownHook(new Thread(shutdownHook));

    	nettyServerLaunchService.start();
    }
}