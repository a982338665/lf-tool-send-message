/**
 * 版权所有：版权所有(C) 2016，学酷网络 
 * 文件名称: ItmpTubasicMsgService.java 
 * 设计作者: 
 * 完成日期: 
 * 内容摘要:
 *
 * 修改记录: 
 * 修改日期:
 * 修 改 人:
 * 修改内容:
 */
package com.tsb.ischool.tmpmsg.service;

import com.tsb.ischool.tmpmsg.model.tmpTubasicMsg;
import com.tsb.ischool.tmpmsg.webservice.c2sbean.C2StmpTubasicMsg;
import com.tsb.ischool.framework.bean.comm.PageBean;

/**
 * 类 编 号： 
 * 类 名 称：ItmpTubasicMsgService
 * 内容摘要：
 * 完成日期：
 * 编码作者：
 */
public interface ItmpTubasicMsgService
{
 
    /**
	 * 新增tmpTubasicMsg
	 * @param tmpTubasicMsg
	 * @return
	 */
	public int insert(tmpTubasicMsg bean);
	
	
	/**
	 * 编辑tmpTubasicMsg
	 * @param tmpTubasicMsg
	 * @return
	 */
	public int update(tmpTubasicMsg bean);
	
	/**
	 * 获取tmpTubasicMsg通过主键
	 * @param pkid
	 * @return
	 */
	public tmpTubasicMsg queryById(String pkid);
	
	/**
	 * 删除tmpTubasicMsg通过主键
	 * @param tmpTubasicMsg
	 * @return
	 */
	public int deleteById(String pkid);
	
	/**
	 * 逻辑删除tmpTubasicMsg通过主键
	 * @param tmpTubasicMsg
	 * @return
	 */
	public int deletelogicById(String pkid);
	
	/**
	 * 查询tmpTubasicMsg
	 * @param C2StmpTubasicMsg
	 * @return
	 */
	public PageBean<tmpTubasicMsg> query(C2StmpTubasicMsg bean);
 	
}
