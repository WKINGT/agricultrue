package websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 扩展 ChannelInitializer
 * @author hasee
 *
 */
public class WebsocketChatServerInitializer extends ChannelInitializer<SocketChannel>  {

	/**
	 * 添加 ChannelHandler　到 ChannelPipeline
	 * initChannel() 方法设置 ChannelPipeline 中所有新注册的 Channel,
	 * 安装所有需要的　 ChannelHandler。
	 */
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		pipeline.addLast(new IdleStateHandler(50 , 50 , 50, TimeUnit.SECONDS));
		pipeline.addLast(new HttpServerCodec());//http解码,websocket是以http发起请求的
		pipeline.addLast(new HttpObjectAggregator(64*1024));//HttpObjectAggregator会把多个消息转换为一个单一的FullHttpRequest或是FullHttpResponse
		pipeline.addLast(new ChunkedWriteHandler());
//		pipeline.addLast(new HttpRequestHandler("/ws"));
		pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
		pipeline.addLast(new TextWebSocketFrameHandler());
		
//		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
//		pipeline.addLast("decoder",new StringDecoder());
//		pipeline.addLast("encoder",new StringEncoder());
//		pipeline.addLast("handler",new SimpleChatServerHandler());
	}

}
