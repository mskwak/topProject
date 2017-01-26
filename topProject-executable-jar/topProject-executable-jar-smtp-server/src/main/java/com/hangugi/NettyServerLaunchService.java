package com.hangugi;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NettyServerLaunchService {
	private static final Logger logger = LoggerFactory.getLogger(NettyServerLaunchService.class);

//	@Autowired
//	private NettyConfig nettyConfig;

	@Autowired
	private NettyServer nettyServer;

	@Autowired
	private int workerCount;

	private EventLoopGroup parentGroup;

	private EventLoopGroup childGroup;

	public EventLoopGroup getParentGroup() {
		return this.parentGroup;
	}

	public EventLoopGroup getChildGroup() {
		return this.childGroup;
	}

	public void start() {
		this.parentGroup = new NioEventLoopGroup(1);
		this.childGroup = new NioEventLoopGroup(this.workerCount);

		/* new로 생성한 객체에서는 @Autowired 된 빈을 이용할 수 없다. 왜일까? */
//		NettyServer nettyServer = new NettyServer();
		this.nettyServer.start(this.parentGroup, this.childGroup);
	}

	public void stop() {
		logger.info("process shutdownGracefully");
		this.parentGroup.shutdownGracefully();
		this.childGroup.shutdownGracefully();
	}
}
