package msg;

import com.jfinal.kit.JsonKit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import net.protocol.Protocol;
import net.util.BytesHelper;
import net.util.Utility;

public class PackageMsg {
	private static final String VERSION = Protocol.VERSION;
	private static final byte beginByte = Protocol.BEGIN_BYTE;
	private static final byte endedByte = Protocol.END_BYTE;
	
	public static ByteBuf packingDevice(byte[] sysIdbyte, byte[] cmdNo, byte cmd, byte[] data) {
		byte[] msg = null;
		if(data!=null) {
			msg = new byte[14+data.length];
		}else {
			msg = new byte[14];
		}
		
		int i = 0;
		msg[i++] = beginByte;
		
		if(data!=null) {
			msg[i++] = (byte) data.length;
		}else {
			msg[i++] = 0;
		}
		
		msg[i++] = beginByte;
		System.arraycopy(sysIdbyte, 0, msg, i, 6);
		i += 6;
		System.arraycopy(cmdNo, 0, msg, i, 2);
		i += 2;
		msg[i++] = cmd;
		if(data!=null) {
			System.arraycopy(data, 0, msg, i, data.length);
			i += data.length;
		}
		
		msg[i++] = 0;
		msg[i++] = endedByte;
		return Unpooled.copiedBuffer(msg);
	}

	public static ByteBuf packingClient(String sysId, String uuid, short cmd, Object body) {
		byte[] version_b = VERSION.getBytes();
		byte[] sysId_b = BytesHelper.getSystemIdByte(sysId);
		byte[] uuid_b = uuid.getBytes();
		byte[] cmd_b = Utility.short2Byte(cmd);
		byte[] body_b = JsonKit.toJson(body).getBytes();

		int length = version_b.length +sysId_b.length+ uuid_b.length + cmd_b.length + body_b.length;

		byte[] all = new byte[length];
		System.arraycopy(version_b, 0, all, 0, version_b.length);
		System.arraycopy(sysId_b, 0, all, version_b.length, sysId_b.length);
		System.arraycopy(uuid_b, 0, all, version_b.length + sysId_b.length, uuid_b.length);
		System.arraycopy(cmd_b, 0, all, version_b.length + sysId_b.length + uuid_b.length, cmd_b.length);
		System.arraycopy(body_b, 0, all, version_b.length + sysId_b.length + uuid_b.length + cmd_b.length,
				body_b.length);

		byte[] len_b = Utility.int2Byte(length);
		byte[] rMsg = new byte[length + 4];
		System.arraycopy(len_b, 0, rMsg, 0, 4);
		System.arraycopy(all, 0, rMsg, 4, length);

		return Unpooled.copiedBuffer(rMsg);
	}
	public static TextWebSocketFrame packingWeb(String sysId, String uuid, short cmd, Object body) {
		RespMsg respmsg = new RespMsg(uuid,JsonKit.toJson(body),String.valueOf(cmd),sysId);
		return new TextWebSocketFrame(JsonKit.toJson(respmsg));
	}
}
