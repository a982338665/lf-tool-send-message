/**
 * 版权所有：版权所有(C) 2016，学酷网络 
 * 文件名称: tmpTubasicMsgDaoImpl.java 
 * 设计作者: 
 * 完成日期: 
 * 内容摘要:
 *
 * 修改记录: 
 * 修改日期:
 * 修 改 人:
 * 修改内容:
 */
package com.tsb.ischool.tmpmsg.dao.impl;

import org.springframework.stereotype.Repository;
import com.tsb.ischool.tmpmsg.dao.ItmpTubasicMsgDao;
import com.tsb.ischool.tmpmsg.model.tmpTubasicMsg;
import com.tsb.ischool.tmpmsg.webservice.c2sbean.C2StmpTubasicMsg;
import com.tsb.ischool.framework.bean.comm.PageBean;
import com.tsb.ischool.framework.common.ISchoolConstants;
import com.tsb.ischool.framework.dao.MybatisDao;
import org.apache.log4j.Logger;

/**
 * 类 名 称：tmpTubasicMsgDaoImpl.java
 * 内容摘要：
 * 完成日期：
 * 编码作者：
 */
@Repository("tmpTubasicMsgDao")
public class tmpTubasicMsgDaoImpl extends MybatisDao implements ItmpTubasicMsgDao
{
    private static Logger logger = Logger.getLogger(tmpTubasicMsgDaoImpl.class);

	@Override
	public int insert(tmpTubasicMsg bean) {
		String operation =ISchoolConstants.LOGGER_PREFIX_INFO+ "THREADID = "+Thread.currentThread().getId()+".|Mapper-method:";
		String method = "tmpTubasicMsg.insert";
		logger.info(operation + method + ".|");
		return insert(method, bean);
	}

	@Override
	public int update(tmpTubasicMsg bean) {
		String operation =ISchoolConstants.LOGGER_PREFIX_INFO+ "THREADID = "+Thread.currentThread().getId()+".|Mapper-method:";
		String method = "tmpTubasicMsg.update";
		logger.info(operation + method + ".|");
		return update(method, bean);
	}

	@Override
	public tmpTubasicMsg queryById(String pkid) {
		String operation =ISchoolConstants.LOGGER_PREFIX_INFO+ "THREADID = "+Thread.currentThread().getId()+".|Mapper-method:";
		String method = "tmpTubasicMsg.queryById";
		logger.info(operation + method + ".|");
		return (tmpTubasicMsg)selectOne(method, pkid);
	}

	@Override
	public int deleteById(String pkid) {
		String operation =ISchoolConstants.LOGGER_PREFIX_INFO+ "THREADID = "+Thread.currentThread().getId()+".|Mapper-method:";
		String method = "tmpTubasicMsg.deleteById";
		logger.info(operation + method + ".|");
		return delete(method, pkid);
	}

	@Override
	public int deletelogicById(String pkid) {
		String operation =ISchoolConstants.LOGGER_PREFIX_INFO+ "THREADID = "+Thread.currentThread().getId()+".|Mapper-method:";
		String method = "tmpTubasicMsg.deletelogicById";
		logger.info(operation + method + ".|");
		return update(method, pkid);
	}

	@Override
	public PageBean<tmpTubasicMsg> query(C2StmpTubasicMsg bean) {
		String operation =ISchoolConstants.LOGGER_PREFIX_INFO+ "THREADID = "+Thread.currentThread().getId()+".|Mapper-method:";
		String method = "tmpTubasicMsg.query";
		logger.info(operation + method + ".|");
		return selectPage(method, bean, bean.getCurpage(), bean.getPagesize());
	}

}