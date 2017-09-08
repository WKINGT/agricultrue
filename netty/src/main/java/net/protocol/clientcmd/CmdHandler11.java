package net.protocol.clientcmd;

import java.util.Map;

import com.alibaba.fastjson.JSON;

import exception.AgriException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import msg.PackageMsg;
import net.protocol.ClientCmdHandler;
import net.protocol.Protocol;
import net.protocol.entity.SearchPlanCmd;
import net.util.BytesHelper;
/**
 * 查询灌溉计划
 * @author TianW
 *
 */
public class CmdHandler11 extends ClientCmdHandler {

	@Override
	public Object exec(String userId, String uuid, String msg, String sysId, Channel channel) throws AgriException {
		
		SearchPlanCmd entity = null;
		byte[] data = new byte[2];
		try {
			entity = JSON.parseObject(msg, SearchPlanCmd.class);
			byte planNO = entity.getPlanNO();         //byte 灌溉计划编号
			byte entryNO = entity.getEntryNO();      // byte  灌溉条目编号
			data[0] = planNO;
			data[1] = entryNO;
			
			
		} catch (Exception e) {
			e.printStackTrace();
			int code = prop.getInt("error.format");
			throw new AgriException(code,prop.get(code+"",e.getMessage()));
		}
//		byte[] a ;
		Map<String, Channel> mapDeviceChannel = mc.getDeviceToChannel();
		//箱子的管道
		Channel ch = mapDeviceChannel.get(sysId);
		if(ch!=null) {
			ByteBuf cmdMsg = PackageMsg.packingDevice(BytesHelper.getSystemIdByte(sysId), BytesHelper.toBytes(++cmdNO), Protocol.SEARCH_PLAN_BYTE, data);
			ch.writeAndFlush(cmdMsg);
			logger.debug("查询灌溉计划命令发送成功");
			String msgId = sysId + cmdNO;
			System.out.println("msgId:"+msgId);
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
