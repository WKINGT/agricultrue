package net.xgs.services;

import net.xgs.commons.annotation.Service;
import net.xgs.commons.utils.DateUtils;
import net.xgs.commons.utils.StrUtils;
import net.xgs.entity.edomain.DataTypeEnum;
import net.xgs.model.DayHistoryDataMachine;
import net.xgs.model.HistoryDataMachine;
import net.xgs.model.ViewMachineBlockType;
import net.xgs.utils.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by duai on 2017-08-10.
 */
@Service
public class DayHistoryDataMachineService extends BaseService{
    public List<DayHistoryDataMachine> findByMachineId(List<ViewMachineBlockType> machines){
        List<String> ids = ObjectUtils.getMethodValue(machines,"getStr","machine_id");
        return DayHistoryDataMachine.dao.find("select * from day_history_data_machine where machine_id in ("+StrUtils.joinInSql(ids,'"')+")");
    }
}
