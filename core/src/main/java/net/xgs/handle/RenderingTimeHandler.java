package net.xgs.handle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.handler.Handler;

public class RenderingTimeHandler extends Handler {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        long start = System.currentTimeMillis();
        String userAgent = request.getHeader("User-Agent");
        next.handle(target, request, response, isHandled);
        long end = System.currentTimeMillis();
        log.info("User-Agent:["+ userAgent + "]\tURL:["+ target + "]\tTRENDING TIME:\t[" + (end - start) + "]ms");
    }

}