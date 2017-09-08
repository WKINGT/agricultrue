package net.xgs.controller;

import com.jfinal.aop.Before;
import net.xgs.commons.annotation.Controller;
import net.xgs.commons.annotation.Inject;
import net.xgs.interceptor.Post;
import net.xgs.services.TaskPlanService;

/**
 * Created by duai on 2017-08-03.
 */
@Controller("/task")
public class TaskPlanController extends BaseController {
    @Inject
    TaskPlanService taskPlanService;


    @Before(Post.class)
    public Object findTaskPageByBlockId(){
        return taskPlanService.findTaskPageByBlockId(
                getPara("ids"),
                getParaToInt("pageNo"),
                getParaToInt("pageSize"));
    }


}
