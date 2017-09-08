package net.xgs.session;


import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Constructor;

/**
 * Created by duai on 2017-07-27.
 */
public class RestfulSessionWrapper {
    private static RestfulSessionManager sessionManager;
    public static void initCfg(String className,String cacheName) {
        try {
            Class clazz = Class.forName(className);
            if (StringUtils.isNotBlank(cacheName)){
                Constructor c1=clazz.getDeclaredConstructor(new Class[]{String.class});
                sessionManager = (RestfulSessionManager)c1.newInstance(new Object[]{cacheName});
            }else {
                sessionManager = (RestfulSessionManager) clazz.newInstance();
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static RestfulSessionManager getRestfulSessionManager(){
        return sessionManager;
    }
}
