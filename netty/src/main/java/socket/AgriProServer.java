package socket;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import socket.testdecode.AgriDecoder;  
  
public class AgriProServer {  
	protected static DruidPlugin dp;
    protected static ActiveRecordPlugin activeRecord;
    private int port;  
      
    public AgriProServer(int port) {  
        this.port = port;  
    }  
      
    public void start(){  
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);  
        EventLoopGroup workerGroup = new NioEventLoopGroup();  
        try {  
            ServerBootstrap sbs = new ServerBootstrap().group(bossGroup,workerGroup).channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(port))  
                    .childHandler(new ChannelInitializer<SocketChannel>() {  
                          
                        protected void initChannel(SocketChannel ch) throws Exception {  
                        	ch.pipeline().addLast(new IdleStateHandler(50 , 50 , 50, TimeUnit.SECONDS));
//                        	ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024*1024, 0, 4, 0, 4));
                        	ch.pipeline().addLast(new AgriDecoder());
                        	ch.pipeline().addLast(new ByteArrayDecoder());
                        	ch.pipeline().addLast(new ByteArrayEncoder());
                        	ch.pipeline().addLast(new DeviceHandler());
                        	ch.pipeline().addLast(new ClientHandler());
                        	ch.pipeline().addLast(new JobHandler());
                        };  
                          
                    }).option(ChannelOption.SO_BACKLOG, 128)     
                    .childOption(ChannelOption.SO_KEEPALIVE, true);  
             // 绑定端口，开始接收进来的连接  
             ChannelFuture future = sbs.bind(port).sync();    
               
             System.out.println("Server start listen at " + port );  
             future.channel().closeFuture().sync();  
        } catch (Exception e) {  
            bossGroup.shutdownGracefully();  
            workerGroup.shutdownGracefully();  
        }  
    }  
      
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {  
        int port;  
        if (args.length > 0) {  
            port = Integer.parseInt(args[0]);  
        } else {  
            port = 8888;  
        }  
        new AgriProServer(port).start();  
    }  
}  