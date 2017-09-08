package net.xgs.commons.interceptors;

import net.xgs.commons.plugin.ioc.InjectUtils;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * 控制器上 {@Inject} 注解处理的拦截器
 */
public class InjectInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Object target = inv.getTarget();
		InjectUtils.inject(target);
		inv.invoke();
	}

}