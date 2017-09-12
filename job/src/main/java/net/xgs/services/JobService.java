package net.xgs.services;

import net.xgs.commons.annotation.Inject;
import net.xgs.commons.annotation.Service;
import net.xgs.core.InitJob;
import net.xgs.entity.edomain.StatusEnum;
import net.xgs.kit.TaskManager;
import net.xgs.model.BaseTaskPlanJob;

/**
 * Created by duai on 2017-08-17.
 */
@Service
public class JobService {
    @Inject
    TaskPlanJobService taskPlanJobService;

    public boolean saveJob(BaseTaskPlanJob planJob,String optId){
        taskPlanJobService.save(planJob,optId);
        planJob = BaseTaskPlanJob.dao.findById(planJob.getId());
        if (planJob.getTaskStatus()!=null&&StatusEnum.NORMAL_USE.getValue().equals(planJob.getTaskStatus().toString())){
            InitJob.createJob(planJob);
        }else if (planJob.getTaskStatus()!=null&&StatusEnum.PROHIBITED_USE.getValue().equals(planJob.getTaskStatus().toString())){
            TaskManager.removeJob(planJob.getId());
        }
        return true;
    }
    public boolean deleteJob(String jobId){
        taskPlanJobService.delete(jobId);
        TaskManager.removeJob(jobId);
        return true;
    }

}
