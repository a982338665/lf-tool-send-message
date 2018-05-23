package com.tsb.ischool.utils;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.UUID;
/**
 * 字符串工具类
 * 
 * @author tzh
 * @version 1.0 
 * @since 1.0 
 * */
public class StringUtil {
	
	private Calendar calendar = null;
	
	
	/**
	 * 构造函数，初始化时间发生器（无参数：当前时间）
	 */
	public StringUtil() {
		calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
	}

	
    // 获得当前日期与本周一相差的天数
    public static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期一是第二天......
        // 如果是星期日 返回1 如果是星期1 返回 2，按照中国人的习惯要减1 cd.get(Calendar.DAY_OF_WEEK)
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 0) {
            return -6;
        } else {
            return 1 - dayOfWeek;
        }
    }

    // 获得本周星期一的日期
    public static String getCurrMonday() {
        int mondayPlus = getMondayPlus();

        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currMonday = sdf.format(monday);
        return currMonday;
    }

    // 获得本周星期日的日期
    public static String getCurrSunday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        Date sunday = currentDate.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currSunday = sdf.format(sunday);
        return currSunday;
    }

    // 获得当天的日期
    public static String getCurrDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currDate = sdf.format(date);
        return currDate;
    }
    
    /**
     * [简要描述]:获得当天的日期,格式yyyyMMdd<br/>
     * [详细描述]:<br/>
     * 
     * @return
     * @exception 
     */
    public static String getCurrDateStr() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currDate = sdf.format(date);
        return currDate;
    }

    // 获得上周星期一的日期
    public static String getPrevMonday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus - 7);
        Date monday = currentDate.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String prevMonday = sdf.format(monday);
        return prevMonday;
    }

    // 获得上周星期日的日期
    public static String getPrevSunday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus - 1);
        Date sunday = currentDate.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String prevSunday = sdf.format(sunday);
        return prevSunday;
    }

    // 获取当月第一天
    public static String getCurrMonthFirstDay() {

        Calendar firstDate = Calendar.getInstance();
        firstDate.set(Calendar.DATE, 1);// 设为当前月的1号
        Date first = firstDate.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currMonthFirstDay = sdf.format(first);
        return currMonthFirstDay;
    }

    // 获取当月最后一天
    public static String getCurrMonthLastDay() {

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
        Date last = lastDate.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currMonthLastDay = sdf.format(last);
        return currMonthLastDay;
    }

    // 获取上月第一天
    public static String getPrevMonthFirstDay() {

        Calendar firstDate = Calendar.getInstance();
        firstDate.set(Calendar.DATE, 1);// 设为当前月的1号
        firstDate.add(Calendar.MONTH, -1);// 减一个月，变为上月的1号
        Date first = firstDate.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String prevMonthFirstDay = sdf.format(first);
        return prevMonthFirstDay;
    }

    // 获得上月最后一天
    public static String getPrevMonthLastDay() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.add(Calendar.DATE, -1);// 减去一天，变为上月最后一天
        Date last = lastDate.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String prevMonthLastDay = sdf.format(last);
        return prevMonthLastDay;
    }
    
    // 获得当前日期是星期几
    public static String getWeek() {
    	
    	String week = "";
    	 
    	Calendar cd = Calendar.getInstance();
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        
        if(dayOfWeek == Calendar.SUNDAY) {
        	week = "周日";
        } else if(dayOfWeek == Calendar.MONDAY) {
        	week = "周一";
        } else if(dayOfWeek == Calendar.TUESDAY) {
        	week = "周二";
        } else if(dayOfWeek == Calendar.WEDNESDAY) {
        	week = "周三";
        } else if(dayOfWeek == Calendar.THURSDAY) {
        	week = "周四";
        } else if(dayOfWeek == Calendar.FRIDAY) {
        	week = "周五";
        } else if(dayOfWeek == Calendar.SATURDAY) {
        	week = "周六";
        }
        return week;
    }
    
    /**
     * 空字符处理
     * 
     * @param value
     * @return
     */
	public static String null2Blank(String value) {
	    return value == null ? "" : value;
	}
	
	 /**
     * 空数字
     * 
     * @param value
     * @return
     */
	public static long null2Zero(Object value) {
	    return value == null ? 0 : Long.valueOf(value.toString());
	}
	
    /**
     * 空字符判断
     * 
     * @param value
     * @return
     */
	public static boolean isNullOrBlank(String value) {
	    return (value == null || "".equals(value)) ? true : false;
	}
	
	/**
	 * 产生验证码
	 * 
	 * @param length
	 * @return
	 */
	public static String retrieveRandomNumber(int length) {

		/*String result = "";
		try {
			SecureRandom r = SecureRandom.getInstance("SHA1PRNG");
			byte[] seed = r.generateSeed(20);
			r.setSeed(seed);
			byte[] bytes = new byte[length];
			r.nextBytes(bytes);
			for (int i = 0; i < bytes.length; i++) {
				byte value = bytes[i];
				result += String.valueOf(Math.abs(value) % 10);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}*/

		return Math.abs(Math.round(Math.random()*(Math.pow(10, length-1)*9-1)+(Math.pow(10, length-1))))+"";
	}
	
    /**
	 * 获取一个六位随机数
	 * 
	 * @param length
	 * @return
	 */
	public static String getCharAndNumr(int length) {
		String val = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			String charOrNum = random.nextInt(2) % 2 != 0 ? "num" : "char";
			if ("char".equalsIgnoreCase(charOrNum)) {
				int choice = random.nextInt(2) % 2 != 0 ? 97 : 65;
				val = (new StringBuilder(String.valueOf(val))).append(
						(char) (choice + random.nextInt(26))).toString();
			} else if ("num".equalsIgnoreCase(charOrNum))
				val = (new StringBuilder(String.valueOf(val))).append(
						String.valueOf(random.nextInt(10))).toString();
		}
		return val.toLowerCase();
	}
    /**
     * 获取时间戳
     * Jul 6, 2011
     * @return
     * 
     * String
     */
    public static String getTimeStamp(String format){
        SimpleDateFormat dateFm = new SimpleDateFormat(format); //格式化当前系统日期
        return dateFm.format(new java.util.Date());
    }
	/**
     * 获取32位的字符类型的随机值
     * @return 32位的字符
     * Author : 邹建松
	 * Date   : 2013-12-26
     */
	public static String getRandomStrId() {
		UUID uuid = UUID.randomUUID();
		String tempId = uuid.toString().replace("-", "");
		return tempId;
	}
	/**
	 * 判断字符串是否为空
	 * 
	 * @param string 设置字符串
	 * @return boolean 返回是否为空
	 */
	public static boolean isEmpty(String string) {
		return string == null || string.length() == 0;
	}
	
	public static boolean isNotEmpty(String string) {
		return string != null && string.length() != 0;
	}
	
	//按字节大小截取字符串
	public static String subStr(String str,int len) {
		if(str!=null && str.getBytes().length>len){
			byte[] newValArr = new byte[len];
			System.arraycopy(str.getBytes(), 0, newValArr, 0, newValArr.length);
			str = new String(newValArr);
		}
		return str;
	}
	
	/**
	 * 根据对象数组拼接对象数组字符串
	 * @param  objArr         对象数组
	 * @param  splitAfterStr  前拼接字符
	 * @param  splitLastStr   后拼接字符
	 * @return reStr          返回对象
	 * @author 邹建松 2014-07-14
	 */
	public static String concatObjArr(Object[] objArr,String splitAfterStr,String splitLastStr){
		String reStr = "";
		if (splitAfterStr==null){
			splitAfterStr = " ";
		}
		if (splitLastStr==null){
			splitLastStr = " ";
		}
		if(objArr!=null && objArr.length>0){
			for(int i=0;i<objArr.length;i++){
				reStr += splitAfterStr+objArr[i]+splitLastStr;
			}
		}
		return reStr;
	}
	
	/**
	 * 根据对象列表拼接对象数组字符串
	 * @param  li          对象列表
	 * @param  replaceStr  替换字符
	 * @param  splitStr    分割字符
	 * @return reStr       返回对象
	 * @author 邹建松 2014-10-14
	 */
	public static String replaceObjArrToStr(List<Object> li,String replaceStr,String splitStr){
		String reStr = "";
		for(int i=0;i<li.size();i++){
			reStr+=(replaceStr+splitStr);
		}
		if(StringUtil.isNotEmpty(reStr)){
			reStr = reStr.substring(0,reStr.length()-splitStr.length());
		}
		return reStr;
	}
	
	
	
	
	
	/**
	 * 通用方法-解决字符串回车换行问题
	 * @param  str    处理的字符串对象
	 * @return String 返回对象
	 * @author 邹建松 2014-07-14
	 */	
	public static String jointString(String str) {
		StringBuffer buf = new StringBuffer();
		for (StringTokenizer st = new StringTokenizer(str != null ? str : "","\n", false); st.hasMoreTokens(); buf.append(st.nextToken().trim()));
		return buf.toString();
	}
	
	/**
	 * 递归将列表生成树节点对象
	 * @param  tempList    	节点列表
	 * @param  topObj      	根节点对象
	 * @param  idName      	id对象名称
	 * @param  parentIdName parentId对象名称
	 * @param  childrenName children对象名称
	 * @return Map<String,Object>   返回对象
	 * @author 邹建松 2014-08-09
	 */
	@SuppressWarnings("unchecked")
	private static Map<String,Object> treeWork(List<Map<String,Object>> tempList,Map<String,Object> topObj,String idName,String parentIdName,String childrenName){
		Map<String,Object> mapObj = null;
		List<Map<String,Object>> objList = null;
		String parentId = topObj.get(idName).toString();
		List<Map<String,Object>> temp2List = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> temp3List = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < tempList.size(); i++) {
			mapObj = tempList.get(i);
			if (mapObj.get(parentIdName).equals(parentId)) {
				if (topObj.containsKey(childrenName)) {
					((List<Map<String,Object>>)topObj.get(childrenName)).add(mapObj);
				} else {
					objList = new ArrayList<Map<String,Object>>();
					objList.add(mapObj);
					topObj.put(childrenName, objList);
				}
				temp2List.add(mapObj);
				topObj.put("leaf", false);
			} else {
				temp3List.add(mapObj);
			}
		}

		for (int i = 0; i < temp2List.size(); i++) {
			mapObj = temp2List.get(i);
			treeWork(temp3List, mapObj, idName, parentIdName, childrenName);
		}
		return topObj;
	}
	
	/**
	 * 递归将列表生成树节点对象
	 * @param  tempList    	节点列表
	 * @param  topObj      	根节点对象
	 * @param  idName      	id对象名称
	 * @param  parentIdName parentId对象名称
	 * @param  childrenName children对象名称
	 * @return List<Map<String,Object>>    返回对象
	 * @author 邹建松 2014-08-09
	 */
	public static List<Map<String,Object>> getJsonArrForList(List<Map<String,Object>> objList,String idName,String parentIdName,String childrenName){
		Map<String,Object> obj = new HashMap<String,Object>();
		List<Map<String,Object>> topList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> tempList = new ArrayList<Map<String,Object>>();
		Map<String,Object> mapObj = null;
		List<Map<String,Object>> reObjList = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < objList.size(); i++) {
			obj = objList.get(i);
			obj.put("leaf", true);
			if (obj.get(parentIdName)==null||obj.get(parentIdName).equals("null")||obj.get(parentIdName).equals("")) {
				topList.add(obj);
			} else {
				tempList.add(obj);
			}
		}

		for (int i = 0; i < topList.size(); i++) {
			mapObj = topList.get(i);
			treeWork(tempList, mapObj, idName, parentIdName, childrenName);
			reObjList.add(mapObj);
		}
		return reObjList;
	}
	
	/**
	 * MD5的32位加密
	 * @param  inStr     对象
	 * @return String  	  返回对象
	 * @author 邹建松 2014-08-09
	 */
	public static String md5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		for (int i = 0; i < charArray.length; i++){
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16){
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	/**
	 * [简要描述]:处理时间字符串<br/>
	 * [详细描述]:<br/>
	 * 
	 * @param time 2014-11-14 17:04:58.0
	 * @return 2014-11-14 17:04:58
	 * @exception 
	 */
	public static String dealTimeStr(String time)
	{
	    // 对参数进行判断
	    if (isEmpty(time))
        {
            return time;
        }
	    else
	    {
            return time.substring(0, time.length() - 2);
        }
	}
	/**
	 * [简要描述]:通过","隔开的字符串获取正则表达式对象<br/>
	 * [详细描述]:<br/>
	 * 
	 * @param extStr ","隔开的字符串
	 * @param caseInsensitive 是否忽略大小写
	 * @return
	 * @exception 
	 */
	public static String getReg(String extStr,boolean caseInsensitive){
	    String[] arr = extStr.split(",");
	    String reStr = "";
	    for(int i=0;i<arr.length;i++){
	        if(i==arr.length-1){
	            reStr += "(.*\\"+arr[i]+")";
	        }else{
	            reStr += "(.*\\"+arr[i]+")|";
	        }
	    }
	    if(caseInsensitive){
	        return "^(?i)"+reStr+"$";
	    }
	    return "^"+reStr+"$";
	}
	
	public static int compare_date(String DATE1, String DATE2)
	{

		DateFormat df = new SimpleDateFormat("yyyyMMdd hh:mm");
		try
		{
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime())
			{
				//dt1 在dt2前
				return 1;
			} else if (dt1.getTime() < dt2.getTime())
			{
				//dt1在dt2后
				return -1;
			} else
			{
				return 0;
			}
		} catch (Exception exception)
		{
			exception.printStackTrace();
		}
		return 0;
	}

	// 获得分区
	public static String getPartition(String date)// 20010101
	{
		return "PARTITION(P" + date.substring(0, 6) + ")";
	}
	
	/**
	 * 取时间 hhmmss
	 * 
	 * @return String
	 */
	public String getShortTime() {
		String hours, minutes, seconds;
		if (this.getHour() < 10) {
			hours = "0" + this.getHour();
		} else {
			hours = String.valueOf(this.getHour());
		}

		if (this.getMinute() < 10) {
			minutes = "0" + this.getMinute();
		} else {
			minutes = String.valueOf(this.getMinute());
		}

		if (this.getSecond() < 10) {
			seconds = "0" + this.getSecond();
		} else {
			seconds = String.valueOf(this.getSecond());
		}

		return hours + minutes + seconds;
	}
	
	/**
	 * 取年 yyyy
	 * 
	 * @return int
	 */
	public int getYear() {
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 取月 mm
	 * 
	 * @return int
	 */
	public int getMonth() {
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 取日 dd
	 * 
	 * @return int
	 */
	public int getDay() {
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 取小时 hh
	 * 
	 * @return int
	 */
	public int getHour() {
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 取分钟 mm
	 * 
	 * @return int
	 */
	public int getMinute() {
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * 取秒钟 ss
	 * 
	 * @return int
	 */
	public int getSecond() {
		return calendar.get(Calendar.SECOND);
	}

	
}

