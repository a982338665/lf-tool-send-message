package com.tsb.cmpp.cmpp3;

public class Msg_Query extends Msg_Head {
	private String time;
	private byte query_Type;
	private String query_Code;
	private String reserve;
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public byte getQuery_Type() {
		return query_Type;
	}
	public void setQuery_Type(byte query_Type) {
		this.query_Type = query_Type;
	}
	public String getQuery_Code() {
		return query_Code;
	}
	public void setQuery_Code(String query_Code) {
		this.query_Code = query_Code;
	}
	public String getReserve() {
		return reserve;
	}
	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

	
}
