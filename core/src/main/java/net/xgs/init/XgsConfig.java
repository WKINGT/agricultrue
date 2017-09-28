package net.xgs.init;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.redis.RedisPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.json.MixedJsonFactory;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.template.Engine;

import net.xgs.commons.interceptors.InjectInterceptor;
import net.xgs.commons.interceptors.LogInterceptor;
import net.xgs.commons.logs.LogPrintStream;
import net.xgs.commons.logs.SLF4JLogFactory;
import net.xgs.commons.session.SessionRepositoryRequestWrapper;
import net.xgs.handle.RenderingTimeHandler;
import net.xgs.handle.SessionHandler;
import net.xgs.model._MappingKit;
import net.xgs.model._MappingViewKit;

public abstract class XgsConfig extends JFinalConfig {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	protected Routes routes;
	public static Prop prop = PropKit.use("cfg.properties");

	@Override
	public void configConstant(Constants me) {
		System.setOut(new LogPrintStream(false));
		System.setErr(new LogPrintStream(true));
		// me.setRenderFactory();
		me.setDevMode(prop.getBoolean("system.dev", false));
		me.setJsonFactory(new MixedJsonFactory());
		me.setLogFactory(new SLF4JLogFactory());
		PathKit.setWebRootPath("/aa");
	}

	@Override
	public void configRoute(Routes me) {
		routes = me;
		logger.debug("init routes");
	}

	@Override
	public void configEngine(Engine me) {
		System.err.println("进入configEngine:>>>"+me.getBaseTemplatePath());
	}

	@Override
	public void configPlugin(Plugins me) {
		logger.debug("init plugins");
		try {
			logger.debug("init database..");
			InitDb db = (InitDb) Class.forName(prop.get("system.db")).newInstance();
			DruidPlugin druidPlugin = db.init();
			druidPlugin.setMaxPoolPreparedStatementPerConnectionSize(InitDb.prop.getInt("jdbc.min_poll", 10));
			druidPlugin.setMaxActive(InitDb.prop.getInt("jdbc.max_poll", 100));
			me.add(druidPlugin);
			ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
			arp.setShowSql(InitDb.prop.getBoolean("jdbc.show_sql", false));
			arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));
			_MappingKit.mapping(arp);
			_MappingViewKit.mapping(arp);
			me.add(arp);
			if (prop.getBoolean("cache.enable", false)) {
				me.add(new RedisPlugin(prop.get("cache.redis.share.name"), prop.get("cache.redis.host"), prop.getInt("cache.redis.port")));
			}
			logger.debug("inited database..");
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		this.plugin(me);
	}

	public abstract void plugin(Plugins me);

	@Override
	public void configInterceptor(Interceptors me) {
		me.addGlobalActionInterceptor(new InjectInterceptor());
		this.interceptor(me);
	}

	public abstract void interceptor(Interceptors me);

	@Override
	public void configHandler(Handlers me) {
		me.add(new RenderingTimeHandler());
		me.add(new DruidStatViewHandler("/druid"));
		if (prop.getBoolean("session.enable", false)) {
			SessionRepositoryRequestWrapper.initCfg(prop.get("session.domain"), prop.getInt("session.maxAgeInSeconds"), prop.get("session.id"), prop.get("session.cache"));
			me.add(new SessionHandler());
		}
		this.handler(me);
	}

	public abstract void handler(Handlers me);



}
