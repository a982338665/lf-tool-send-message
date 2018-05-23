package com.tsb.ischool.utils;

import java.sql.Timestamp;
import java.util.Map;

@SuppressWarnings("unchecked")
public class MapUtil {
	
	public static String getString(Map map,String key,boolean isNullException,boolean isLength0Exception){
		if(map==null){
			if(isNullException){
				throw new RuntimeException("The map is null.|Map "+map+".|key="+key);
			}else{
				return null;
			}
		}
		Object o = map.get(key);
		if(o==null){
			if(isNullException){
				throw new RuntimeException("No Object in the Map.|Map "+map+".|key="+key);
			}else{
				return null;
			}
		}else if(o.toString().trim().length()<1){
			if(isLength0Exception){
				throw new RuntimeException("Empty String in the Map.|Map "+map+".|key="+key);
			}else{
				return null;
			}
		}else{
			return o.toString().trim();
		}
	}
	
	public static Timestamp getTimestamp(Map map,String key,boolean isNullException){
		if(map==null){
			if(isNullException){
				throw new RuntimeException("The map is null.|Map "+map+".|key="+key);
			}else{
				return null;
			}
		}
		Object o = map.get(key);
		if(o==null){
			if(isNullException){
				throw new RuntimeException("No Object in the Map.|Map "+map+".|key="+key);
			}else{
				return null;
			}
		}else{
				return (Timestamp)o;
		}
	}
	
	public static long getLong(Map map,String key,boolean isNullException,boolean isLength0Exception){
		if(map==null){
			if(isNullException){
				throw new RuntimeException("The map is null.|Map "+map+".|key="+key);
			}else{
				return 0;
			}
		}
		Object o = map.get(key);
		if(o==null){
			if(isNullException){
				throw new RuntimeException("No Object in the Map.|Map "+map+".|key="+key);
			}else{
				return 0;
			}
		}else if(o.toString().trim().length()<1){
			if(isLength0Exception){
				throw new RuntimeException("Empty String in the Map.|Map "+map+".|key="+key);
			}else{
				return 0;
			}
		}else{
				return Long.valueOf(o.toString());
		}
	}

}
