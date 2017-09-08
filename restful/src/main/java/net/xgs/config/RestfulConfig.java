package net.xgs.config;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.Plugins;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.redis.RedisPlugin;
import net.xgs.commons.plugin.IocPlugin;
import net.xgs.handle.Handler4xx;
import net.xgs.init.XgsConfig;
import net.xgs.interceptor.Error5xx;
import net.xgs.interceptor.LoginInterceptor;
import net.xgs.session.RestfulSessionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestfulConfig extends XgsConfig {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private Prop prop = PropKit.use("app.properties");
	Prop cfg = PropKit.use("cfg.properties");

	@Override
	public void configConstant(Constants me) {
		super.configConstant(me);
	}

	@Override
	public void plugin(Plugins me) {
		// TODO Auto-generated method stub
		if (cfg.getBoolean("cache.enable", false)) {
			me.add(new RedisPlugin(cfg.get("cache.redis.restful.name"), cfg.get("cache.redis.host"), cfg.getInt("cache.redis.port")));
		}
		String[] pkgs = cfg.get("system.annotation.scan").split(",");
		IocPlugin ioc = new IocPlugin(routes, pkgs);
		me.add(ioc);
	}
	@Override
	public void interceptor(Interceptors me) {
		me.addGlobalActionInterceptor(new Error5xx());
		me.addGlobalActionInterceptor(new LoginInterceptor());
	}
	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub
		super.configHandler(me);
		me.add(new Handler4xx());
	}

	@Override
	public void handler(Handlers me) {

	}
	@Override
	public void afterJFinalStart() {
		RestfulSessionWrapper.initCfg(prop.get("restful.session.clazz"),cfg.get("cache.redis.restful.name"));
	}

}
