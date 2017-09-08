package socket;

import com.jfinal.aop.Enhancer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<byte[]> {
	private ClientMsgHandler mh = Enhancer.enhance(ClientMsgHandler.class);
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
		// TODO Auto-generated method stub
		boolean next = mh.channelRead0(ctx, msg);
		if(!next) {
			ctx.fireChannelRead(msg);
		}
	}

}
