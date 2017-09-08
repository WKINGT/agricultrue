package net.protocol;

import com.jfinal.aop.Enhancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

import channel.MapDeviceChannel;
import channel.MsgMapping;
import channel.Session;
import exception.AgriException;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ClientCmdHandler {

//	protected Prop prop = PropKit.use("errcode.txt");
//	
//	public final static String emptyStr = "";
	private static Map<String,ClientCmdHandler> handlerPool = new ConcurrentHashMap<>();

	protected short cmdNO = -32768; //short min=-32768;short max=32767
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected MapDeviceChannel mc = MapDeviceChannel.instance();
	
	protected Session mSession = Session.instance();
	
	protected MsgMapping msgMapping = MsgMapping.instance();
	
	protected Prop prop = PropKit.use("errcode.txt");
	
	public abstract Object exec(String userId, String uuid,String msg,String sysId,Channel channel) throws AgriException;
	
	public Object execCmd(String userId, String uuid,String msg,String sysId,Channel channel) throws AgriException{
		return this.exec(userId, uuid, msg, sysId, channel);
	}

	public static ClientCmdHandler getHandler(String className) throws Exception {
		String classNameFull = "net.protocol.clientcmd.CmdHandler"+className;
		ClientCmdHandler handler = handlerPool.get(classNameFull);
		if (handler == null){
			Class<?> clazz =Class.forName(classNameFull);
			handler = Enhancer.enhance(clazz.newInstance());
			handlerPool.put(classNameFull,handler);
		}
		return handler;
	}

}
