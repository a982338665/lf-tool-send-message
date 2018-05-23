package com.tsb.cmpp.cmpp3;

public class Msg_Submit extends Msg_Head{

	 @Override
	public String toString() {
		return "Msg_Submit [msg_Id=" + msg_Id + ", pk_total=" + pk_total
				+ ", pk_number=" + pk_number + ", registered_Delivery="
				+ registered_Delivery + ", msg_level=" + msg_level
				+ ", service_Id=" + service_Id + ", fee_UserType="
				+ fee_UserType + ", fee_terminal_Id=" + fee_terminal_Id
				+ ", fee_terminal_type=" + fee_terminal_type + ", tP_pId="
				+ tP_pId + ", tP_udhi=" + tP_udhi + ", msg_Fmt=" + msg_Fmt
				+ ", msg_src=" + msg_src + ", feeType=" + feeType
				+ ", feeCode=" + feeCode + ", valId_Time=" + valId_Time
				+ ", at_Time=" + at_Time + ", src_Id=" + src_Id
				+ ", destUsr_tl=" + destUsr_tl + ", dest_terminal_Id="
				+ dest_terminal_Id + ", dest_terminal_type="
				+ dest_terminal_type + ", msg_Length=" + msg_Length
				+ ", msg_Content=" + msg_Content + ", linkID=" + linkID + "]";
	}
	private long      msg_Id                  ;
	 private byte      pk_total                ;
	 private byte      pk_number               ;
	 private byte      registered_Delivery     ;
	 private byte      msg_level               ;
	 private String    service_Id              ;
	 private byte      fee_UserType            ;
	 private String    fee_terminal_Id         ;
	 private byte      fee_terminal_type       ;
	 private byte      tP_pId                  ;
	 private byte      tP_udhi                 ;
	 private byte      msg_Fmt                 ;
	 private String      msg_src                 ;
	 private String      feeType                 ;
	 private String      feeCode                 ;
	 private String      valId_Time              ;
	 private String      at_Time                 ;
	 private String      src_Id                  ;
	 private byte      destUsr_tl              ;
	 private String      dest_terminal_Id        ;
	 private byte      dest_terminal_type      ;
	 private byte      msg_Length              ;
	 private String      msg_Content             ;
	 private String      linkID                  ;
	//以下为对应的getter/Setter方法
	public long getMsg_Id() {
		return msg_Id;
	}
	public void setMsg_Id(long msg_Id) {
		this.msg_Id = msg_Id;
	}
	public byte getPk_total() {
		return pk_total;
	}
	public void setPk_total(byte pk_total) {
		this.pk_total = pk_total;
	}
	public byte getPk_number() {
		return pk_number;
	}
	public void setPk_number(byte pk_number) {
		this.pk_number = pk_number;
	}
	public byte getRegistered_Delivery() {
		return registered_Delivery;
	}
	public void setRegistered_Delivery(byte registered_Delivery) {
		this.registered_Delivery = registered_Delivery;
	}
	public byte getMsg_level() {
		return msg_level;
	}
	public void setMsg_level(byte msg_level) {
		this.msg_level = msg_level;
	}
	public String getService_Id() {
		return service_Id;
	}
	public void setService_Id(String service_Id) {
		this.service_Id = service_Id;
	}
	public byte getFee_UserType() {
		return fee_UserType;
	}
	public void setFee_UserType(byte fee_UserType) {
		this.fee_UserType = fee_UserType;
	}
	public String getFee_terminal_Id() {
		return fee_terminal_Id;
	}
	public void setFee_terminal_Id(String fee_terminal_Id) {
		this.fee_terminal_Id = fee_terminal_Id;
	}
	public byte getFee_terminal_type() {
		return fee_terminal_type;
	}
	public void setFee_terminal_type(byte fee_terminal_type) {
		this.fee_terminal_type = fee_terminal_type;
	}
	public byte getTP_pId() {
		return tP_pId;
	}
	public void setTP_pId(byte id) {
		tP_pId = id;
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
	public String getMsg_src() {
		return msg_src;
	}
	public void setMsg_src(String msg_src) {
		this.msg_src = msg_src;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public String getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}
	public String getValId_Time() {
		return valId_Time;
	}
	public void setValId_Time(String valId_Time) {
		this.valId_Time = valId_Time;
	}
	public String getAt_Time() {
		return at_Time;
	}
	public void setAt_Time(String at_Time) {
		this.at_Time = at_Time;
	}
	public String getSrc_Id() {
		return src_Id;
	}
	public void setSrc_Id(String src_Id) {
		this.src_Id = src_Id;
	}
	public byte getDestUsr_tl() {
		return destUsr_tl;
	}
	public void setDestUsr_tl(byte destUsr_tl) {
		this.destUsr_tl = destUsr_tl;
	}
	public String getDest_terminal_Id() {
		return dest_terminal_Id;
	}
	public void setDest_terminal_Id(String dest_terminal_Id) {
		this.dest_terminal_Id = dest_terminal_Id;
	}
	public byte getDest_terminal_type() {
		return dest_terminal_type;
	}
	public void setDest_terminal_type(byte dest_terminal_type) {
		this.dest_terminal_type = dest_terminal_type;
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
}
