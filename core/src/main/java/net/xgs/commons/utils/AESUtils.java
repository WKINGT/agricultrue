package net.xgs.commons.utils;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
	private AESUtils(){}

	public static String encrypt(String secret, String value) {
		SecretKeySpec keySpec = getKey(secret);
		IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
			byte[] encrypted = cipher.doFinal(value.getBytes(Charsets.UTF_8));
			return Base64Utils.encode(encrypted);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String decrypt(String secret, String value) {
		SecretKeySpec keySpec = getKey(secret);
		IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
			byte[] encrypted1 = Base64Utils.decodeBase64(value);

			byte[] original = cipher.doFinal(encrypted1);
			return new String(original, Charsets.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static SecretKeySpec getKey(String secret) {
		byte[] bytes = secret.getBytes(Charsets.UTF_8);
		return new SecretKeySpec(Arrays.copyOf(bytes, 16), "AES");
	}
}