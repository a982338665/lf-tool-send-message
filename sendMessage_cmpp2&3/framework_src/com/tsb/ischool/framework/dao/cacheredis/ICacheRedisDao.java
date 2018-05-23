package com.tsb.ischool.framework.dao.cacheredis;

import java.io.Serializable;

public interface ICacheRedisDao {

	public void saveCacheInfo(String key, String jsonstr, int minute);

	public String getCacheInfo(String key);

	public void clearCache(String key);

	void saveCacheInfo2(String key, Serializable obj, int minute);

	Serializable getCacheInfo2(String key);

	
	public void saveWtihWrapper(String key, Object obj, int minute);
	public Object getWtihWrapper(String key);
	
	
	public String getToken(String phone);
	
	
	public void deleteToken(String phone);
}