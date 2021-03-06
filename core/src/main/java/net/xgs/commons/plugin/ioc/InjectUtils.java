package net.xgs.commons.plugin.ioc;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;




import net.xgs.commons.annotation.Inject;

import com.jfinal.log.Log;

/**
 * Bean注入工具类
 */
public class InjectUtils {
	private static Log log = Log.getLog(InjectUtils.class);
	
	public static void inject(final Object target) {
		Class<?> clazz = target.getClass();
		inject(clazz, target);
	}
	
	public static void inject(Class<?> clazz, final Object target) {
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields){
			if (null == field || !field.isAnnotationPresent(Inject.class)) {
				continue;
			}
			int modifiers = field.getModifiers();
			if (Modifier.isFinal(modifiers)) {
				throw new RuntimeException("Class: " + clazz + " field: " + field.getName() + " is final!");
			}
			if (Modifier.isStatic(modifiers)) {
				log.warn("Class " + clazz + " field " + field.getName() + "is static!");
			}
			Object value = IocKit.getBean(field.getType());
			if (value == null) {
				throw new RuntimeException("not has bean:" + field.getType());
			}
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			try {
				field.set(target, value);
			} catch (IllegalArgumentException e) {
				log.error(e.getMessage(), e);
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				log.error(e.getMessage(), e);
				throw new RuntimeException(e);
			}
		}
		
	}
}