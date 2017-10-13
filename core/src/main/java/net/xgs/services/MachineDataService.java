package net.xgs.services;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import net.xgs.commons.annotation.Service;
import net.xgs.commons.interceptors.MachineLimitInterceptor;
import net.xgs.commons.utils.DateUtils;
import net.xgs.commons.utils.StrUtils;
import net.xgs.init.XgsConfig;
import net.xgs.model.DataMachine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duai on 2017-07-31.
 */
@Service
public class MachineDataService extends BaseService{
    Cache cache = Redis.use(XgsConfig.prop.get("cache.redis.share.name"));
    private final static String machine_data_cache = "MACHINE_DATA_CACHE";
    public DataMachine findByMachine(String machineId){
        DataMachine dataMachine = null;
        if (cache!=null)
            dataMachine = cache.get(machine_data_cache+machineId);
        if (dataMachine==null){
            dataMachine = DataMachine.dao.findFirst("select machine_id,joint_data from data_machine where machine_id = ? order by create_time desc limit 0,1",machineId);
            cache.set(machine_data_cache+machineId,dataMachine);
        }
       return  dataMachine;
    }
    @Before({Tx.class,MachineLimitInterceptor.class})
    public void save(String machineId,String data,Integer dataType,String jointData){
        DataMachine dataMachine = new DataMachine();
        dataMachine.setId(getUUID());
        dataMachine.setCreateTime(System.currentTimeMillis());
        dataMachine.setMachineId(machineId);
        dataMachine.setDataType(dataType);
        dataMachine.setData(data);
        dataMachine.setJointData(jointData);
        if (cache!=null)
            cache.set(machine_data_cache+machineId,dataMachine);
        new Thread(() -> dataMachine.save()).start();
    }

    public List<DataMachine> findByMachine(String[] ids) {
        List<DataMachine> machines = new ArrayList<>();
        for (String id:ids){
            machines.add(findByMachine(id));
        }
       return machines;
    }

    /**
     * 获取指定两个日期中的最大数据值
     * @param machineIds
     * @return
     */
    public List<DataMachine> findDateDataByMachineIds(List<String> machineIds,
                                                      Long startDate,
                                                      Long endDate,String order) {

        return DataMachine.dao.find(getSql(machineIds,order),startDate,endDate);
    }

    private String getSql(List<String> machineIds,String order){
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT");
        sb.append(" 	*");
        sb.append(" FROM");
        sb.append(" 	(");
        sb.append(" 		SELECT");
        sb.append(" 			*");
        sb.append(" 		FROM");
        sb.append(" 			data_machine");
        sb.append(" 		WHERE");
        sb.append(" 			machine_id in ("+StrUtils.joinInSql(machineIds)+")");
        sb.append(" 			and create_time >= ? and create_time <= ?");
        sb.append(" 		ORDER BY");
        sb.append(" 			`data` "+order);
        sb.append(" 	) AS a");
        sb.append(" GROUP BY");
        sb.append(" 	a.machine_id,");
        sb.append(" 	a.data_type");
        return sb.toString();
    }



    public static void main(String[] args) {
      /*  List<String> dates = DateUtils.getTimesByDate("2017-08-09 15:00:00");
        for (String str:dates){
            System.out.println(str);
        }*/
        System.out.println( DateUtils.getTimeStart("2017-08-09 15:00:00"));
    }
}
