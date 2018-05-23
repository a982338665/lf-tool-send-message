package com.tsb.ischool.utils;

import javax.annotation.Resource;

import com.tsb.ischool.framework.dao.cacheredis.ICacheRedisDao;



public class CacheRedisUtil {
	@Resource
	private static ICacheRedisDao cacheRedisDao;

	public static void save(String key, Object result) {
		cacheRedisDao.saveCacheInfo(key, JsonUtil.buildeJson(result), 5);
	}

	public static void save(String key, Object result, int minute) {
		cacheRedisDao.saveCacheInfo(key, JsonUtil.buildeJson(result), minute);
	}

	public static String get(String key) {
		return cacheRedisDao.getCacheInfo(key);
	}

	public static void deleteCache(String key) {
		cacheRedisDao.clearCache(key);
	}

}
