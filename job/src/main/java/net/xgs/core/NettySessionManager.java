package net.xgs.core;

import net.xgs.entity.SessionEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by duai on 2017-08-18.
 */
public class NettySessionManager {
    private static Map<String,SessionEntity> session = new ConcurrentHashMap<>();

    public static Map<String, SessionEntity> getSession() {
        return session;
    }

    public static void putSession(String key,SessionEntity value){
        session.put(key,value);
    }
    public static SessionEntity getSession(String key){
        return session.get(key);
    }
}
