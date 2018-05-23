package com.tsb.ischool.framework.bean.comm;

import java.io.Serializable;
/**
 * 
 * @author shenzimig
 * @date 2014-7-17 下午3:46:54
 * @version 1.0
 */
public class ResultBean implements Serializable{

	private static final long serialVersionUID = -8363923199593511398L;
	
	public static final long CODE_ERROR = 0;
	public static final long CODE_SUCCESS = 1;
	
	private long code;
	private long errorCode;
	private Object data;
	private String errorMessage;
	
	public ResultBean(){}
	
	public ResultBean(long code, long errorCode, Object data, String errorMessage) {
		super();
		this.code = code;
		this.errorCode = errorCode;
		this.data = data;
		this.errorMessage = errorMessage;
	}
	
	public long getCode() {
		return code;
	}
	public void setCode(long code) {
		this.code = code;
	}
	public long getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(long errorCode) {
		this.errorCode = errorCode;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "ResultBean [code=" + code + ", errorCode=" + errorCode
				+ ", data=" + data + ", errorMessage=" + errorMessage + "]";
	}

	
}
