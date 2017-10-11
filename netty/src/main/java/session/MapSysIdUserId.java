package session;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class MapSysIdUserId {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static MapSysIdUserId session = new MapSysIdUserId();

    private static Map<String,UserMap> sessionMap = new ConcurrentHashMap<String,UserMap>();
    private static Map<Channel,String> mapChannelToUserId = new ConcurrentHashMap<Channel,String>();
    private static Map<Channel,String> mapChannelToClient = new ConcurrentHashMap<Channel,String>();

    public Map<String,UserMap> getSessionMap (){
        return sessionMap;
    }

    public Map<Channel,String> getMapChannelToUserId(){
        return mapChannelToUserId;
    }
    public Map<Channel,String> getMapChannelToClient(){
        return mapChannelToClient;
    }
    public String getUserId(Channel channel){
        return mapChannelToUserId.get(channel);
    }
    public String getClient(Channel channel){
        return this.mapChannelToClient.get(channel);
    }

    public void put(String sysId,String userId,String client,Channel channel){
        UserMap userMap = sessionMap.get(sysId);
        if(userMap==null){
            userMap = new UserMap();
        }
        Terminal terminal = userMap.get(userId);
        if(terminal==null){
            terminal = new Terminal();
        }
        terminal.put(client,channel);
        userMap.put(userId,terminal);
        sessionMap.put(sysId,userMap);
        mapChannelToUserId.put(channel,userId);
        mapChannelToClient.put(channel,client);
    }

    public void remove(String userId,String client,Channel channel){
        mapChannelToUserId.remove(channel);
        mapChannelToClient.remove(channel);

        //FIXME 手机用户channel 与 systemId 映射
        for(String sid : sessionMap.keySet()){
            if(sessionMap.get(sid).containsKey(userId)){
                Terminal ter = sessionMap.get(sid).getTerminal(userId);
                ter.remove(client);
                if(ter.size()==0){
                    sessionMap.get(sid).remove(userId);
                }
            }
        }
        getMapChannelToUserId().remove(channel);
        getMapChannelToClient().remove(channel);
        logger.debug("用户Id为{}登出,地址---{}" ,userId,channel.remoteAddress());
    }
    private MapSysIdUserId(){}
    public static MapSysIdUserId getInstance(){
        return session;
    }
}
