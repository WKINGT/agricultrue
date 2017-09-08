package net.xgs.handle;

import com.jfinal.core.Action;
import com.jfinal.core.JFinal;
import com.jfinal.handler.Handler;
import com.jfinal.render.RenderFactory;
import net.xgs.entity.RenderJson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Handler4xx extends Handler {

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		String[] urlPara = { null };
		Action action = JFinal.me().getAction(target, urlPara);
		if (action == null) {
			new RenderFactory().getJsonRender(new RenderJson().setCode(404).setMsg("404")).setContext(request, response).render();
			isHandled[0] = true;
			return;
		}
		next.handle(target, request, response, isHandled);
	}

}