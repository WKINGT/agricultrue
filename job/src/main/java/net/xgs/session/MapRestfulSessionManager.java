package net.xgs.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by duai on 2017-07-27.
 */
public class MapRestfulSessionManager implements RestfulSessionManager {

    private Map<String,RestfulSession> cache = new ConcurrentHashMap<>();

    @Override
    public RestfulSession get(String sessionId) {
        return cache.get(sessionId);
    }

    @Override
    public void save(RestfulSession session) {
        String sessionId = session.getId();
        cache.put(sessionId,session);
    }

    @Override
    public void update(RestfulSession session) {
        this.save(session);
    }

    @Override
    public void remove(String sessionId) {
        cache.remove(sessionId);
    }
}
