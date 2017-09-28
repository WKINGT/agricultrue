package net.protocol.clientcmd;

import java.util.List;

import com.alibaba.fastjson.JSON;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import exception.AgriException;
import io.netty.channel.Channel;
import msg.PackageMsg;
import net.protocol.ClientCmdHandler;
import net.protocol.Protocol;
import net.protocol.entity.LoginReq;
import net.protocol.entity.LoginResp;
import net.util.BytesHelper;
import net.xgs.commons.annotation.Inject;
import net.xgs.entity.edomain.StatusEnum;
import net.xgs.model.BaseMember;
import net.xgs.services.MemberService;
import net.xgs.utils.RSAEncrypt;

/**
 * 登入
 * @author TianW
 *S
 */
public class CmdHandler40 extends ClientCmdHandler {

	@Inject
	MemberService memberService;
	@Override
	public Object exec(String client, String userId, String uuid, String msg, String sysId, Channel channel) throws AgriException {
		String recMsg = msg;
		if(Protocol.WEB.equals(client)){
			Prop prop = PropKit.use("rsa_websocket.properties");
			RSAEncrypt rsaEncrypt= new RSAEncrypt(prop.get("private.key"),prop.get("public.key"));
			try {
//				BytesHelper.hexStringToBytes(msg);
//				byte[] msg_b = msg.getBytes("utf-8");
				byte[] msg_b = BytesHelper.hexStringToBytes(msg);
				byte[] plainText = rsaEncrypt.decrypt(rsaEncrypt.getPrivateKey(), msg_b);
//				System.out.println(new String(plainText,"utf-8"));
				recMsg = new String(plainText,"utf-8");
			} catch (Exception e) {
				logger.debug(e.getMessage());
			}

		}

		LoginReq entity = null;
		LoginResp resp = new LoginResp();
		String loginAccount = null;
		String loginPwd = null;
		List<String> sysIds = null;
		try {
			entity = JSON.parseObject(recMsg, LoginReq.class);
			loginAccount = entity.getLoginAccount();
			loginPwd = entity.getLoginPwd();
			BaseMember su = memberService.loginAndroid(loginAccount, loginPwd);
			if (su == null || StatusEnum.PROHIBITED_USE.getValue().equals(su.getEnabled())) {
				int code = prop.getInt("error.loginUser");
				throw new AgriException(code,prop.get(code+""));
			}
			sysIds = su.get("mains");
			
		} catch (Exception e) {
			if(e instanceof AgriException) {
				throw e;
			}
			e.printStackTrace();
			int code = prop.getInt("error.format");
			throw new AgriException(code,prop.get(code+"",e.getMessage()));
		}
		
		//登录成功，将用户的id和管道加入到session中

		for (String id:sysIds ) {
			session.put(id,userId,client,channel);
//			session.putUserChannelSysIdMap(channel,id);
		}
		session.getMapChannelToClient().put(channel,client);
		session.getMapChannelToUserId().put(channel,userId);

		resp.setCode(0);
		Object respMsg = null;
		if(Protocol.PHONE.equals(client)){
			respMsg = PackageMsg.packingClient(sysId, uuid, Protocol.LOGININ, resp);
		}else if(Protocol.WEB.equals(client)){
			respMsg = PackageMsg.packingWeb(sysId,uuid,Protocol.LOGININ,resp);
		}

		return respMsg;
	}

}
