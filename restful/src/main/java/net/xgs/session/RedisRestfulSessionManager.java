package net.xgs.session;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

public class RedisRestfulSessionManager implements RestfulSessionManager{
    private final String SESSION_PREFIX = "restfulsession:";

    private final Cache cache;


    public RedisRestfulSessionManager() {
        this.cache = Redis.use();
    }

    public RedisRestfulSessionManager(String cacheName) {
        this.cache = Redis.use(cacheName);
    }

    public RedisRestfulSessionManager(Cache cache) {
        this.cache = cache;
    }
    
    @Override
    public RestfulSession get(String sessionId) {
        return cache.get(SESSION_PREFIX.concat(sessionId));
    }

    @Override
    public void save(RestfulSession session) {
        String sessionId = session.getId();
        int seconds = 100000;
        cache.setex(SESSION_PREFIX.concat(sessionId), seconds, session);
    }

    @Override
    public void update(RestfulSession session) {
        this.save(session);
    }

    @Override
    public void remove(String sessionId) {
        cache.del(SESSION_PREFIX.concat(sessionId));
    }

}