package socket;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.protocol.ClientCmdHandlerPool;
import net.protocol.clientcmd.CmdHandler40;
import net.xgs.commons.annotation.Inject;
import net.xgs.commons.plugin.ioc.InjectUtils;
import net.xgs.services.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.jfinal.kit.PropKit;

import exception.AgriException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import msg.HeaderBody;
import msg.PackageMsg;
import net.protocol.ClientCmdHandler;
import net.protocol.Protocol;
import net.util.BytesHelper;
import net.util.UUIDHexGenerator;
import net.util.Utility;

public class ClientMsgHandler {
	private static final int started = Protocol.CLIENT_BEGIN_BYTE;
	private Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * 该方法是记录一个socket的连接已断开，
	 */
	public void handlerRemoved(boolean isWeb, ChannelHandlerContext ctx)
			throws Exception {
		logger.debug("离线=============="+ctx.channel().remoteAddress());
		Channel channel = ctx.channel();
		//将给用户的session管道里相关删除
//		MapChannel.instance().destroy(channel);
		channel.close();
	}


	public boolean channelRead0(ChannelHandlerContext ctx,
			byte[] req) throws Exception {
		
		Channel incoming = ctx.channel();
		String sysId= "0-0-0-0-0-0";
		try {
			if(BytesHelper.getUnsignedInt(req[0]) != started) {
				logger.error("不是客户端通信协议格式");
				return false;
			}
			
			byte[] len_b = new byte[4];
			System.arraycopy(req, 1, len_b, 0, 4);
			int len = Utility.byte2Int(len_b);
			
			byte[] src=new byte[req.length];
			System.arraycopy(req, 5, src, 0, len);

			int i = 0;
			byte[] version_b = new byte[3];
			System.arraycopy(src, i, version_b, 0, 3);
			String version = new String(version_b);
			i += 3;
			
			byte[] userId_b = new byte[32];
			System.arraycopy(src, i, userId_b, 0, 32);
			i += 32;
			String userId = new String(userId_b);
			
			byte[] sysId_b = new byte[6];
			System.arraycopy(src, i, sysId_b, 0, 6);
			i += 6;
//			sysId = new String(sysId_b);
			sysId = BytesHelper.getSystemId(sysId_b);
			
			byte[] uuid_b = new byte[32];
			System.arraycopy(src, i, uuid_b, 0, 32);
			String uuid = new String(uuid_b);
			i += 32;
			
			byte[] cmd_b = new byte[2];
			System.arraycopy(src, i, cmd_b, 0, 2);
			short cmd = Utility.byte2Short(cmd_b);
			i += 2;
			byte[] msg_b = new byte[src.length-(i+1)];
			System.arraycopy(src, i, msg_b, 0, src.length-(i+1));
			String  msg = new String(msg_b);
			logger.debug("智慧农业通信协议报文格式");
			if(PropKit.use("cnf.txt").getBoolean("isprint", false)){
				HeaderBody entity = new HeaderBody(len, version, userId, sysId, uuid, cmd, msg);
				logger.debug("%n=={},报文内容：{}" ,ctx.channel().remoteAddress(), JSON.toJSONString(entity));
			}
			
			
//			Object objMsg;
			try {
				if(PropKit.use("cnf.txt").getBoolean("isprint", false)){
					logger.debug("%n=={},报体：{}" ,ctx.channel().remoteAddress(), msg);
				}
//				objMsg = JSON.parseObject(msg);
			} catch (Exception e) {
				throw new AgriException(PropKit.use("errcode.txt").getInt("error.format"), "消息格式错误");
			}
			ClientCmdHandler rech = ClientCmdHandlerPool.getHandler(String.valueOf(cmd));
//			System.out.println(rech.hashCode());
			Object obj = rech.execCmd(Protocol.PHONE, userId, uuid, msg, sysId, incoming);
			//Object obj = rech.execCmd(Protocol.WEB, userId, uuid, msg, sysId, incoming);
			if(obj != null){
				incoming.write(obj);
			}
			incoming.flush();
		}catch (Exception e) {
			e.printStackTrace();
			//FIXME 有待修改出 错时的信息
			Map<String,Object> errorMsg = new HashMap<String,Object>();
			if(e instanceof AgriException){
				errorMsg.put("code", ((AgriException)e).getCode());
				errorMsg.put("msg",  ((AgriException)e).getMessage());
				ByteBuf rMsg = PackageMsg.packingClient(sysId, UUIDHexGenerator.getId(), Protocol.ERROR, errorMsg);
				incoming.write(rMsg);
				incoming.flush();
			}else{
				errorMsg.put("code", Protocol.ERROR);
				errorMsg.put("msg",  e.getMessage());
				ByteBuf rMsg = PackageMsg.packingClient(sysId, UUIDHexGenerator.getId(), Protocol.ERROR, errorMsg);
				incoming.write(rMsg);
				incoming.flush();
			}
		}
		incoming.flush();
		return true;
	}

	/**
	 * 该用户在线
	 * @param ctx
	 * @throws Exception
	 */
	public void channelActive(ChannelHandlerContext ctx)
			throws Exception {
		Channel incoming = ctx.channel();
		System.out.println("other" + incoming.remoteAddress()
				+ "在线\n在线时间为："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}

}
