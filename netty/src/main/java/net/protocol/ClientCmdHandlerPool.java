package net.protocol;

import com.jfinal.aop.Enhancer;
import net.xgs.commons.plugin.ioc.InjectUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientCmdHandlerPool {
    private static Map<String,ClientCmdHandler> handlerPool = new ConcurrentHashMap<>();

    public static ClientCmdHandler getHandler(String className) throws Exception {
        String classNameFull = "net.protocol.clientcmd.CmdHandler"+className;
        ClientCmdHandler handler = handlerPool.get(classNameFull);
        if (handler == null){
            Class<?> clazz =Class.forName(classNameFull);
            handler = (ClientCmdHandler)Enhancer.enhance(clazz);//(ClientCmdHandler)clazz.newInstance();
            InjectUtils.inject(handler.getClass().getSuperclass(),handler);
            handlerPool.put(classNameFull,handler);
        }
        return handler;
    }
}
