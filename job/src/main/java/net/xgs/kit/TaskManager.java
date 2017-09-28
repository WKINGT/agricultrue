package net.xgs.kit;

import com.duaicxx.core.TaskEntity;
import com.duaicxx.core.TaskImpl;
import com.duaicxx.core.TimerTaskPool;
import com.duaicxx.kit.DateKit;
import net.xgs.core.SendMsgJob;
import net.xgs.commons.utils.DateUtils;
import net.xgs.entity.edomain.CommandEnum;
import net.xgs.model.BaseTaskPlanJob;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by duai on 2017-08-16.
 * 此管理类是根据当前系统需求定制
 */
public class TaskManager {
    private static Map<String,String> startMap = new ConcurrentHashMap<>();
    private static Map<String,String> endMap = new ConcurrentHashMap<>();
    private static final String STARTPREFIX="start-->";
    private static final String ENDPREFIX="end-->";
    /**
     *
     * @param baseTaskPlanJob 数据
     */
    public static boolean addJob(BaseTaskPlanJob baseTaskPlanJob) {
        //获取当前时间
        Date nowDate = new Date();
        //计算结束时间
        Date job2Date = DateUtils.add(DateKit.format(baseTaskPlanJob.getRunTime()),Calendar.MINUTE,baseTaskPlanJob.getDuration());
        //计算发送开始命令时间(如果当前时间在发送开始命令与发送结束命令之间则创建当前任务)
        //如果这个发送命令不在当前区间则创建下一次任发送命令
        Date job1Date = calcRunTime(DateKit.format(baseTaskPlanJob.getRunTime()),nowDate,job2Date,baseTaskPlanJob.getPeriod());

        TaskEntity job1 = new TaskEntity().setEndTime(DateKit.format(baseTaskPlanJob.getEndTime())).setId(CommandEnum.START.getValue()+baseTaskPlanJob.getId());
        job1.setRunTime(job1Date).putUnitPeriod(Calendar.DAY_OF_MONTH,baseTaskPlanJob.getPeriod());
        job1.setTaskExecute(new SendMsgJob(baseTaskPlanJob, CommandEnum.START.getValue()));
        TimerTaskPool.addJob(new TaskImpl(job1));
        /*if (job1Date.before(nowDate)){
            TimerTaskPool.startJob(job1.getId());
        }*/
        startMap.put(baseTaskPlanJob.getId(),job1.getId());
        //使用创建发送的命令时间计算发送结束命令的任务
        job2Date = DateUtils.add(job1Date,Calendar.MINUTE,baseTaskPlanJob.getDuration());
        TaskEntity job2 = new TaskEntity().setEndTime(DateKit.format(baseTaskPlanJob.getEndTime())).setId(CommandEnum.STOP.getValue()+baseTaskPlanJob.getId());
        job2.setTaskExecute(new SendMsgJob(baseTaskPlanJob, CommandEnum.STOP.getValue()));
        job2.setRunTime(job2Date).
                putUnitPeriod(Calendar.DAY_OF_MONTH,baseTaskPlanJob.getPeriod());
        TimerTaskPool.addJob(new TaskImpl(job2));
        endMap.put(baseTaskPlanJob.getId(),job2.getId());
        return true;
    }

    /**
     * 计算运行时间与当前时间最近一次应该执行的任务时间
     * (例：runTime:12点20
     *      period:20s
     *      nowTime:12点24分00秒
     *      stopTime:12点20分10秒
     *      return:最近一次运行时间为12点23分43秒)
     * @param runTime 运行时间（开始时间）
     * @param nowTime 当前时间
     * @param stopTime 停止时间（结束时间）
     * @param period 周期
     * @return
     */
    private static Date calcRunTime(Date runTime,Date nowTime,Date stopTime,int period){
        if (nowTime.before(runTime)){//如果当前时间在运行时间之前则直接设置运行时间
            return  runTime;
        }
        Calendar beforeCalendar = Calendar.getInstance();
        beforeCalendar.setTime(runTime);
        Calendar afterCalendar = Calendar.getInstance();
        afterCalendar.setTime(stopTime);
        while (true){
            Date beforeTime = beforeCalendar.getTime();
            Date afterTime = afterCalendar.getTime();
            beforeCalendar.add(Calendar.DAY_OF_MONTH,period);
            afterCalendar.add(Calendar.DAY_OF_MONTH,period);
            //如果上一次时间在当前时间之前并且下一次时间在当前时间之后则表示在这两次之间
            if (beforeTime.before(nowTime)&&beforeCalendar.getTime().after(nowTime)){
                //如果上次结束时间在当前时间之后则返回上次时间表示上次任务未完成需要继续执行
                if (afterTime.after(nowTime))
                    return  beforeTime;
                else//如果已经过了上次结束任务则返回下一次开始时间
                    return  beforeCalendar.getTime();
            }
            if (afterCalendar.getTime().before(beforeTime)){
                return afterCalendar.getTime();
            }
        }
    }

    /**
     * @Description: 移除任务
     *
     */
    public static void removeJob(String taskId) {
        getStartMap().remove(taskId);
        getEndMap().remove(taskId);
        TimerTaskPool.delJob(STARTPREFIX+taskId);
        TimerTaskPool.startJob(ENDPREFIX+taskId);
        TimerTaskPool.delJob(ENDPREFIX+taskId);
    }

    public static Map<String, String> getStartMap() {
        return startMap;
    }

    public static Map<String, String> getEndMap() {
        return endMap;
    }

    public static void main(String[] args) {
        Date start = DateKit.format("2017-08-22 11:27:00");
        start = DateUtils.add(start,Calendar.SECOND,30);
        Date end = DateKit.format("2017-08-22 11:28:00");
        System.out.println(start.before(end));
    }
}
