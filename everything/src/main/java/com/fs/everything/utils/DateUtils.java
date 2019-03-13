package com.fs.everything.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat sdf_m = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static SimpleDateFormat sdf_d = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdfANSI = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	private static SimpleDateFormat sdfANSI_m = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
	private static SimpleDateFormat sdfANSI_d = new SimpleDateFormat("yyyy年MM月dd日");
	private static SimpleDateFormat sdf_no_space = new SimpleDateFormat("yyyyMMddHHmmss");
	

	//格式化日期
	public static String format(Date date){
		if (date==null) {
			System.out.println("日期格式化异常！传入的参数为null");
			return "1970-01-01 08:00:00";
		}
		return sdf.format(date);
	}
	//格式化日期,精确到分
	public static String format_m(Date date){
		if (date==null) {
			System.out.println("日期格式化异常！传入的参数为null");
			return "1970-01-01 08:00";
		}
		return sdf_m.format(date);
	}
	//格式化日期,精确到天
	public static String format_d(Date date){
		if (date==null) {
			System.out.println("日期格式化异常！传入的参数为null");
			return "1970-01-01";
		}
		return sdf_d.format(date);
	}
	//格式化日期,精确到秒
	public static String formatANSI(Date date){
		if (date==null) {
			System.out.println("日期格式化异常！传入的参数为null");
			return "1970年01月01日 08:00:00";
		}
		return sdfANSI.format(date);
	}
	//格式化日期,精确到分
	public static String formatANSI_m(Date date){
		if (date==null) {
			System.out.println("日期格式化异常！传入的参数为null");
			return "1970年01月01日 08:00";
		}
		return sdfANSI_m.format(date);
	}
	//格式化日期,精确到天
	public static String formatANSI_d(Date date){
		if (date==null) {
			System.out.println("日期格式化异常！传入的参数为null");
			return "1970年01月01日";
		}
		return sdfANSI_d.format(date);
	}
	
	//将字符串形式的日期转换为日期对象
	public static Date parse(String dateStr) throws ParseException{
		/*try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			log.error("将("+dateStr+")转换为日期对象异常！-->"+e);
			return new Date(0);
		}*/
		return sdf.parse(dateStr);
	}
	
	//将字符串形式的日期转换为日期对象
	public static Date parseANSI(String dateStr){
		try {
			return sdfANSI.parse(dateStr);
		} catch (ParseException e) {
			System.out.println("将("+dateStr+")转换为日期对象异常！-->"+e);
			return new Date(0);
		}
	}

	public static String format_no_space(Date date){
		if (date==null){
			return "19700101080000";
		}
		return sdf_no_space.format(date);
	}
}
