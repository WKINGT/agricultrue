package net.xgs.controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import net.xgs.entity.Constants;
import net.xgs.exception.MethodException;
import net.xgs.model.BaseMember;
import net.xgs.session.RestfulSession;
import net.xgs.session.RestfulSessionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseController extends Controller {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected Prop prop = PropKit.use("error.properties") ;

    public RestfulSession getRestSession(){
        String token = this.getPara("token");
        if(token == null){
           throwException("login.overdue");
        }
        RestfulSession restfulSession = RestfulSessionWrapper.getRestfulSessionManager().get(token);
        if (restfulSession == null) {
            throwException("login.rewrite.login");
        }
        return restfulSession;
    }
    public BaseMember getSessionBaseMember(){
        BaseMember baseMember = (BaseMember) getRestSession().getAttribute(Constants.sessionUser);
        return baseMember;
    }

    public void throwException(String key){
        int code = prop.getInt(key);
        throw new MethodException(code,prop.get(String.valueOf(code)));
    }
    public void setSessiontAttribute(String key, Object value){
        RestfulSession session = getRestSession();
        session.setAttribute(Constants.sessionUser,value);
        RestfulSessionWrapper.getRestfulSessionManager().save(session);
    }

}
