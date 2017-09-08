package net.xgs.commons.session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.xgs.commons.utils.StrUtils;
import net.xgs.commons.utils.WebUtils;

import com.jfinal.kit.StrKit;

public class SessionRepositoryRequestWrapper extends HttpServletRequestWrapper {
    private static SessionManager sessionManager;
    private static String sessionCookieDomain;
    private static String sessionCookieName = "PHPSESSION";
    private static int maxAgeInSeconds;
    
    public static void initCfg(String sessionCookieDomain,int maxAgeInSeconds,String sessionId,String cacheName) {
    	SessionRepositoryRequestWrapper.sessionCookieDomain = sessionCookieDomain;
    	SessionRepositoryRequestWrapper.maxAgeInSeconds = maxAgeInSeconds * 60;
    	sessionCookieName = sessionId;
        sessionManager = new RedisSessionManager(cacheName);
    }
    
    private final HttpServletResponse response;
    
    public SessionRepositoryRequestWrapper(HttpServletRequest request, HttpServletResponse response) {
        super(request);
        this.response = response;
    }
    
    private HttpServletRequest getHttpRequest() {
        return (HttpServletRequest) super.getRequest();
    }
    
    private String getSessionId() {
        HttpServletRequest request = getHttpRequest();
        return WebUtils.getCookie(request, sessionCookieName);
    }
    
    /**
     * 此处用cookie给重写掉
     */
    @Override
    public String getRequestedSessionId() {
        return getSessionId();
    }

    @Override
    public HttpSession getSession() {
        String sessionId = getRequestedSessionId();
        // 默认getSession(true)
        if (null == sessionId) {
            sessionId = StrUtils.getRandomUUID();
            WebUtils.setCookie(response, sessionCookieName, sessionId, sessionCookieDomain, maxAgeInSeconds);
        }
        Session session = sessionManager.get(sessionId);
        if (null == session) {
            session = new Session(sessionId);
            session.setMaxInactiveInterval(maxAgeInSeconds);
            sessionManager.save(session);
        }else{
            session.setMaxInactiveInterval(maxAgeInSeconds);
        	sessionManager.update(session);
        }
        // 由于sessionManager不参与序列化，加上序列化的问题，故手动设置
        session.setManager(sessionManager);
        return session;
    }

    @Override
    public HttpSession getSession(boolean create) {
        if (create) {
            return this.getSession();
        }
        String sessionId = getRequestedSessionId();
        if (null == sessionId) {
            return null;
        }
        Session session = sessionManager.get(sessionId);
        session.setMaxInactiveInterval(maxAgeInSeconds);
        sessionManager.update(session);
        if (null != session) {
            // 由于sessionManager不参与序列化，加上序列化的问题，故手动设置
            session.setManager(sessionManager);
        }
        return session;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        String sessionId = getRequestedSessionId();
        return StrKit.notBlank(sessionId);
    }

}