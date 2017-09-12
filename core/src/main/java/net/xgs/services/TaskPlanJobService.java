package net.xgs.services;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import net.xgs.commons.annotation.Service;
import net.xgs.commons.utils.StrUtils;
import net.xgs.entity.edomain.StatusEnum;
import net.xgs.model.BaseTaskPlanJob;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by duai on 2017-08-17.
 */
@Service
public class TaskPlanJobService extends BaseService {
    @Before(Tx.class)
    public void save(BaseTaskPlanJob baseTaskPlanJob,String optId){
        if (StringUtils.isNotBlank(baseTaskPlanJob.getId())){
            baseTaskPlanJob.update();
            return;
        }
        baseTaskPlanJob.setCreateBy(optId);
        baseTaskPlanJob.setCreateTime(getDateTime());
        baseTaskPlanJob.setId(getUUID());
        baseTaskPlanJob.save();
    }
    @Before(Tx.class)
    public void delete(String taskId){
        Db.update("update base_task_plan_job set status = ? where id = ?", StatusEnum.PROHIBITED_USE.getValue(),taskId);
    }
    public List<BaseTaskPlanJob> findAll(){
        String sql = "select * from base_task_plan_job where status = ? and end_time > now() and task_status = ?";
        return BaseTaskPlanJob.dao.find(sql,StatusEnum.NORMAL_USE.getValue(),StatusEnum.NORMAL_USE.getValue());
    }

    public Page<BaseTaskPlanJob> findTaskByBlockId(Integer pageSize,Integer pageNum,String blockIds){
        Page<BaseTaskPlanJob> result = BaseTaskPlanJob.dao.paginate(pageNum,pageSize,"select * "," from base_task_plan_job where block_id in ("+ StrUtils.joinInSql(Arrays.asList(blockIds.split(",")))+") and status = ?",StatusEnum.NORMAL_USE.getValue());
        return result;
    }
    @Before(Tx.class)
    public void updateRuntime(String id, String format) {
        Db.update("update base_task_plan_job set run_time = ? where id = ?",format,id);
    }
}
