package com.tsb.cmpp.model;

/**
 * cmpp submit消息包体 <br>
 * 如下字段默认值如下： <br>
 * ucPkTotal = 1 <br>
 * ucPkNumber = 1 <br>
 * ucRegister = 0 <br>
 * ucMsgLevel = 1 <br>
 * ucTpPid = 0 <br>
 * ucTpUdhi = 0 <br>
 * ucMsgFmt = 15 <br>
 * 
 * @author HuaFeng
 * @version 1.0 (2005-3-29 16:34:57)
 */
public class SubmitBody {

	public SubmitBody() {
		usMsgId = new byte[8];
	}

	/**
	 * 信息标识，由SP接入的短信网关本身产生，本处填空。
	 */
	byte usMsgId[];

	/**
	 * 相同Msg_Id的信息总条数，从1开始
	 */
	public byte ucPkTotal = 1;

	/**
	 * 相同Msg_Id的信息序号，从1开始
	 */
	public byte ucPkNumber = 1;

	/**
	 * 是否要求返回状态确认报告： <br>
	 * 0：不需要 <br>
	 * 1：需要 <br>
	 * 2：产生SMC话单 （该类型短信仅供网关计费使用，不发送给目的终端)
	 */
	public byte ucRegister;

	/**
	 * 信息级别
	 */
	public byte ucMsgLevel = 1;

	/**
	 * 业务类型，是数字、字母和符号的组合
	 */
	public String sServiceId;

	/**
	 * 计费用户类型字段 <br>
	 * 0：对目的终端MSISDN计费 <br>
	 * 1：对源终端MSISDN计费 <br>
	 * 2：对SP计费 <br>
	 * 3：表示本字段无效，对谁计费参见Fee_terminal_Id字段。
	 */
	public byte ucFeeUserType;

	/**
	 * 被计费用户的号码（如本字节填空，则表示本字段无效，对谁计费参见Fee_UserType字段，本字段与Fee_UserType字段互斥）
	 */
	public String sFeeTermId;

	/**
	 * 被计费用户的号码类型，0：真实号码；1：伪码。
	 */
	public byte fee_terminal_type;

	/**
	 * GSM协议类型 详细是解释请参考GSM03.40中的9.2.3.9
	 */
	public byte ucTpPid;

	/**
	 * GSM协议类型 详细是解释请参考GSM03.40中的9.2.3.23,仅使用1位，右对齐
	 */
	public byte ucTpUdhi;

	/**
	 * 信息格式 <br>
	 * 0：ASCII串 <br>
	 * 3：短信写卡操作 <br>
	 * 4：二进制信息 <br>
	 * 8：UCS2编码 <br>
	 * 15：含GB汉字
	 */
	public byte ucMsgFmt = 15;

	/**
	 * 信息内容来源(SP_Id)
	 */
	public String sMsgSrc = "";

	/**
	 * 资费类别 <br>
	 * 01：对“计费用户号码”免费 <br>
	 * 02：对“计费用户号码”按条计信息费 <br>
	 * 03：对“计费用户号码”按包月收取信息费 <br>
	 * 04：对“计费用户号码”的信息费封顶 <br>
	 * 05：对“计费用户号码”的收费是由SP实现
	 */
	public String sFeeType = "";

	/**
	 * 资费代码（以分为单位）
	 */
	public String sFeeCode = "0";

	/**
	 * 存活有效期，格式遵循SMPP3.3协议
	 */
	public String sValidTime = "";

	/**
	 * 定时发送时间，格式遵循SMPP3.3协议
	 */
	public String sAtTime = "";

	/**
	 * 源号码 SP的服务代码或前缀为服务代码的长号码,
	 * 网关将该号码完整的填到SMPP协议Submit_SM消息相应的source_addr字段，该号码最终在用户手机上显示为短消息的主叫号码
	 */
	public String sSrcTermId = "";

	/**
	 * 接收信息的用户数量(小于100个用户)
	 */
	byte ucDestUsrTl;

	/**
	 * 接收短信的MSISDN号码,以英文逗号","分隔开 <br>
	 */
	public String sDstTermId;

	/**
	 * 接收短信的用户的号码类型，0：真实号码；1：伪码。
	 */
	public byte dest_terminal_type;

	/**
	 * 信息长度,由usMsgContent确定(Msg_Fmt值为0时： <160个字节；其它 <=140个字节)
	 */
	byte ucMsgLen;

	/**
	 * 信息内容的字节流形式。用户需自己调用getBytes()等将内容或者铃声图片等转化成字节流
	 */
	public byte[] ucMsgContent;

	/**
	 * 保留
	 */
	public String reserver = "";
	// /**
	// * 点播业务使用的LinkID，非点播类业务的MT流程不使用该字段。
	// */
	// public String linkID = "";

	/** 
	 * @param usMsgId 要设置的 usMsgId 
	 */
	
