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
	public Object exec(String client, String userId, String uuid, String msg, String sysId, Channel channel) throws AgriException {
		Object respMsg = null;
		if(Protocol.PHONE.equals(client)){
			respMsg = PackageMsg.packingClient(sysId, uuid, Protocol.LOGININ, "{}");
		}else if(Protocol.WEB.equals(client)){
			respMsg = PackageMsg.packingWeb(sysId,uuid,Protocol.LOGININ,"{}");
		}

		return respMsg;
	}

}
