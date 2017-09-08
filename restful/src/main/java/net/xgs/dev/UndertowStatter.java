package net.xgs.dev;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;

import com.jfinal.core.JFinalFilter;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.FilterInfo;
import net.xgs.RestfulStart;

public class UndertowStatter implements Startter {

	public void start() throws ServletException {
		String MYAPP = "/";
		DeploymentInfo di = Servlets.deployment().setClassLoader(RestfulStart.class.getClassLoader()).setDeploymentName("app.war").setContextPath(MYAPP);
		di.setEagerFilterInit(true);
		//设置启动的时候，初始化servlet或filter
		
		FilterInfo info = new FilterInfo("jfinal", JFinalFilter.class);
		info.addInitParam("configClass", prop.get("system.start.configClazz"));
		di.addFilter(info);

		di.addFilterUrlMapping("jfinal", "/*", DispatcherType.REQUEST);
		DeploymentManager manager = Servlets.defaultContainer().addDeployment(di);
		manager.deploy();
		
		HttpHandler servletHandler = manager.start();
		PathHandler path = Handlers.path(servletHandler);
		Undertow server = Undertow.builder().addHttpListener(prop.getInt("system.start.port",80), "0.0.0.0").setHandler(path).build();
		server.start();
	}

}
