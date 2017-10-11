package socket.testdecode;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.protocol.Protocol;
import net.util.BytesHelper;

public class AgriDecoder extends ByteToMessageDecoder {
	byte deviceBeginByte = Protocol.BEGIN_BYTE;
	byte clientBeginByte = Protocol.CLIENT_BEGIN_BYTE;
	byte jobBeginByte = Protocol.JOB_BEGIN_BYTE;
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		try {
			byte begin = in.readByte();
			if(begin==deviceBeginByte) {
//				in.markReaderIndex();
//				in.resetReaderIndex()
				byte len_b = in.readByte();
				int len = BytesHelper.getUnsignedInt(len_b);
				
				if(in.readableBytes()<len+12){
//					in.skipBytes(-1);
					return;
				}else {
					
					byte nextbegin = in.readByte();
					if(nextbegin!=deviceBeginByte) {
						return;
					}
					byte[] msg = new byte[len+14];
					msg[0] = deviceBeginByte;
					msg[1] = len_b;
					msg[2] = deviceBeginByte;
					byte[] rest = new byte[len+11];
					in.readBytes(rest);
					System.arraycopy(rest, 0, msg, 3, rest.length);
					out.add(msg);
					return;
				}
			}
			if(begin==clientBeginByte) {
				byte[] len_b = new byte[4];
				in.readBytes(len_b);
				int len = BytesHelper.bytesToInt2(len_b);
				byte[] src=new byte[len];
				in.readBytes(src);
				byte[] msg = new byte[src.length+5];
				msg[0] = begin;
				System.arraycopy(len_b, 0, msg, 1, len_b.length);
				System.arraycopy(src, 0, msg, 5, src.length);
				out.add(msg);
				return;
			}
			if(begin == jobBeginByte) {
				byte[] len_b = new byte[4];
				in.readBytes(len_b);
				int len = BytesHelper.bytesToInt2(len_b);
				byte[] src=new byte[len];
				in.readBytes(src);
				byte[] msg = new byte[src.length+5];
				msg[0] = begin;
				System.arraycopy(len_b, 0, msg, 1, len_b.length);
				System.arraycopy(src, 0, msg, 5, src.length);
				out.add(msg);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
