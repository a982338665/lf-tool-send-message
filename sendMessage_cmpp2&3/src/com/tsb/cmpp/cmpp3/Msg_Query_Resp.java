package com.tsb.cmpp.cmpp3;

public class Msg_Query_Resp extends Msg_Head {
	
	private String time;
	private byte query_Type;
	private String query_Code;
	private int MT_TLMsg;
	private int MT_Tlusr;
	private int MT_Scs;
	private int MT_WT;
	private int MT_FL;
	private int MO_Scs;
	private int MO_WT;
	private int MO_FL;
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
	public int getMT_TLMsg() {
		return MT_TLMsg;
	}
	public void setMT_TLMsg(int msg) {
		MT_TLMsg = msg;
	}
	public int getMT_Tlusr() {
		return MT_Tlusr;
	}
	public void setMT_Tlusr(int tlusr) {
		MT_Tlusr = tlusr;
	}
	public int getMT_Scs() {
		return MT_Scs;
	}
	public void setMT_Scs(int scs) {
		MT_Scs = scs;
	}
	public int getMT_WT() {
		return MT_WT;
	}
	public void setMT_WT(int mt_wt) {
		MT_WT = mt_wt;
	}
	public int getMT_FL() {
		return MT_FL;
	}
	public void setMT_FL(int mt_fl) {
		MT_FL = mt_fl;
	}
	public int getMO_Scs() {
		return MO_Scs;
	}
	public void setMO_Scs(int scs) {
		MO_Scs = scs;
	}
	public int getMO_WT() {
		return MO_WT;
	}
	public void setMO_WT(int mo_wt) {
		MO_WT = mo_wt;
	}
	public int getMO_FL() {
		return MO_FL;
	}
	public void setMO_FL(int mo_fl) {
		MO_FL = mo_fl;
	}
	


}
