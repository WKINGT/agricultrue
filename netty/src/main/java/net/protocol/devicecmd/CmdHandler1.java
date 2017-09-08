package net.protocol.devicecmd;

import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import msg.PackageMsg;
import net.protocol.DeviceCmdHandler;
import net.protocol.Protocol;
import net.protocol.entity.GetTimeResp;
import net.util.BytesHelper;
import net.util.DateUtil;
/**
 * 设置系统时钟命令
 * @author TianW
 *
 */
public class CmdHandler1 extends DeviceCmdHandler {
	@Override
	public Object exec(byte[] sysIdbyte, byte[] cmdNo, byte cmd, byte[] data, Channel channel) {
		logger.debug("{},箱子给客户端设置系统时钟命令的返回消息{}" ,channel.remoteAddress(), BytesHelper.byte2hex(data));
		int i = 0;
		int yearlast = BytesHelper.getUnsignedInt(data[i++]);
		int month = BytesHelper.getUnsignedInt(data[i++]);
		int day = BytesHelper.getUnsignedInt(data[i++]);
		int hour = BytesHelper.getUnsignedInt(data[i++]);
		int minute = BytesHelper.getUnsignedInt(data[i++]);
		int second = BytesHelper.getUnsignedInt(data[i++]);
		
		String time = DateUtil.getStringTime(yearlast, month, day, hour, minute, second);
		String sysId = BytesHelper.getSystemId(sysIdbyte);
		short cmdNoshort = BytesHelper.toShort(cmdNo);
		String cmdNO = Integer.toString(cmdNoshort);
		String msgId = sysId + cmdNO;
		GetTimeResp resp = new GetTimeResp();
		resp.setCode(0);
		resp.setTime(time);
		
		Map<String, String> msgIDtoUser = msgMapping.getMsgIdtoUser();
		Map<String, String> msgIdtoUUID = msgMapping.getMsgIdtoUUID();
		Map<String, Channel> userIdtoChannel = mSession.getUserIdTOChannel();
		String userId = msgIDtoUser.get(msgId);
		String uuid = msgIdtoUUID.get(msgId);
		//下命令用户的管道
		Channel ch = userIdtoChannel.get(userId);
		if(ch!=null) {
			ByteBuf respMsg = PackageMsg.packingClient(sysId, uuid, Protocol.SET_TIME, resp);
			ch.writeAndFlush(respMsg);
		}
		//清除该消息的session
		msgIDtoUser.remove(msgId);
		msgIdtoUUID.remove(msgId);

		return null;
	}

}
