/**
 * 版权所有：版权所有(C) 2016，学酷网络 
 * 文件名称: SendMsgLogDaoImpl.java 
 * 设计作者: 
 * 完成日期: 
 * 内容摘要:
 *
 * 修改记录: 
 * 修改日期:
 * 修 改 人:
 * 修改内容:
 */
package com.tsb.ischool.msglog.dao.impl;

import org.springframework.stereotype.Repository;
import com.tsb.ischool.msglog.dao.ISendMsgLogDao;
import com.tsb.ischool.msglog.model.SendMsgLog;
import com.tsb.ischool.msglog.webservice.c2sbean.C2SSendMsgLog;
import com.tsb.ischool.framework.bean.comm.PageBean;
import com.tsb.ischool.framework.common.ISchoolConstants;
import com.tsb.ischool.framework.dao.MybatisDao;
import org.apache.log4j.Logger;

/**
 * 类 名 称：SendMsgLogDaoImpl.java
 * 内容摘要：
 * 完成日期：
 * 编码作者：
 */
@Repository("sendMsgLogDao")
public class SendMsgLogDaoImpl extends MybatisDao implements ISendMsgLogDao
{
    private static Logger logger = Logger.getLogger(SendMsgLogDaoImpl.class);

	@Override
	public int insert(SendMsgLog bean) {
		String operation =ISchoolConstants.LOGGER_PREFIX_INFO+ "THREADID = "+Thread.currentThread().getId()+".|Mapper-method:";
		String method = "SendMsgLog.insert";
		logger.info(operation + method + ".|");
		return insert(method, bean);
	}

	@Override
	public int update(SendMsgLog bean) {
		String operation =ISchoolConstants.LOGGER_PREFIX_INFO+ "THREADID = "+Thread.currentThread().getId()+".|Mapper-method:";
		String method = "SendMsgLog.update";
		logger.info(operation + method + ".|");
		return update(method, bean);
	}

	@Override
	public SendMsgLog queryById(String pkid) {
		String operation =ISchoolConstants.LOGGER_PREFIX_INFO+ "THREADID = "+Thread.currentThread().getId()+".|Mapper-method:";
		String method = "SendMsgLog.queryById";
		logger.info(operation + method + ".|");
		return (SendMsgLog)selectOne(method, pkid);
	}

	@Override
	public int deleteById(String pkid) {
		String operation =ISchoolConstants.LOGGER_PREFIX_INFO+ "THREADID = "+Thread.currentThread().getId()+".|Mapper-method:";
		String method = "SendMsgLog.deleteById";
		logger.info(operation + method + ".|");
		return delete(method, pkid);
	}

	@Override
	public int deletelogicById(String pkid) {
		String operation =ISchoolConstants.LOGGER_PREFIX_INFO+ "THREADID = "+Thread.currentThread().getId()+".|Mapper-method:";
		String method = "SendMsgLog.deletelogicById";
		logger.info(operation + method + ".|");
		return update(method, pkid);
	}

	@Override
	public PageBean<SendMsgLog> query(C2SSendMsgLog bean) {
		String operation =ISchoolConstants.LOGGER_PREFIX_INFO+ "THREADID = "+Thread.currentThread().getId()+".|Mapper-method:";
		String method = "SendMsgLog.query";
		logger.info(operation + method + ".|");
		return selectPage(method, bean, bean.getCurpage(), bean.getPagesize());
	}

	@Override
	public int insertSequence(SendMsgLog bean) {
		String operation =ISchoolConstants.LOGGER_PREFIX_INFO+ "THREADID = "+Thread.currentThread().getId()+".|Mapper-method:";
		String method = "SendMsgLog.insertSequence";
		logger.info(operation + method + ".|");
		return insert(method, bean);
	}

}