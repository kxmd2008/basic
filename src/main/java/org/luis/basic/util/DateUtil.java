package org.luis.basic.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * 
 * @author guoliang.li
 */
public final class DateUtil {

	public static final String PATTERN_DATE = "yyyyMMdd";
	public static final String PATTERN_DATE2 = "yyyy-MM-dd";
	public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_TIME = "HH:mm:ss";
	public static final String PATTERN_TIME2 = "HHmmss";

	/**
	 * 设置日期的时、分、秒、毫秒为0
	 */
	public static Date trimDateTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 获取date后i天,如果date为当前月最后一天，获得的Date将为下月第i天 如：date=20120430, i=1;
	 * return=20120501
	 * 
	 * @param date
	 * @param i
	 * @return
	 */
	public static Date nextDay(Date date, int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, i);
		return calendar.getTime();
	}

	/**
	 * 获取日期所在月的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	/**
	 * 获取日期所在月的下个月的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFistDayOfNextMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	private DateUtil() {

	}

	public static Date getCurrentTime() {
		return new Date();
	}

	/**
	 * 得到当前系统日期,格式："yyyy-MM-dd HH:mm:ss"
	 * 
	 * @return
	 */
	public static String getFormatCurrentTime() {
		return format(getCurrentTime(), PATTERN_DATETIME);
	}

	/**
	 * 输出字符串类型的格式化日期
	 * 
	 * @param dt
	 *            Date
	 * @param pattern
	 *            时间格式
	 * @return sDate
	 */
	public static String format(Date dt, String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(dt);
	}

	/**
	 * 
	 * 判断是否是下一天
	 * @param date1
	 * @param date2
	 * 
	 * @return
	 */

	public static boolean isNextDay(Date date1, Date date2) {
		Date nextDate1 = nextDay(date1);
		if (date2.after(nextDate1)) {
			return true;
		}
		return false;
	}

	/**
	 * 得到指定日期的前一天
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static Date preDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, -1);
		return trimDateTime(c.getTime());
	}
	
	/**
	 * 得到指定日期的前一天
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static Date preDay(Date date, int i) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, -i);
		return trimDateTime(c.getTime());
	}


	/**
	 * 得到指定日期的下一天
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static Date nextDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 1);
		return trimDateTime(c.getTime());
	}

	/**
	 * String转为Date 类型，解析出错返回null
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static Date toDate(String date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	public static void main(String args[]) {
		System.out.println(preDay(new Date(), 30));
	}

}
