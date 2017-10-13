package net.xgs.commons.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by L.cm on 2016/5/18.
 */
public abstract class DateUtils {
    /**
     * 设置年
     * @param date 时间
     * @param amount 年数，-1表示减少
     * @return 设置后的时间
     */
    public static Date setYears(Date date, int amount) {
        return set(date, Calendar.YEAR, amount);
    }
    
    /**
     * 设置月
     * @param date 时间
     * @param amount 月数，-1表示减少
     * @return 设置后的时间
     */
    public static Date setMonths(Date date, int amount) {
        return set(date, Calendar.MONTH, amount);
    }
    
    /**
     * 设置周
     * @param date 时间
     * @param amount 周数，-1表示减少
     * @return 设置后的时间
     */
    public static Date setWeeks(Date date, int amount) {
        return set(date, Calendar.WEEK_OF_YEAR, amount);
    }
    
    /**
     * 设置天
     * @param date 时间
     * @param amount 天数，-1表示减少
     * @return 设置后的时间
     */
    public static Date setDays(Date date, int amount) {
        return set(date, Calendar.DATE, amount);
    }
    
    /**
     * 设置小时
     * @param date 时间
     * @param amount 小时数，-1表示减少
     * @return 设置后的时间
     */
    public static Date setHours(Date date, int amount) {
        return set(date, Calendar.HOUR_OF_DAY, amount);
    }
    
    /**
     * 设置分钟
     * @param date 时间
     * @param amount 分钟数，-1表示减少
     * @return 设置后的时间
     */
    public static Date setMinutes(Date date, int amount) {
        return set(date, Calendar.MINUTE, amount);
    }
    
    /**
     * 设置秒
     * @param date 时间
     * @param amount 秒数，-1表示减少
     * @return 设置后的时间
     */
    public static Date setSeconds(Date date, int amount) {
        return set(date, Calendar.SECOND, amount);
    }
    
    /**
     * 设置毫秒
     * @param date 时间
     * @param amount 毫秒数，-1表示减少
     * @return 设置后的时间
     */
    public static Date setMilliseconds(Date date, int amount) {
        return set(date, Calendar.MILLISECOND, amount);
    }
    
    /**
     * 设置日期属性
     * @param date 时间
     * @param calendarField 更改的属性
     * @param amount 更改数，-1表示减少
     * @return 设置后的时间
     */
    private static Date set(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar c = Calendar.getInstance();
        c.setLenient(false);
        c.setTime(date);
        c.set(calendarField, amount);
        return c.getTime();
    }


    public static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar c = Calendar.getInstance();
        c.setLenient(false);
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }
    
    public static final String PATTERN_DATETIME      = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE          = "yyyy-MM-dd";
    public static final String PATTERN_TIME          = "HH:mm:ss";

    /**
     * 获取指定日期的开始时间 如 2017-08-09 00:00:00
     * @param date
     * @return
     */
    public static Date getStartDate(Date date){
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取指定日期的结束时间 2017-08-09 23:59:59
     * @param date
     * @return
     */
    public static Date getEndDate(Date date){
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 59);
        return calendar.getTime();
    }

    public static String getStartDateStr(Date date){
        return format(getStartDate(date),PATTERN_DATETIME);
    }

    public static String getEndDateStr(Date date){

        return format(getEndDate(date),PATTERN_DATETIME);
    }
    public static Long getLongDateByStr(String dateStr){
       return parse(dateStr,PATTERN_DATETIME).getTime();
    }
    /**
     * 当天的前一天开始时间
     */
    public static String getBeforeStartDateStr(){
        return format(getStartDate(getBeforeDate()),PATTERN_DATETIME);
    }
    public static Date getBeforeDate(){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.setTime(date);
        calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)-1);
        return calendar.getTime();
    }

    /**
     * 当天前一天结束时间
     * @return
     */
    public static String getBeforeEndDateStr(){
        return format(getEndDate(getBeforeDate()),PATTERN_DATETIME);
    }
    public static Long getBeforeEndDate(){
        return getEndDate(getBeforeDate()).getTime();
    }
    public static Long getBeforeStartDate(){
        return getStartDate(getBeforeDate()).getTime();
    }
    /**
     * 日期格式化
     * @param date 时间
     * @param pattern 表达式
     * @return 格式化后的时间
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
    
    /**
     * 将字符串转换为时间
     * @param dateStr 时间字符串
     * @param pattern 表达式
     * @return 时间
     */
    public static Date parse(String dateStr, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 将long转换为时间
     * @param dateStr 时间字符串
     * @param pattern 表达式
     * @return 时间
     */
    public static Date parse(Long dateStr, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(format.format(new Date(dateStr)));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 将long转换为时间
     * @param dateStr 时间字符串
     * @param pattern 表达式
     * @return 时间
     */
    public static String format(Long dateStr, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.format(new Date(dateStr));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean isNotBlank(Long dateStr) {
        if (dateStr==null||dateStr<=-1L){
            return false;
        }
        return true;
    }

    /**
     * 时间一是否在时间二前面
     * @param dateStr
     * @param dateStr1
     * @return
     */
    public static boolean before(String dateStr,String dateStr1){
        Date date = parse(dateStr,PATTERN_DATETIME);
        Date date1 = parse(dateStr1,PATTERN_DATETIME);
        return date.before(date1);
    }

    /**
     * 时间一是否在当前时间之前
     * @param dateStr
     * @return
     */
    public static boolean before(String dateStr){
        Date date = parse(dateStr,PATTERN_DATETIME);
        Date date1 = new Date();
        return date.before(date1);
    }


    /**
     * 获取指定日期内一天中每小时最大最小值
     * @param date
     * @return
     */
    public static List<String> getTimesByDate(String date) {
        List<String> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0;i<24;i++){
            calendar.setLenient(false);
            calendar.setTime(parse(date,PATTERN_DATETIME));
            result.add(getTimeStart(calendar,i));
            result.add(getTimeEnd(calendar,i));
        }
        return result;
    }

    /**
     * 获取一个小时的开始时间
     * @param calendar
     * @param hour
     * @return
     */
    public static String getTimeStart(Calendar calendar,Integer hour){
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return format(calendar.getTime(),PATTERN_DATETIME);
    }
    public static String getTimeStart(String dateStr){
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.setTime(parse(dateStr,PATTERN_DATETIME));
        return getTimeStart(calendar,calendar.get(Calendar.HOUR_OF_DAY));
    }
    public static String getTimeEnd(String dateStr){
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.setTime(parse(dateStr,PATTERN_DATETIME));
        return getTimeEnd(calendar,calendar.get(Calendar.HOUR_OF_DAY));
    }

    public static void main(String[] args) {
        System.out.println(getBeforeStartDateStr());
    }
    /**
     * 获取一个小时的结束时间
     * @param calendar
     * @param hour
     * @return
     */
    public static String getTimeEnd(Calendar calendar,Integer hour){
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 59);
        return format(calendar.getTime(),PATTERN_DATETIME);
    }
}
