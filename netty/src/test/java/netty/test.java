package netty;

public class test {
	
	public static void main(String[] args) {
//		Calendar now = Calendar.getInstance();
//		int year =now.get(Calendar.YEAR);
//		int month = now.get(Calendar.MONTH) + 1;
//		int day = now.get(Calendar.DAY_OF_MONTH);
//		int hour = now.get(Calendar.HOUR_OF_DAY);
//		int minute = now.get(Calendar.MINUTE);
//		int second = now.get(Calendar.SECOND);
//		String yearLast = new SimpleDateFormat("yy",Locale.CHINESE).format(Calendar.getInstance().getTime());
//		System.out.println(yearLast);
//		int yearLast = Integer.parseInt(new SimpleDateFormat("yy",Locale.CHINESE).format(Calendar.getInstance().getTime()));
//		System.out.println(yearLast);
//		byte a = (byte) yearLast;
//		System.out.println(a);
//		byte a =49;
//		int cmd = BytesHelper.getUnsignedInt(a); 
//		String cmdS = Integer.toHexString(cmd).toUpperCase();
//		System.out.println(a);
//		System.out.println(cmd);
//		System.out.println(cmdS);
//		byte[] a = {17, 34, 51, 68, 85, 1};
//		String s = new String(a);
//		System.out.println(s);
//		byte[] b = s.getBytes();
//		System.out.println(Arrays.toString(b));
//		short m= 2;
//		System.out.println(Arrays.toString(BytesHelper.toBytes(m)));
//		short n = m++;
//		byte[] j = BytesHelper.toBytes(m);
//		System.out.println(Arrays.toString(BytesHelper.toBytes(m)));
//		
//		System.out.println(BytesHelper.toShort(j));
//		byte[] a = {17, 34, 51, 68, 85, 1};
//		System.out.println(new String(a));
		String a  = "2A";
		System.out.println(Integer.parseInt(a, 16));
		
		
	}
	/**protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
	{
		boolean done = false;
		while (!done)
		{
			try
			{
				switch (m_state)
				{
				case WAIT_START1:
				{
					if (in.isReadable())
					{
						if (in.readByte() == 0x68)
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
						m_frame.SetLen(in.readByte());
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
						if (in.readByte() == 0x68)
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
					if (in.readableBytes() >= IIFrame.TERM_ID_LENGHT)
					{
						mTemp = in.readBytes(IIFrame.TERM_ID_LENGHT);
						m_frame.SetTerminalID(mTemp.array());
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
						m_frame.SetCommandID(mTemp.array());
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
						m_frame.SetCommand(in.readByte());
						if (m_frame.GetLen() != 0)
						{
							m_state = DecodeState.WAIT_DATA;
						}
						else
						{
							m_frame.SetData(null);
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
					if (in.readableBytes() >= m_frame.GetLen())
					{
						mTemp = in.readBytes(m_frame.GetLen());
						m_frame.SetData(mTemp.array());
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
						if (in.readByte() == m_frame.CalcCrc())
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
						if (in.readByte() == 0x16)
						{
							out.add(m_frame);
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
			catch (Exception e)
			{
				logger.error(e.getMessage(), e);
				m_state = DecodeState.WAIT_START1;
			}
		}
	}*/
}
