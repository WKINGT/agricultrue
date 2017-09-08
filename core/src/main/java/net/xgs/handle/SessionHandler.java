package net.xgs.handle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.xgs.commons.session.SessionRepositoryRequestWrapper;

import com.jfinal.handler.Handler;

public class SessionHandler extends Handler {

    @Override
    public void handle(String target, HttpServletRequest request, 
            HttpServletResponse response, boolean[] isHandled) {
        request = new SessionRepositoryRequestWrapper(request, response);
        next.handle(target, request, response, isHandled);
    }

}