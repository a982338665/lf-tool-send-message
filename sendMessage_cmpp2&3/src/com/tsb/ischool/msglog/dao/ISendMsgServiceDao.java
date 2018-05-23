package com.tsb.ischool.msglog.dao;

import java.util.List;
import java.util.Map;

import com.tsb.ischool.framework.common.ISchoolConstants;
import com.tsb.ischool.msglog.model.SendMsgServiceBean;

/**
 * 短信业务
 * @author admin
 *
 */
public interface ISendMsgServiceDao {

	/**
	 * 根据学酷id获取Token和openid
	 * @param xkSendUserId
	 * @return
	 */
	public SendMsgServiceBean getUserTokenByXkId(String xkSendUserId);
	/**
	 * 根据家长学酷id获取孩子联创id
	 * @param sid
	 * @return
	 */
	public  List<Map<String,Object>>  getLcIdByXkId(String sid);

    String getLcIdByXkIdStu(String sid);
}
