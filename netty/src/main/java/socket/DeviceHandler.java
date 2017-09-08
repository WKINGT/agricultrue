package socket;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Enhancer;

import channel.OfflineHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class DeviceHandler extends SimpleChannelInboundHandler<byte[]> {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private int i = 0;
	private DeviceMsgHandler mh = Enhancer.enhance(DeviceMsgHandler.class);  
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
		// TODO Auto-generated method stub
		boolean next = mh.channelRead0(ctx, msg);
		if(!next) {
			ctx.fireChannelRead(msg);
		}
		
	}
	@Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
        cause.printStackTrace();  
//        ctx.close();  
    }
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception { // (3)
		Channel incoming = ctx.channel();

		// Broadcast a message to multiple Channels
		incoming.writeAndFlush("[SERVER] - " + incoming.remoteAddress()
				+ " 离开\n");
		OfflineHandler.getInstance().offlineDestory(ctx.channel());
		logger.info("HandlerRemoved ,sessionMap and channel are destroyed!!");
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			String type = "";
			if (event.state() == IdleState.READER_IDLE) {
				i++;
				if(i==3){
					OfflineHandler.getInstance().offlineDestory(ctx.channel());
					ctx.channel().close();
					logger.error("{}超时,类型{}",ctx.channel().remoteAddress(),type);
					logger.info("idleStateEvent , sessionMap and channel are destroyed!!");
					return;
				}
				type = "read idle";
			} else if (event.state() == IdleState.WRITER_IDLE) {
				type = "write idle";
			} else if (event.state() == IdleState.ALL_IDLE) {
				type = "all idle";
			}
			logger.info("{}超时,类型{}",ctx.channel().remoteAddress(),type);
		} else {
			logger.debug(evt.toString());
			super.userEventTriggered(ctx, evt);
		}
	}
	public void channelActive(ChannelHandlerContext ctx) throws Exception{
		Channel incoming = ctx.channel();
		System.out.println("连接" + incoming.remoteAddress()
				+ "在线\n在线时间为："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		System.out.println(incoming.remoteAddress() + "掉线\n掉线时间为："
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		//FIXME session remove
	}
}
