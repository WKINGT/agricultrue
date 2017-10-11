package websocket;


import com.alibaba.fastjson.JSON;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import exception.AgriException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import msg.PackageMsg;
import msg.ReqMsg;
import net.protocol.ClientCmdHandler;
import net.protocol.ClientCmdHandlerPool;
import net.protocol.Protocol;
import net.protocol.clientcmd.CmdHandler40;
import net.util.UUIDHexGenerator;
import net.xgs.commons.plugin.ioc.InjectUtils;
import net.xgs.utils.RSAEncrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WebMsgHandler {
//	private Logger logger = LoggerFactory.getLogger(getClass());

	public void channelRead0(ChannelHandlerContext ctx,
								String req) throws Exception {

		Channel incoming = ctx.channel();
		ReqMsg msgEntity = null;
		String userId = null;
		String uuid = null;
		String msg = null;
		String cmd = null;
		String sysId = null;
		try {
			msgEntity = JSON.parseObject(req,ReqMsg.class);
			userId = msgEntity.getUserId();
			uuid = msgEntity.getUuid();
			cmd = msgEntity.getCmd();
			msg = msgEntity.getMsg();
			sysId = msgEntity.getSysId();
			ClientCmdHandler rech = ClientCmdHandlerPool.getHandler(String.valueOf(cmd));
			Object obj = rech.execCmd(Protocol.WEB, userId, uuid, msg, sysId, incoming);
			if(obj != null){
				incoming.write(obj);
			}
			incoming.flush();
		} catch (Exception e) {
			Map<String,Object> errorMsg = new HashMap<String,Object>();
			if(e instanceof AgriException){
				errorMsg.put("code", ((AgriException)e).getCode());
				errorMsg.put("msg",  ((AgriException)e).getMessage());
				TextWebSocketFrame rMsg = PackageMsg.packingWeb(sysId, UUIDHexGenerator.getId(), Protocol.ERROR, errorMsg);
				incoming.write(rMsg);
				incoming.flush();
			}else{
				errorMsg.put("code", Protocol.ERROR);
				errorMsg.put("msg",  e.getMessage());
				TextWebSocketFrame rMsg = PackageMsg.packingWeb(sysId, UUIDHexGenerator.getId(), Protocol.ERROR, errorMsg);
				incoming.write(rMsg);
				incoming.flush();
			}
			incoming.flush();
		}


	}
}