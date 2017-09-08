package net.xgs.services;

import com.jfinal.aop.Before;
import com.jfinal.aop.Enhancer;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import net.xgs.commons.annotation.Service;
import net.xgs.commons.utils.StrUtils;
import net.xgs.init.XgsConfig;
import net.xgs.model.BaseAlarmMsg;
import net.xgs.model.BaseMachine;
import net.xgs.model.ViewMachine;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by duai on 2017-08-11.
 */
@Service
public class AlarmMsgService extends BaseService {
    Prop prop = PropKit.use("deviceError.txt");
	MachineService machineService = Enhancer.enhance(MachineService.class);
	Cache cache = Redis.use(XgsConfig.prop.get("cache.redis.share.name"));
	private final static String block_msg_prefix = "BLOCK_MSG_PREFIX";
	private final static String manufactor_msg_prefix = "MANUFACTOR_MSG_PREFIX";
	@Before({Tx.class})
    public boolean save(String systemId,String deviceId,String msgCode){
        BaseMachine machine = machineService.findByDevice(systemId, deviceId);
        return save(machine,msgCode);
    }

    public boolean save(BaseMachine machine,String msgCode){
        BaseAlarmMsg alarmMsg = new BaseAlarmMsg();
        alarmMsg.setMachineId(machine.getId());
        alarmMsg.setMachineName(machine.getName());
        alarmMsg.setMsgCode(msgCode);
        return save(alarmMsg);
    }
    private boolean save(BaseAlarmMsg alarmMsg){
        Record record =  Db.findFirst("select * from base_block_machine where machine_id = ?",alarmMsg.getMachineId());
        alarmMsg.setId(getUUID());
        Integer value = cache.get(block_msg_prefix+record.getStr("block_id"));
        if (value==null){
            value = 0;
        }
        cache.set(block_msg_prefix+record.getStr("block_id"),value+1);
        alarmMsg.setBlockId(record.getStr("block_id"));
        alarmMsg.setCreateTime(System.currentTimeMillis());
        return alarmMsg.save();
    }
    public boolean save(String machineId,String machineName,String msgCode,
                        Integer alarmType,String addData){
        BaseAlarmMsg alarmMsg = new BaseAlarmMsg();
        alarmMsg.setMachineId(machineId);
        alarmMsg.setAlarmType(alarmType);
        alarmMsg.setMachineName(machineName);
        alarmMsg.setAddData(addData);
        alarmMsg.setMsgCode(msgCode);
        return save(alarmMsg);
    }
    @Before({Tx.class})
    public boolean save(String machineId,String msgCode,Integer data,Integer thresholdData){
        BaseMachine machine = machineService.getById(machineId);
        BaseAlarmMsg alarmMsg = new BaseAlarmMsg();
        alarmMsg.setMachineId(machine.getId());
        alarmMsg.setMachineName(machine.getName());
        alarmMsg.setMsgCode(msgCode);
        alarmMsg.setMachineData(data);
        alarmMsg.setThresholdData(thresholdData);
        return save(alarmMsg);
    }

    public BaseAlarmMsg findMaxByCode(String machineId,String code){
        BaseAlarmMsg baseAlarmMsg = BaseAlarmMsg.dao.findFirst("select create_time from base_alarm_msg where machine_id = ? and msg_code = ? order by create_time desc",machineId,code);
        return baseAlarmMsg;
    }

    public Page<BaseAlarmMsg> findPageByBlockIds(Integer pageNum, Integer pageSize, String ids) {
        Page<BaseAlarmMsg> result = BaseAlarmMsg.dao.paginate(pageNum,pageSize,
                "select machine_id,machine_name,msg_code,create_time,machine_data ",
                "from base_alarm_msg where block_id = ? order by create_time desc",ids);
        for (BaseAlarmMsg baseAlarmMsg:result.getList()){
            String code = cache.get(manufactor_msg_prefix+baseAlarmMsg.getMachineId());
            if (StringUtils.isBlank(code)){
                ViewMachine viewMachine  = ViewMachine.dao.findFirst("SELECT `code` FROM view_machine_type_manufactor where id = ?",baseAlarmMsg.getMachineId());
                code =viewMachine.getStr("code");
                cache.set(manufactor_msg_prefix+baseAlarmMsg.getMachineId(),code);
            }
            String msg =  prop.get(code+"."+baseAlarmMsg.getMsgCode());
            if (baseAlarmMsg.getMachineData()!=null){
                msg += " "+baseAlarmMsg.getMachineData()+" 当前值是:%d";
            }
            baseAlarmMsg.put("msg_content",msg);
        }
        cache.set(block_msg_prefix+ids,0);
        return result;
    }
    @Before(Tx.class)
    public void saveReadingState(String blockId){
         Db.update("update base_alarm_msg set reading_state = 1 where block_id = ?",blockId);
    }

    public Map<String,Integer> findByCount(String ids) {
        Map<String,Integer> map = new HashMap<>();
        String[] idsStr = ids.split(",");
        int sum = 0;
        for (String id : idsStr){
            Integer value = cache.get(block_msg_prefix+id);
            if (value==null){
                value = 0;
                cache.set(block_msg_prefix+id,value);
            }
            map.put(id,value);
            sum += value;
        }
        map.put("sum",sum);
        return map;
    }
}
