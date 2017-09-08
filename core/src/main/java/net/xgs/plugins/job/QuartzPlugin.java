package net.xgs.plugins.job;
import java.util.ArrayList;
import java.util.List;

import com.jfinal.kit.PropKit;
import org.apache.commons.lang.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.jfinal.plugin.IPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class QuartzPlugin implements IPlugin {
 
    private Logger logger = LoggerFactory.getLogger(QuartzPlugin.class);
    private static SchedulerFactory sf = new StdSchedulerFactory();
    private Class[] objects;
    public List<Scheduler> Schedulers=new ArrayList<Scheduler>();
    private static QuartzPlugin instance;
    public QuartzPlugin(Class...objects) {
        this.objects=objects;
    }
    public static QuartzPlugin me(Class...objects){
    	if(instance == null){
    		instance = new QuartzPlugin(objects);
    	}
    	return instance;
    }
    @SuppressWarnings({"unchecked" })
    public boolean start() {
         String jobGroup = "xgs-job";
        if(objects!=null){
            for (Class clazz : objects) {
                Scheduled scheduled=(Scheduled)clazz.getAnnotation(Scheduled.class);
                try {
                    if(scheduled==null){
                        logger.warn("JOB 没有注解。");
                        continue;
                    }
                    Scheduler sched = sf.getScheduler();
                    Schedulers.add(sched);
                    String jobClassName = clazz.getName();
                    String jobCronExp = null;
                    if (StringUtils.isNotBlank(scheduled.cron())){
                        jobCronExp = scheduled.cron();
                    }else {
                        jobCronExp = PropKit.use("cfg.properties").get(scheduled.prop());
                    }
                    if (StringUtils.isBlank(jobCronExp)){
                        continue;
                    }
                    int fixedDelay =scheduled.fixedDelay();
                    boolean enable=scheduled.enable();
                     
                    if (!enable) {
                        continue;
                    }
                     
                    try {
                        clazz = Class.forName(jobClassName);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
 
                    CronTrigger cronTrigger=null;
                    Trigger trigger=null;
                     
                    JobDetail jobDetail = JobBuilder.newJob(clazz)
                            .withIdentity(jobClassName, jobGroup).build();
 
                    if(fixedDelay>0){
                         
                        int second=fixedDelay/1000;
                        trigger=TriggerBuilder.newTrigger()
                                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(second))
                                .startNow()
                                .build();
                        sched.scheduleJob(jobDetail, trigger);
                        sched.start();
                    }
                     
                    if(jobCronExp!=null&&!"".equals(jobCronExp)){
                        cronTrigger=TriggerBuilder.newTrigger()
                        		.withIdentity(jobClassName, jobGroup)
                                .withSchedule(CronScheduleBuilder.cronSchedule(jobCronExp))
                                .startNow()
                                .build();
                         
                        sched.scheduleJob(jobDetail, cronTrigger);
                        sched.start();
                    }
                } catch (SchedulerException e) {
                	logger.error("start error", e);
                    new RuntimeException(e);
                }
                 
            }
        }
         
        return true;
    }
 
    public boolean stop() {
        for (Scheduler scheduler : Schedulers) {
            try {
                scheduler.shutdown();
                logger.info("shutdown！");
            } catch (SchedulerException e) {
                logger.error("shutdown error", e);
            }
        }
        Schedulers = new ArrayList<Scheduler>();
        return true;
    }
 
    public static void main(String[] args) {
        QuartzPlugin plugin = new QuartzPlugin(QueryJob.class);
        plugin.start();
        System.out.println("执行成功！！！");
 
    }
 
}