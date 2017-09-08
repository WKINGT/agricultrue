package channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
/**
 * 设备systemId和管道的映射
 * @author TianW
 *
 */
public final class MapDeviceChannel {

	private Map<String,Channel> deviceChannels = new ConcurrentHashMap<String,Channel>();
	private Map<Channel,String>  channelToSystemId = new ConcurrentHashMap<Channel, String>();
	
	private MapDeviceChannel(){}
	
	private static MapDeviceChannel mc = new MapDeviceChannel();
	
	public static MapDeviceChannel instance(){
		return mc;
	}
	
	public Map<String,Channel> getDeviceToChannel(){
		return this.deviceChannels;
	}
	public Map<Channel,String> getChannelToDevice(){
		return this.channelToSystemId;
	}
	public void put(String id, Channel channel) {
		deviceChannels.put(id, channel);
	}

	public Channel get(String id) {
		return deviceChannels.get(id);
	}
	
}