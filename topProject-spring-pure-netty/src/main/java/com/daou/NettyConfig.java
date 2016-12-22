package com.daou;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan("com.daou")
@PropertySource("classpath:netty-config.properties")
public class NettyConfig {

	@Value("${parent.thread.count}")
	private int parentCount;

	@Value("${worker.thread.count}")
	private int workerCount;

	@Value("${server.tcp.port}")
	private int serverTcpPort;

	@Value("${server.io.timeout.second}")
	private int serverIoTimeout;

	@Value("${client.io.timeout.second}")
	private int clientIoTimeout;

	@Bean(name="parentCount")
	public int getParentCount() {
		return this.parentCount;
	}

	@Bean(name="workerCount")
	public int getWorkerCount() {
		return this.workerCount;
	}

	@Bean(name="serverIoTimeout")
	public int getServerIoTimeout() {
		return this.serverIoTimeout;
	}

	@Bean(name="clientIoTimeout")
	public int getClientIoTimeout() {
		return this.clientIoTimeout;
	}

	//@Bean(name = "tcpSocketAddress")
	@Bean
	public InetSocketAddress inetSocketAddress() {
		return new InetSocketAddress(this.serverTcpPort);
	}

	// p.102 스프링 3.0 프로그래밍
	// 스프링 3.1 부터는 PropertySourcesPlaceholderConfigurer 를 사용해라.
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}