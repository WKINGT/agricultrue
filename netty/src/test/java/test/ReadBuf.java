package test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ReadBuf {
//	private Logger logger = LoggerFactory.getLogger(getClass());
	private static ByteBuf mTemp = null;
	private static IIFrame m_frame = null;
	private static DecodeState m_state = DecodeState.WAIT_START1;
//	private static List<IIFrame> out = new ArrayList<IIFrame>();
	public static void Decode(ChannelHandlerContext ctx,ByteBuf in) {
		boolean done = false;
		try {
			while(!done) {
				
				switch (m_state)
				{
				case WAIT_START1:
				{
					if (in.isReadable())
					{
						if (in.readByte() == (byte)0x68)
						{
							m_state = DecodeState.WAIT_LEN;
							if (m_frame == null)
							{
								m_frame = new IIFrame();
							}
						}
					}
					else
					{
						done = true;
					}
					break;
				}

				case WAIT_LEN:
				{
					if (in.isReadable())
					{
						m_frame.setLen(in.readByte());
						m_state = DecodeState.WAIT_START2;
					}
					else
					{
						done = true;
					}
					break;
				}

				case WAIT_START2:
				{
					if (in.isReadable())
					{
						if (in.readByte() == (byte)0x68)
						{
							m_state = DecodeState.WAIT_ID;
						}
						else
						{
							m_state = DecodeState.WAIT_START1;
						}
					}
					else
					{
						done = true;
					}
					break;
				}

				case WAIT_ID:
				{
					if (in.readableBytes() >= 6)
					{
						mTemp = in.readBytes(6);
						byte[] b = new byte[6];
						mTemp.readBytes(b);
						m_frame.setTerminalID(b);
						mTemp.release();
						m_state = DecodeState.WAIT_CMD_ID;
					}
					else
					{
						done = true;
					}
					break;
				}

				case WAIT_CMD_ID:
				{
					if (in.readableBytes() >= 2)
					{
						mTemp = in.readBytes(2);
						byte[] b = new byte[2];
						mTemp.readBytes(b);
						m_frame.setCommandID(b);
						mTemp.release();
						m_state = DecodeState.WAIT_CMD;
					}
					else
					{
						done = true;
					}
					break;
				}

				case WAIT_CMD:
				{
					if (in.isReadable())
					{
						m_frame.setCommand(in.readByte());
						if (m_frame.getLen() != 0)
						{
							m_state = DecodeState.WAIT_DATA;
						}
						else
						{
							m_frame.setData(null);
							m_state = DecodeState.WAIT_CRC;
						}
					}
					else
					{
						done = true;
					}
					break;
				}

				case WAIT_DATA:
				{
					if (in.readableBytes() >= m_frame.getLen())
					{
						mTemp = in.readBytes(m_frame.getLen());
						byte[] b = new byte[m_frame.getLen()];
						mTemp.readBytes(b);
						m_frame.setData(b);
						mTemp.release();
						m_state = DecodeState.WAIT_CRC;
						break;
					}
					else
					{
						done = true;
					}
					break;
				}

				case WAIT_CRC:
				{
					if (in.isReadable())
					{
						if (in.readByte() == (byte)0x00)
						{
							m_state = DecodeState.WAIT_END;
						}
						else
						{
							m_state = DecodeState.WAIT_START1;
						}
					}
					else
					{
						done = true;
					}
					break;
				}

				case WAIT_END:
				{
					if (in.isReadable())
					{
						if (in.readByte() == (byte)0x16)
						{
//							out.add(m_frame);
							TestHandler.channelRead0(m_frame, ctx);
							m_frame = null;
						}
						m_state = DecodeState.WAIT_START1;
					}
					else
					{
						done = true;
					}
					break;
				}
				default:
				{
					m_state = DecodeState.WAIT_START1;
				}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_state = DecodeState.WAIT_START1;
		}
		
	}
}
