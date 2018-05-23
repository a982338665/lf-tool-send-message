package com.tsb.ischool.utils;

import java.util.UUID;

/**
 * @function :
 * @author :Administrator
 * @company :天士博
 * @date :2011-4-29
 */
public class UuidUtil {
	/**
	 * 获得一个UUID
	 * 
	 * @return String UUID
	 */
	public static String generateUUID() {
		String s = UUID.randomUUID().toString();
		// 去掉“-”符号
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
	}

}
