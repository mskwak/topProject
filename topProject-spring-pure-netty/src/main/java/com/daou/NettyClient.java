package com.daou;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClient {
	private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

	public String doJob(ChannelHandlerContext ctx, String emailAddress) {
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();

		bootstrap.group(eventLoopGroup).
		channel(NioSocketChannel.class).
		handler(new NettyClientInitializer(ctx));

		try {
			ChannelFuture connectChannelFuture = bootstrap.connect("inbound.daou.co.kr", 25);
			connectChannelFuture.sync();

			Channel channel = connectChannelFuture.channel();

//			System.out.println("xxxxxxxxxxxxx:" + channel);

			ChannelFuture c0 = channel.writeAndFlush("mail from: " + emailAddress + "\r\n");

			ChannelFuture c1 = channel.writeAndFlush("quit" + "\r\n");

//			System.out.println("xxxxxxxxxx111:" + c0.channel());
//
//			System.out.println("xxxxxxxxxx111:" + channel.read().toString());
//
//			ChannelHandlerContext channelHandlerContext0 = channel.pipeline().firstContext();
//			System.out.println("xxxxxxxxxx111:" + channelHandlerContext0.name());
//
//			ChannelHandlerContext channelHandlerContext1 = channel.pipeline().lastContext();
//			System.out.println("xxxxxxxxxx111:" + channelHandlerContext1.name());
//
//			ChannelFuture c1 = channel.writeAndFlush("bbbbbbbbbbb\r\n");
//			System.out.println("xxxxxxxxxx222:" + c1.channel());

			ChannelFuture closeChannelFuture = channel.closeFuture();
			closeChannelFuture.sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			logger.info("eventLoopGroup shutdownGracefully on client");
			eventLoopGroup.shutdownGracefully();
		}

		return null;
	}
}
