/**
 * 版权所有：版权所有(C) 2016，学酷网络 
 * 文件名称: ISendMsgLogService.java 
 * 设计作者: 
 * 完成日期: 
 * 内容摘要:
 *
 * 修改记录: 
 * 修改日期:
 * 修 改 人:
 * 修改内容:
 */
package com.tsb.ischool.msglog.service;

import com.tsb.ischool.msglog.model.SendMsgLog;
import com.tsb.ischool.msglog.webservice.c2sbean.C2SSendMsgLog;
import com.tsb.ischool.framework.bean.comm.PageBean;

/**
 * 类 编 号： 
 * 类 名 称：ISendMsgLogService
 * 内容摘要：
 * 完成日期：
 * 编码作者：
 */
public interface ISendMsgLogService
{
 
    /**
	 * 新增SendMsgLog
	 * @param SendMsgLog
	 * @return
	 */
	public int insert(SendMsgLog bean);
	
	
	/**
	 * 编辑SendMsgLog
	 * @param SendMsgLog
	 * @return
	 */
	public int update(SendMsgLog bean);
	
	/**
	 * 获取SendMsgLog通过主键
	 * @param pkid
	 * @return
	 */
	public SendMsgLog queryById(String pkid);
	
	/**
	 * 删除SendMsgLog通过主键
	 * @param SendMsgLog
	 * @return
	 */
	public int deleteById(String pkid);
	
	/**
	 * 逻辑删除SendMsgLog通过主键
	 * @param SendMsgLog
	 * @return
	 */
	public int deletelogicById(String pkid);
	
	/**
	 * 查询SendMsgLog
	 * @param C2SSendMsgLog
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
