package com.tsb.ischool.sms.service;

import com.tsb.ischool.sms.model.C2SMobile;

/**
 * 类 编 号： <br>
 * 类 名 称：ISendSMSMessageService<br>
 * <b>内容摘要：发送手机短消息的接口类</b><br>
 * 完成日期：<br>
 * 编码作者：dongrisheng
 */
public interface ISendSMSMessageService {

	 
	/**
	 * 批量发送手机短信息
	 * @param c2sMobile 手机短信列表对象
	 * @return 返回整型数，如果大于零：表示发送成功的数量 ；<br>如果等于零：表示什么也没发生；<br>如果小于零：表示传入的列表为空
	 */
	public int toSendBatchMessage(C2SMobile c2sMobile);

	int toSendBatchMessageHB(C2SMobile c2sMobile);
}
