package net.protocol.clientcmd;

import java.util.Map;

import com.alibaba.fastjson.JSON;

import exception.AgriException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import msg.PackageMsg;
import net.protocol.ClientCmdHandler;
import net.protocol.Protocol;
import net.protocol.entity.LoginReq;
import net.protocol.entity.LoginResp;
import net.xgs.entity.edomain.StatusEnum;
import net.xgs.model.BaseMember;
import net.xgs.services.MemberService;
/**
 * 登入
 * @author TianW
 *S
 */
public class CmdHandler40 extends ClientCmdHandler {
	MemberService memberService = new MemberService();
	@Override
	public Object exec(String userId, String uuid, String msg, String sysId, Channel channel) throws AgriException {
		
		LoginReq entity = null;
		LoginResp resp = new LoginResp();
		String loginAccount = null;
		String loginPwd = null;
		try {
			entity = JSON.parseObject(msg, LoginReq.class);
			loginAccount = entity.getLoginAccount();
			loginPwd = entity.getLoginPwd();
			BaseMember su = memberService.loginAndroid(loginAccount, loginPwd);
			if (su == null || StatusEnum.PROHIBITED_USE.getValue().equals(su.getEnabled())) {
				int code = prop.getInt("error.loginUser");
				throw new AgriException(code,prop.get(code+""));
			}
			
		} catch (Exception e) {
			if(e instanceof AgriException) {
				throw e;
			}
			e.printStackTrace();
			int code = prop.getInt("error.format");
			throw new AgriException(code,prop.get(code+"",e.getMessage()));
		}
		
		//登录成功，将用户的id和管道加入到session中
		Map<String, Channel> userIdtoChannel = mSession.getUserIdTOChannel();
		Map<Channel, String> channelToUserId = mSession.getChannelTOUserId();
		userIdtoChannel.put(userId, channel);
		channelToUserId.put(channel, userId);
		resp.setCode(0);
		ByteBuf respMsg = PackageMsg.packingClient(sysId, uuid, Protocol.LOGININ, resp);
		
		return respMsg;
	}

}
