package net.protocol;

import channel.MapDeviceChannel;
import channel.MsgMapping;
import com.jfinal.aop.Enhancer;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import exception.AgriException;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class WebCmdHandler {
    private static Map<String,WebCmdHandler> handlerPool = new ConcurrentHashMap<>();

    protected short cmdNO = -32768; //short min=-32768;short max=32767

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected MapDeviceChannel mc = MapDeviceChannel.instance();

    protected MsgMapping msgMapping = MsgMapping.instance();

    protected Prop prop = PropKit.use("errcode.txt");

    public abstract Object exec(String userId, String uuid,String msg,String sysId,Channel channel) throws AgriException;

    public Object execCmd(String userId, String uuid,String msg,String sysId,Channel channel) throws AgriException{
        return this.exec(userId, uuid, msg, sysId, channel);
    }

    public static WebCmdHandler getHandler(String className) throws Exception {
        String classNameFull = "net.protocol.webcmd.CmdHandler"+className;
        WebCmdHandler handler = handlerPool.get(classNameFull);
        if (handler == null){
            Class<?> clazz =Class.forName(classNameFull);
            handler = Enhancer.enhance(clazz.newInstance());
            handlerPool.put(classNameFull,handler);
        }
        return handler;
    }
}
