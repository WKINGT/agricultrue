package net.xgs.plugins.job;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Db;
import net.xgs.commons.utils.DateUtils;
import net.xgs.commons.utils.StrUtils;
import net.xgs.entity.edomain.IsShowEnum;
import net.xgs.model.DataMachine;
import net.xgs.model.DayHistoryDataMachine;
import net.xgs.model.ViewMachineBlockType;
import net.xgs.services.MachineBlockTypeService;
import net.xgs.services.MachineDataService;
import net.xgs.utils.DBUtils;
import net.xgs.utils.ObjectUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by duai on 2017-09-05.
 */
@Scheduled(prop = "quartz.day_data.handle.cron")
public class YesterdayQueryJob implements Job {
    private MachineBlockTypeService machineBlockTypeService = Enhancer.enhance(MachineBlockTypeService.class);
    private MachineDataService machineDataService = Enhancer.enhance(MachineDataService.class);

    Logger logger = LoggerFactory.getLogger(YesterdayQueryJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.debug("==============定时任务开始==============");
        List<ViewMachineBlockType> machineList = machineBlockTypeService.findMachineByParams("machine_id", IsShowEnum.FALSE.getValue(), IsShowEnum.FALSE.getValue(), IsShowEnum.TRUE.getValue());
        logger.debug("查询所有产生数据的设备->完成");
        List<String> machineIds = ObjectUtils.getMethodValue(machineList, "getStr", "machine_id");
        Db.update("call proc_yesterday_machine_data('"+ StrUtils.joinInSql(machineIds,'"')+"')");
    }
}