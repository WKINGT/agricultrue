package net.xgs.dev;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import com.jfinal.core.JFinalFilter;

public class JettyStartter implements Startter {

	public void start() {
		EnumSet<DispatcherType> all = EnumSet.of(DispatcherType.ASYNC, DispatcherType.ERROR, DispatcherType.FORWARD,
				DispatcherType.INCLUDE, DispatcherType.REQUEST);
		final Server server = new Server(prop.getInt("system.start.port", 80));
		try {
			WebAppContext context = new WebAppContext("/", "/");
			FilterHolder filter = new FilterHolder(new JFinalFilter());
			// configClass 要改成自己的配置文件
			filter.setInitParameter("configClass", prop.get("system.start.configClazz"));
			context.addFilter(filter, "/*", all);
			server.setHandler(context);
			server.start();
			server.join();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
