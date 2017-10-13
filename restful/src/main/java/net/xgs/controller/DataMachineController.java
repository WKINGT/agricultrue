package net.xgs.controller;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import net.xgs.commons.annotation.Controller;
import net.xgs.commons.annotation.Inject;
import net.xgs.commons.utils.DateUtils;
import net.xgs.entity.edomain.IsShowEnum;
import net.xgs.model.DayHistoryDataMachine;
import net.xgs.model.HistoryDataMachine;
import net.xgs.model.ViewMachineBlockType;
import net.xgs.services.DayHistoryDataMachineService;
import net.xgs.services.HistoryDataMachineService;
import net.xgs.services.MachineBlockTypeService;
import net.xgs.services.MachineDataService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by duai on 2017-08-10.
 */
@Controller("/data/machine")
public class DataMachineController extends BaseController {
    @Inject
    HistoryDataMachineService historyDataMachineService;
    @Inject
    MachineDataService machineDataService;
    @Inject
    MachineBlockTypeService machineBlockTypeService;
    @Inject
    DayHistoryDataMachineService dayHistoryDataMachineService;
    @Before(POST.class)
    public Object findDataByMachine() {
        Map<String, List<HistoryDataMachine>> map = new HashMap<>();
        String blockId = getPara("blockId");
        String startDate = getPara("startDate");
        String endDate = getPara("endDate");
        String dataType = getPara("dataType");
        List<ViewMachineBlockType> machines = machineBlockTypeService.findMachineByBlockAndMain(blockId,
                IsShowEnum.FALSE.getValue(),
                IsShowEnum.FALSE.getValue(),
                IsShowEnum.TRUE.getValue());
        if ("day".equals(dataType)) {
            map = historyDataMachineService.findHistoryByMachine(machines, startDate, endDate);
        } else if ("hour".equals(dataType)) {
            List<DayHistoryDataMachine> result = dayHistoryDataMachineService.findByMachineId(machines);
            List<HistoryDataMachine> historyDataMachines = new ArrayList<>();
            for (DayHistoryDataMachine dataMachine : result) {
                HistoryDataMachine historyDataMachine = new HistoryDataMachine();
                historyDataMachine.setDataType(dataMachine.getDataType());
                historyDataMachine.setMaxData(dataMachine.getMaxData());
                historyDataMachine.setMinData(dataMachine.getMinData());
                historyDataMachine.setCreateTime(DateUtils.getTimeStart(
                        DateUtils.format(
                                Long.valueOf(dataMachine.getCreateTime()),
                                DateUtils.PATTERN_DATETIME)));
                historyDataMachine.setMachineId(dataMachine.getMachineId());
                historyDataMachines.add(historyDataMachine);
            }
            map = historyDataMachineService.findHistoryByMachine(machines, historyDataMachines);
        }
        return map;
    }
}