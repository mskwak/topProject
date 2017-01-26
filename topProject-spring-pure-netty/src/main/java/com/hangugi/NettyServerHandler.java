package com.hangugi;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hangugi.command.server.ServerCommand;

/* If this annotation is not specified, you have to create a new handler instance every time you add it to a pipeline because it has unshared state such as member variables.*/
@Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {
	private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

	/* greeting message 설정 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		/* SimpleChannelInboundHandler<String> 와 같이 제네릭을 String으로 설정했기 때문에 그런지 클라이언트에서 greeting message가 출력되지 않는다.
		ctx.write(InetAddress.getLocalHost());
		ctx.flush();
		ctx.write(new Date());
		ctx.flush();
		*/
		logger.info("call the channelActive on server.");

		ctx.write("* OK greeting sf_ladmd dbagt service ready protocol-version:1.0\r\n");
		ctx.flush();
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
		logger.info("call the channelRead0 on server.");
		String response;

		List<String> commandList = CommandParserUtils.getCommandList(request);

		if(commandList.isEmpty()) {
			ctx.write(CommandParserUtils.getResponse(Constants.TAG, Constants.BAD, Constants.INVALID_COMMAND));
			return;
		}

		String protocolName = commandList.get(1);
		String classFullName = Constants.getMap().get(protocolName);

		ServerCommand command = (ServerCommand) Class.forName(classFullName).newInstance();
		response = command.executeCommand(commandList);

		ChannelFuture channelFuture = ctx.write(response);

		if(command.isClose()) {
			channelFuture.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		logger.info("call the channelReadComplete on server.");
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.error("call the exceptionCaught on server.");
		ctx.close();
	}
}
