package net.xgs.commons.interceptors;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.JsonKit;

import net.xgs.commons.annotation.Log;

public class LogInterceptor implements Interceptor{

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void intercept(Invocation inv) {
		try {
			inv.invoke();
			print(inv);
		} catch (Exception e) {
			print(inv,e);
			throw e;
		}
		
	}
	private void print(Invocation inv,Throwable ex){
		//FIXME 异步执行
		Log log = inv.getMethod().getAnnotation(Log.class);
		if(log == null) return;
		String keyWord = log.describe();
		if (log.describe().isEmpty()) {
			 keyWord = inv.getTarget().getClass().getSimpleName() + " > "   + inv.getMethodName();
		}
		Map<String,Object> map = new HashMap<>();
		map.put("className", inv.getTarget().getClass().getName());
		map.put("methodName", inv.getMethodName());
		map.put("keyword", keyWord);
		map.put("args", JsonKit.toJson(inv.getArgs()));
		if(ex != null){
			map.put("exception", ExceptionUtils.getFullStackTrace(ex));
		}
		logger.info(JsonKit.toJson(map));
	}
	private void print(Invocation inv){
		this.print(inv, null);
	}
}
