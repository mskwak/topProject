package com.daou;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class NettyMain {
	private static final Logger logger = LoggerFactory.getLogger(NettyMain.class);

    public static void main( String[] args ) {
    	String configLocation = "classpath:netty.xml";
    	ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext(configLocation);

    	final NettyServerLaunchService nettyServerLaunchService = classPathXmlApplicationContext.getBean(NettyServerLaunchService.class);

		Runnable shutdownHook = new Runnable() {
			@Override
			public void run() {
				logger.info("Stopping server...");
				nettyServerLaunchService.stop();
			}
		};

    	Runtime.getRuntime().addShutdownHook(new Thread(shutdownHook));

    	nettyServerLaunchService.start();

    	classPathXmlApplicationContext.close();
    }
}