package net.protocol.clientcmd;

import java.util.Map;

import com.alibaba.fastjson.JSON;

import exception.AgriException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import msg.PackageMsg;
import net.protocol.ClientCmdHandler;
import net.protocol.Protocol;
import net.protocol.entity.OperateDeviceCmd;
import net.util.BytesHelper;
/**
 * 操作外接设备
 * @author TianW
 *
 */
public class CmdHandler24 extends ClientCmdHandler {

	@Override
	public Object exec(String client, String userId, String uuid, String msg, String sysId, Channel channel) throws AgriException {
		
		OperateDeviceCmd entity = null;
		byte[] data = new byte[4];
		try {
			entity = JSON.parseObject(msg, OperateDeviceCmd.class);
			String objId = entity.getObjId();
			String[] objIdSplit = objId.split("-");
			byte operation = entity.getOperation();
			
			String terminalId = objIdSplit[0];
			String type = objIdSplit[1];
			String number = objIdSplit[2];
			
			data[0] = (byte) Integer.parseInt(terminalId, 16);
			data[1] = (byte) Integer.parseInt(type, 16);
			data[2] = (byte) Integer.parseInt(number, 16);
			data[3] = operation;
		} catch (Exception e) {
			e.printStackTrace();
			int code = prop.getInt("error.format");
			throw new AgriException(code,prop.get(code+"",e.getMessage()));
		}
		Map<String, Channel> mapDeviceChannel = mc.getDeviceToChannel();
		//箱子的管道
		Channel ch = mapDeviceChannel.get(sysId);
		if(ch!=null) {
			ByteBuf cmdMsg = PackageMsg.packingDevice(BytesHelper.getSystemIdByte(sysId), BytesHelper.toBytes(++cmdNO), Protocol.OPERATE_DEVICE_BYTE, data);
			ch.writeAndFlush(cmdMsg);
			logger.debug("操作外接设备命令发送成功");
			//TODO 修改msgMapping
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
