package net.protocol.devicecmd;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.kit.JsonKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import msg.PackageMsg;
import net.protocol.DeviceCmdHandler;
import net.protocol.Protocol;
import net.protocol.entity.OperateDeviceResp;
import net.util.BytesHelper;
import net.xgs.utils.RSAEncrypt;
/**
 * 操作外接设备
 * @author TianW
 *
 */
public class CmdHandler2D extends DeviceCmdHandler {
	private static Prop prop = PropKit.use("rsa.properties");
	private static RSAEncrypt rsaEncrypt= new RSAEncrypt(prop.get("private.key"),prop.get("public.key"));

	@Override
	public Object exec(byte[] sysIdbyte, byte[] cmdNo, byte cmd, byte[] data, Channel channel) {
		logger.debug("%n=={},箱子给客户端操作外接设备命令返回消息内容16进制：{}" ,channel.remoteAddress(), BytesHelper.byte2hex(data));
		
		OperateDeviceResp resp = new OperateDeviceResp();
		byte terminalId_b = data[0];
		byte type_b = data[1];
		byte number_b = data[2];
		//FIXME
		String objId = BytesHelper.getTwoNumByte(BytesHelper.getUnsignedInt(terminalId_b))+"-"+
					BytesHelper.getTwoNumByte(BytesHelper.getUnsignedInt(type_b))+"-"+
					BytesHelper.getTwoNumByte(BytesHelper.getUnsignedInt(number_b));
		byte operation = data[3];
		byte operationResult = data[4];
		resp.setCode(0);
		resp.setObjId(objId);
		resp.setOperation(operation);
		resp.setOperationResult(operationResult);
		
		
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
		
		if(userId.equals("jobId"+cmdNO)) {
			byte[] returnMsgs;
			Map<String,Object> respJb = new HashMap<String,Object>();
			if(ch!=null&&operation==operationResult) {
				respJb.put("code", 0);
				respJb.put("uuid", uuid);
				try {
					returnMsgs = rsaEncrypt.encrypt(rsaEncrypt.getPublicKey(), JsonKit.toJson(respJb).getBytes());
					ch.writeAndFlush(returnMsgs);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {
				respJb.put("code", 1);
				respJb.put("uuid", uuid);
				try {
					returnMsgs = rsaEncrypt.encrypt(rsaEncrypt.getPublicKey(), JsonKit.toJson(respJb).getBytes());
					ch.writeAndFlush(returnMsgs);
				} catch (Exception e) {
				}
			}
			userIdtoChannel.remove(userId);
			// 清除该消息的session
			msgIDtoUser.remove(msgId);
			msgIdtoUUID.remove(msgId);
			return null;
		}
		
		if(ch!=null) {
			ByteBuf respMsg = PackageMsg.packingClient(sysId, uuid, Protocol.OPERATE_DEVICE, resp);
			ch.writeAndFlush(respMsg);
		}
		// 清除该消息的session
		msgIDtoUser.remove(msgId);
		msgIdtoUUID.remove(msgId);
		
		
		return null;
	}

}
