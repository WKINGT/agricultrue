package net.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

import channel.MapDeviceChannel;
import channel.MsgMapping;
import exception.AgriException;
import io.netty.channel.Channel;
import session.MapSysIdUserId;


public abstract class ClientCmdHandler {

	protected short cmdNO = -32768; //short min=-32768;short max=32767
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected MapDeviceChannel mc = MapDeviceChannel.instance();

	protected MapSysIdUserId session = MapSysIdUserId.getInstance();
	
	protected MsgMapping msgMapping = MsgMapping.instance();
	
	protected Prop prop = PropKit.use("errcode.txt");
	
	public abstract Object exec(String client,String userId, String uuid,String msg,String sysId,Channel channel) throws AgriException;
	
	public Object execCmd(String client, String userId, String uuid,String msg,String sysId,Channel channel) throws AgriException{
		return this.exec(client, userId, uuid, msg, sysId, channel);
	}
}
