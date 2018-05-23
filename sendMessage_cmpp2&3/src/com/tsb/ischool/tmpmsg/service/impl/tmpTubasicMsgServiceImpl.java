/**
 * 版权所有：版权所有(C) 2016，学酷网络 
 * 文件名称: tmpTubasicMsgServiceImpl.java 
 * 设计作者: 
 * 完成日期: 
 * 内容摘要:
 *
 * 修改记录: 
 * 修改日期:
 * 修 改 人:
 * 修改内容:
 */
package com.tsb.ischool.tmpmsg.service.impl;

import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.tsb.ischool.tmpmsg.dao.ItmpTubasicMsgDao;
import com.tsb.ischool.tmpmsg.model.tmpTubasicMsg;
import com.tsb.ischool.tmpmsg.service.ItmpTubasicMsgService;
import com.tsb.ischool.tmpmsg.webservice.c2sbean.C2StmpTubasicMsg;
import com.tsb.ischool.framework.bean.comm.PageBean;
import com.tsb.ischool.framework.common.ISchoolConstants;

/**
 * 类 编 号： 
 * 类 名 称：tmpTubasicMsgServiceImpl
 * 内容摘要：
 * 完成日期：
 * 编码作者：
 */
@Service("tmpTubasicMsgService")
public class tmpTubasicMsgServiceImpl implements ItmpTubasicMsgService
{
   private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Resource
	private ItmpTubasicMsgDao tmpTubasicMsgDao;
	
	
	@Override
	public int insert(tmpTubasicMsg bean) {
		String operation = ISchoolConstants.LOGGER_PREFIX_DEBUG + "THREADID = "
				+ Thread.currentThread().getId() + ".|新增 甘肃临时发短信tmpTubasicMsg.|bean="
				+ bean.toString() + ".|";
		logger.debug(operation + "开始.|");
		int result =tmpTubasicMsgDao.insert(bean);
		logger.debug(operation + "结束.|");
		return result;
	}
	
	@Override
	public int update(tmpTubasicMsg bean) {
		String operation = ISchoolConstants.LOGGER_PREFIX_DEBUG + "THREADID = "
				+ Thread.currentThread().getId() + ".|修改甘肃临时发短信tmpTubasicMsg.|bean="
				+ bean.toString() + ".|";
		logger.debug(operation + "开始.|");
		int result = tmpTubasicMsgDao.update(bean);
		logger.debug(operation + "结束.|");
		return result;
	}
	@Override
	public tmpTubasicMsg queryById(String pkid) {
		String operation = ISchoolConstants.LOGGER_PREFIX_DEBUG + "THREADID = "
				+ Thread.currentThread().getId() + ".|按id查询甘肃临时发短信tmpTubasicMsg.|bean="
				+ pkid + ".|";
		logger.debug(operation + "开始.|");
		tmpTubasicMsg tmpTubasicMsg = tmpTubasicMsgDao.queryById(pkid);
		logger.debug(operation + "结束.|");
		return tmpTubasicMsg;
	}
	
	@Override
	public int deleteById(String pkid) {
		String operation = ISchoolConstants.LOGGER_PREFIX_DEBUG + "THREADID = "
				+ Thread.currentThread().getId() + ".|删除甘肃临时发短信tmpTubasicMsg.|bean="
				+ pkid + ".|";
		logger.debug(operation + "开始.|");
		int result = tmpTubasicMsgDao.deleteById(pkid);
		logger.debug(operation + "结束.|");
		return result;
	}
	
	@Override
	public int deletelogicById(String pkid) {
		String operation = ISchoolConstants.LOGGER_PREFIX_DEBUG + "THREADID = "
				+ Thread.currentThread().getId() + ".|逻辑删除甘肃临时发短信tmpTubasicMsg.|bean="
				+ pkid + ".|";
		logger.debug(operation + "开始.|");
		int result = tmpTubasicMsgDao.deletelogicById(pkid);
		logger.debug(operation + "结束.|");
		return result;
	}
	
	@Override
	public PageBean<tmpTubasicMsg> query(C2StmpTubasicMsg bean) {
		String operation = ISchoolConstants.LOGGER_PREFIX_DEBUG + "THREADID = "
				+ Thread.currentThread().getId() + ".|查询甘肃临时发短信tmpTubasicMsg.|bean="
				+ bean.toString() + ".|";
		logger.debug(operation + "开始.|");
		PageBean<tmpTubasicMsg> pageBean = tmpTubasicMsgDao.query(bean);
		logger.debug(operation + "结束.|");
		return pageBean;
	}
	
}
