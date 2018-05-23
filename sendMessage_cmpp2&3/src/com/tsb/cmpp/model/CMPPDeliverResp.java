package com.tsb.cmpp.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsb.cmpp.Common;
/**
 * @ClassName: CMPPDeliverResp
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author A18ccms a18ccms_gmail_com
 * @date 2013-11-21 上午09:44:02
 * 
 */
public class CMPPDeliverResp implements CMPPRequestBody {

	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(CMPPDeliverResp.class);

	/**
	 * 企业代码
	 */
	private byte[] msg_Id;

	/**
	 * 密码
	 */
	private int result;

	public CMPPDeliverResp(byte[] msg_Id, int result) {
		this.msg_Id = msg_Id;
		this.result = result;
	}

	public byte[] getRequestBody() {
		byte[] body = new byte[9];

		int cur = 0;
		System.arraycopy(msg_Id, 0, body, cur, msg_Id.length);
		cur += msg_Id.length;

		byte resultbytes[] = Common.intToBytes4(result);
		System.arraycopy(resultbytes, 0, body, cur, resultbytes.length);

		return body;
	}
}