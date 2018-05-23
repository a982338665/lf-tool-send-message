package com.tsb.ischool.utils;

import com.tsb.ischool.framework.common.PropertyUtil;

public class ConstantParam {

	public static final String APP_KEY 			=PropertyUtil.getProperty("app_key").trim();
	public static final String APP_ID 			=PropertyUtil.getProperty("app_id").trim();
	public static final String AUTH_URL 		=PropertyUtil.getProperty("auth_url").trim();
	public static final String IV 				=PropertyUtil.getProperty("iv").trim();


	
}
