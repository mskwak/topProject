package com.hangugi;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NettyServer {
	private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

	@Autowired
	private InetSocketAddress inetSocketAddress;

	@Autowired
	private NettyServerInitializer nettyServerInitializer;

	public void start(EventLoopGroup parentGroup, EventLoopGroup childGroup) {
		ServerBootstrap serverBootstrap = new ServerBootstrap();

		serverBootstrap.
		group(parentGroup, childGroup).
		channel(NioServerSocketChannel.class).
		option(ChannelOption.SO_REUSEADDR, true).
		childHandler(this.nettyServerInitializer);

		try {
			/* p.150 자바 네트워크 소녀 네티 */
			logger.info("bind: " + this.inetSocketAddress.getHostName() + ":" + this.inetSocketAddress.getPort());

			ChannelFuture bindChannelFuture = serverBootstrap.bind(this.inetSocketAddress);
			bindChannelFuture.sync();

			Channel channel = bindChannelFuture.channel();

			ChannelFuture closeChannelFuture = channel.closeFuture();
			closeChannelFuture.sync();
		} catch (InterruptedException e) {
			logger.error("", e.getStackTrace());
		} finally {
			logger.info("call shutdownGracefully on server.");
			parentGroup.shutdownGracefully();
			childGroup.shutdownGracefully();
		}
	}
}
