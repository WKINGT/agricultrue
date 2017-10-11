package net.protocol.jobcmd;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import channel.MapDeviceChannel;
import channel.MsgMapping;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import msg.PackageMsg;
import net.protocol.Protocol;
import net.protocol.entity.JobControlDeviceReq;
import net.util.BytesHelper;
import session.MapSysIdUserId;
import session.Terminal;
import session.UserMap;

public class ContronlDevice {
	
	protected static Logger logger = LoggerFactory.getLogger(ContronlDevice.class);
	private static MapDeviceChannel mc = MapDeviceChannel.instance();
	private static short cmdN0=100;
	private static MapSysIdUserId session = MapSysIdUserId.getInstance();


	public static Object exec(Channel channel, Object msg) {
		JobControlDeviceReq entity = (JobControlDeviceReq) msg;
		String systemId = entity.getSystemId();
		String deviceId = entity.getDeviceId();
		String stringOperation = entity.getOperation();
		String uuid = entity.getUuid();
		byte operation = (byte) Integer.parseInt(stringOperation, 16);
		byte[] data = new byte[4];
		byte[] objId = BytesHelper.getObjIdByte(deviceId);

		data[0] = objId[0];
		data[1] = objId[1];
		data[2] = objId[2];
		data[3] = operation;
		
		Map<String, Channel> mapDeviceChannel = mc.getDeviceToChannel();
		//箱子的管道
		Channel ch = mapDeviceChannel.get(systemId);
		Map<String,Object> resp = new HashMap<String,Object>();
		if(ch!=null) {
			ByteBuf cmdMsg = PackageMsg.packingDevice(BytesHelper.getSystemIdByte(systemId), BytesHelper.toBytes(++cmdN0), Protocol.OPERATE_DEVICE_BYTE, data);
			ch.writeAndFlush(cmdMsg);
			logger.debug("计划操作设备命令发送成功");
			

			String userId = "jobId"+ cmdN0;
			session.getMapChannelToUserId().put(channel,userId);
			session.getMapChannelToClient().put(channel,Protocol.JOB);
			Terminal ter = new Terminal();
			ter.put(Protocol.JOB,channel);
			UserMap userMap = new UserMap();
			userMap.put(userId,ter);
			session.getSessionMap().put(systemId,userMap);
			String msgId = systemId + cmdN0;

			//消息映射
			Map<String, String> msgIDtoUser = MsgMapping.instance().getMsgIdtoUser();
			Map<String, String> msgIdtoUUID = MsgMapping.instance().getMsgIdtoUUID();
			msgIDtoUser.put(msgId, userId);
			msgIdtoUUID.put(msgId, uuid);
			return null;
		}else {
			logger.debug("计划操作设备命令发送失败，要操作的设备不存在，或离线");
			resp.put("code", 1);
			resp.put("uuid", uuid);
		}
		return resp;
	}
}
