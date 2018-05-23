package com.tsb.ischool.utils;

import org.apache.log4j.Logger;

/**
 * 针对redis的key集中管理
 * @author shenziming
 * @date 2014-8-4
 * @version 1.0
 */
public class RedisKeysUtil {

	private static Logger logger = Logger.getLogger(RedisKeysUtil.class.getName());

	/**
	 * SNS内容Hash的key前缀
	 */
	public final static String REDIS_HASH_KEY_SNS_CONTENT_PREFIX = "T_SNS_CONTENT";
	
	/**
	 * 构造SNS内容Hash的key
	 * @param contentid
	 * @return
	 */
	public static String buildSnsContentHashKey(String contentid){
		return REDIS_HASH_KEY_SNS_CONTENT_PREFIX +":"+contentid;
	}
	
	
	
	
}
