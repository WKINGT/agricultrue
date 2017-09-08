package net.xgs.commons.plugin.ioc;

import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("unchecked")
public class IocKit {
	private IocKit(){}
	private static ConcurrentMap<String, Object> beanMap;
	
	public static void init(ConcurrentMap<String, Object> beanMap) {
		IocKit.beanMap = beanMap;
	}
	
	public static ConcurrentMap<String, Object> getBeanMap() {
		return beanMap;
	}
	
	public static <T> T getBean(Class<?> clazz) {
		String beanName = clazz.getName();
		return (T) beanMap.get(beanName);
	}

}