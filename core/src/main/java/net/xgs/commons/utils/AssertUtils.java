package net.xgs.commons.utils;

import net.xgs.exception.TurburException;

public class AssertUtils {

	/**
	 * Object必须为空
	 * @param object
	 * @param expMsg
	 * @throws TurburException
	 */
	public static void isNull(Object object, String expMsg) throws TurburException {

		if (object != null) {
			throw new TurburException(TurburException.getCode("obj.null"), expMsg);
		}
		
	}
	
	/**
	 * Object不能为空
	 * @param object
	 * @param expMsg
	 * @throws TurburException
	 */
	public static void isNotNull(Object object, String expMsg) throws TurburException {
		
		if (object == null) {
			throw new TurburException(TurburException.getCode("obj.notnull"), expMsg);
		}
		
	}
}
