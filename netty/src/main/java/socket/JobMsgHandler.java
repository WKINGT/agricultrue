package socket;

import com.alibaba.fastjson.JSON;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import net.protocol.Protocol;
import net.protocol.entity.JobControlDeviceReq;
import net.protocol.jobcmd.ContronlDevice;
import net.util.BytesHelper;
import net.xgs.utils.RSAEncrypt;

public class JobMsgHandler {
	private static Prop prop = PropKit.use("rsa.properties");
	private static final int started = Protocol.JOB_BEGIN_BYTE;
	private static RSAEncrypt rsaEncrypt= new RSAEncrypt(prop.get("private.key"),prop.get("public.key"));
	public boolean channelRead0(ChannelHandlerContext ctx,byte[] req) throws Exception {
		Channel channel = ctx.channel();
		
		if(BytesHelper.getUnsignedInt(req[0]) != started){
			return false;
		}
		
		byte[] msg_b = new byte[req.length-5];
		System.arraycopy(req, 5, msg_b, 0, req.length-5);
		byte[] plainText = rsaEncrypt.decrypt(rsaEncrypt.getPrivateKey(), msg_b);
        System.out.println(new String(plainText,"utf-8"));
		
		String  msg = new String(plainText);
		JobControlDeviceReq entity = JSON.parseObject(msg, JobControlDeviceReq.class);
		Object resp = ContronlDevice.exec(channel,entity);
		if(resp!=null) {
			byte[] returnMsgs = rsaEncrypt.encrypt(rsaEncrypt.getPublicKey(), JsonKit.toJson(resp).getBytes());
			channel.writeAndFlush(returnMsgs);
		}
		
		return true;
	}

}
