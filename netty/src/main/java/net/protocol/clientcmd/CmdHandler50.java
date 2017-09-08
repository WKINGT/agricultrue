package net.protocol.clientcmd;

import exception.AgriException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import msg.PackageMsg;
import net.protocol.ClientCmdHandler;
import net.protocol.Protocol;
/**
 * 心跳
 * @author TianW
 *
 */
public class CmdHandler50 extends ClientCmdHandler {

	@Override
	public Object exec(String userId, String uuid, String msg, String sysId, Channel channel) throws AgriException {
		
		ByteBuf respMsg = PackageMsg.packingClient(sysId, uuid, Protocol.HEART_BEAT, "{}");
		
		return respMsg;
	}

}
