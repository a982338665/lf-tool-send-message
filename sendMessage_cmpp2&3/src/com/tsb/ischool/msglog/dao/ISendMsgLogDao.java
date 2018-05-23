 /**
  * 版权所有：版权所有(C) 2016，学酷网络  
 * 文件名称: ISendMsgLogDAO.java 
 * 设计作者: 
 * 完成日期: 
 * 内容摘要:
 *
 * 修改记录: 
 * 修改日期:
 * 修 改 人:
 * 修改内容:
 */

package com.tsb.ischool.msglog.dao;

import com.tsb.ischool.msglog.model.SendMsgLog;
import com.tsb.ischool.msglog.webservice.c2sbean.C2SSendMsgLog;
import com.tsb.ischool.framework.bean.comm.PageBean;

/**
 * 类 名 称：ISendMsgLogDao
 * 内容摘要：
 * 完成日期：
 * 编码作者：
 */
public interface ISendMsgLogDao
{

 	/**
	 * 新增SendMsgLog
	 * @param SendMsgLog bean
	 * @return
	 */
	public int insert(SendMsgLog bean);
	
	
	/**
	 * 编辑SendMsgLog
	 * @param SendMsgLog bean
	 * @return
	 */
	public int update(SendMsgLog bean);
	
	/**
	 * 通过SendMsgLog主键查询SendMsgLog信息
	 * @param  id  SendMsgLog主键
	 * @return SendMsgLog
	 */
	public SendMsgLog queryById(String id);
	
	/**
	 * 删除SendMsgLog通过主键
	 * @param   id  SendMsgLog主键
	 * @return
	 */
	public int deleteById(String id);
	
	/**
	 * 逻辑删除SendMsgLog通过主键
	 * @param   id  SendMsgLog主键
	 * @return
	 */
	public int deletelogicById(String id);
	
	/**
	 * 查询SendMsgLog
	 * @param C2SSendMsgLog bean
	 * @return
	 */
	public PageBean<SendMsgLog> query(C2SSendMsgLog bean);
	
	/**
	 * 新增cmpp返回的发送状态
	 * @param sequenceId
	 * @param returnResult
	 * @return
	 */
	public int insertSequence(SendMsgLog bean);
}
