package net.protocol.devicecmd;

import java.util.Map;

import io.netty.channel.Channel;
import net.protocol.DeviceCmdHandler;
import net.util.BytesHelper;
/**
 * 保活命令
 * @author TianW
 *
 */
public class CmdHandler31 extends DeviceCmdHandler {

	@Override
	public Object exec(byte[] sysIdbyte, byte[] cmdNo, byte cmd, byte[] data, Channel channel) {
		logger.debug("收到来自{}的保活报文。" ,channel.remoteAddress());
		//如果一定时间内收不到保活，上报故障
		
		String sysId = BytesHelper.getSystemId(sysIdbyte);
		
		Map<String, Channel> deviceChannels = mc.getDeviceToChannel();
		
		if(!deviceChannels.containsKey(sysId)) {
			logger.info("add deviceChannel map!!!");
			Map<Channel,String> channelToSystemId = mc.getChannelToDevice();
			deviceChannels.put(sysId, channel);
			channelToSystemId.put(channel, sysId);
		}
		
		return null;
	}
}
