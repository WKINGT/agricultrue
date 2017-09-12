package netty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Test1 {
	public static void main(String[] args) {
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yy-MM-dd HH:mm:ss");   

		Calendar calendar = new GregorianCalendar(2018, 11-1, 25,12,32,23);   
		Date date = calendar.getTime();  
		System.out.println("time is:"+format.format(date));
		
		
		
		Date date1 = null;
		try {
			date1 = format.parse(format.format(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println(date1.toLocaleString());
		
		Calendar now = Calendar.getInstance();
		now.setTime(date1);
		int year =now.get(Calendar.YEAR);
		int yearLast = Integer.parseInt(new SimpleDateFormat("yy",Locale.CHINESE).format(now.getTime()));
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);
		int second = now.get(Calendar.SECOND);
		
		System.out.println("年"+year);
		System.out.println("年后两位"+yearLast);
		System.out.println("月"+month);
		System.out.println("日"+day);
		System.out.println("时"+hour);
		System.out.println("分"+minute);
		System.out.println("秒"+second);
		
	}
	
}
