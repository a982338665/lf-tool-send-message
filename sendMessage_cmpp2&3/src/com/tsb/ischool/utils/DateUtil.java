package com.tsb.ischool.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * 
 * @author zkl
 * @date 2014-7-17 下午3:49:06
 * @version 1.0
 */
public class DateUtil {

	private static Logger logger = Logger.getLogger(DateUtil.class.getName());

	/**
	 * 时区：东8区（北京时间）
	 */
	public static final String TIMEZONE_GMT8 = "GMT+08:00";

	/**
	 * 日期格式化模板 yyyy-MM-dd HH:mm:ss
	 */
	public static final String DATE_FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 日期格式化模板 yyyy-MM-dd
	 */
	public static final String DATE_FORMAT_SHORT = "yyyy-MM-dd";

	/**
	 * 中文小时
	 */
	private static final String CN_HOUR = " 小时 ";

	/**
	 * 中文分
	 */
	private static final String CN_MINITE = " 分";

	/**
	 * 日期增加-按月增加
	 * 
	 * @param date
	 * @param days
	 * @return java.util.Date
	 */
	public static String dateIncreaseByMonth(int mnt) {

		Calendar c = Calendar.getInstance();

		c.add(Calendar.MONTH, mnt);

		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_SHORT);

		return sdf.format(c.getTime());

	}

	/**
	 * 日期增加-按天增加
	 * 
	 * @param date
	 * @param days
	 * @return java.util.Date
	 */
	public static String dateIncreaseByDay(int mnt) {

		Calendar c = Calendar.getInstance();

		c.add(Calendar.DATE, mnt);

		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_SHORT);

		return sdf.format(c.getTime());

	}

	/**
	 * 取得按要求格式按天增加的字符串
	 * 
	 * @param mnt
	 * @param format
	 * @return
	 */
	public static String dateIncreaseByDay(int mnt, String format) {

		Calendar c = Calendar.getInstance();

		c.add(Calendar.DATE, mnt);

		SimpleDateFormat sdf = new SimpleDateFormat(format);

		return sdf.format(c.getTime());

	}
	/**
	 * 取得按要求格式按天增加的英文月份字符串
	 * 
	 * @param mnt
	 * @param format
	 * @return
	 */
	public static String dateIncreaseByDayEx(int mnt, String format) {

		Calendar c = Calendar.getInstance();
        if(mnt!=0){
		c.add(Calendar.DATE, mnt);
        }
		SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.US);

		return sdf.format(c.getTime()).toUpperCase();

	}
	
	public static String digtialCovertENDate(String digtalString ){
		
		Date date=null;
		try {
			date=formatStrToDate(digtalString,"yyyyMMdd");
		} catch (Exception e) {
			
			return "";
		}
	 
	 SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyy",Locale.US);		
	  return sdf.format(date.getTime()).toUpperCase();
	}

	/**
	 * 日期转换为字符串
	 * 
	 * @param date
	 *            要转换的日期
	 * @return 默认转为yyyy-mm-dd的字符串结果
	 */
	public static String getDateString(Date date) {

		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_SHORT);

		return sdf.format(date);

	}

	/**
	 * 日期转换为字符串
	 * 
	 * @param date
	 *            要转换的日期
	 * @return 默认转为yyyy-mm-dd hh:mm:ss的字符串结果
	 */
	public static String getDateLongString(Date date) {

		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_LONG);

		return sdf.format(date);

	}

	/**
	 * 日期转化成制定格式字符串
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDateLongString(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 字符串转换为日期
	 * 
	 * @param str
	 *            要转换的字符串
	 * @param format
	 *            转换格式，yyyy-mm-dd
	 * @return Date
	 * @throws Exception
	 */
	public static Date formatStrToDate(String str, String format) throws Exception {

		SimpleDateFormat fmt = new SimpleDateFormat(format);

		return fmt.parse(str);

	}
	
	public static void main(String[] args) {
		try{
			String str="2012-09-12 16:34:46";
			System.out.println(formatStrToTimestamp(str));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	public static Timestamp formatStrToTimestamp(String str,String format) throws ParseException{
		SimpleDateFormat fmt = new SimpleDateFormat(format);
		return new Timestamp(fmt.parse(str).getTime());
	}
	
	public static Timestamp formatStrToTimestamp(String str) throws ParseException{
		return formatStrToTimestamp(str,DATE_FORMAT_LONG);
	}
	
	/**
	 * 转换秒成 xx小时xx分钟
	 * 
	 * @param seconds
	 * @return
	 */
	public static String parseElapsedTime(long startDate, long endDate) {

		String convert = "";

		long endTime = endDate;

		long startTime = startDate;

		long diff = Math.abs(endTime - startTime) / 1000;

		long hours = diff / 3600;

		convert += hours + CN_HOUR;

		long minutes = (diff - (hours * 3600)) / 60;

		convert += minutes + CN_MINITE;

		return convert;
	}

	/**
	 * 取指定字符串时间是星期几
	 * 
	 * @param datetime
	 *            for"2009-1-1"
	 * @return 0是星期天，1是星期一，…… for all
	 */
	public static int getWeekdayOfDateTime(String datetime) {

		DateFormat df = new SimpleDateFormat(DATE_FORMAT_SHORT);

		Calendar c = Calendar.getInstance();

		try {

			c.setTime(df.parse(datetime));

		} catch (Exception e) {

			logger.error("", e);

		}

		int weekday = c.get(Calendar.DAY_OF_WEEK) - 1;

		return weekday;
	}

	/**
	 * 获取系统当前时间
	 * 
	 * @return
	 */
	public static Date getCurrentDate() {

		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_LONG);// 设置日期格式

		Date currentDate = new Date();

		try {

			currentDate = df.parse(df.format(new Date()));

		} catch (ParseException e) {

		}

		return currentDate;

	}

	// 比较日期
	public static int compare_date(String DATE1, String DATE2) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		try {

			Date dt1 = df.parse(DATE1);

			Date dt2 = df.parse(DATE2);

			if (dt1.getTime() > dt2.getTime()) {

				// System.out.println("dt1 在dt2前");
				return 1;

			} else if (dt1.getTime() < dt2.getTime()) {

				// System.out.println("dt1在dt2后");
				return -1;

			} else {

				return 0;

			}

		} catch (Exception e) {

			logger.error("", e);

		}

		return 0;
	}
