package net.xgs.commons.session;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

public class RedisSessionManager implements SessionManager {
    private final String SESSION_PREFIX = "session:";

    private final Cache cache;

    public RedisSessionManager() {
        this.cache = Redis.use();
    }

    public RedisSessionManager(String cacheName) {
        this.cache = Redis.use(cacheName);
    }

    public RedisSessionManager(Cache cache) {
        this.cache = cache;
    }
    
    @Override
    public Session get(String sessionId) {
        return cache.get(SESSION_PREFIX.concat(sessionId));
    }

    @Override
    public void save(Session session) {
        String sessionId = session.getId();
        int seconds = 100000;
        cache.setex(SESSION_PREFIX.concat(sessionId), seconds, session);
    }

    @Override
    public void update(Session session) {
        this.save(session);
    }

}