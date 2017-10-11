package channel;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.kit.PropKit;

import io.netty.channel.Channel;
import net.xgs.services.AlarmMsgService;
import session.MapSysIdUserId;
import session.Terminal;

public class OfflineHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private MapDeviceChannel mc = MapDeviceChannel.instance();
	
	private MapSysIdUserId session = MapSysIdUserId.getInstance();
	
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
		//控制箱管道与系统id映射map
		Map<Channel, String> channelToSystemId = mc.getChannelToDevice();

		//用户管道与userId映射map
		Map<Channel, String> mapChannelToUserId = session.getMapChannelToUserId();
		//掉线的为控制器
		if(channelToSystemId.containsKey(channel)) {
			AlarmMsgService service = new AlarmMsgService();
			//获得控制箱的系统id
			String systemId = channelToSystemId.get(channel);
			//在map中异常掉线的控制箱管道映射
			channelToSystemId.remove(channel);
			//控制箱系统Id与控制箱管道的映射map
			Map<String,Channel> deviceChannels = mc.getDeviceToChannel();
			deviceChannels.remove(systemId);
			logger.debug("控制器设备Id为{}掉线,地址---{}" ,systemId,channel.remoteAddress());
			
			String code = PropKit.use("deviceError.txt").get("dev.offline");
			//上报控制箱异常
			service.save(systemId, "00-80-00", code);
			
		}
		//掉线的为用户
		if(mapChannelToUserId.containsKey(channel)) {
			//根据channel在管道-userid映射map找到掉线用户的id
			String userId = session.getUserId(channel);
//			String userId = session.getMapChannelToUserId().get(channel);
			mapChannelToUserId.remove(channel);
			String client = session.getClient(channel);
//			String client = session.getMapChannelToClient().get(channel);
			//FIXME 手机用户channel 与 systemId 映射
			for(String sid : session.getSessionMap().keySet()){
				if(session.getSessionMap().get(sid).containsKey(userId)){
					Terminal ter = session.getSessionMap().get(sid).getTerminal(userId);
					ter.remove(client);
					if(ter.size()==0){
						session.getSessionMap().get(sid).remove(userId);
					}
				}
			}
			session.getMapChannelToUserId().remove(channel);
			session.getMapChannelToClient().remove(channel);
			logger.debug("用户Id为{}掉线,地址---{}" ,userId,channel.remoteAddress());
		}
	}
}
