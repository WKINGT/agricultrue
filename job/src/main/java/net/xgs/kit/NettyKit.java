package net.xgs.kit;

import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.xgs.config.JobConfig;
import net.xgs.entity.NettyEntity;
import net.xgs.utils.RSAEncrypt;

import java.util.UUID;

/**
 * Created by duai on 2017-08-17.
 */
public class NettyKit {
    //给服务端标识此报文来自于任务
    private static final byte ID = 0x70;
    static RSAEncrypt rsaEncrypt= new RSAEncrypt(JobConfig.RSA.get("private.key"),JobConfig.RSA.get("public.key"));

    public static void sendMsg(String data) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new NettyClientInitializer());
            // 连接服务端
            Channel ch = b.connect(JobConfig.prop.get("system.host"), JobConfig.prop.getInt("system.port")).sync().channel();
            //加密
            byte[] cipher = rsaEncrypt.encrypt(rsaEncrypt.getPublicKey(), data.getBytes());
            byte[] requestData = new byte[cipher.length + 5];
            requestData[0] = ID;
            System.arraycopy(intToBytes2(cipher.length), 0, requestData, 1, 4);
            System.arraycopy(cipher, 0, requestData, 5, cipher.length);
            ChannelFuture future = ch.writeAndFlush(requestData);
            future.channel().closeFuture().sync();
        }catch (Exception e){

        }finally {
            // The connection is closed automatically on shutdown.
            group.shutdownGracefully();
        }
    }
    public static byte[] intToBytes2(int value)
    {
        byte[] src = new byte[4];
        src[0] = (byte) ((value>>24) & 0xFF);
        src[1] = (byte) ((value>>16)& 0xFF);
        src[2] = (byte) ((value>>8)&0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }


    public static void main(String[] args) throws InterruptedException {
      while (true){
          NettyEntity nettyEntity = new NettyEntity();
          nettyEntity.setDeviceId("00-03-00");
          nettyEntity.setSystemId("11-22-33-44-55-02");
          nettyEntity.setOperation("64");
          nettyEntity.setUuid(UUID.randomUUID().toString().replace("-",""));
          sendMsg( JSONObject.toJSONString(nettyEntity));
          Thread.sleep(30000);
      }
    }
}
