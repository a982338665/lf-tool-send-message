package com.tsb.cmpp.model;

/**
 * @ClassName: CMPPConnectStatus
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author A18ccms a18ccms_gmail_com
 * @date 2013-11-25 下午04:00:09
 * 
 */
public enum CMPPConnectStatus {
	Message_Structure_Fail(1, "消息结构错"), Source_Address_Fail(2, "非法源地址"), Authentication_Fail(
			3, "认证错误"), Versiontoohigh_Fail(4, "版本太高"), Other_Fail(5, "其他错误"), Connect_Fail(
			6, "连接错误"), Parameter_Fail(7, "参数格式错误"), Sp_NotExists_Fail(14,
			"SP不存在");

	private int code;
	private String description;

	private CMPPConnectStatus(int code, String description) {
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
		for (CMPPConnectStatus _enum : CMPPConnectStatus.values()) {
			if (value.equals(String.valueOf(_enum.getCode())))
				return _enum.getDescription();
		}
		return "";
	}
}
