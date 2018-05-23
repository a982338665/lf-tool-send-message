 /**
  * 版权所有：版权所有(C) 2016，学酷网络  
 * 文件名称: ItmpTubasicMsgDAO.java 
 * 设计作者: 
 * 完成日期: 
 * 内容摘要:
 *
 * 修改记录: 
 * 修改日期:
 * 修 改 人:
 * 修改内容:
 */

package com.tsb.ischool.tmpmsg.dao;

import com.tsb.ischool.tmpmsg.model.tmpTubasicMsg;
import com.tsb.ischool.tmpmsg.webservice.c2sbean.C2StmpTubasicMsg;
import com.tsb.ischool.framework.bean.comm.PageBean;

/**
 * 类 名 称：ItmpTubasicMsgDao
 * 内容摘要：
 * 完成日期：
 * 编码作者：
 */
public interface ItmpTubasicMsgDao
{

 	/**
	 * 新增tmpTubasicMsg
	 * @param tmpTubasicMsg bean
	 * @return
	 */
	public int insert(tmpTubasicMsg bean);
	
	
	/**
	 * 编辑tmpTubasicMsg
	 * @param tmpTubasicMsg bean
	 * @return
	 */
	public int update(tmpTubasicMsg bean);
	
	/**
	 * 通过tmpTubasicMsg主键查询tmpTubasicMsg信息
	 * @param  mobile  tmpTubasicMsg主键
	 * @return tmpTubasicMsg
	 */
	public tmpTubasicMsg queryById(String mobile);
	
	/**
	 * 删除tmpTubasicMsg通过主键
	 * @param   mobile  tmpTubasicMsg主键
	 * @return
	 */
	public int deleteById(String mobile);
	
	/**
	 * 逻辑删除tmpTubasicMsg通过主键
	 * @param   mobile  tmpTubasicMsg主键
	 * @return
	 */
	public int deletelogicById(String mobile);
	
	/**
	 * 查询tmpTubasicMsg
	 * @param C2StmpTubasicMsg bean
	 * @return
	 */
	public PageBean<tmpTubasicMsg> query(C2StmpTubasicMsg bean);
}
