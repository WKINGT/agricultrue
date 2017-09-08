package net.xgs.services;

import net.xgs.commons.annotation.Inject;
import net.xgs.commons.annotation.Service;
import net.xgs.commons.utils.DateUtils;
import net.xgs.commons.utils.StrUtils;
import net.xgs.entity.edomain.DataTypeEnum;
import net.xgs.entity.edomain.IsShowEnum;
import net.xgs.model.HistoryDataMachine;
import net.xgs.model.ViewMachineBlockType;
import net.xgs.utils.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by duai on 2017-08-10.
 */
@Service
public class HistoryDataMachineService extends BaseService{

    public Map<String,List<HistoryDataMachine>> findHistoryByMachine(List<ViewMachineBlockType> machines,String dateStartStr,String dateEndStr){
        List<String> ids = ObjectUtils.getMethodValue(machines,"getStr","machine_id");
        List<HistoryDataMachine> historyDataMachines = HistoryDataMachine.dao.find("SELECT * FROM history_data_machine where machine_id in ("+ StrUtils.joinInSql(ids)+") and create_time >= ? and create_time <= ? GROUP BY create_time,data_type",dateStartStr,dateEndStr);
        return findHistoryByMachine(machines,historyDataMachines);
    }
    public Map<String,List<HistoryDataMachine>> findHistoryByMachine(List<ViewMachineBlockType> machines,List<HistoryDataMachine> historyDataMachines){
        Map<String,List<HistoryDataMachine>> map = new HashMap<>();
        for (ViewMachineBlockType viewMachineBlockType:machines){
            for (HistoryDataMachine historyDataMachine:historyDataMachines){
                if (viewMachineBlockType.getStr("machine_id").equals(historyDataMachine.getMachineId())){
                    String tempStr =  viewMachineBlockType.getStr("control_data_name");;
                    if (viewMachineBlockType.getStr("control_data_unit").indexOf(",")>-1){
                        tempStr += DataTypeEnum.findDescByValue(historyDataMachine.getDataType());
                    }
                    List<HistoryDataMachine> temp = map.get(tempStr);
                    if (temp==null) temp = new ArrayList<>();
                    temp.add(historyDataMachine);
                    map.put(tempStr,temp);
                }
            }
        }
        Map<String,List<HistoryDataMachine>> sortList = new HashMap<>();
        for (String key:map.keySet()){
            List<HistoryDataMachine> temp = map.get(key);
            listSort(temp);
            sortList.put(key,temp);
        }
        return sortList;
    }
    private static void listSort(List<HistoryDataMachine> list) {
        Collections.sort(list, (o1, o2) -> {
            SimpleDateFormat format = new SimpleDateFormat(DateUtils.PATTERN_DATETIME);
            try {
                Date dt1 = format.parse(o1.getCreateTime());
                Date dt2 = format.parse(o2.getCreateTime());
                if (dt1.getTime() > dt2.getTime()) {
                    return 1;
                } else if (dt1.getTime() < dt2.getTime()) {
                    return -1;
                } else {
                    return 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        });
    }
}
