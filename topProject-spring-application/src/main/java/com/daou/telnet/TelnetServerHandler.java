package com.daou.telnet;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetAddress;
import java.util.Date;

public class TelnetServerHandler extends SimpleChannelInboundHandler<String> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.write(InetAddress.getLocalHost());
		ctx.write(new Date());
		ctx.write("xxxxxxxxxxxxxxxxxyyyyyyyyyyyyyyy");
		ctx.flush();
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
		String response;
		boolean close = false;

		if(request.isEmpty()) {
			response = "Input the command.\r\n";
		} else if("bye".equals(request.toLowerCase())) {
			response = "have a good time\r\n";
			close = true;
		} else {
			response = "Did you input the " + request + " ?";
		}

		ChannelFuture future = ctx.write(response);

		if(close) {
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
