package com.zhexinit.gameapi.util;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	private static final String DATE_PATTEN_YYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String DATE_PATTEN_YY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	private static final String DATE_PATTEN_YY_MM_DD = "yyyyMMdd";
	private static final String DATE_PATTEN_YY_MM_DD2 = "yyyy-MM-dd";
	private static final String DATE_PATTEN_YY_MM_DD3 = "yyyy_MM_dd";
	private static final String DATE_PATTERN_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";

	private static SimpleDateFormat YYYYMMDDHHMMSSSSS_FORMAT = new SimpleDateFormat(DATE_PATTERN_YYYYMMDDHHMMSSSSS);
	
	public static void main(String[] args) {
//		System.out.println(getDateFromMillisTime(System.currentTimeMillis()));
//		LocalDateTime ldt = changeStringToLocalDateTime("2021-06-01", "yyyy-MM-dd");
//		System.out.println("time=" + ldt.toString());
		
		LocalDateTime ldt = LocalDateTime.now();
//		System.out.println("***=" + changeLocalDateTimeToString(ldt, DATE_PATTEN_YY_MM_DD_HH_MM_SS));
//		System.out.println(changeLocalDateTimeToString(ldt, "HH:mm"));
		System.out.println(changeLocalDateTimeToString(ldt, "yyyy年MM月dd日"));
		
//		LocalDateTime before = LocalDateTime.of(2021, 9, 16, 9, 45, 00);
//		LocalDateTime now = LocalDateTime.now();
//		System.out.println("before=" + changeLdtToStringWithFixPattern(before));
//		System.out.println("now=" + changeLdtToStringWithFixPattern(now));
//		Duration duration = Duration.between(before, now);
//		System.out.println("seconds=" + duration.toMillis() / 1000);
//		System.out.println("before seconds=" + before.toEpochSecond(ZoneOffset.of("+8")));
	}

	/**
	 * 根据传入的时间毫秒数返回相应的日期
	 * 
	 * @param currentTimeMillis
	 * @return
	 */
	public static Date getDateFromMillisTime(long currentTimeMillis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(currentTimeMillis);
		return calendar.getTime();
	}

	public static String getCurrentYmdHmsDateStr() {
		Calendar calendar = Calendar.getInstance();
		return new SimpleDateFormat(DATE_PATTEN_YYMMDDHHMMSS).format(calendar.getTime());
	}

	public static String getCurrentDateStr() {
		Calendar calendar = Calendar.getInstance();
		return new SimpleDateFormat(DATE_PATTEN_YY_MM_DD_HH_MM_SS).format(calendar.getTime());
	}

	public static String getCurrentDayStr() {
		Calendar calendar = Calendar.getInstance();
		return new SimpleDateFormat(DATE_PATTEN_YY_MM_DD).format(calendar.getTime());
	}

	public static String getCurrentDay() {
		Calendar calendar = Calendar.getInstance();
		return new SimpleDateFormat(DATE_PATTEN_YY_MM_DD2).format(calendar.getTime());
	}

	/**
	 * 
	 * getCurrentMonthDays:获取当月天数
	 * 
	 * @return
	 */
	public static Integer getCurrentMonthDays() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 
	 * 以15分钟为梯度，得到当前时间所处的时间段 <br>
	 * 
	 * 
	 * @return
	 */
	public static String getCurrentIntervalTime() {
		Calendar rightNow = Calendar.getInstance();
		SimpleDateFormat sdfMM = new SimpleDateFormat("mm");
		SimpleDateFormat sdf = new SimpleDateFormat("HH");
		String current = sdf.format(rightNow.getTime());
		int mm = Integer.parseInt(sdfMM.format(rightNow.getTime()));
		if (mm < 15) {
			current = current + ":00:00";
		} else {
			mm = (mm / 15) * 15;
			current = current + ":" + mm + ":00";
		}
		return current;
	}

	/**
	 * 
	 * 获取本月的最后一天 <br>
	 * 
	 * @return
	 */
	public static Date getMonthLastDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}
	
	public static String getCurrentTimeStrWithMilSec() {
		return YYYYMMDDHHMMSSSSS_FORMAT.format(new Date());
	}
	
	public static String getCurrentDayByPatten3() {
		Calendar calendar = Calendar.getInstance();
		return new SimpleDateFormat(DATE_PATTEN_YY_MM_DD3).format(calendar.getTime());
	}
	
	/**
	 * 将字符串转化为LocalDateTime，若日期只到月份，如2021-06，需要再拼接日期，拼接成2021-06-01
	 * @param timeStr
	 * @param pattern
	 * @return
	 */
	public static LocalDateTime changeStringToLocalDateTime(String timeStr, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		//直接执行这个转化，得到的LocalDateTime，使用时会报错Unable to obtain LocalTime from TemporalAccessor
//		LocalDateTime ldt = LocalDateTime.parse(timeStr, formatter);

		LocalDateTime ldt = LocalDateTime.from(LocalDate.parse(timeStr, formatter).atStartOfDay());
		return ldt;
	}
	
	public static String changeLocalDateTimeToString(LocalDateTime dateTime, String pattern) {
		if (null == dateTime) {
			return "";
		}
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		String dateTimeStr = formatter.format(dateTime);
		return dateTimeStr;
	}
	
	/**
	 * 将LocalDateTime转化成yyyy-MM-dd HH:mm:ss格式的字符串
	 * @param dateTime
	 * @return
	 */
	public static String changeLdtToStringWithFixPattern(LocalDateTime dateTime) {
		if (null == dateTime) {
			return "";
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTEN_YY_MM_DD_HH_MM_SS);
		String dateTimeStr = formatter.format(dateTime);
		return dateTimeStr;
	}
}
