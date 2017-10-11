package net.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;

public final class BytesHelper {

	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}
	/**
	 * Convert char to byte
	 * @param c char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}




	/**
	 * short 转化为两个字节网络序
	 * @param s
	 * @return
	 */
	public static byte[] shortToByteArray(short s) {
		  byte[] shortBuf = new byte[2];
		  for(int i=0;i<2;i++) {
		     int offset = (shortBuf.length - 1 -i)*8;
		     shortBuf[i] = (byte)((s>>>offset)&0xff);
		  }
		  return shortBuf;
	}
	/**
	 * 两个字节网络序转化为short
	 * @param b
	 * @return
	 */
	public static short bytesToShort(byte[] b) {
		return (short) (b[1] & 0xff
		| (b[0] & 0xff) << 8);
	}
	/**
	 * 4个字节转int
	 * @param b
	 * @return
	 */
	public static int byteToInt(byte[] b) {
		int s = 0;
		int s0 = b[0] & 0xff;// 最低位
		int s1 = b[1] & 0xff;
		int s2 = b[2] & 0xff;
		int s3 = b[3] & 0xff;
		s3 <<= 24;
		s2 <<= 16;
		s1 <<= 8;
		s = s0 | s1 | s2 | s3;
		return s;
	} 
	
	public static byte[] getObjIdByte(String objId) {
		String sti[] = objId.split("-");
		byte[] stringByte = new byte[3];
		for (int i = 0; i < 3; i++) {
       
			stringByte[i] = (byte) Integer.parseInt(sti[i], 16);
		}
		return stringByte;
	}
	/**
	 * 转换String型systemId成byte[]
	 * @param systemId 6个字节
	 * @return
	 */
	public static byte[] getSystemIdByte(String systemId) {
		String sti[] = systemId.split("-");
		byte[] stringByte = new byte[6];
		for (int i = 0; i < 6; i++) {
       
			stringByte[i] = (byte) Integer.parseInt(sti[i], 16);
		}
		return stringByte;
	}
	/**
	 * 转换byte[]型systemId成String
	 * @param systemIdbyte 6个字节
	 * @return
	 */
	public static String getSystemId(byte[] systemIdbyte) {
		return  getTwoNumByte(BytesHelper.getUnsignedInt(systemIdbyte[0]))+"-"+
				getTwoNumByte(BytesHelper.getUnsignedInt(systemIdbyte[1]))+"-"+
				getTwoNumByte(BytesHelper.getUnsignedInt(systemIdbyte[2]))+"-"+
				getTwoNumByte(BytesHelper.getUnsignedInt(systemIdbyte[3]))+"-"+
				getTwoNumByte(BytesHelper.getUnsignedInt(systemIdbyte[4]))+"-"+
				getTwoNumByte(BytesHelper.getUnsignedInt(systemIdbyte[5]));
	}
	/**
	 * 将byte转成两位的String类型
	 * @param s
	 * @return
	 */
	public static String getTwoNumByte(int s) {
           String val= Integer.toHexString(s);
           int len = val.length();
           if (len<2){
               val = "0"+val;
           }
           return val;
    }

	//将byte数组按16进制的方式输出
	public static String byte2hex(byte [] buffer){  
        String h = "";  
        if (buffer==null) {
        	return null;
        }
        for(int i = 0; i < buffer.length; i++){  
            String temp = Integer.toHexString(buffer[i] & 0xFF);  
            if(temp.length() == 1){  
                temp = "0" + temp;  
            }  
            h = h + " "+ temp;  
        }  
          
        return h;  
          
    } 
	
	
	public static int getUnsignedInt(byte b){
		return b & 0xff;
	}
	
	public static int toInt(byte[] bytes) {
		int result = 0;
		for (int i = 0; i < 4; i++) {
			result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
		}
		return result;
	}
	
	
	public static short toShort(byte[] bytes) {
		return (short) (((-(short) Byte.MIN_VALUE + (short) bytes[0]) << 8)
				- (short) Byte.MIN_VALUE + (short) bytes[1]);
	}

	public static byte[] toBytes(int value) {
		byte[] result = new byte[4];
		for (int i = 3; i >= 0; i--) {
			result[i] = (byte) ((0xFFl & value) + Byte.MIN_VALUE);
			value >>>= 8;
		}
		return result;
	}

	public static byte[] toBytes(short value) {
		byte[] result = new byte[2];
		for (int i = 1; i >= 0; i--) {
			result[i] = (byte) ((0xFFl & value) + Byte.MIN_VALUE);
			value >>>= 8;
		}
		return result;
	}

	public static byte[] toBytes(Object object) throws IOException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream outObj = new ObjectOutputStream(byteOut);
		outObj.writeObject(object);
		byte[] objbytes = byteOut.toByteArray();
		return objbytes;
	}

	public static Object toObject(Blob blob) throws Exception {
		InputStream is = blob.getBinaryStream();
		BufferedInputStream input = new BufferedInputStream(is);
		
		//byte[] buff = blob.getBytes(0, blob.length());
		byte[] buff = new byte[Integer.parseInt(blob.length()+"")];
		while (-1 != (input.read(buff, 0, buff.length)))
		{}
		
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buff));
		Object object = (Object) in.readObject();
		return object;
	}
	
	public static byte[] intToBytes2(int value)   
	{   
	    byte[] src = new byte[4];  
	    src[0] = (byte) ((value>>24) & 0xFF);  
	    src[1] = (byte) ((value>>16)& 0xFF);  
	    src[2] = (byte) ((value>>8)&0xFF);    
	    src[3] = (byte) (value & 0xFF);       
	    return src;  
	}  
	public static int bytesToInt2(byte[] src) {  
	    int value;    
	    value = (int) ( ((src[0] & 0xFF)<<24)  
	            |((src[1] & 0xFF)<<16)  
	            |((src[2] & 0xFF)<<8)  
	            |(src[3] & 0xFF));  
	    return value;  
	}
}
