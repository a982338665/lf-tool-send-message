package com.tsb.cmpp.cmpp3;

/**
 * 1：Cmpp_Deliver消息
 *
 */
public class Msg_Deliver extends Msg_Head {
	 private  long  msg_Id              ;
	  private  String  dest_Id           ; 
	  private  String  service_Id        ;
	  private  byte  tP_pid              ;
	  private  byte  tP_udhi             ;
	  private  byte  msg_Fmt             ;
	  private  String  src_terminal_Id   ;
	  private  byte  src_terminal_type   ;
	  private  byte  registered_Delivery ;
	  private  byte  msg_Length          ;
	  private  String  msg_Content       ;
	  private  String  linkID   ; 
	  //如果是状态报�?
	  private long  msg_Id_report      ;
	  private String  stat             ;
	  private String  submit_time      ;
	  private String  done_time        ;
	  private String  dest_terminal_Id ;
	  private int  sMSC_sequence       ;
	  
	  
	  //以下为对应的getter/Setter方法
	  
	 
	public long getMsg_Id() {
		return msg_Id;
	}
	public void setMsg_Id(long msg_Id) {
		this.msg_Id = msg_Id;
	}
	public String getDest_Id() {
		return dest_Id;
	}
	public void setDest_Id(String dest_Id) {
		this.dest_Id = dest_Id;
	}
	public String getService_Id() {
		return service_Id;
	}
	public void setService_Id(String service_Id) {
		this.service_Id = service_Id;
	}
	public byte getTP_pid() {
		return tP_pid;
	}
	public void setTP_pid(byte tp_pid) {
		tP_pid = tp_pid;
	}
	public byte getTP_udhi() {
		return tP_udhi;
	}
	public void setTP_udhi(byte tp_udhi) {
		tP_udhi = tp_udhi;
	}
	public byte getMsg_Fmt() {
		return msg_Fmt;
	}
	public void setMsg_Fmt(byte msg_Fmt) {
		this.msg_Fmt = msg_Fmt;
	}
	public String getSrc_terminal_Id() {
		return src_terminal_Id;
	}
	public void setSrc_terminal_Id(String src_terminal_Id) {
		this.src_terminal_Id = src_terminal_Id;
	}
	public byte getSrc_terminal_type() {
		return src_terminal_type;
	}
	public void setSrc_terminal_type(byte src_terminal_type) {
		this.src_terminal_type = src_terminal_type;
	}
	public byte getRegistered_Delivery() {
		return registered_Delivery;
	}
	public void setRegistered_Delivery(byte registered_Delivery) {
		this.registered_Delivery = registered_Delivery;
	}
	public byte getMsg_Length() {
		return msg_Length;
	}
	public void setMsg_Length(byte msg_Length) {
		this.msg_Length = msg_Length;
	}
	public String getMsg_Content() {
		return msg_Content;
	}
	public void setMsg_Content(String msg_Content) {
		this.msg_Content = msg_Content;
	}
	public String getLinkID() {
		return linkID;
	}
	public void setLinkID(String linkID) {
		this.linkID = linkID;
	}
	public long getMsg_Id_report() {
		return msg_Id_report;
	}
	public void setMsg_Id_report(long msg_Id_report) {
		this.msg_Id_report = msg_Id_report;
	}
	public String getStat() {
		return stat;
	}
	public void setStat(String stat) {
		this.stat = stat;
	}
	public String getSubmit_time() {
		return submit_time;
	}
	public void setSubmit_time(String submit_time) {
		this.submit_time = submit_time;
	}
	public String getDone_time() {
		return done_time;
	}
	public void setDone_time(String done_time) {
		this.done_time = done_time;
	}
	public String getDest_terminal_Id() {
		return dest_terminal_Id;
	}
	public void setDest_terminal_Id(String dest_terminal_Id) {
		this.dest_terminal_Id = dest_terminal_Id;
	}
	public int getSMSC_sequence() {
		return sMSC_sequence;
	}
	public void setSMSC_sequence(int smsc_sequence) {
		sMSC_sequence = smsc_sequence;
	}

}
