/**
 * 版权所有：版权所有(C) 2016，学酷网络 
 * 文件名称: C2StmpTubasicMsg.java 
 * 设计作者: 
 * 完成日期: 
 * 内容摘要:
 *
 * 修改记录: 
 * 修改日期:
 * 修 改 人:
 * 修改内容:
 */
package com.tsb.ischool.tmpmsg.webservice.c2sbean;

import java.io.Serializable;
import com.tsb.ischool.framework.common.ErrorCode;
import com.tsb.ischool.framework.exception.ISchoolException;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
* 类 名 称：C2StmpTubasicMsg
* 内容摘要：
* 完成日期：
* 编码作者：
*/
public class C2StmpTubasicMsg implements Serializable 
{

    /** 
     *PRIMARY KEY,对应表[TMP_TUBASIC_MSG]的主键mobile
     */
    private String mobile;//主键

    /** 
     *对应表[TMP_TUBASIC_MSG]的字段context_ws
     */
	private String contextWs;//

    /** 
     *对应表[TMP_TUBASIC_MSG]的字段send_status
     */
	private Integer sendStatus;//

    private int curpage;//当前页码
	private int pagesize;//每页个数
	
	public boolean verify() throws ISchoolException {
		if (null == this) {
			throw new ISchoolException(ErrorCode.ISCHOOL_REQBODY_INVALID_MSGTYPE, "参数错误，入参为空。");
		}
		return true;
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
		return "mobile";
	}
	
}