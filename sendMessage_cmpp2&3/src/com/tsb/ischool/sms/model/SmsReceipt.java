package com.tsb.ischool.sms.model;

public class SmsReceipt {

	private int runningnumber;//流水号
	private int status;//短信状态(0：待发送、1：成功、2：失败)
	
	public int getRunningnumber() {
		return runningnumber;
	}
	public void setRunningnumber(int runningnumber) {
		this.runningnumber = runningnumber;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	

}
