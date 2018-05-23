package com.tsb.ischool.framework.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tsb.ischool.framework.common.ISchoolConstants;


public class AccessStatisticsIntceptor implements HandlerInterceptor {
	
	private static Logger logger= Logger.getLogger(AccessStatisticsIntceptor.class);
	
	
	@Override
	public void afterCompletion(HttpServletRequest arg0,HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		System.out.println("完成处理请求");
		String opration = ISchoolConstants.LOGGER_PREFIX_DEBUG + "THREADID = "+Thread.currentThread().getId() + ".|afterCompletion.";
		logger.debug(opration);
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		System.out.println("处理请求");
		String opration = ISchoolConstants.LOGGER_PREFIX_DEBUG + "THREADID = "+Thread.currentThread().getId() + ".|postHandle.";
		logger.debug(opration);
		
	}

	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2) throws Exception {
		System.out.println("准备处理请求");
		String opration = ISchoolConstants.LOGGER_PREFIX_DEBUG + "THREADID = "+Thread.currentThread().getId() + ".|preHandle.";
		logger.debug(opration);
		return true;
	}

}
