package channel;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.kit.PropKit;

import io.netty.channel.Channel;
import net.xgs.services.AlarmMsgService;

public class OfflineHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private MapDeviceChannel mc = MapDeviceChannel.instance();
	
	private Session mSession = Session.instance();
	
	private static OfflineHandler instance = new OfflineHandler();
	
	private OfflineHandler() {}
	
	public static OfflineHandler getInstance() {
		return instance;
	}
	/**
	 * 掉线后，如果是用户，删除session
	 * 如果是设备，删除session,上报异常
	 * @param channel
	 */
	public void offlineDestory(Channel channel) {
		
		Map<Channel, String> channelToSystemId = mc.getChannelToDevice();
		
		Map<Channel, String> channelToUserId = mSession.getChannelTOUserId();
		//掉线的为控制器
		if(channelToSystemId.containsKey(channel)) {
			
			AlarmMsgService service = new AlarmMsgService();
			String systemId = channelToSystemId.get(channel);
			channelToSystemId.remove(channel);
			Map<String,Channel> deviceChannels = mc.getDeviceToChannel();
			deviceChannels.remove(systemId);
			logger.debug("控制器设备Id为{}掉线,地址---{}" ,systemId,channel.remoteAddress());
			
			String code = PropKit.use("deviceError.txt").get("dev.offline");
			service.save(systemId, "00-80-00", code);
			
		}
		//掉线的为用户
		if(channelToUserId.containsKey(channel)) {
			String userId = channelToUserId.get(channel);
			channelToUserId.remove(channel);
			Map<String,Channel>  userIdChannels = mSession.getUserIdTOChannel();
			userIdChannels.remove(userId);
			logger.debug("用户Id为{}掉线,地址---{}" ,userId,channel.remoteAddress());
		}
		
	}
}
