package socket;

import com.jfinal.aop.Enhancer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class JobHandler extends SimpleChannelInboundHandler<byte[]> {
	private JobMsgHandler mh = Enhancer.enhance(JobMsgHandler.class);
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
		mh.channelRead0(ctx, msg);
	}

}
