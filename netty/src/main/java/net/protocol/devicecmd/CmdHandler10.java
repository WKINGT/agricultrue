package net.protocol.devicecmd;

import java.util.Map;

import com.jfinal.aop.Enhancer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import msg.PackageMsg;
import net.protocol.DeviceCmdHandler;
import net.protocol.Protocol;
import net.protocol.entity.CreatePlanResp;
import net.util.BytesHelper;
import net.xgs.services.TaskPlanService;

public class CmdHandler10 extends DeviceCmdHandler {
	private TaskPlanService taskPlanService = Enhancer.enhance(TaskPlanService.class);
	@Override
	public Object exec(byte[] sysIdbyte, byte[] cmdNo, byte cmd, byte[] data, Channel channel) {
		logger.debug("=={},箱子给客户端创建灌溉计划命令的返回消息{}" ,channel.remoteAddress(), BytesHelper.byte2hex(data));
		CreatePlanResp resp = new CreatePlanResp();
		resp.setCode(BytesHelper.getUnsignedInt(data[0]));
		
		String sysId = BytesHelper.getSystemId(sysIdbyte);
		short cmdNoshort = BytesHelper.toShort(cmdNo);
		String cmdNO = Integer.toString(cmdNoshort);
		String msgId = sysId + cmdNO;
		
		Map<String, String> msgIDtoUser = msgMapping.getMsgIdtoUser();
		Map<String, String> msgIdtoUUID = msgMapping.getMsgIdtoUUID();
		Map<String, Channel> userIdtoChannel = mSession.getUserIdTOChannel();
		String userId = msgIDtoUser.get(msgId);
		String uuid = msgIdtoUUID.get(msgId);
		//下命令用户的管道
		Channel ch = userIdtoChannel.get(userId);
		if(ch!=null) {
			ByteBuf respMsg = PackageMsg.packingClient(sysId, uuid, Protocol.CREATE_PLAN, resp);
			ch.writeAndFlush(respMsg);
			if(BytesHelper.getUnsignedInt(data[0])!=0) {
				logger.debug("箱子给客户端创建灌溉计划命令的返回消息，命令执行失败，错误码为--{}",Integer.toHexString(BytesHelper.getUnsignedInt(data[0])));
				taskPlanService.deleteTask(sysId,data[1]);
			}else {
				logger.debug("箱子给客户端创建灌溉计划命令的返回消息，命令执行成功，返回码为--{}",Integer.toHexString(BytesHelper.getUnsignedInt(data[0])));
			}
		}
		// 清除该消息的session
		msgIDtoUser.remove(msgId);
		msgIdtoUUID.remove(msgId);
		return null;
	}

}
