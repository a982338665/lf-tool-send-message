package com.tsb.cmpp.cmpp3;
/**
 * 1：_Cmpp_Cancel_Resp消息
 * @author 陈挺
 */
public class Msg_Cancel_Resp extends Msg_Head {

	private int Success_Id;

	public int getSuccess_Id() {
		return Success_Id;
	}

	public void setSuccess_Id(int success_Id) {
		Success_Id = success_Id;
	}
}
