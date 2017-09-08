package net.xgs.services;

import com.jfinal.plugin.activerecord.Db;
import net.xgs.commons.annotation.Service;
import net.xgs.commons.utils.StrUtils;
import net.xgs.entity.edomain.StatusEnum;
import net.xgs.model.BaseMachineParams;
import net.xgs.model.ViewMachineParams;

import java.util.Arrays;
import java.util.List;

/**
 * Created by duai on 2017-07-27.
 */
@Service
public class MachineParamService extends BaseService{
    public List<BaseMachineParams> findParamByMachine(String machineId){
        return BaseMachineParams.dao.find("select id,param_name,param_command from base_machine_params where status = ? and machine_id = ?", StatusEnum.NORMAL_USE.getValue(),machineId);
    }

    public void deleteByMachine(String machineId, List<String> ids) {
        Db.update("update base_machine_params set status = ? where machine_id = ? and id not in ("+ StrUtils.joinInSql(ids)+")",StatusEnum.PROHIBITED_USE.getValue(),machineId);
    }

    public List<ViewMachineParams> findByMachineIds(String machineIds,String cmd){
        return ViewMachineParams.dao.find("select * from view_machine_params where param_name = ? and machine_id in ("+StrUtils.joinInSql(Arrays.asList(machineIds.split(",")))+")",cmd);
    }
}
