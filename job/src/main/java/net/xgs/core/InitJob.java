package net.xgs.core;

import com.jfinal.aop.Enhancer;
import net.xgs.kit.TaskManager;
import net.xgs.model.BaseTaskPlanJob;
import net.xgs.services.TaskPlanJobService;

import java.util.List;

/**
 * Created by duai on 2017-08-17.
 */
public class InitJob {
    static TaskPlanJobService taskPlanJobService = Enhancer.enhance(TaskPlanJobService.class);
    public static boolean createJob(BaseTaskPlanJob planJob){
        TaskManager.addJob(planJob);
        return true;
    }
    public static void initJob(){
        List<BaseTaskPlanJob>  result = taskPlanJobService.findAll();
        for (BaseTaskPlanJob baseTaskPlanJob:result){
            createJob(baseTaskPlanJob);
        }
    }
}
