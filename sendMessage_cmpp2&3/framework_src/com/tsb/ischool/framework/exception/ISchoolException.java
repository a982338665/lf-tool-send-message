package com.tsb.ischool.framework.exception;

import com.tsb.ischool.framework.common.ErrorMsgManager;



/**
 * 协议参数接收异常
 * @author zkl
 * @date 2014-7-17 上午11:06:33
 * @version 1.0
 */
public class ISchoolException extends RuntimeException{
	private static final long serialVersionUID = -3116878121283778384L;
	private long perrorcode;
	private String perrormessage;
	
	public ISchoolException(long perrorcode){
		this.perrorcode = perrorcode;
		this.perrormessage = ErrorMsgManager.GetErrorMsg(perrorcode);
	}
	
	public ISchoolException(long perrorcode,String perrormessage){
		this.perrorcode = perrorcode;
		this.perrormessage = perrormessage;
	}

	

	public long getPerrorcode() {
		return perrorcode;
	}



	public void setPerrorcode(long perrorcode) {
		this.perrorcode = perrorcode;
	}



	public String getPerrormessage() {
		return perrormessage;
	}

	public void setPerrormessage(String perrormessage) {
		this.perrormessage = perrormessage;
	}
	
	
}
