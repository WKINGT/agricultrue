package net.protocol.devicecmd;

import java.util.Map;

import com.jfinal.aop.Enhancer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import msg.PackageMsg;
import net.protocol.DeviceCmdHandler;
import net.protocol.Protocol;
import net.protocol.entity.EndPlanResp;
import net.util.BytesHelper;
import net.xgs.commons.annotation.Inject;
import net.xgs.services.TaskPlanService;

public class CmdHandler13 extends DeviceCmdHandler {
	@Inject
	private TaskPlanService taskPlanService;
	@Override
	public Object exec(byte[] sysIdbyte, byte[] cmdNo, byte cmd, byte[] data, Channel channel) {
		logger.debug("{},箱子给客户端发停止灌溉计划命令的返回消息{}" ,channel.remoteAddress(), BytesHelper.byte2hex(data));
		
		EndPlanResp resp = new EndPlanResp();
		resp.setCode(BytesHelper.getUnsignedInt(data[0]));
		resp.setPlanNO(data[1]);
		
		String sysId = BytesHelper.getSystemId(sysIdbyte);
		short cmdNoshort = BytesHelper.toShort(cmdNo);
		String cmdNO = Integer.toString(cmdNoshort);
		String msgId = sysId + cmdNO;
		
		Map<String, String> msgIDtoUser = msgMapping.getMsgIdtoUser();
		Map<String, String> msgIdtoUUID = msgMapping.getMsgIdtoUUID();
//		Map<String, Channel> userIdtoChannel = mSession.getUserIdTOChannel();
		String userId = msgIDtoUser.get(msgId);
		String uuid = msgIdtoUUID.get(msgId);
		//下命令用户的管道
//		Channel ch = userIdtoChannel.get(userId);
//		if(ch!=null) {
//			ByteBuf respMsg = PackageMsg.packingClient(sysId, uuid, Protocol.END_PLAN, resp);
//			ch.writeAndFlush(respMsg);
//			if(BytesHelper.getUnsignedInt(data[0])==0) {
//				logger.debug("箱子给客户端发送的停止灌溉计划状态命令的返回，执行成功");
//				taskPlanService.stopTask(sysId,data[1]);
//			}
//		}
		// 清除该消息的session
		msgIDtoUser.remove(msgId);
		msgIdtoUUID.remove(msgId);
		
		return null;
	}
}
