package net.protocol.clientcmd;

import java.util.Map;

import exception.AgriException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import msg.PackageMsg;
import net.protocol.ClientCmdHandler;
import net.protocol.Protocol;
import net.protocol.entity.LogoutResp;
/**
 * 登出
 * @author TianW
 *
 */
public class CmdHandler41 extends ClientCmdHandler{

	@Override
	public Object exec(String userId, String uuid, String msg, String sysId, Channel channel) throws AgriException {
		
		LogoutResp resp = new LogoutResp();
		Map<String, Channel> userIdtoChannel = mSession.getUserIdTOChannel();
		Map<Channel, String> channelToUserId = mSession.getChannelTOUserId();
		Channel ch = userIdtoChannel.get(userId);
		userIdtoChannel.remove(uuid);
		channelToUserId.remove(ch);
		resp.setCode(0);
		ByteBuf respMsg = PackageMsg.packingClient(sysId, uuid, Protocol.LOGINOUT, resp);
		return respMsg;
	}

}
