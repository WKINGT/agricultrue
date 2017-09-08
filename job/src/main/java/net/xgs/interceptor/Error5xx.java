package net.xgs.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;
import net.xgs.entity.RenderJson;
import net.xgs.exception.MethodException;

public class Error5xx implements Interceptor {

	private static Log log = Log.getLog(Error5xx.class);

	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		try {
			inv.invoke();
			Object object = inv.getReturnValue();
			controller.renderJson(new RenderJson().setCode(200).setData(object));
		} catch (Exception e) {
			if (e instanceof MethodException) {
				MethodException me = (MethodException) e;
				controller.renderJson(new RenderJson().setCode(me.getCode()).setMsg(me.getMessage()));
			} else {
				e.printStackTrace();
				log.error(e.getMessage());
				controller.renderJson(new RenderJson().setCode(500).setMsg(e.getMessage()));
			}
		}
	}
}