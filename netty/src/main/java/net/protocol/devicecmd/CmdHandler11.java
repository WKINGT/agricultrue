package net.protocol.devicecmd;

import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import msg.PackageMsg;
import net.protocol.DeviceCmdHandler;
import net.protocol.Protocol;
import net.protocol.entity.SearchPlanResp;
import net.util.BytesHelper;

public class CmdHandler11 extends DeviceCmdHandler {
	@Override
	public Object exec(byte[] sysIdbyte, byte[] cmdNo, byte cmd, byte[] data, Channel channel) {
		logger.debug("{},箱子给客户端发查询灌溉计划命令的返回消息{}" ,channel.remoteAddress(), BytesHelper.byte2hex(data));
		SearchPlanResp resp = new SearchPlanResp();
		resp.setCode(BytesHelper.getUnsignedInt(data[0]));
		if(BytesHelper.getUnsignedInt(data[0])==0) {//执行结果为0，表示成功，非0时，表示失败的错误码，非0时只有执行结果字段
			resp.setPlanStatus(data[1]);
		}
		
		String sysId = BytesHelper.getSystemId(sysIdbyte);
		short cmdNoshort = BytesHelper.toShort(cmdNo);
		String cmdNO = Integer.toString(cmdNoshort);
//		String cmdNO = new String(cmdNo);
		String msgId = sysId + cmdNO;
		
		Map<String, String> msgIDtoUser = msgMapping.getMsgIdtoUser();
		Map<String, String> msgIdtoUUID = msgMapping.getMsgIdtoUUID();
//		Map<String, Channel> userIdtoChannel = mSession.getUserIdTOChannel();
		String userId = msgIDtoUser.get(msgId);
		String uuid = msgIdtoUUID.get(msgId);
		//下命令用户的管道
//		Channel ch = userIdtoChannel.get(userId);
//		if(ch!=null) {
//			ByteBuf respMsg = PackageMsg.packingClient(sysId, uuid, Protocol.SEARCH_PLAN, resp);
//			ch.writeAndFlush(respMsg);
//
//			if(BytesHelper.getUnsignedInt(data[0])==0) {
//				logger.debug("箱子给客户端发查询灌溉计划状态命令的返回消息，返回码为--{}，命令状态码为--{}",Integer.toHexString(BytesHelper.getUnsignedInt(data[0])),Integer.toHexString(BytesHelper.getUnsignedInt(data[1])));
//			}else {
//				logger.debug("箱子给客户端发查询灌溉计划状态命令的返回消息成功，查询计划错误码为--{}",Integer.toHexString(BytesHelper.getUnsignedInt(data[0])));
//			}
//
//		}
		// 清除该消息的session
		msgIDtoUser.remove(msgId);
		msgIdtoUUID.remove(msgId);
		return null;
	}

}
