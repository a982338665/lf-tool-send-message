package com.tsb.ischool.sms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import org.springframework.util.CollectionUtils;

import com.tsb.ischool.framework.common.ErrorCode;
import com.tsb.ischool.framework.exception.ISchoolException;

/**
 * 
 * 类 名 称：C2SMobile<br>
 * 内容摘要：手机短消息的实体类外部传参接口类<br>
 * 完成日期：<br>
 * 编码作者：Dongrisheng<br>
 */
public class C2SMobile implements Serializable {

	private String xkSendUserId;//发送者ID

	private String sid;//接收者ID

	private List<MobileMessage> list;

	public String getXkSendUserId() {
		return xkSendUserId;
	}

	public void setXkSendUserId(String xkSendUserId) {
		this.xkSendUserId = xkSendUserId;
	}
	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}
	public List<MobileMessage> getList() {
		return list;
	}

	public void setList(List<MobileMessage> list) {
		this.list = list;
	}

	public boolean verify() throws ISchoolException {
		if (null == this || CollectionUtils.isEmpty(list)) {
			throw new ISchoolException(ErrorCode.ISCHOOL_REQBODY_INVALID_MSGTYPE, "参数错误，入参为空。");
		}
		return true;
	}

	@Override
	public String toString() {
		return "C2SMobile{" +
				"xkSendUserId='" + xkSendUserId + '\'' +
				", sid='" + sid + '\'' +
				", list=" + list +
				'}';
	}

	/*public static void main(String[] args) {
		C2SMobile c2SMobile=new C2SMobile();
		List<MobileMessage> list=new ArrayList<>();
		MobileMessage mobileMessage=new MobileMessage();
		mobileMessage.setMobileNo("17846551234");
		mobileMessage.setMessageContent("000000");
		mobileMessage.setSequenceId(789444);
		list.add(mobileMessage);
		c2SMobile.setList(list);
		String json= new Gson().toJson(c2SMobile);
		System.out.println(json);

	}*/
}
