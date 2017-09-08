package net.xgs.services;

import java.util.Arrays;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import net.xgs.commons.annotation.Service;
import net.xgs.commons.utils.StrUtils;
import net.xgs.entity.edomain.StatusEnum;
import net.xgs.model.BaseTaskPlan;

/**
 * Created by duai on 2017-08-02.
 */
@Service
public class TaskPlanService extends BaseService {



    public void deleteTask(String sysId, byte planNO) {
        BaseTaskPlan baseTaskPlan =findBySysIdPlanNo(sysId, planNO,StatusEnum.NORMAL_USE.getValue());
         baseTaskPlan.setStatus(StatusEnum.PROHIBITED_USE.getValue());
        baseTaskPlan.update();
        Db.update("update base_task_object set status = ? where plan_id = ?",StatusEnum.PROHIBITED_USE.getValue(),baseTaskPlan.getId());
    }

    public BaseTaskPlan findBySysIdPlanNo(String sysId, byte planNO,String status){
        BaseTaskPlan baseTaskPlan = BaseTaskPlan.dao.findFirst("select * from base_task_plan where system_id = ? and task_no = ? and status = ?",sysId,planNO,status);
        return baseTaskPlan;
    }
    public void stopTask(String sysId, byte planNO) {
        Db.update("update base_task_plan set task_status = ? where system_id = ? and task_no = ?",StatusEnum.PROHIBITED_USE.getValue(),sysId,planNO);
    }

    public void startTask(String sysId, byte planNO) {
        Db.update("update base_task_plan set task_status = ? where system_id = ? and task_no = ?",StatusEnum.NORMAL_USE.getValue(),sysId,planNO);
    }

    public Page<BaseTaskPlan> findTaskPageByBlockId(String ids,Integer pageNum,Integer pageSize){
        Page<BaseTaskPlan> baseTaskPlanPage = BaseTaskPlan.dao.paginate(pageNum,pageSize,"select * "," from base_task_plan where status = ? and block_id in ("+ StrUtils.joinInSql(Arrays.asList(ids.split(",")))+")",StatusEnum.NORMAL_USE.getValue());
        return baseTaskPlanPage;
    }

    public List<BaseTaskPlan> findTaskByBlockId(String id){
        List<BaseTaskPlan> baseTaskPlanPage = BaseTaskPlan.dao.find("select *  from base_task_plan where status = ? and block_id = ?",StatusEnum.NORMAL_USE.getValue(),id);
        return baseTaskPlanPage;
    }

}
