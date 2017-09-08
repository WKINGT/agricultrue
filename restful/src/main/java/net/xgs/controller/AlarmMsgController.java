package net.xgs.controller;

import com.jfinal.aop.Before;
import net.xgs.commons.annotation.Controller;
import net.xgs.commons.annotation.Inject;
import net.xgs.interceptor.Post;
import net.xgs.model.BaseAlarmMsg;
import net.xgs.query.FilterBuilder;
import net.xgs.services.AlarmMsgService;

import java.util.Map;

/**
 * Created by duai on 2017-08-12.
 */
@Controller(value = "alarm")
public class AlarmMsgController extends BaseController{
    @Inject
    AlarmMsgService alarmMsgService;
    @Before(Post.class)
    public Object findByPage(){
        Integer pageNum = getParaToInt("pageNum");
        Integer pageSize = getParaToInt("pageSize");
        String ids = getPara("ids");
        return alarmMsgService.findPageByBlockIds(pageNum,pageSize,ids);
    }
    @Before(Post.class)
    public Object findByCount(){
        Map<String,Integer> result = alarmMsgService.findByCount(getPara("ids"));
        return result;
    }
}
