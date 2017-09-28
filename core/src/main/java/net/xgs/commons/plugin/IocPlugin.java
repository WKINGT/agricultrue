package net.xgs.commons.plugin;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.xgs.commons.annotation.Controller;
import net.xgs.commons.annotation.Service;
import net.xgs.commons.interceptors.LogInterceptor;
import net.xgs.commons.plugin.ioc.InjectUtils;
import net.xgs.commons.plugin.ioc.IocKit;
import net.xgs.commons.searcher.ClassSearcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Enhancer;
import com.jfinal.config.Routes;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.IPlugin;

@SuppressWarnings("unchecked")
public class IocPlugin implements IPlugin {

	private String[] pkgs;
	private Routes routes;

	public IocPlugin(Routes routes, String[] pkgs) {
		this.pkgs = pkgs;
		this.routes = routes;
	}

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final ConcurrentMap<String, Object> iocBeanMap = new ConcurrentHashMap<String, Object>();

	@Override
	public boolean start() {
		logger.info("loading annotaion");
		Set<Class<?>> clazzs = ClassSearcher.getClasses(pkgs, Controller.class,Service.class);
		for (Class<?> clazz : clazzs) {
			// 如果是控制器
			logger.info("loading {}",clazz.getName());
			if (com.jfinal.core.Controller.class.isAssignableFrom(clazz)) {
				initController(clazz);
				continue;
			}
			
			String beanName = clazz.getName();
			Object enhanceBean = Enhancer.enhance(clazz,new LogInterceptor());
			if (iocBeanMap.containsKey(beanName)) {
				logger.warn("bean:" + beanName + " reloading!");
			}
			iocBeanMap.put(beanName, enhanceBean);
		}
		
		IocKit.init(iocBeanMap);
		// 处理Bean的相互@Inject

		Collection<Object> beanColl = iocBeanMap.values();
		for (Object object : beanColl) {
			Class<?> superclass = object.getClass().getSuperclass();
			InjectUtils.inject(superclass, object);
		}
		
		logger.info("annotation loaded ");
		return true;
	}

	@Override
	public boolean stop() {
		iocBeanMap.clear();
		return true;
	}

	private void initController(Class<?> clazz){
		Controller controller = clazz.getAnnotation(Controller.class);
		String viewPath = controller.viewPath();
		if(StrKit.isBlank(viewPath)){
			viewPath = controller.value();
		}
		routes.add(controller.value(), (Class<? extends com.jfinal.core.Controller>)clazz,viewPath);
	}
}
