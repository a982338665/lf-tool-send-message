package com.tsb.cmpp.cmpp3;

//submit应答消息
public class Msg_Submit_Resp extends Msg_Head {
	 private  long msg_Id           ;
	 private  int  result           ;
	public long getMsg_Id() {
		return msg_Id;
	}
	public void setMsg_Id(long msg_Id) {
		this.msg_Id = msg_Id;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
}
