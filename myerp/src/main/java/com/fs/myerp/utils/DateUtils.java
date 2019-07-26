package com.fs.myerp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static Logger log = LoggerFactory.getLogger(DateUtils.class);

	//格式化日期
	public static String format(Date date){
		if (date==null) {
			log.error("日期格式化异常！传入的参数为null");
			return "1970-01-01 08:00:00";
		}
		return sdf.format(date);
	}
	//格式化日期,精确到分
	public static String format_m(Date date){
		if (date==null) {
			log.error("日期格式化异常！传入的参数为null");
			return "1970-01-01 08:00";
		}
		return sdf_m.format(date);
	}
	//格式化日期,精确到天
	public static String format_d(Date date){
		if (date==null) {
			log.error("日期格式化异常！传入的参数为null");
			return "1970-01-01";
		}
		return sdf_d.format(date);
	}
	//格式化日期,精确到秒
	public static String formatANSI(Date date){
		if (date==null) {
			log.error("日期格式化异常！传入的参数为null");
			return "1970年01月01日 08:00:00";
		}
		return sdfANSI.format(date);
	}
	//格式化日期,精确到分
	public static String formatANSI_m(Date date){
		if (date==null) {
			log.error("日期格式化异常！传入的参数为null");
			return "1970年01月01日 08:00";
		}
		return sdfANSI_m.format(date);
	}
	//格式化日期,精确到天
	public static String formatANSI_d(Date date){
		if (date==null) {
			log.error("日期格式化异常！传入的参数为null");
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
			log.error("将("+dateStr+")转换为日期对象异常！-->"+e);
			return new Date(0);
		}
	}

	public static String format_no_space(Date date){
		if (date==null){
			return "19700101080000";
		}
		return sdf_no_space.format(date);
	}

    public static String dateDiff(Date begin, Date end, int style) throws Exception{
        long diff = end.getTime()-begin.getTime();
        if (diff<=0){
            throw new Exception("DateUtils.dateDiff() exception! -->结束时间须大于开始时间！");
        }
        long days = diff / (1000 * 60 * 60 * 24);
        long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
        long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
        long seconds = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60)-minutes*(1000*60))/1000;
        //log.error(days+"天"+hours+"小时"+minutes+"分"+seconds+"秒");
        String retval = null;
        switch (style){
            case 1:
                retval = days+"天"+hours+"小时"+minutes+"分"+seconds+"秒";
                break;
            case 2:
                retval = days+"天"+hours+"小时"+minutes+"分";
                break;
            case 3:
                retval = days+"天"+hours+"小时";
                break;
            case 4:
                retval = days+"天";
                break;
            default:
                throw new Exception("DateUtils.dateDiff() exception! -->参数style无效！");
        }
        return retval;
    }

    public static int dateDiff(Date begin, Date end) throws Exception{
        long diff = end.getTime()-begin.getTime();
        if (diff<=0){
            throw new Exception("DateUtils.dateDiff() exception! -->结束时间须大于开始时间！");
        }
        long days = diff / (1000 * 60 * 60 * 24);
        return (int)days;
    }
}
