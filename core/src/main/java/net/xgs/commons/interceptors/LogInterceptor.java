package net.xgs.commons.interceptors;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.JsonKit;

import net.xgs.commons.annotation.Log;

@SuppressWarnings("unchecked")
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
		//java 在编译的时候 加    -parameter
		new Thread(()->{
			Log log = inv.getMethod().getAnnotation(Log.class);
			if(log == null) return;
			Object args = null;
			if(inv.getTarget() instanceof com.jfinal.core.Controller){
//			if(com.jfinal.core.Controller.class.isAssignableFrom(inv.getClass())){
				args = inv.getController().getRequest().getParameterMap();
			}else{
				Parameter[] parameters = inv.getMethod().getParameters();
				args = new HashMap<>();
				for(int i=0;i<parameters.length;i++){
					((Map<String,Object>)args).put(parameters[i].getName(), inv.getArg(i));
				}
			}
			
			String keyWord = log.value();
			if (log.value().isEmpty()) {
				 keyWord = inv.getTarget().getClass().getSimpleName() + " > "   + inv.getMethodName();
			}
			Map<String,Object> map = new HashMap<>();
			map.put("className", inv.getTarget().getClass().getName());
			map.put("methodName", inv.getMethodName());
			map.put("keyword", keyWord);
			map.put("args", args);
			if(ex != null){
				map.put("exception", ExceptionUtils.getFullStackTrace(ex));
			}
			logger.info(JsonKit.toJson(map));
		}).start();
	}
	private void print(Invocation inv){
		this.print(inv, null);
	}
}
