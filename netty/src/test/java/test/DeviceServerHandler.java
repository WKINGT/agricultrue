package test;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jfinal.aop.Enhancer;

import exception.AgriException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
  
public class DeviceServerHandler extends ChannelInboundHandlerAdapter{  

	
	
	private DeviceMsgHandler mh = Enhancer.enhance(DeviceMsgHandler.class);  
	private ClientMsgHandler mh1 = Enhancer.enhance(ClientMsgHandler.class);
	
    @Override  
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
    	
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		
		System.out.println(new String(req));
		try {
			mh.channelRead0(ctx, req);
		} catch (Exception e) {
			if(e instanceof AgriException) {
				mh1.channelRead0(ctx, req);
			}else {
				e.printStackTrace();
			}
		}
		
//		ctx.fireChannelRead(msg);
		
		buf.release();
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
	}
    
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		System.out.println(incoming.remoteAddress()
				+ "掉线\n掉线时间为："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}
  
}  