package net.xgs.kit;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by duai on 2017-08-17.
 */
public class SubReqClientHanler extends ChannelHandlerAdapter {
    public SubReqClientHanler(){

    }

    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("----------------handler channelActive-----准备发送十个数据-------");
        ctx.write("ssss");
        ctx.flush();
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception{
        System.out.println("--------channelRead---服务器发来的数据为：[" + msg + "]");
    }

    public void channelReadComplete(ChannelHandlerContext ctx)
            throws Exception{
        System.out.println("----------------handler channelReadComplete");
        ctx.flush();
    }


    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        System.out.println("--------------------------------------------------------------------------handler exceptionCaught");
        cause.printStackTrace();
        ctx.close();
    }
}
