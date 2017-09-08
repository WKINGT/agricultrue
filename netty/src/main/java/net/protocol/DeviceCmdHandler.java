package net.protocol;

import com.jfinal.aop.Enhancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import channel.MapDeviceChannel;
import channel.MsgMapping;
import channel.Session;
import exception.AgriException;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class DeviceCmdHandler {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected MapDeviceChannel mc = MapDeviceChannel.instance();

	private static Map<String,DeviceCmdHandler> handlerPool = new ConcurrentHashMap<>();

	protected Session mSession = Session.instance();
	
	protected MsgMapping msgMapping = MsgMapping.instance();
	
	public abstract Object exec(byte[] sysIdbyte, byte[] cmdNo, byte cmd, byte[] data, Channel channel) throws AgriException;
	
	public Object execCmd(byte[] sysIdbyte, byte[] cmdNo, byte cmd, byte[] data, Channel channel) throws AgriException{
		return this.exec(sysIdbyte, cmdNo, cmd ,data, channel);
	}

	public static DeviceCmdHandler getHandler(String className) throws Exception {
		String classNameFull = "net.protocol.devicecmd.CmdHandler"+className;
		DeviceCmdHandler handler = handlerPool.get(classNameFull);
		if (handler == null){
			Class<?> clazz =Class.forName(classNameFull);
			handler = Enhancer.enhance(clazz.newInstance());
			handlerPool.put(classNameFull,handler);
		}
		return handler;
	}

}
