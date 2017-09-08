package msg;

import java.util.Map;

import channel.MapDeviceChannel;
import io.netty.channel.Channel;

public class SendMsg {
	protected static Map<String, Channel> mapDeviceChannel = MapDeviceChannel.instance().getDeviceToChannel();
	
//	public static void toDevice(String sysId,short cmdNO) {
//		//箱子的管道
//		Channel ch = mapDeviceChannel.get(sysId);
//		if(ch!=null) {
//			ByteBuf cmdMsg = PackageMsg.packingDevice(sysId.getBytes(), BytesHelper.toBytes(cmdNO), Protocol.CREATE_PLAN_BYTE, data);
//			ch.writeAndFlush(cmdMsg);
//			//TODO 修改msgMapping
//			String msgId = sysId + cmdNO;
//			Map<String, String> msgIDtoUser = msgMapping.getMsgIdtoUser();
//			Map<String, String> msgIdtoUUID = msgMapping.getMsgIdtoUUID();
//			msgIDtoUser.put(msgId, userId);
//			msgIdtoUUID.put(msgId, uuid);
//		}else {
//			//TODO 给手机端返回错误信息
//			int code = prop.getInt("error.wrongSysId");
//			throw new AgriException(code,prop.get(code+""));
//		}
//	}
	
}
