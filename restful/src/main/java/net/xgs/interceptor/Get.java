package net.xgs.interceptor;

import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

import net.xgs.exception.MethodException;

public class Get extends Method {

	public void inter(Invocation inv) {
		Controller controller = inv.getController();
		if ("GET".equals(controller.getRequest().getMethod().toUpperCase())) {
			inv.invoke();
			return;
		}
		throw new MethodException(405, "method must be GET");
	}
}
