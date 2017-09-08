package netty;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientServerHandler extends ChannelInboundHandlerAdapter {
	private Logger logger = LoggerFactory.getLogger(getClass());
@Override  
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		logger.error("报文转到第二个handler开始处理");
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];

		buf.readBytes(req);
		
		System.out.println(new String(req));
		
//		mh.channelRead0(ctx, req);
		
		buf.release();
		
		ctx.fireChannelRead(msg);

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		// ctx.close();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
//		mh.channelActive(ctx);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception { // (3)
		Channel incoming = ctx.channel();

		// Broadcast a message to multiple Channels
		incoming.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 离开\n");
	}
	    
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		System.out.println(incoming.remoteAddress() + "掉线\n掉线时间为："
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
//		if (evt instanceof IdleStateEvent) {
//			IdleStateEvent event = (IdleStateEvent) evt;
//			String type = "";
//			if (event.state() == IdleState.READER_IDLE) {
//				i++;
//				if(i==3){
//					Session.instance().destroy(ctx.channel());
//					ctx.channel().close();
//					logger.error("{}超时,类型{}",ctx.channel().remoteAddress(),type);
//					return;
//				}
//				type = "read idle";
//			} else if (event.state() == IdleState.WRITER_IDLE) {
//				type = "write idle";
//			} else if (event.state() == IdleState.ALL_IDLE) {
//				type = "all idle";
//			}
//			logger.info("{}超时,类型{}",ctx.channel().remoteAddress(),type);
//		} else {
//			logger.debug(evt.toString());
//			super.userEventTriggered(ctx, evt);
//		}
	}
}
