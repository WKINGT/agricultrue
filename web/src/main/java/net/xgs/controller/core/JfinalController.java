package net.xgs.controller.core;

import com.alibaba.fastjson.JSON;
import com.jfinal.core.Controller;
import com.jfinal.core.Injector;
import com.jfinal.kit.StrKit;
import net.xgs.entity.Constants;
import net.xgs.model.BaseMember;
import net.xgs.model.BaseOrg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

public class JfinalController extends Controller {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	public HttpSession getSession() {
//		try {
//			return SessionManager.getSession(getRequest(), getResponse());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
		return getRequest().getSession();
	}
	
	protected boolean isGet() {
		if (getRequest().getMethod().toLowerCase().equals("get")) {
			return true;
		}
		return false;
	}
	
	public BaseMember getSessionSysUser(){
		return (BaseMember) getSession().getAttribute(Constants.sessionUser);
	}

	public String getSessionUserId(){
		return getSessionSysUser().getId();
	}

	public String getOrgId(){
		return getSessionSysUser().getOrgId();
	}

	//当前登录用户的组织机构
	public BaseOrg getOrg(){
		return getSessionSysUser().get(Constants.baseOrg);
	}

	/**
	 * Get model from http request.
	 */
	public <T> T getModel(Class<T> modelClass) {
		return (T)Injector.injectModel(modelClass, null, getRequest(), true);
	}
	
}
