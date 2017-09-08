package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Enhancer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import net.protocol.DeviceCmdHandler;
import net.util.BytesHelper;

public class TestHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public static void channelRead0(IIFrame m_frame, ChannelHandlerContext ctx) throws Exception {
		
		Channel channel = ctx.channel();
		
//		logger.debug("%n=={},报文内容：{}" ,ctx.channel().remoteAddress(), Arrays.toString(req));
//		logger.debug("%n=={},报文内容16进制：{}" ,ctx.channel().remoteAddress(), BytesHelper.byte2hex(req));
		//数据长度
		int length = BytesHelper.getUnsignedInt(m_frame.getLen());
		byte[] equipmentIdbyte = m_frame.getTerminalID();
		//命令编号
		byte[] cmdNobyte = m_frame.getCommandID();
		
		short cmdNo = BytesHelper.toShort(cmdNobyte);
		
		byte cmdbyte = m_frame.getCommand();
		int cmd = BytesHelper.getUnsignedInt(cmdbyte);//如何得到 01 or 02  0E
		String cmdS = Integer.toHexString(cmd).toUpperCase();
		//用户数据
		byte[] data = m_frame.getData();
		
		
		//service handler
		
		DeviceCmdHandler rech = (DeviceCmdHandler)Enhancer.enhance(Class.forName("net.protocol.devicecmd.CmdHandler"+ cmdS));
		
		Object obj = rech.execCmd(equipmentIdbyte, cmdNobyte, cmdbyte, data, channel);
		
		if(obj != null){
			channel.write(obj);
		}
		channel.flush();
	}
}
