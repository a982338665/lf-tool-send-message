package com.tsb.cmpp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsb.cmpp.Common;

/**
 * @author David Yin
 * @description :CMPP_SUBMIT操作的目的是SP在与ISMG建立应用层连接后向ISMG提交短信。  
   * ISMG以CMPP_SUBMIT_RESP消息响应
 * @version 1.0 (2013-11-21 16:37:23)
 */
public class CMPPSubmit implements CMPPRequestBody {
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(CMPPSubmit.class);

	private SubmitBody submit;

	public CMPPSubmit(SubmitBody sb) {
		this.submit = sb;
	}

	/**
	 * 解析以逗号分隔开的手机号码
	 * 
	 * @param terminate
	 * @return
	 */
	private String[] getTerminate(String terminate) {
		List list = new ArrayList();
		StringTokenizer st = new StringTokenizer(terminate, ",");
		while (st.hasMoreElements()) {
			list.add(st.nextElement());
		}
		String result[] = new String[list.size()];
		list.toArray(result);
		return result;

	}

	/**
	 * 得到包体的字节流形式
	 */
	public byte[] getRequestBody() {
		log.info("接收方:" + submit.sDstTermId + ",业务代码:" + submit.sServiceId);

		byte[] body = new byte[3400];

		int cur = 0;
		System.arraycopy(submit.usMsgId, 0, body, cur, 8);
		cur += 8;

		body[cur] = submit.ucPkTotal;
		cur += 1;

		body[cur] = submit.ucPkNumber;
		cur += 1;

		body[cur] = submit.ucRegister;
		cur += 1;

		body[cur] = submit.ucMsgLevel;
		cur += 1;

		byte[] serviceid = Common.getText(10, submit.sServiceId);
		System.arraycopy(serviceid, 0, body, cur, 10);
		cur += 10;

		body[cur] = submit.ucFeeUserType;
		cur += 1;

		byte[] feeid = Common.getText(21, submit.sFeeTermId);
		System.arraycopy(feeid, 0, body, cur, 21);
		cur += 21;

		// body[cur] = submit.fee_terminal_type;
		// cur += 1;

		body[cur] = submit.ucTpPid;
		cur += 1;

		body[cur] = submit.ucTpUdhi;
		cur += 1;

		body[cur] = submit.ucMsgFmt;
		cur += 1;

		byte[] msgsrc = Common.getText(6, submit.sMsgSrc);
		System.arraycopy(msgsrc, 0, body, cur, 6);
		cur += 6;

		byte[] feetype = Common.getText(2, submit.sFeeType);
		System.arraycopy(feetype, 0, body, cur, 2);
		cur += 2;

		byte[] feecode = Common.getText(6, submit.sFeeCode);
		System.arraycopy(feecode, 0, body, cur, 6);
		cur += 6;

		byte[] validtime = Common.getText(17, submit.sValidTime);
		System.arraycopy(validtime, 0, body, cur, 17);
		cur += 17;

		byte[] attime = Common.getText(17, submit.sAtTime);
		System.arraycopy(attime, 0, body, cur, 17);
		cur += 17;

		byte[] srcid = Common.getText(21, submit.sSrcTermId);
		System.arraycopy(srcid, 0, body, cur, 21);
		cur += 21;

		String[] terminate = getTerminate(submit.sDstTermId);
		body[cur] = (byte) terminate.length;
		cur += 1;

		for (int i = 0; i < terminate.length; i++) {
			byte[] dest = Common.getText(21, terminate[i]);
			System.arraycopy(dest, 0, body, cur, 21);
			cur += 21;
		}

		int msglen = submit.ucMsgContent.length;
		if (submit.ucMsgFmt == 0 && msglen >= 160) {// <160
			msglen = 159;
		} else {
			if (msglen > 140) {
				msglen = 140;
			}
		}
		// body[cur] = submit.dest_terminal_type;
		// cur += 1;

		body[cur] = (byte) msglen;
		cur += 1;

		System.arraycopy(submit.ucMsgContent, 0, body, cur, msglen);
		cur += msglen;

		// byte[] linkID = Common.getText(20, submit.linkID);
		// System.arraycopy(linkID, 0, body, cur, 20);
		byte[] linkID = Common.getText(8, submit.reserver);
		System.arraycopy(linkID, 0, body, cur, 8);

		byte[] bodynow = new byte[cur + 8];
		System.arraycopy(body, 0, bodynow, 0, bodynow.length);

		return bodynow;
	}
}