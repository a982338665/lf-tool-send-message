package com.tsb.ischool.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class ClientIpUtil {
	/**
	 * 取客户端IP地址　如果有反向代理通过该方法获取真实ＩＰ
	 * @param request
	 * @return
	 */
    public static String getClientIP(HttpServletRequest request) {
		String ipAddress = request.getHeader("x-forwarded-for"); 
		if(StringUtils.isBlank(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)){
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if(StringUtils.isBlank(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)){
			ipAddress = request.getHeader("WL-Proxy-Client-IP"); 
		}
		if(StringUtils.isBlank(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)){
			ipAddress = request.getRemoteAddr(); 
		}
		return ipAddress;
    }
    
}