/**
 * @param ENWeek
 * @return
 */
public static String getCNWeekByENWeek(String ENWeek){
	String cNWeek="";
	if(ENWeek==null){
		
	}	
	else if(ENWeek.equalsIgnoreCase("MON")){
		cNWeek="星期一";
		
	}else if(ENWeek.equalsIgnoreCase("MON")){
		cNWeek="星期二";
		
	}else if(ENWeek.equalsIgnoreCase("TUE")){
		cNWeek="星期三";
		
	}else if(ENWeek.equalsIgnoreCase("WED")){
		cNWeek="星期四";
		
	}else if(ENWeek.equalsIgnoreCase("THU")){
		cNWeek="星期五";
		
	}else if(ENWeek.equalsIgnoreCase("FRI")){
		
		cNWeek="星期六";
	}else if(ENWeek.equalsIgnoreCase("SAT")){
		
		cNWeek="星期日";
	}else if(ENWeek.equalsIgnoreCase("SUN")){
		
		cNWeek="";
	}
	
	
	return cNWeek;
	
}

/**
 * @param eNdate
 * @return
 */
	public static String getCNDateByENDate(String eNdate) {
		String cNdate = "";
		if (eNdate == null || eNdate.length() != 7) {
			return cNdate;
		}
		String cNYear = "20" + eNdate.substring(5, 7) + "年";
		String cNDate = eNdate.substring(0, 2) + "日";
		String enMonth = eNdate.substring(2, 5);
		String cnMonth = "";
		if (enMonth.equalsIgnoreCase("JAN")) {
			cnMonth = "01";
		} else if (enMonth.equalsIgnoreCase("FEB")) {
			cnMonth = "02";
		} else if (enMonth.equalsIgnoreCase("MAR")) {
			cnMonth = "03";
		} else if (enMonth.equalsIgnoreCase("APR")) {
			cnMonth = "04";
		} else if (enMonth.equalsIgnoreCase("MAY")) {
			cnMonth = "05";
		} else if (enMonth.equalsIgnoreCase("JUN")) {
			cnMonth = "06";
		} else if (enMonth.equalsIgnoreCase("JUL")) {
			cnMonth = "07";
		} else if (enMonth.equalsIgnoreCase("AUG")) {
			cnMonth = "08";
		} else if (enMonth.equalsIgnoreCase("SEP")) {
			cnMonth = "09";
		} else if (enMonth.equalsIgnoreCase("OCT")) {
			cnMonth = "10";
		} else if (enMonth.equalsIgnoreCase("NOV")) {
			cnMonth = "11";
		} else if (enMonth.equalsIgnoreCase("DEC")) {
			cnMonth = "12";
		}

		return cNYear + cnMonth + "月" + cNDate;
	}

	/**
	 * 1228  to 12:28
	 * @param fourDigtal
	 * @return
	 */
	public static String dateTimeConvert(String fourDigtal){
		
		if(fourDigtal==null || fourDigtal.length()!=4){
			return "";
			
		}
		else{
			String oneString =fourDigtal.substring(0,2);
			String twoString =fourDigtal.substring(2,4);
			return oneString+":"+twoString;
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//  wangyan add 2012-05-13
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	/**
//	 * oracle.sql.TIMESTAMP 转 成 java.sql.Timestamp 并已经yyyy-MM-dd HH:mm:ss 字符串格式输出
//	 * @param oTimeStamp
//	 * @return
//	 * @throws SQLException
//	 */
//	public static String OracleTimeStamp2DefaultFormatString(oracle.sql.TIMESTAMP oTimeStamp) throws SQLException{
//		java.sql.Timestamp sTimeStamp = oTimeStamp.timestampValue();
//		SimpleDateFormat sDF = new SimpleDateFormat(DATE_FORMAT_LONG);
//		return sDF.format(sTimeStamp);
//	}
//	/**
//	 * oracle.sql.TIMESTAMP 转 成 java.sql.Timestamp 并已经自定义的format 字符串格式输出
//	 * @param oTimeStamp
//	 * 					
//	 * @param format
//	 *             列如：yyyy-MM-dd HH:mm:ss
//	 * @return
//	 * @throws SQLException
//	 */
//	public static String OracleTimeStamp2String(oracle.sql.TIMESTAMP oTimeStamp,String format) throws SQLException{
//		java.sql.Timestamp sTimeStamp = oTimeStamp.timestampValue();
//		SimpleDateFormat sDF = new SimpleDateFormat(format);
//		return sDF.format(sTimeStamp);
//	}
	
	/**
	 * java.sql.Timestamp 转成 默认格式（yyyy-MM-dd HH:mm:ss）的时间字符串
	 * @param jTimeStamp
	 * @return
	 */
	public static String TimeStamp2DefaultFormatString(java.sql.Timestamp jTimeStamp){
		SimpleDateFormat sDF = new SimpleDateFormat(DATE_FORMAT_LONG);
		return sDF.format(jTimeStamp);
	}
	/**
	 * java.sql.Timestamp 转成 自定义格式的时间字符串
	 * @param jTimeStamp
	 * @param format
	 * 					定义格式
	 * @return
	 */
	public static String TimeStamp2String(java.sql.Timestamp jTimeStamp,String format){
		if(jTimeStamp==null){
			return "";
		}
		SimpleDateFormat sDF = new SimpleDateFormat(format);
		return sDF.format(jTimeStamp);
	}

	/**
	 * 解析字符串型时间元素转成默认格式的字符串
	 * 默认格式：yyyy-MM-dd HH:mm:ss.SSS
	 * @param _year
	 * 				必须为4位例如（2012）,不能为NULL，不能为空字串
	 * @param _month
	 * 				必须为1-12数字型字符串,不能为NULL，不能为空字串
	 * @param _date
	 * 				必须为1-31数字型字符串,不能为NULL，不能为空字串
	 * @param _hour
	 * 				必须为0-23数字型字符串，不能为NULL，不能为空字串
	 * @param _minute
	 * 				必须为0-59数字型字符串，不能为NULL，不能为空字串
	 * @param _second
	 * 				必须为0-59数字型字符串，不能为NULL，不能为空字串
	 * @param _millisecond
	 * 				必须为0-999数字型字符串，不能为NULL，不能为空字串
	 * @return
	 * @throws Exception 
	 */
	public static String parseDateElement2FormatString(String _year,String _month,String _date,String _hour,String _minute,String _second,String _millisecond) throws Exception{
		
		if(_year==null || _month==null || _date==null || _hour==null || _minute==null || _second==null || _millisecond==null)
			throw new Exception("参数不可以为null");
		if(_year.isEmpty() || _month.isEmpty() || _date.isEmpty() || _hour.isEmpty() || _minute.isEmpty() || _second.isEmpty() || _millisecond.isEmpty())
			throw new Exception("参数不可以为空字串");
		if(_year.length()!= 4)
			throw new Exception("年份参数不正确");
		if(_month.length() != 2 || Integer.valueOf(_month) > 12 || Integer.valueOf(_month) < 1)
			throw new Exception("月份参数不正确");
		if(_date.length() != 2 || Integer.valueOf(_date) > 31 || Integer.valueOf(_date) < 1)
			throw new Exception("日期参数不正确");
		if(_hour.length() != 2 || Integer.valueOf(_hour) > 23 || Integer.valueOf(_hour) < 0)
			throw new Exception("小时参数不正确");
		if(_minute.length() != 2 || Integer.valueOf(_minute) > 59 || Integer.valueOf(_minute) < 0)
			throw new Exception("分钟参数不正确");
		if(_second.length() != 2 || Integer.valueOf(_second) > 59 || Integer.valueOf(_second) < 0)
			throw new Exception("秒参数不正确");
		if(_millisecond.length() != 3 || Integer.valueOf(_millisecond) > 999 || Integer.valueOf(_millisecond) < 0)
			throw new Exception("毫秒参数不正确");
		
		
		String formatStr = "";
		if(_month.length() == 1)
			_month = "0" + _month;
		if(_date.length() == 1)
			_date = "0" + _date;
		if(_hour.length() == 1)
			_hour = "0" + _hour;
		if(_minute.length() == 1)
			_minute = "0" + _minute;
		if(_second.length() == 1)
			_second = "0" + _second;
		if(_millisecond.length() == 0)
			_millisecond = "000";
		else if(_millisecond.length() == 1)
			_millisecond = "00"+_millisecond;
		else if(_millisecond.length() == 2)
			_millisecond = "0"+_millisecond;
		else if(_millisecond.length() > 3)
			_millisecond = _millisecond.substring(0, 3);
		
		formatStr = _year+_month+_date+_hour+_minute+_second+_millisecond;
		
		return formatStr;
	}
	
	
	
	
	public static Timestamp getOrigin(){
		Calendar c = Calendar.getInstance();
		c.set(1, 0, 1, 0, 0, 0);
		Timestamp tm = new Timestamp(c.getTimeInMillis());
		return tm;
	}
	
	/**
	 * 获取活动状态
	 * @param begintime
	 * @param endtime
	 * @return//1:未开始 2 进行中 3 已结束
	 */
	public static long getActivityStatus(String begintime,String endtime){
		//begintime="1997-10-10 11:11:11";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date=new Date();
		try {
			Date btime = sdf.parse(begintime.split("\\.")[0]);
			int compareTo = btime.compareTo(date);
			if(compareTo>0){
				return 1L;
			}else{
				Date etime = sdf.parse(endtime.split("\\.")[0]);
				int compareTo1 = etime.compareTo(date);
				if(compareTo1>0){
					return 2L;
				}else{
					return 3L;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
