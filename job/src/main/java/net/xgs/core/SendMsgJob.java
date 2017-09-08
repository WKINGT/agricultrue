package net.xgs.core;

import com.alibaba.fastjson.JSONObject;
import com.duaicxx.core.TaskExecute;
import com.duaicxx.kit.DateKit;
import com.jfinal.aop.Enhancer;
import net.xgs.commons.utils.DateUtils;
import net.xgs.entity.NettyEntity;
import net.xgs.entity.SessionEntity;
import net.xgs.entity.edomain.CommandEnum;
import net.xgs.kit.NettyKit;
import net.xgs.model.BaseTaskPlanJob;
import net.xgs.model.ViewMachineParams;
import net.xgs.services.MachineParamService;
import net.xgs.services.TaskPlanJobService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by duai on 2017-08-17.
 */
public class SendMsgJob implements TaskExecute {
    private final BaseTaskPlanJob baseTaskPlanJob;
    private final String prefix;
    private TaskPlanJobService taskPlanJobService = Enhancer.enhance(TaskPlanJobService.class);
   private MachineParamService machineParamService = Enhancer.enhance(MachineParamService.class);
    public SendMsgJob(BaseTaskPlanJob baseTaskPlanJob, String prefix) {
        this.baseTaskPlanJob = baseTaskPlanJob;
        this.prefix = prefix;
    }

    @Override
    public void execute(){
        new Thread(() -> {
            List<ViewMachineParams>  machineParams = machineParamService.findByMachineIds(baseTaskPlanJob.getMachineId(), CommandEnum.findDescByValue(prefix));
            for (ViewMachineParams param:machineParams){
                String session = UUID.randomUUID().toString().replace("-","");
                NettyEntity nettyEntity = new NettyEntity();
                nettyEntity.setUuid(session);
                nettyEntity.setOperation(param.get("param_command"));
                nettyEntity.setSystemId(param.get("system_id"));
                nettyEntity.setDeviceId(param.get("device_id"));
                NettySessionManager.putSession(session,new SessionEntity(baseTaskPlanJob,prefix,param));
                NettyKit.sendMsg(JSONObject.toJSONString(nettyEntity));
            }
        }).start();
        //运行时间存库以便重启服务器接连运行
        Date runTime = DateKit.format(baseTaskPlanJob.getStartTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        runTime = DateUtils.setYears(runTime,calendar.get(Calendar.YEAR));
        runTime = DateUtils.setMonths(runTime,calendar.get(Calendar.MONTH));
        runTime = DateUtils.setDays(runTime,calendar.get(Calendar.DAY_OF_MONTH));
        taskPlanJobService.updateRuntime(baseTaskPlanJob.getId(),DateUtils.format(runTime,DateUtils.PATTERN_DATETIME));
    }
}
