package com.hangugi.telnet;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class TelnetServer {
	private static final int PORT = 8023;

	public static void main(String[] args) {
		EventLoopGroup parentGroup = new NioEventLoopGroup(1);
		EventLoopGroup childGroup = new NioEventLoopGroup();

		ServerBootstrap b = new ServerBootstrap();

		b.group(parentGroup, childGroup).
		channel(NioServerSocketChannel.class).
		handler(new LoggingHandler(LogLevel.INFO)).
		childHandler(new TelnetServerInitializer());

		ChannelFuture future;

		try {
			future = b.bind(PORT).sync();
			System.out.println("11111111");
			future.channel().closeFuture().sync();
			System.out.println("22222222");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			parentGroup.shutdownGracefully();
			childGroup.shutdownGracefully();
		}
	}
}
