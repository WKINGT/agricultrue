package net.xgs.session;

import com.jfinal.core.JFinal;

import javax.servlet.ServletContext;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Session
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class RestfulSession implements  Serializable {
    private static final long serialVersionUID = -719739616470614546L;
    
    protected String id = null;
    protected final Map<String, Object> attributes = new ConcurrentHashMap();
    protected final long creationTime = System.currentTimeMillis();

    public RestfulSession(String id) {
        this.id = id;
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public Enumeration getAttributeNames() {
        Set<String> names = new HashSet();
        names.addAll(this.attributes.keySet());
        return Collections.enumeration(names);
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public String getId() {
        return this.id;
    }

    public long getLastAccessedTime() {
        return this.creationTime;
    }


    public ServletContext getServletContext() {
        return JFinal.me().getServletContext();
    }


    public synchronized void removeAttribute(String key) {
        attributes.remove(key);
        getManager().update(this);
    }


    public synchronized void setAttribute(String key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("setAttribute: name parameter cannot be null");
        }
        if (value == null) {
            removeAttribute(key);
            return;
        }
        attributes.put(key, value);
        getManager().update(this);
    }
    public RestfulSessionManager getManager(){
      return  RestfulSessionWrapper.getRestfulSessionManager();
    }
}