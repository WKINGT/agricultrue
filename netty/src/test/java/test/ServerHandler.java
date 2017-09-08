package test;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.jfinal.aop.Enhancer;
  
public class ServerHandler extends ChannelInboundHandlerAdapter{  

	
//	ChannelGroup channelGroups = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	private MsgHandler mh = Enhancer.enhance(MsgHandler.class);  
	
//	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
////		session.put(ctx.channel().id().asShortText(),ctx.channel());
//        channelGroups.add(ctx.channel());
//    }
	
    @Override  
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
    	
		ByteBuf buf = (ByteBuf) msg;
		
//		List<IIFrame> out = null;
		ReadBuf.Decode(ctx,buf);
//		if(buf.readByte()==0x68) {
//				System.out.println(68);
//				buf.readBytes(3);
//				int len  = BytesHelper.getUnsignedInt(buf.readByte());
//				if(buf.readByte()==0x68) {
//					System.out.println(68);
//					buf.release();
//					System.out.println(buf.readableBytes());
//				}
//			}
//		byte[] req = new byte[buf.readableBytes()];
//		buf.readBytes(req);
		
//		System.out.println(new String(req));
//		
//		mh.channelRead0(false, ctx, req);
		
		
//		buf.release();

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