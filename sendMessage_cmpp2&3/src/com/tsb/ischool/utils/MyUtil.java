package com.tsb.ischool.utils;


/**
 * 字符串操作工具类
 * @author   ：wangyan
 * @version  ：1.0
 * @company  :
 * @date     :2012-06-20
 */
public class MyUtil {
	
	
	
	/**
	 * 从字符串右侧查找分割字符串进行拆分
	 * @param org
	 * 				源字符串
	 * @param str
	 * 				分割字符串
	 * @return
	 * 				字符串数组
	 */
	public static String[] splitFromLastIndexOf(String org ,String str){
		String[] arr = new String[2];
		int index = org.lastIndexOf(str);
		if(index > 0){
			arr[0] = org.substring(0, index);
			arr[1] = org.substring(index+1, org.length());
		}
		return arr;
	}
	
	/**
	 * 从字符串指定位置进行拆分
	 * @param org
	 * 				源字符串
	 * @param index
	 * 				拆分位置
	 * @return
	 * 				字符串数组
	 */
	public static String[] splitFromLastIndexOf(String org ,int index){
		String[] arr = new String[2];		
		if(index > 0){
			arr[0] = org.substring(0, index);
			arr[1] = org.substring(index+1, org.length());
		}
		return arr;
	}
	
	/**
	 * 解析文件扩展名，对于扩展名前不加(.)的，加上(.)。对于有(.)的不做处理
	 * @param fileExtName
	 * 				文件扩展名
	 * @return
	 */
	public static String parseFileExt(String fileExtName){
		if(!fileExtName.startsWith("."))
			return "."+fileExtName;
		return fileExtName;
	}
	
	public static byte[] removeUTF8Bom(byte[] orgBytes){	

	    int n = 0;	    
	    for(int i=0;i<orgBytes.length;i++){
	      	if(orgBytes[i] == 0 && orgBytes[i+1] == 0 && orgBytes[i+2] == 0  && orgBytes[i+3] == 0 && orgBytes[i+4] == 0 && orgBytes[i+5] == 0 && orgBytes[i+6] == 6 && orgBytes[i+7] == 76){
	    		
	    		if(orgBytes[i+8] == -17 && orgBytes[i+9] == -69 && orgBytes[i+10] == -65){
		    		byte[] strs = new byte[orgBytes.length - i - 11];
		    		for(int j=i+11;j<orgBytes.length;j++){
		    			strs[n] = orgBytes[j];
		    			n++;
		    		}
		    		return strs;
		    	}
	    		else{
	    			byte[] strs = new byte[orgBytes.length - i - 8];
		    		for(int j=i+8;j<orgBytes.length;j++){
		    			strs[n] = orgBytes[j];
		    			n++;
		    		}
		    		return strs;
	    		}
	    		
	    	}
	      	else if(orgBytes[i] == -17 && orgBytes[i+1] == -69 && orgBytes[i+2] == -65){
	    		byte[] strs = new byte[orgBytes.length - i - 3];//去掉该内容的bom标示
	    		for(int j=i+3;j<orgBytes.length;j++){
	    			strs[n] = orgBytes[j];
	    			n++;
	    		}
	    		return strs;
	    	}
	  
	    }	    	
	    
	   return orgBytes;
		
	}
	
	/**
	 * 获取字符串的值，如果为空返回空串
	 * @param str
	 * @return
	 */
	public static String getString(String str) {
		return str == null ? "" : str;
	}
	
	/**
	 * 获取long值，如果为空返回默认值
	 * @param str
	 * @return
	 */
	public static long getlong(String str) {
		if(null == str || str.equals("")){
			return 0;
		}
		return Long.parseLong(str);
	}
	
	/**
	 * 获取long值，如果为空返回默认值
	 * @param str
	 * @return
	 */
	public static double getdouble(String str) {
		if(null == str || str.equals("")){
			return 0;
		}
		return Double.parseDouble(str);
	}
	
	/**
	 * 获取int值，如果为空返回默认值
	 * @param str
	 * @return
	 */
	public static int getint(String str) {
		if(null == str || str.equals("")){
			return 0;
		}
		return Integer.parseInt(str);
	}
	
	/**
	 * 获取Long类型变量的值，如果为空返回"0"
	 * @param obj
	 * @return
	 */
	public static String getLongString(Long obj) {
		return obj == null ? "0" : obj.toString();
	}
	
	/**
	 * 获取Double类型变量的值，如果为空返回"0"
	 * @param obj
	 * @return
	 */
	public static String getDoubleString(Double obj) {
		return obj == null ? "0" : obj.toString();
	}
	
	/**
	 * 获取Integer类型变量的值，如果为空返回"0"
	 * @param obj
	 * @return
	 */
	public static String getIntegerString(Long obj) {
		
		return obj == null ? "0" : obj.toString();
	}
	
	/**
	 * 判断一个数是否在与一个数据里
	 * @param args
	 * @param comp
	 * @return
	 */
	public static boolean isExist(long [] args,long comp) {
		if(args == null  && args.length == 0){
			return false;
		}
		for(long temp : args){
			if(comp == temp){
				return true;
			}
		}
		return false;
	}
	

}
