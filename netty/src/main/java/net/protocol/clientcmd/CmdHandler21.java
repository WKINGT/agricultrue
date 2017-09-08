package net.protocol.clientcmd;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import exception.AgriException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import msg.PackageMsg;
import net.protocol.ClientCmdHandler;
import net.protocol.Protocol;
import net.util.BytesHelper;
/**
 * 设置系统时间
 * @author TianW
 *
 */
public class CmdHandler21 extends ClientCmdHandler {

	@Override
	public Object exec(String userId, String uuid, String msg, String sysId, Channel channel) throws AgriException {
		
		// 给箱子发送命令
		
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
		Map<String, Channel> mapDeviceChannel = mc.getDeviceToChannel();
		//箱子的管道
		Channel ch = mapDeviceChannel.get(sysId);
		if(ch!=null) {
			ByteBuf cmdMsg = PackageMsg.packingDevice(BytesHelper.getSystemIdByte(sysId), BytesHelper.toBytes(++cmdNO), Protocol.SET_TIME_BYTE, rData);
			ch.writeAndFlush(cmdMsg);
			String msgId = sysId + cmdNO;
			Map<String, String> msgIDtoUser = msgMapping.getMsgIdtoUser();
			Map<String, String> msgIdtoUUID = msgMapping.getMsgIdtoUUID();
			msgIDtoUser.put(msgId, userId);
			msgIdtoUUID.put(msgId, uuid);
		}else {
			//TODO 给手机端返回错误信息
			int code = prop.getInt("error.wrongSysId");
			throw new AgriException(code,prop.get(code+""));
		}
		
		return null;
	}

}
