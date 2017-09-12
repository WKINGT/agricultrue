package test;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import msg.PackageMsg;
  
public class TestClient {  
      
    static final String HOST = System.getProperty("host", "127.0.0.1");  
    static final int PORT = Integer.parseInt(System.getProperty("port", "8888"));  
    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));  
  
    public static void main(String[] args) throws Exception {  
  
        // Configure the client.  
        EventLoopGroup group = new NioEventLoopGroup();  
        try {  
            Bootstrap b = new Bootstrap();  
            b.group(group)  
             .channel(NioSocketChannel.class)  
             .option(ChannelOption.TCP_NODELAY, true)  
             .handler(new ChannelInitializer<SocketChannel>() {  
                 @Override  
                 public void initChannel(SocketChannel ch) throws Exception {  
                     ChannelPipeline p = ch.pipeline();  
//                   p.addLast(new LengthFieldBasedFrameDecoder(1024*1024, 0, 4, 0, 4));
                     p.addLast(new ClientHandler());  
                 }  
             });  
  
            ChannelFuture future = b.connect(HOST, PORT).sync();  
//            byte [] a = {104, 6, 104, 17, 34, 51, 68, 85, 2, 0, 100, 01, 17, 7, 18, 11, 24, 10, 0, 22};//设置系统时钟
//            byte[] a = {104,0,104,17, 34, 51, 68, 85, 2,33,33,02,0,22};//获取系统时钟
//            byte[] a = {104,0,104,66,66,66,66,66,66,44,44,49,0,22};//保活
//          byte[] a = {104,4,104,六个字节的设备Id,两个字节命令编号,45,终端ID,设备类型,设备ID,设备操作,0,22};  //操作外接设备。
//            
//            handle1(future.channel());
//            handle2(future.channel());
//            handle2D(future.channel());
//            handle10(future.channel());
//            handle11(future.channel());
//            handle12(future.channel());
//            handle13(future.channel());
//            handle14(future.channel());
            
//            byte [] a = {24, 6, 35, 17, 34, 51, 68, 85, 2, 0, 100, 01, 17, 7, 18, 11, 24, 10, 0, 46};
//            future.channel().writeAndFlush(Unpooled.copiedBuffer(a)); 
            future.channel().closeFuture().sync();  
        } finally {  
            group.shutdownGracefully();  
        }  
    }  
    //设置系统时钟
    protected static void handle1(Channel channel) {
    	
//        byte [] a = {104, 6, 104, 17, 34, 51, 68, 85, 1, 0, 100, 01, 17, 7, 18, 11, 24, 10, 0, 22};
    	
    	Calendar now = Calendar.getInstance();
//		int year =now.get(Calendar.YEAR);
		int yearLast = Integer.parseInt(new SimpleDateFormat("yy",Locale.CHINESE).format(now.getTime()));
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);
		int second = now.get(Calendar.SECOND);
		
		byte[] rData = new byte[6];
		rData[0] = (byte) yearLast;
		rData[1] = (byte) month;
		rData[2] = (byte) day;
		rData[3] = (byte) hour;
		rData[4] = (byte) minute;
		rData[5] = (byte) second;
		
		byte[] equipmentIdbyte = {17, 34, 51, 68, 85, 1};
		byte[] cmdNo = {0,1};
		byte cmd = 1;
		
		ByteBuf a = PackageMsg.packingDevice(equipmentIdbyte, cmdNo, cmd, rData);
        channel.writeAndFlush(a); 
	}
    
    //获取系统时钟
    protected static void handle2(Channel channel) {
    	byte[] a = {104,0,104,17, 34, 51, 68, 85, 1,33,33,02,0,22};
    	channel.writeAndFlush(Unpooled.copiedBuffer(a)); 
	}
    
    //操作外接设备命令
    protected static void handle2D(Channel channel) {
    	//[68 04 68 11 22 33 44 55 01 00 02 2D 00 02 00 00 00 16]
    	byte[] a = {104,4,104,17, 34, 51, 68, 85, 1,44,44,45,0, 02, 0, 0x64, 0,22};
    	channel.writeAndFlush(Unpooled.copiedBuffer(a)); 
	}
    
    //灌溉计划命令
    protected static void handle10(Channel channel) {
    	
    	//[68 17 68 11 22 33 44 55 01 00 01 10 00 01 00 00 00 01 00 64 00 11 01 01 08 01 11 01 02 08 00 00 03 00 01 00 16] 
    	byte[] a = {104,23,104,17, 34, 51, 68, 85, 1,55,55,0x10,0,1,0,0,0, 01, 0,0x64,0x00,17,1,1,8,1,18,1,1,8,1,00,05,00,01,0,22};
    	channel.writeAndFlush(Unpooled.copiedBuffer(a)); 
	}
    
    //查询灌溉计划命令
    protected static void handle11(Channel channel) {
    	byte[] a = {104,2,104,17, 34, 51, 68, 85, 1,66,66,0x11,0,0,0,22};//[68 02 68 11 22 33 44 55 01 00 01 11 00 00 00 16]
    	channel.writeAndFlush(Unpooled.copiedBuffer(a)); 
	}
    
    //启动灌溉计划命令
    protected static void handle12(Channel channel) {
    	byte[] a = {104,1,104,17, 34, 51, 68, 85, 1,77,77,0x12,0,0,22};
    	channel.writeAndFlush(Unpooled.copiedBuffer(a)); 
	}
    
    //停止灌溉计划命令
    protected static void handle13(Channel channel) {
    	byte[] a = {104,1,104,17, 34, 51, 68, 85, 1,88,88,0x13,0,0,22};//[68 01 68 11 22 33 44 55 01 00 01 13 00 00 16]
    	channel.writeAndFlush(Unpooled.copiedBuffer(a)); 
	}
    
    //删除灌溉计划命令
    protected static void handle14(Channel channel) {
    	byte[] a = {104,1,104,17, 34, 51, 68, 85, 1,99,99,0x14,0,0,22};//[68 01 68 11 22 33 44 55 01 00 01 14 00 00 16]
    	channel.writeAndFlush(Unpooled.copiedBuffer(a)); 
	}
  
}  