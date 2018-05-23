/**
 * 版权所有：版权所有(C) 2016，学酷网络 
 * 文件名称: tmpTubasicMsg.java 
 * 设计作者: 
 * 完成日期: 
 * 内容摘要:对应表[TMP_TUBASIC_MSG],映射配置文件为tmpTubasicMsg.xml 
 *
 * 修改记录: 
 * 修改日期:
 * 修 改 人:
 * 修改内容:
 */
package com.tsb.ischool.tmpmsg.model;

import java.io.Serializable;
import com.tsb.ischool.tmpmsg.model.tmpTubasicMsg;
import com.tsb.ischool.framework.common.ErrorCode;
import com.tsb.ischool.framework.exception.ISchoolException;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
* 类 名 称：tmpTubasicMsg
* 内容摘要：
* 完成日期：
* 编码作者：
*/
public class tmpTubasicMsg implements Serializable 
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

public boolean verify() throws ISchoolException {
		if (null == this) {
			throw new ISchoolException(ErrorCode.ISCHOOL_REQBODY_INVALID_MSGTYPE, "参数错误，入参为空。");
		}
		return true;
	}
	


  /**
	*
	* Default Empty Constructor for class tmpTubasicMsg
	*
	*/
	public tmpTubasicMsg() 
	{
		super();
	}
	
  /**
	*
	* Default All Fields Constructor for class tmpTubasicMsg
	*
	*/
    public tmpTubasicMsg (
		 String mobile
		,String contextWs
		,Integer sendStatus
        ) {
		this.setMobile(mobile);
		this.setContextWs(contextWs);
		this.setSendStatus(sendStatus);
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
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object)
	{
		if (!(object instanceof tmpTubasicMsg)) 
		{
			return false;
		}
		tmpTubasicMsg rhs = (tmpTubasicMsg) object;
		return new EqualsBuilder()
				.append(this.mobile, rhs.mobile)
				.append(this.contextWs, rhs.contextWs)
				.append(this.sendStatus, rhs.sendStatus)
				.isEquals();
	}
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
				.append(this.mobile) 
				.append(this.contextWs) 
				.append(this.sendStatus) 
				.toHashCode();
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