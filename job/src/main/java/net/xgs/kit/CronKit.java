package net.xgs.kit;

import com.sun.javafx.binding.StringFormatter;
import net.xgs.commons.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by duai on 2017-08-17.
 */
public class CronKit {
    private static final String cronTemplate = "%d %d %d /%d * ?";

    /**
     *
     * @param dataStr 传入格式化时间只获取 时分秒
     * @param day 周期时间（单位：天）
     * @return
     */
    public static String formatCron(String dataStr,Integer day){
        Date data = DateUtils.parse(dataStr,"yyyy-MM-dd HH:mm:ss");
        return formatCron(data,day);
    }
    public static String formatCron(Date data,Integer day){
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.setTime(data);
        Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
        Integer minute = calendar.get(Calendar.MINUTE);
        Integer second = calendar.get(Calendar.SECOND);
        String cron = StringFormatter.format(cronTemplate,second,minute,hour,day).getValue();
        return cron;
    }

    /**
     * 添加指定分钟在进行格式化
     * @param data
     * @param day
     * @param addMinute
     * @return
     */
    public static String formatCron(Date data,Integer day,Integer addMinute){
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.setTime(data);
        calendar.set(Calendar.MINUTE,addMinute);
        return formatCron(calendar.getTime(),day);
    }
    public static String formatCron(String dataStr,Integer day,Integer addMinute){
        Date data = DateUtils.parse(dataStr,"yyyy-MM-dd HH:mm:ss");
        return formatCron(data,day,addMinute);
    }
}
