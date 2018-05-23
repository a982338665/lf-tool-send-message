package com.tsb.ischool.framework.dao.cacheredis.impl;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.tsb.ischool.framework.bean.comm.RedisWrapperBean;
import com.tsb.ischool.framework.dao.cacheredis.ICacheRedisDao;


@Repository("cacheRedisDao")
public class CacheRedisDaoImpl implements ICacheRedisDao {
	private Logger logger = Logger.getLogger(CacheRedisDaoImpl.class);
	@Autowired
	protected RedisTemplate<Serializable, Serializable> redisTemplate;

	@Override
	public void saveCacheInfo(String key, String jsonstr, int minute) {
		try {
			redisTemplate.opsForValue().set("CACHE_" + key, jsonstr);
			redisTemplate.expire("CACHE_" + key, minute, TimeUnit.MINUTES);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getCacheInfo(String key) {
		logger.debug("=======fromcache========key=>" + key);
		String jsonstr = (String) redisTemplate.opsForValue().get("CACHE_" + key);
		return jsonstr;

	}

	@Override
	public void clearCache(String key) {
		Set<Serializable> set = redisTemplate.keys("CACHE_" + key);
		redisTemplate.delete(set);
	}

	@Override
	public void saveCacheInfo2(String key, Serializable obj, int minute) {
		try {
			redisTemplate.setValueSerializer(new org.springframework.data.redis.serializer.JdkSerializationRedisSerializer());
			redisTemplate.opsForValue().set("CACHE_" + key, obj);
			redisTemplate.expire("CACHE_" + key, minute, TimeUnit.MINUTES);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Serializable getCacheInfo2(String key) {
		redisTemplate.setValueSerializer(new org.springframework.data.redis.serializer.JdkSerializationRedisSerializer());
		Serializable obj = redisTemplate.opsForValue().get("CACHE_" + key);
		return obj;
	}
	
	@Override
	public void saveWtihWrapper(String key, Object bean, int minute) {
		try {
			RedisWrapperBean wrapperBean = new RedisWrapperBean();
			wrapperBean.setBean(bean);
			redisTemplate.setValueSerializer(new org.springframework.data.redis.serializer.JdkSerializationRedisSerializer());
			redisTemplate.opsForValue().set("CACHE_" + key, wrapperBean);
			redisTemplate.expire("CACHE_" + key, minute, TimeUnit.MINUTES);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public Object getWtihWrapper(String key) {
		redisTemplate.setValueSerializer(new org.springframework.data.redis.serializer.JdkSerializationRedisSerializer());
		Serializable obj = redisTemplate.opsForValue().get("CACHE_" + key);
		if(obj==null){
			return null;
		}
		RedisWrapperBean result = (RedisWrapperBean) obj;
		return result.getBean();
	}
	
	@Override
	public String getToken(String phone) {
		String yanzhengma=(String) redisTemplate.opsForValue().get("DXYZ_"+phone);
		return yanzhengma;
	}

	@Override
	public void deleteToken(String phone){
		String key = "DXYZ_"+phone;
		Set<Serializable> set = redisTemplate.keys(key);
		redisTemplate.delete(set);
	}
	
}
