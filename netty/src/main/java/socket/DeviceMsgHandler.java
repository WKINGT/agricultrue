package socket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.kit.PropKit;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import net.protocol.DeviceCmdHandler;
import net.protocol.Protocol;
import net.util.BytesHelper;

public class DeviceMsgHandler {
	private static final int started = Protocol.BEGIN_BYTE;
	private static final int ended = Protocol.END_BYTE;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
//	private Channel incoming = null;
	
	public boolean channelRead0(ChannelHandlerContext ctx, byte[] req) throws Exception {
		
		Channel channel = ctx.channel();
		
//		logger.debug("%n=={},报文内容：{}" ,ctx.channel().remoteAddress(), Arrays.toString(req));
		logger.debug("%n=={},报文内容16进制：{}" ,ctx.channel().remoteAddress(), BytesHelper.byte2hex(req));
		
		int i=0;
		//起始字符
		if(BytesHelper.getUnsignedInt(req[i]) != started){
			//
			logger.error("不是控制器通信协议格式");
			PropKit.use("errcode.txt").getInt("error.deviceFormat");
//			throw new AgriException(code,PropKit.use("errcode.txt").get(code+""));
			return false;
		}
		//数据长度
		i++;
		int length = BytesHelper.getUnsignedInt(req[i]);
		//起始字符
		i++;
//		byte start2 = req[i];
		if(BytesHelper.getUnsignedInt(req[i]) != started){
			//
			logger.error("报文格式错误");
//			return;
		}
		i++;
		//设备ID
		byte[] equipmentIdbyte = new byte[6];
		System.arraycopy(req, i, equipmentIdbyte, 0, 6);
		i = i+6;
		//命令编号
		byte[] cmdNobyte = new byte[2];
		System.arraycopy(req, i, cmdNobyte, 0, 2);
//		int cmdNo = BytesHelper.toInt(cmdNobyte);
		BytesHelper.toShort(cmdNobyte);
		i = i+2;
		//命令
		byte cmdbyte = req[i];
		int cmd = BytesHelper.getUnsignedInt(cmdbyte);//如何得到 01 or 02  0E
		String cmdS = Integer.toHexString(cmd).toUpperCase();
		i++;
		//用户数据
		byte[] data = null;
		if(length!=0) {
			data = new byte[length];
			System.arraycopy(req,i, data, 0, length);
			i += length;
		}
		
		
		byte crc = req[i];
		System.out.println(crc);
		i++;
		
		if(BytesHelper.getUnsignedInt(req[i]) != ended){
			//
			logger.error("报文格式错误");
//			return;
		}
		
		//services handler
		
		DeviceCmdHandler rech = DeviceCmdHandler.getHandler(cmdS);
		
		Object obj = rech.execCmd(equipmentIdbyte, cmdNobyte, cmdbyte, data, channel);
		
		if(obj != null){
			channel.write(obj);
		}
		channel.flush();
		return true;
	}
	
}
