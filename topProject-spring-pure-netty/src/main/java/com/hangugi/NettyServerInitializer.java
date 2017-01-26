package com.hangugi;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
//	private static final Logger logger = LoggerFactory.getLogger(NettyServerInitializer.class);

	@Autowired
	private int serverIoTimeout;

	/* StringDecoder, StringEncoder 는 기본적으로 @Sharable 이다.
	 * @Sharable 자체가 스레드 안전함을 보장하지는 않는다.
	 * 오히려 @Sharable 어노테이션이 붙은 클래스는 스레드 안전하게 만들어야 한다.
	 * */
	private static final StringDecoder DECODER = new StringDecoder();
	private static final StringEncoder ENCODER = new StringEncoder();

	/* ReadTimeoutHandler는 @Sharable 아니기 때문에 객체(인스턴스)를 매번 명시적으로 생성해 주어야 한다. */
	//private static final ReadTimeoutHandler TIMEOUT = new ReadTimeoutHandler(30);
	//private static final NettyServerHandler SERVER_HANDLER = new NettyServerHandler();

	@Override
	public void initChannel(SocketChannel socketChannel) throws Exception {
		ChannelPipeline channelPipeline = socketChannel.pipeline();

		channelPipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		channelPipeline.addLast(DECODER);
		channelPipeline.addLast(ENCODER);

		//int timeout = this.nettyConfig.getServerIoTimeout();
		//logger.info("server io timeout: " + timeout);

		channelPipeline.addLast(new ReadTimeoutHandler(serverIoTimeout));
		channelPipeline.addLast(new NettyServerHandler());
	}
}