package net.xgs.plugins.job;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Enhancer;

import net.xgs.commons.utils.DateUtils;
import net.xgs.entity.edomain.IsShowEnum;
import net.xgs.model.DataMachine;
import net.xgs.model.HistoryDataMachine;
import net.xgs.model.ViewMachineBlockType;
import net.xgs.services.MachineBlockTypeService;
import net.xgs.services.MachineDataService;
import net.xgs.utils.DBUtils;
import net.xgs.utils.ObjectUtils;


@Scheduled(prop="quartz.data.handle.cron")
public class QueryJob implements Job{
	private MachineBlockTypeService machineBlockTypeService = Enhancer.enhance(MachineBlockTypeService.class);
	private MachineDataService machineDataService = Enhancer.enhance(MachineDataService.class);
	Logger logger = LoggerFactory.getLogger(QueryJob.class);
	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		logger.debug("==============定时任务开始==============");
		List<ViewMachineBlockType> machineList = machineBlockTypeService.findMachineByParams("machine_id",IsShowEnum.FALSE.getValue(),IsShowEnum.FALSE.getValue(),IsShowEnum.TRUE.getValue());
		logger.debug("查询所有产生数据的设备->完成");
		List<String> machineIds = ObjectUtils.getMethodValue(machineList,"getStr","machine_id");
		List<DataMachine> dataMaxMachines =machineDataService.findDateDataByMachineIds(machineIds,DateUtils.getBeforeStartDateStr(),DateUtils.getBeforeEndDateStr(),"DESC");
		logger.debug("查询指定设备的最大数据值->完成");
		List<DataMachine> dataMinMachines =machineDataService.findDateDataByMachineIds(machineIds,DateUtils.getBeforeStartDateStr(),DateUtils.getBeforeEndDateStr(),"ASC");
		logger.debug("查询指定设备的最小数据值->完成");
		for (DataMachine dataMaxMachine:dataMaxMachines){
			for (DataMachine dataMinMachine:dataMinMachines){
				if (dataMaxMachine.getMachineId().equals(dataMinMachine.getMachineId())&&
						dataMaxMachine.getDataType().equals(dataMinMachine.getDataType())){
					HistoryDataMachine historyDataMachine = new HistoryDataMachine();
					historyDataMachine.setId(DBUtils.getUUID());
					historyDataMachine.setDataType(dataMaxMachine.getDataType());
					historyDataMachine.setMaxData(dataMaxMachine.getData());
					historyDataMachine.setMinData(dataMinMachine.getData());
					historyDataMachine.setMachineId(dataMaxMachine.getMachineId());
					historyDataMachine.setCreateTime(DateUtils.getBeforeStartDateStr());
					historyDataMachine.save();
				}
			}
		}
		logger.debug("==============定时任务完成==============");
	}
	
}
