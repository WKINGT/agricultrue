package net.xgs.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import net.xgs.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public  class LoginInterceptor implements Interceptor {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Override
	public void intercept(Invocation inv) {
		BaseController controller = (BaseController)inv.getController();
		controller.getRestSession();
		inv.invoke();
	}
}
