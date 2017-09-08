package net.xgs.services;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import net.xgs.commons.annotation.Inject;
import net.xgs.commons.annotation.Service;
import net.xgs.commons.utils.StrUtils;
import net.xgs.model.ViewMachineBlockType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by duai on 2017-08-24.
 */
@Service
public class MemberBlockDataService extends BaseService {
    @Inject
    BlockService blockService;
    public Map<String,Object> findIndexDataByMember(String memberId){
        Map<String,Object> result = new HashMap<>();
        List<String>  blocks = blockService.findBlockIdByMemberId(memberId);
        List<ViewMachineBlockType> machineBlockTypes = ViewMachineBlockType.dao.find("SELECT type_name,COUNT(machine_id) as types FROM view_machine_block_type where block_id in ("+ StrUtils.joinInSql(blocks)+") GROUP BY type_id");
        String sql = "SELECT COUNT(CASE WHEN end_time<NOW() THEN 1 ELSE NULL END) as yes," +
                "COUNT(CASE WHEN end_time>NOW() THEN 1 ELSE NULL END) as no FROM base_task_plan_job WHERE block_id in ("+ StrUtils.joinInSql(blocks)+")";
        Record task = Db.findFirst(sql);
        Record alarmMsg = Db.findFirst("SELECT COUNT(1) as alarmCount FROM base_alarm_msg WHERE block_id in ("+ StrUtils.joinInSql(blocks)+")");
        result.put("machines",machineBlockTypes);
        result.put("runTask",task.get("yes"));
        result.put("endTask",task.get("no"));
        result.put("alarmCount",alarmMsg.get("alarmCount"));
        return result;
    }
}
