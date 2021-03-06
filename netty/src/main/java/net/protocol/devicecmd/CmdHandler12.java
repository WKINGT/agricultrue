package net.protocol.devicecmd;

import java.util.Map;

import com.jfinal.aop.Enhancer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import msg.PackageMsg;
import net.protocol.DeviceCmdHandler;
import net.protocol.Protocol;
import net.protocol.entity.StartPlanResp;
import net.util.BytesHelper;
import net.xgs.services.TaskPlanService;

public class CmdHandler12 extends DeviceCmdHandler {
	private TaskPlanService taskPlanService = Enhancer.enhance(TaskPlanService.class);
	@Override
	public Object exec(byte[] sysIdbyte, byte[] cmdNo, byte cmd, byte[] data, Channel channel) {
		logger.debug("{}客户端给箱子下发启动灌溉计划命令,data内容为：{}",channel.remoteAddress(), BytesHelper.byte2hex(data));
		
		StartPlanResp resp = new StartPlanResp();
		resp.setCode(BytesHelper.getUnsignedInt(data[0]));
		resp.setPlanNO(data[1]);

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
			ByteBuf respMsg = PackageMsg.packingClient(sysId, uuid, Protocol.START_PLAN, resp);
			ch.writeAndFlush(respMsg);
			if(BytesHelper.getUnsignedInt(data[0])==0) {
				logger.debug("箱子给客户端发启动灌溉计划状态命令的返回消息，返回码为--{}，灌溉计划编码为--{}",Integer.toHexString(BytesHelper.getUnsignedInt(data[0])),Integer.toHexString(BytesHelper.getUnsignedInt(data[1])));
				taskPlanService.startTask(sysId,data[1]);
			}
		}
		// 清除该消息的session
		msgIDtoUser.remove(msgId);
		msgIdtoUUID.remove(msgId);
		
		return null;
	}

}
