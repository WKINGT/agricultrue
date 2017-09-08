package net.xgs.controller;

import com.duaicxx.core.TaskEntity;
import com.duaicxx.core.TimerTaskPool;
import com.duaicxx.kit.DateKit;
import com.jfinal.aop.Before;
import net.xgs.commons.annotation.Controller;
import net.xgs.commons.annotation.Inject;
import net.xgs.interceptor.Post;
import net.xgs.kit.TaskManager;
import net.xgs.model.BaseTaskPlanJob;
import net.xgs.services.JobService;
import net.xgs.services.TaskPlanJobService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller(value = "/job")
public class IndexController extends BaseController {
    @Inject
    JobService jobService;
    @Inject
    TaskPlanJobService taskPlanJobService;
    @Before(Post.class)
    public Object save(){
        BaseTaskPlanJob planJob = getModel(BaseTaskPlanJob.class,"planJob");
        jobService.saveJob(planJob,this.getPara("token"));
        return planJob.getId();
    }
    @Before(Post.class)
    public Object delete(){
        jobService.deleteJob(this.getPara("taskId"));
        return "删除任务成功";
    }
    @Before(Post.class)
    public Object stop(){
        TaskManager.removeJob(this.getPara("taskId"));
        return "停止任务成功";
    }
    @Before(Post.class)
    public Object queryTaskList(){
        return taskPlanJobService.findTaskByBlockId(getParaToInt("pageSize"),getParaToInt("pageNum"),getPara("blockIds"));
    }
    @Before(Post.class)
    public Object findList(){
        List<String> result = new ArrayList<>();
        for (String key : TaskManager.getStartMap().keySet()){
            result.add("开始："+key);
        }
        for (String key : TaskManager.getEndMap().keySet()){
            result.add("结束："+key);
        }
        return result;
    }
    @Before(Post.class)
    public Object getById(){
        Map<String,Object> result = new HashMap<>();
        TaskEntity taskEntity = TimerTaskPool.getTask(getPara("id"));
        result.put("运行时间", DateKit.format(taskEntity.getRunTime(),DateKit.FORMAT));
        result.put("结束时间",DateKit.format(taskEntity.getEndTime(),DateKit.FORMAT));
        result.put("周期",taskEntity.getUnitPeriod());
        return result;
    }
}