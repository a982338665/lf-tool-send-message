/**
 * 版权所有：版权所有(C) 2016，学酷网络 
 * 文件名称: C2SSendMsgLog.java 
 * 设计作者: 
 * 完成日期: 
 * 内容摘要:
 *
 * 修改记录: 
 * 修改日期:
 * 修 改 人:
 * 修改内容:
 */
package com.tsb.ischool.msglog.webservice.c2sbean;

import java.io.Serializable;
import com.tsb.ischool.framework.common.ErrorCode;
import com.tsb.ischool.framework.exception.ISchoolException;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
* 类 名 称：C2SSendMsgLog
* 内容摘要：
* 完成日期：
* 编码作者：
*/
public class C2SSendMsgLog implements Serializable 
{

    /** 
     *PRIMARY KEY,对应表[SEND_MSG_LOG]的主键id
     */
    private String id;//主键

    /** 
     *对应表[SEND_MSG_LOG]的字段mobile
     */
	private String mobile;//

    /** 
     *对应表[SEND_MSG_LOG]的字段context_ws
     */
	private String contextWs;//

    /** 
     *对应表[SEND_MSG_LOG]的字段send_status
     */
	private Integer sendStatus;//0=初始；1=发送成功
	
	/**
	 * 对应表[SEND_MSG_LOG]的字段sequence_id 发送序列号
	 */
	private Integer sequenceId;

    private int curpage;//当前页码
	private int pagesize;//每页个数
	
	public boolean verify() throws ISchoolException {
		if (null == this) {
			throw new ISchoolException(ErrorCode.ISCHOOL_REQBODY_INVALID_MSGTYPE, "参数错误，入参为空。");
		}
		return true;
	}
	
	public Integer getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}

	public int getCurpage() {
		return curpage;
	}

	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}





  
    
  /**
	*
	* @return Long
	*/
	public String getId() 
	{
		return this.id;
	}
	
  /**
	* Set the id
	*/	
	public void setId(String aValue) 
	{
		this.id = aValue;
	}	
  /**
	*
	* @return String
	*/
	public String getMobile() 
	{
		return this.mobile;
	}
	
  /**
	* Set the mobile
	*/	
	public void setMobile(String aValue) 
	{
		this.mobile = aValue;
	}	
  /**
	*
	* @return String
	*/
	public String getContextWs() 
	{
		return this.contextWs;
	}
	
  /**
	* Set the contextWs
	*/	
	public void setContextWs(String aValue) 
	{
		this.contextWs = aValue;
	}	
  /**
	*
	* @return Integer
	*/
	public Integer getSendStatus() 
	{
		return this.sendStatus;
	}
	
  /**
	* Set the sendStatus
	*/	
	public void setSendStatus(Integer aValue) 
	{
		this.sendStatus = aValue;
	}	
  
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
				.append("id", this.id) 
				.append("mobile", this.mobile) 
				.append("contextWs", this.contextWs) 
				.append("sendStatus", this.sendStatus) 
				.append("curpage", this.curpage).append("pagesize", this.pagesize) 
		.toString();
	}

   /**
	 * Return the name of the first key column
	 */
	public String getFirstKeyColumnName() 
	{
		return "id";
	}
	
}