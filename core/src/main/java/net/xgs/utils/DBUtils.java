package net.xgs.utils;

import com.jfinal.plugin.activerecord.Db;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

public class DBUtils {
	//redis连接符
	public static String REDISKEYSEPARATOR="_";
	public static String getUUID(){
		String uuid = Db.findFirst("select replace(uuid(),'-','') as uuid").get("uuid");
		return uuid;
	}

	/**
	 * redis联合key计算
	 * @param keys
	 * @return
	 */
	public static String getRedisKey(String... keys){
		Arrays.sort(keys);
		String key = StringUtils.join(keys,REDISKEYSEPARATOR);
		return MD5Utils.md5(key);
	}

	public static void main(String[] args) {
		String key = getRedisKey("aaa","bbbbb","cccc");
		String key1 = getRedisKey("cccc","aaa","bbbbb");
		System.out.println(key+"===="+key1);
	}
}
