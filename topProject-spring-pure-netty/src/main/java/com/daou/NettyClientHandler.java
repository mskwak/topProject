package com.daou;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * wmtad -> serverChannelHandlerContext <-> clentChannelHandlerContext -> nate server
 */

public class NettyClientHandler extends SimpleChannelInboundHandler<String> {

	private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

	private final ChannelHandlerContext serverChannelHandlerContext;

	public NettyClientHandler(ChannelHandlerContext serverChannelHandlerContext) {
		this.serverChannelHandlerContext = serverChannelHandlerContext;
	}

	@Override
    public void channelActive(ChannelHandlerContext clientChannelHandlerContext) throws Exception {
		// 네이트 수신허용 리스트 요청 프로토콜 던지기
    }

	@Override
	public void channelRead0(ChannelHandlerContext clientChannelHandlerContext, String response) throws Exception {
        Channel channel = clientChannelHandlerContext.channel();
//        System.out.println("yyyyyyyyyyyyy:" + channel);
        this.serverChannelHandlerContext.writeAndFlush("mmmmmmmmmmmmmm" + response + "\r\n");
//		logger.info(response);
		logger.info("call the channelRead0 on client");
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext clientChannelHandlerContext) {
		logger.info("call the channelReadComplete on client");
		clientChannelHandlerContext.flush();
		//ctx.close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext clientChannelHandlerContext, Throwable cause) {
		logger.info("call the exceptionCaught on client");
		cause.printStackTrace();
		clientChannelHandlerContext.close();
	}
}
