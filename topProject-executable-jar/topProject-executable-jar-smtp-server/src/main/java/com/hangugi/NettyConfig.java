package com.hangugi;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan("com.hangugi")
@PropertySource("classpath:netty-config.properties")
public class NettyConfig {

	@Value("${base.dir}") private String baseDir;
	@Value("${parent.thread.count}") private int parentCount;
	@Value("${worker.thread.count}") private int workerCount;
	@Value("${server.tcp.port}") private int serverTcpPort;
	@Value("${server.io.timeout.second}") private int serverIoTimeout;
	@Value("${client.io.timeout.second}") private int clientIoTimeout;
	@Value("${nate.server.address}") private String nateServerAddress;
	@Value("${nate.server.port}") private int nateServerPort;
	// 기본값 설정 방법 @Value("${parent.log.level:INFO}")
	@Value("${parent.log.level:INFO}") private String parentLogLevel;
	@Value("${worker.log.level:INFO}") private String workerLogLevel;

	@Bean(name="baseDir")
	public String getbaseDir() {
		return this.baseDir;
	}

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

	@Bean(name="parentLogLevel")
	public String getParentLogLevel() {
		return this.parentLogLevel;
	}

	@Bean(name="workerLogLevel")
	public String getWorkerLogLevel() {
		return this.workerLogLevel;
	}

	@Bean(name="nateServerAddress")
	public String getNateServerAddress() {
		return this.nateServerAddress;
	}

	@Bean(name="nateServerPort")
	public int getNateServerPort() {
		return this.nateServerPort;
	}

	//@Bean(name = "tcpSocketAddress") 을 지정하지 않으면 메소드 이름인 inetSocketAddress이 bean 이름이 된다.
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