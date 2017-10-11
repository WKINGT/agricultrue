package net.protocol.devicecmd;

import java.util.Map;

import com.jfinal.aop.Enhancer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import msg.PackageMsg;
import net.protocol.DeviceCmdHandler;
import net.protocol.Protocol;
import net.protocol.entity.DeletePlanResp;
import net.util.BytesHelper;
import net.xgs.commons.annotation.Inject;
import net.xgs.services.TaskPlanService;

public class CmdHandler14 extends DeviceCmdHandler {
	@Inject
	private TaskPlanService taskPlanService;
	
	@Override
	public Object exec(byte[] sysIdbyte, byte[] cmdNo, byte cmd, byte[] data, Channel channel) {
		logger.debug("{},箱子给客户端发删除灌溉计划命令的返回消息{}",channel.remoteAddress(), BytesHelper.byte2hex(data));
		
		DeletePlanResp resp = new DeletePlanResp();
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
//
//			if(BytesHelper.getUnsignedInt(data[0])==0) {
//				ByteBuf respMsg = PackageMsg.packingClient(sysId, uuid, Protocol.DELETE_PLAN, resp);
//				ch.writeAndFlush(respMsg);
//				logger.debug("箱子给客户端发送的删除灌溉计划状态命令的返回，执行成功");
//				taskPlanService.deleteTask(sysId,data[1]);//箱子灌溉计划删除成功，然后删除数据库计划
//			}
//			if(BytesHelper.getUnsignedInt(data[0])==0x85) {
//				resp.setCode(0);
//				ByteBuf respMsg1 = PackageMsg.packingClient(sysId, uuid, Protocol.DELETE_PLAN, resp);
//				ch.writeAndFlush(respMsg1);
//				logger.debug("箱子给客户端发送的删除灌溉计划状态命令的返回，执行成功,箱子没有这个计划，直接删除数据库");
//				taskPlanService.deleteTask(sysId,data[1]);//箱子灌溉计划删除成功，然后删除数据库计划
//			}
//		}
		// 清除该消息的session
		msgIDtoUser.remove(msgId);
		msgIdtoUUID.remove(msgId);
		
		
		return null;
	}

}
