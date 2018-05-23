package com.tsb.ischool.msglog.dao.impl;


import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.tsb.ischool.framework.common.ISchoolConstants;
import com.tsb.ischool.framework.dao.MybatisDao;
import com.tsb.ischool.msglog.dao.ISendMsgServiceDao;
import com.tsb.ischool.msglog.model.SendMsgServiceBean;

@Repository("sendMsgServiceDao")
public class SendMsgServiceDaoImpl  extends MybatisDao implements ISendMsgServiceDao {
	  private static Logger logger = Logger.getLogger(SendMsgLogDaoImpl.class);

		@Override
		public SendMsgServiceBean getUserTokenByXkId(String xkSendUserId) {
			String operation =ISchoolConstants.LOGGER_PREFIX_INFO+ "THREADID = "+Thread.currentThread().getId()+".|Mapper-method:";
			String method = "SendMsg.getUserTokenByXkId";
			logger.info(operation + method + ".|");
			return (SendMsgServiceBean) selectOne(method, xkSendUserId);
		}
		
		@Override
		public List<Map<String,Object>>  getLcIdByXkId(String sid) {
			String operation =ISchoolConstants.LOGGER_PREFIX_INFO+ "THREADID = "+Thread.currentThread().getId()+".|Mapper-method:";
			String method = "SendMsg.getLcIdByXkId";
			logger.info(operation + method + ".|");
			return (List<Map<String, Object>>) selectList(method, sid);

		}

	@Override
	public String getLcIdByXkIdStu(String sid) {
		String operation =ISchoolConstants.LOGGER_PREFIX_INFO+ "THREADID = "+Thread.currentThread().getId()+".|Mapper-method:";
		String method = "SendMsg.getLcIdByXkIdStu";
		logger.info(operation + method + ".|");
		return String.valueOf(selectOne(method, sid));
	}

}
