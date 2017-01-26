package com.hangugi;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

import org.springframework.beans.factory.annotation.Autowired;

public final class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
	@Autowired
	private NettyConfig nettyConfig;

	private final ChannelHandlerContext ctx;

	public NettyClientInitializer(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	private static final StringDecoder DECODER = new StringDecoder();
	private static final StringEncoder ENCODER = new StringEncoder();
	//private static final NettyClientHandler CLIENT_HANDLER = new NettyClientHandler();

	@Override
	public void initChannel(SocketChannel socketChannel) throws Exception {
//		System.out.println("zzzzzzzzzzzzz:" + socketChannel);
		ChannelPipeline channelPipeline = socketChannel.pipeline();

		channelPipeline.addLast(DECODER);
		channelPipeline.addLast(ENCODER);
		channelPipeline.addLast(new ReadTimeoutHandler(300));
		channelPipeline.addLast(new NettyClientHandler(this.ctx));
	}
}
