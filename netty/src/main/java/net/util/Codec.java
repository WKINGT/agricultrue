package net.util;

import com.jfinal.kit.PropKit;

import exception.AgriException;

/**
 * 报文加密解密
 * @author TianW
 *
 */

public class Codec {
	
	public static byte[] Encoder(byte[] src) throws AgriException {
		try {
			return ThreeDES.encryptThreeDESECB(src, PropKit.use("cnf.txt").get("3des.key"));
		} catch (Exception e) {
			throw new AgriException(PropKit.use("errcode.txt").getInt("error.encoding"));
		}
	}
	
	public static byte[] Decoder(byte[] src) throws AgriException {
		try {
			return ThreeDES.decryptThreeDESECB(src, PropKit.use("cnf.txt").get("3des.key"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new AgriException(PropKit.use("errcode.txt").getInt("error.decoding"));
		}
	}
}
