package net.xgs.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import net.xgs.controller.BaseController;
import net.xgs.exception.MethodException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public  class LoginInterceptor implements Interceptor {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static Prop prop = PropKit.use("app.properties");
	@Override
	public void intercept(Invocation inv) {
		BaseController controller = (BaseController)inv.getController();
		List<String> paths = Arrays.asList(prop.get("unauthorized.url").split(","));
		if (!paths.contains(inv.getActionKey())){
			logger.debug(inv.getActionKey()+"登录验证");
			controller.getRestSession();
		}


		inv.invoke();
	}
}
