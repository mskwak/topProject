package com.daou;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class NettyMain {
    public static void main( String[] args ) {
    	String configLocation = "classpath:netty.xml";
    	ApplicationContext applicationContext = new ClassPathXmlApplicationContext(configLocation);

    	final NettyServerLaunchService nettyServerLaunchService = applicationContext.getBean(NettyServerLaunchService.class);
    	nettyServerLaunchService.start();

    	Runtime.getRuntime().addShutdownHook(new Thread() {
    		@Override
			public void run() {
    			nettyServerLaunchService.stop();
    		}
    	});

    	((ClassPathXmlApplicationContext)applicationContext).close();
    }
}