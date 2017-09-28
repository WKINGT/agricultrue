package net.xgs.config;

import com.jfinal.aop.Enhancer;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.Plugins;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.redis.RedisPlugin;
import net.xgs.commons.plugin.IocPlugin;
import net.xgs.entity.InitData;
import net.xgs.init.XgsConfig;
import net.xgs.model.SysFunctions;
import net.xgs.model.SysMenu;
import net.xgs.plugins.job.QuartzPlugin;
import net.xgs.plugins.job.QueryJob;
import net.xgs.plugins.job.YesterdayQueryJob;
import net.xgs.services.MachineBlockTypeService;

import java.util.List;

public class WebConfig extends XgsConfig {
	@Override
	public void plugin(Plugins me) {
		PathKit.setWebRootPath("/");
		if (prop.getBoolean("cache.enable", false)) {
			me.add(new RedisPlugin(prop.get("cache.redis.web.name"), prop.get("cache.redis.host"), prop.getInt("cache.redis.port")));
		}
		String[] pkgs = prop.get("system.annotation.scan").split(",");
		IocPlugin ioc = new IocPlugin(routes, pkgs);
		me.add(ioc);
		if (prop.getBoolean("quartz.enable",false)){
			QuartzPlugin plugin = QuartzPlugin.me(QueryJob.class,YesterdayQueryJob.class);
			me.add(plugin);
		}
	}

	@Override
	public void interceptor(Interceptors me) {
	}
	@Override
	public void handler(Handlers me) {

	}
	@Override
	public void afterJFinalStart() {
		List<SysMenu> sysMenus =  SysMenu.dao.find("SELECT * FROM sys_menu where is_enabled = 0  ORDER BY no asc");
		List<SysFunctions> sysFunctions = SysFunctions.dao.find("SELECT * FROM sys_functions where is_enabled = 0 AND is_del = 0 ORDER BY no asc");
		InitData.instance().init(sysMenus,sysFunctions);
		MachineBlockTypeService machineBlockTypeService = Enhancer.enhance(MachineBlockTypeService.class);
	}
}