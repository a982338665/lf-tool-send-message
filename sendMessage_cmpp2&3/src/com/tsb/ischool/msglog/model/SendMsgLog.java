/**
 * 版权所有：版权所有(C) 2016，学酷网络 
 * 文件名称: SendMsgLog.java 
 * 设计作者: 
 * 完成日期: 
 * 内容摘要:对应表[SEND_MSG_LOG],映射配置文件为SendMsgLog.xml 
 *
 * 修改记录: 
 * 修改日期:
 * 修 改 人:
 * 修改内容:
 */
package com.tsb.ischool.msglog.model;

import java.io.Serializable;
import com.tsb.ischool.msglog.model.SendMsgLog;
import com.tsb.ischool.framework.common.ErrorCode;
import com.tsb.ischool.framework.exception.ISchoolException;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * 类 名 称：SendMsgLog 内容摘要： 完成日期： 编码作者：
 */
public class SendMsgLog implements Serializable {

	/**
	 * PRIMARY KEY,对应表[SEND_MSG_LOG]的主键id
	 */
	private String id;// 主键

	/**
	 * 对应表[SEND_MSG_LOG]的字段mobile
	 */
	private String mobile;//

	/**
	 * 对应表[SEND_MSG_LOG]的字段context_ws
	 */
	private String contextWs;//

	/**
	 * 对应表[SEND_MSG_LOG]的字段send_status
	 */
	private Integer sendStatus;// 0=初始；1=发送成功

	/**
	 * 对应表[SEND_MSG_LOG]的字段sequence_id 发送序列号
	 */
	private Integer sequenceId;

	/**
	 * 对应表[SEND_MSG_LOG]的字段return_result Unsigned Integer cmpp返回结果 <br>
	 * 0：正确 <br>
	 * 1：消息结构错 <br>
	 * 2：命令字错 <br>
	 * 3：消息序号重复 <br>
	 * 4：消息长度错 <br>
	 * 5：资费代码错 <br>
	 * 6：超过最大信息长 <br>
	 * 7：业务代码错 <br>
	 * 8：流量控制错 <br>
	 * 9：本网关不负责服务此计费号码 <br>
	 * 10： Src_Id错误 <br>
	 * 11：Msg_src错误 <br>
	 * 12：Fee_terminal_Id错误 <br>
	 * 13：Dest_terminal_Id错误 ……
	 */
	private Integer returnResult;

	public boolean verify() throws ISchoolException {
		if (null == this) {
			throw new ISchoolException(
					ErrorCode.ISCHOOL_REQBODY_INVALID_MSGTYPE, "参数错误，入参为空。");
		}
		return true;
	}

	/**
	 * 
	 * Default Empty Constructor for class SendMsgLog
	 * 
	 */
	public SendMsgLog() {
		super();
	}

	/**
	 * 
	 * Default All Fields Constructor for class SendMsgLog
	 * 
	 */
	public SendMsgLog(String id, String mobile, String contextWs,
			Integer sendStatus) {
		this.setId(id);
		this.setMobile(mobile);
		this.setContextWs(contextWs);
		this.setSendStatus(sendStatus);
	}

	/**
	 * 
	 * @return Long
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Set the id
	 */
	public void setId(String aValue) {
		this.id = aValue;
	}

	/**
	 * 
	 * @return String
	 */
	public String getMobile() {
		return this.mobile;
	}

	/**
	 * Set the mobile
	 */
	public void setMobile(String aValue) {
		this.mobile = aValue;
	}

	/**
	 * 
	 * @return String
	 */
	public String getContextWs() {
		return this.contextWs;
	}

	/**
	 * Set the contextWs
	 */
	public void setContextWs(String aValue) {
		this.contextWs = aValue;
	}

	/**
	 * 
	 * @return Integer
	 */
	public Integer getSendStatus() {
		return this.sendStatus;
	}

	/**
	 * Set the sendStatus
	 */
	public void setSendStatus(Integer aValue) {
		this.sendStatus = aValue;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof SendMsgLog)) {
			return false;
		}
		SendMsgLog rhs = (SendMsgLog) object;
		return new EqualsBuilder().append(this.id, rhs.id)
				.append(this.mobile, rhs.mobile)
				.append(this.contextWs, rhs.contextWs)
				.append(this.sendStatus, rhs.sendStatus).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.mobile).append(this.contextWs)
				.append(this.sendStatus).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id)
				.append("mobile", this.mobile)
				.append("contextWs", this.contextWs)
				.append("sendStatus", this.sendStatus).toString();
	}

	/**
	 * Return the name of the first key column
	 */
	public String getFirstKeyColumnName() {
		return "id";
	}

	public Integer getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}

	public Integer getReturnResult() {
		return returnResult;
	}

	public void setReturnResult(Integer returnResult) {
		this.returnResult = returnResult;
	}

	

}