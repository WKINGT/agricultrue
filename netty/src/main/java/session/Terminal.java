package session;


import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端和管道映射
 * argument1:客户端标识{0表示手持终端，1表示web端}
 * argument2:表示客户端所用的管道
 * @author TianW
 *
 */
public class Terminal extends ConcurrentHashMap<String,Channel>{
    /**
     * 根据客户端标识获取它所用的管道
     * @param client
     * @return
     */
    public Channel getChannel(String client){
        return this.get(client);
    }
}
