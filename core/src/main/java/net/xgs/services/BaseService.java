package net.xgs.services;

import java.util.Date;

import net.xgs.commons.utils.DateUtils;
import net.xgs.commons.utils.StrUtils;
import net.xgs.utils.DBUtils;

/**
 * baseService
 * 
 * @author mengr
 *
 */
public class BaseService {
	
	/**
	 * uuid
	 * @return
	 */
	protected String getUUID() {
//		return UUID.randomUUID().toString().replace("-", "");
		return DBUtils.getUUID();
	}
	
	/**
	 * 32位随机数
	 * @return
	 */
	protected String random32() {
		return StrUtils.random(32);
	}
	
	/**
	 * 获取当前时间(yyyy-MM-dd HH:mm:ss)
	 * @return
	 */
	protected String getDateTime() {
		return DateUtils.format(new Date(), DateUtils.PATTERN_DATETIME);
	}

	public static void main(String[] args) {
		BaseService baseService = new BaseService();
		System.out.println(baseService.getUUID());
		System.out.println(baseService.random32());
		System.out.println(baseService.getDateTime());
	}
}
