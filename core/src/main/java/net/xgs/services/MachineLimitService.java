package net.xgs.services;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import net.xgs.commons.annotation.Service;
import net.xgs.entity.edomain.StatusEnum;
import net.xgs.model.BaseMachineLimit;
import net.xgs.query.FilterBuilder;
import org.apache.commons.lang.StringUtils;

/**
 * Created by duai on 2017-08-15.
 */
@Service
public class MachineLimitService extends BaseService {
    public BaseMachineLimit findLimitByMachineId(String machineId,Integer dataType){
        return BaseMachineLimit.dao.findFirst("select * from base_machine_limit where machine_id = ? and data_type = ? and status = ? order by create_time desc",machineId,dataType,StatusEnum.NORMAL_USE.getValue());
    }
    @Before(Tx.class)
    public void save(BaseMachineLimit machineLimit,String optId){
        if (StringUtils.isBlank(machineLimit.getId())) {
            Db.update("update base_machine_limit set status = ? where data_type = ? and machine_id = ?", StatusEnum.NORMAL_USE.getValue(),machineLimit.getDataType(),machineLimit.getMachineId());
            machineLimit.setCreateBy(optId);
            machineLimit.setId(getUUID());
            machineLimit.setCreateTime(getDateTime());
            machineLimit.save();
            return;
        }
        machineLimit.update();
        return;
    }
    public Page<BaseMachineLimit> findByPage(Integer pageNum, Integer pageSize, FilterBuilder filterBuilder){
        String select = "select *  ";
        String sqlExceptSelect = " from base_machine_limit where status = ? ";
        filterBuilder.getParams().add(0,StatusEnum.NORMAL_USE.getValue());
        return BaseMachineLimit.dao.paginate(pageNum,pageSize,select,sqlExceptSelect +filterBuilder.getSnippets() ,filterBuilder.getParams().toArray());
    }
    public void delete(String limitId){
        Db.deleteById("base_machine_limit", limitId);
    }
}
