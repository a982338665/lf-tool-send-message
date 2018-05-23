package com.tsb.ischool.sms.model;

import java.io.Serializable;
/**
 * 
 *  类 名 称：MobileMessage<br>
 *  内容摘要：手机短消息的实体类<br>
 *  完成日期：
 *  编码作者：Dongrisheng<br>
 */
public class MobileMessage implements Serializable {

	private String id;
	
	private String receiveId;
	
	private String accessToken;
	
	private String openId;
	//手机号
	private String mobileNo;
	//短信内容
	private String messageContent;
	//发送序列号
	private Integer sequenceId;
	
	private int sendFailCount;//发送失败次数
	
	/**
	 * 类型:
	 * 1==向send_msg_log表插入数据
	 * 2==不需要向send_msg_log表插入数据
	 */
	private String type;//非数据库字段
	


	
	public String getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public int getSendFailCount() {
		return sendFailCount;
	}

	public void setSendFailCount(int sendFailCount) {
		this.sendFailCount = sendFailCount;
	}

	public String getMobileNo() {
		return mobileNo;
	}
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * set the mobileNo
	 * @param mobileNo
	 */
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	/**
	 * get the messageContent
	 * @return
	 */
	public String getMessageContent() {
		return messageContent;
	}
	/**
	 * set the messageContent
	 * @param messageContent
	 */
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	
	
	public Integer getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}

	/**
	 * toString MobileMessage [mobileNo=xxx, messageContent=xxx ]
	 */
	@Override
	public String toString() {
		return "MobileMessage [mobileNo=" + mobileNo + ", messageContent=" + messageContent + "]";
	}
	
	
}
