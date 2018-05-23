package com.tsb.cmpp.model;

/** 
 * @ClassName: CMPPSubmitStatus 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author A18ccms a18ccms_gmail_com 
 * @date 2013-11-25 下午03:39:11 
 *  
 */

public enum CMPPSubmitStatus {
	Message_Structure_Fail(1, "消息结构错"), Command_Key_Fail(2, "命令字错"), Message_Serial_Repeat(
			3, "消息序号重复"), Message_length_Fail(4, "消息长度错"), Tariff_Code_Fail(5,
			"资费代码错"), Exceed_maxlen_Fail(6, "超过最大信息长"), Service_Code_Fail(7,
			"业务代码错"), Flow_Control_Fail(8, "流量控制错"), Fee_Code_Fail(9,
			"本网关不负责服务此计费号码"), Src_Id_Fail(10, "Src_Id错误"), Msg_src_Fail(11,
			"Msg_src错误"), Fee_terminal_Id_Fail(12, "Fee_terminal_Id错误"), Dest_terminal_Id_Fail(
			13, "Dest_terminal_Id错误");

	private int code;
	private String description;

	private CMPPSubmitStatus(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static String getDesByCode(int code) {
		String value = String.valueOf(code);
		if (null == value)
			return null;
		for (CMPPSubmitStatus _enum : CMPPSubmitStatus.values()) {
			if (value.equals(String.valueOf(_enum.getCode())))
				return _enum.getDescription();
		}
		return "";
	}
}
