package net.protocol.devicecmd;

import io.netty.channel.Channel;
import net.protocol.DeviceCmdHandler;
/**
 * 透明传输指令
 * @author TianW
 *
 */
public class CmdHandlerE extends DeviceCmdHandler {

	@Override
	public Object exec(byte[] sysIdbyte, byte[] cmdNo, byte cmd, byte[] data, Channel channel) {
		// TODO Auto-generated method stub
		/**
//		int i = 0;
//		int terId = data[i];
//		i += 3;
//		byte[] serialIdb = new byte[4];
//		System.arraycopy(data, i, serialIdb, 0, 4);
//		i += 4;
		
		
		String sysId = new String(sysIdbyte);
		short cmdNoshort = BytesHelper.toShort(cmdNo);
		String cmdNO = Integer.toString(cmdNoshort);
//		String cmdNO = new String(cmdNo);
		String msgId = sysId + cmdNO;
		//map,用来控制报文的发送 
//		Map<String, String> map = mSession.getAll();
		//客户端发送命令给箱子
//		if(!map.containsKey(uid)) {
			Map<String, Channel> mapChannel = mc.getAll();
			//箱子的管道
			Channel ch = mapChannel.get(sysId);
			//将消息发送给箱子
			if(ch!=null) {
				ch.writeAndFlush(PackageMsg.packingDevice(sysIdbyte, cmdNo, cmd, data));
				//给箱子发完消息后，消息加入session.
//				map.put(uid,"");
			}
			
			
//		}else {//箱子给客户端命令返回消息
			//收到返回消息后，删除该命令session.
//			map.remove(uid);
			logger.debug("%n=={},箱子给客户端命令返回消息，或箱子主动上报信息的data内容：{}" ,channel.remoteAddress(), BytesHelper.byte2hex(data));
//		}
		*/
		return null;
	}

}
