package test;

import java.util.Arrays;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		System.out.println("TestClientHandler Active");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {

		ByteBuf buf = (ByteBuf) msg;
		byte[] response = new byte[buf.readableBytes()];
		buf.readBytes(response);
		System.out.print("收到服务器响应：");
		System.out.println(Arrays.toString(response));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

}