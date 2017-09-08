package net.xgs.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

import net.xgs.exception.MethodException;

public abstract class Method implements Interceptor {

	public void intercept(Invocation inv) {
		this.inter(inv);
	}

	public abstract void inter(Invocation inv) throws MethodException;

}
