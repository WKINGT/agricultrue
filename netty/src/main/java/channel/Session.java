package channel;


import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
/**
 * 用户userId和管道映射
 * @author TianW
 *
 */
public class Session {

	private Session(){}
	
	private Map<String,Channel>  userIdChannels = new ConcurrentHashMap<String, Channel>();
	private Map<Channel,String>  channelToUserId = new ConcurrentHashMap<Channel, String>();
	
	private static Session msession = new Session();
	
	public static Session instance(){
		return msession;
	}
	
	public Map<String, Channel> getUserIdTOChannel() {
		return this.userIdChannels;
	}
	
	public Map<Channel, String> getChannelTOUserId() {
		return this.channelToUserId;
	}
	
	public void destroy(Channel channel) {
		for(Entry<String, Channel> entity : userIdChannels.entrySet()) {
			if(entity.getValue().equals(channel)) {
				userIdChannels.remove(entity.getKey());
			}
		}
	}
	
	
}
