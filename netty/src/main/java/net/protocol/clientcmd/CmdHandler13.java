package net.protocol.clientcmd;

import java.util.Map;

import com.alibaba.fastjson.JSON;

import exception.AgriException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import msg.PackageMsg;
import net.protocol.ClientCmdHandler;
import net.protocol.Protocol;
import net.protocol.entity.EndPlanCmd;
import net.util.BytesHelper;

/**
 * 停止灌溉计划
 * @author TianW
 *
 */
public class CmdHandler13 extends ClientCmdHandler {
	@Override
	public Object exec(String client, String userId, String uuid, String msg, String sysId, Channel channel) throws AgriException {
		
		EndPlanCmd entity = null;
		byte[] data = new byte[1];
		try {
			entity = JSON.parseObject(msg, EndPlanCmd.class);
			byte planNO = entity.getPlanNO();         //byte 灌溉计划编号
			data[0] = planNO;
		} catch (Exception e) {
			e.printStackTrace();
			int code = prop.getInt("error.format");
			throw new AgriException(code,prop.get(code+"",e.getMessage()));
		}
		Map<String, Channel> mapDeviceChannel = mc.getDeviceToChannel();
		//箱子的管道
		Channel ch = mapDeviceChannel.get(sysId);
		if(ch!=null) {
			ByteBuf cmdMsg = PackageMsg.packingDevice(BytesHelper.getSystemIdByte(sysId), BytesHelper.toBytes(++cmdNO), Protocol.END_PLAN_BYTE, data);
			ch.writeAndFlush(cmdMsg);
			//TODO 修改msgMapping
			String msgId = sysId + cmdNO;
			Map<String, String> msgIDtoUser = msgMapping.getMsgIdtoUser();
			Map<String, String> msgIdtoUUID = msgMapping.getMsgIdtoUUID();
			msgIDtoUser.put(msgId, userId);
			msgIdtoUUID.put(msgId, uuid);
			logger.debug("停止灌溉计划命令发送成功");
			
		}else {
			//TODO 给手机端返回错误信息
			int code = prop.getInt("error.wrongSysId");
			throw new AgriException(code,prop.get(code+""));
		}
		return null;
	}

}
