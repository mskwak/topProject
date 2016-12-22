package com.daou;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.daou.command.server.ServerCommand;

/* If this annotation is not specified, you have to create a new handler instance every time you add it to a pipeline because it has unshared state such as member variables.*/
@Sharable
@Component
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {
	private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
	
	// 인터페이스를 구현한 빈에 @Component를 붙여야 한다. 인터페이스에 @Component를 붙이면 스프링이 빈을 스캔하지 못한다.
	@Autowired
	private Map<String, ServerCommand> serverCommandMap;
	
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

		String protocolName = commandList.get(1).toLowerCase();
//		String classFullName = Constants.getMap().get(protocolName);
		ServerCommand command = serverCommandMap.get(protocolName);
//		스프링에 의한 빈 관리를 위해 new 로 객체 생성하는 것을 피한다.
//		ServerCommand command = (ServerCommand) Class.forName(classFullName).newInstance();
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
