package com.tsb.ischool.framework.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.tsb.ischool.framework.common.ISchoolConstants;

/**
 * 为松耦合mybatisdao而用
 * 
 * @author hpx
 * @version 创建时间：2014-12-3 下午3:30:01
 */
@Component
public class MybatisDaoTool extends MybatisDao {
	private Logger logger = Logger.getLogger(getClass());

	public void logMethod(String method) {
		String operation = ISchoolConstants.LOGGER_PREFIX_INFO + "THREADID = " + Thread.currentThread().getId() + ".|Mapper-method:";
		logger.info(operation + method + ".|");
	}

}
