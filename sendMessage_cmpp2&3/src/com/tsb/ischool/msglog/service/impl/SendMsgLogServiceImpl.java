/**
 * 版权所有：版权所有(C) 2016，学酷网络 
 * 文件名称: SendMsgLogServiceImpl.java 
 * 设计作者: 
 * 完成日期: 
 * 内容摘要:
 *
 * 修改记录: 
 * 修改日期:
 * 修 改 人:
 * 修改内容:
 */
package com.tsb.ischool.msglog.service.impl;

import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.tsb.ischool.msglog.dao.ISendMsgLogDao;
import com.tsb.ischool.msglog.model.SendMsgLog;
import com.tsb.ischool.msglog.service.ISendMsgLogService;
import com.tsb.ischool.msglog.webservice.c2sbean.C2SSendMsgLog;
import com.tsb.ischool.framework.bean.comm.PageBean;
import com.tsb.ischool.framework.common.ISchoolConstants;

/**
 * 类 编 号： 
 * 类 名 称：SendMsgLogServiceImpl
 * 内容摘要：
 * 完成日期：
 * 编码作者：
 */
@Service("sendMsgLogService")
public class SendMsgLogServiceImpl implements ISendMsgLogService
{
   private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Resource
	private ISendMsgLogDao sendMsgLogDao;
	
	
	@Override
	public int insert(SendMsgLog bean) {
		String operation = ISchoolConstants.LOGGER_PREFIX_DEBUG + "THREADID = "
				+ Thread.currentThread().getId() + ".|新增 短信日志SendMsgLog.|bean="
				+ bean.toString() + ".|";
		logger.debug(operation + "开始.|");
		int result =sendMsgLogDao.insert(bean);
		logger.debug(operation + "结束.|");
		return result;
	}
	
	@Override
	public int update(SendMsgLog bean) {
		String operation = ISchoolConstants.LOGGER_PREFIX_DEBUG + "THREADID = "
				+ Thread.currentThread().getId() + ".|修改短信日志SendMsgLog.|bean="
				+ bean.toString() + ".|";
		logger.debug(operation + "开始.|");
		int result = sendMsgLogDao.update(bean);
		logger.debug(operation + "结束.|");
		return result;
	}
	@Override
	public SendMsgLog queryById(String pkid) {
		String operation = ISchoolConstants.LOGGER_PREFIX_DEBUG + "THREADID = "
				+ Thread.currentThread().getId() + ".|按id查询短信日志SendMsgLog.|bean="
				+ pkid + ".|";
		logger.debug(operation + "开始.|");
		SendMsgLog sendMsgLog = sendMsgLogDao.queryById(pkid);
		logger.debug(operation + "结束.|");
		return sendMsgLog;
	}
	
	@Override
	public int deleteById(String pkid) {
		String operation = ISchoolConstants.LOGGER_PREFIX_DEBUG + "THREADID = "
				+ Thread.currentThread().getId() + ".|删除短信日志SendMsgLog.|bean="
				+ pkid + ".|";
		logger.debug(operation + "开始.|");
		int result = sendMsgLogDao.deleteById(pkid);
		logger.debug(operation + "结束.|");
		return result;
	}
	
	@Override
	public int deletelogicById(String pkid) {
		String operation = ISchoolConstants.LOGGER_PREFIX_DEBUG + "THREADID = "
				+ Thread.currentThread().getId() + ".|逻辑删除短信日志SendMsgLog.|bean="
				+ pkid + ".|";
		logger.debug(operation + "开始.|");
		int result = sendMsgLogDao.deletelogicById(pkid);
		logger.debug(operation + "结束.|");
		return result;
	}
	
	@Override
	public PageBean<SendMsgLog> query(C2SSendMsgLog bean) {
		String operation = ISchoolConstants.LOGGER_PREFIX_DEBUG + "THREADID = "
				+ Thread.currentThread().getId() + ".|查询短信日志SendMsgLog.|bean="
				+ bean.toString() + ".|";
		logger.debug(operation + "开始.|");
		PageBean<SendMsgLog> pageBean = sendMsgLogDao.query(bean);
		logger.debug(operation + "结束.|");
		return pageBean;
	}

	@Override
	public int insertSequence(SendMsgLog bean) {
		return sendMsgLogDao.insertSequence(bean);
	}
	
}