	public void setUsMsgId(byte[] usMsgId) {
		this.usMsgId = usMsgId;
	}

	/** 
	 * @param ucPkTotal 要设置的 ucPkTotal 
	 */
	
	public void setUcPkTotal(byte ucPkTotal) {
		this.ucPkTotal = ucPkTotal;
	}

	/** 
	 * @param ucPkNumber 要设置的 ucPkNumber 
	 */
	
	public void setUcPkNumber(byte ucPkNumber) {
		this.ucPkNumber = ucPkNumber;
	}

	/** 
	 * @param ucRegister 要设置的 ucRegister 
	 */
	
	public void setUcRegister(byte ucRegister) {
		this.ucRegister = ucRegister;
	}

	/** 
	 * @param ucMsgLevel 要设置的 ucMsgLevel 
	 */
	
	public void setUcMsgLevel(byte ucMsgLevel) {
		this.ucMsgLevel = ucMsgLevel;
	}

	/** 
	 * @param sServiceId 要设置的 sServiceId 
	 */
	
	public void setsServiceId(String sServiceId) {
		this.sServiceId = sServiceId;
	}

	/** 
	 * @param ucFeeUserType 要设置的 ucFeeUserType 
	 */
	
	public void setUcFeeUserType(byte ucFeeUserType) {
		this.ucFeeUserType = ucFeeUserType;
	}

	/** 
	 * @param sFeeTermId 要设置的 sFeeTermId 
	 */
	
	public void setsFeeTermId(String sFeeTermId) {
		this.sFeeTermId = sFeeTermId;
	}

	/** 
	 * @param feeTerminalType 要设置的 fee_terminal_type 
	 */
	
	public void setFee_terminal_type(byte feeTerminalType) {
		fee_terminal_type = feeTerminalType;
	}

	/** 
	 * @param ucTpPid 要设置的 ucTpPid 
	 */
	
	public void setUcTpPid(byte ucTpPid) {
		this.ucTpPid = ucTpPid;
	}

	/** 
	 * @param ucTpUdhi 要设置的 ucTpUdhi 
	 */
	
	public void setUcTpUdhi(byte ucTpUdhi) {
		this.ucTpUdhi = ucTpUdhi;
	}

	/** 
	 * @param ucMsgFmt 要设置的 ucMsgFmt 
	 */
	
	public void setUcMsgFmt(byte ucMsgFmt) {
		this.ucMsgFmt = ucMsgFmt;
	}

	/** 
	 * @param sMsgSrc 要设置的 sMsgSrc 
	 */
	
	public void setsMsgSrc(String sMsgSrc) {
		this.sMsgSrc = sMsgSrc;
	}

	/** 
	 * @param sFeeType 要设置的 sFeeType 
	 */
	
	public void setsFeeType(String sFeeType) {
		this.sFeeType = sFeeType;
	}

	/** 
	 * @param sFeeCode 要设置的 sFeeCode 
	 */
	
	public void setsFeeCode(String sFeeCode) {
		this.sFeeCode = sFeeCode;
	}

	/** 
	 * @param sValidTime 要设置的 sValidTime 
	 */
	
	public void setsValidTime(String sValidTime) {
		this.sValidTime = sValidTime;
	}

	/** 
	 * @param sAtTime 要设置的 sAtTime 
	 */
	
	public void setsAtTime(String sAtTime) {
		this.sAtTime = sAtTime;
	}

	/** 
	 * @param sSrcTermId 要设置的 sSrcTermId 
	 */
	
	public void setsSrcTermId(String sSrcTermId) {
		this.sSrcTermId = sSrcTermId;
	}

	/** 
	 * @param ucDestUsrTl 要设置的 ucDestUsrTl 
	 */
	
	public void setUcDestUsrTl(byte ucDestUsrTl) {
		this.ucDestUsrTl = ucDestUsrTl;
	}

	/** 
	 * @param sDstTermId 要设置的 sDstTermId 
	 */
	
	public void setsDstTermId(String sDstTermId) {
		this.sDstTermId = sDstTermId;
	}

	/** 
	 * @param destTerminalType 要设置的 dest_terminal_type 
	 */
	
	public void setDest_terminal_type(byte destTerminalType) {
		dest_terminal_type = destTerminalType;
	}

	/** 
	 * @param ucMsgLen 要设置的 ucMsgLen 
	 */
	
	public void setUcMsgLen(byte ucMsgLen) {
		this.ucMsgLen = ucMsgLen;
	}

	/** 
	 * @param ucMsgContent 要设置的 ucMsgContent 
	 */
	
	public void setUcMsgContent(byte[] ucMsgContent) {
		this.ucMsgContent = ucMsgContent;
	}

	/** 
	 * @param reserver 要设置的 reserver 
	 */
	
	public void setReserver(String reserver) {
		this.reserver = reserver;
	}
}