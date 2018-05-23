package com.tsb.cmpp.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsb.cmpp.Common;

/**
 * @ClassName: CMPPConnect
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author A18ccms a18ccms_gmail_com
 * @date 2013-11-21 上午09:40:21
 * 
 */
public class CMPPConnect implements CMPPRequestBody {

	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(CMPPConnect.class);

	/**
	 * 企业代码
	 */
	private String spid;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 版本
	 */
	private int version;

	/**
	 * Unsigned Integer 时间戳的明文,由客户端产生,格式为MMDDHHMMSS，即月日时分秒，10位数字的整型，右对齐
	 */
	private String timestamp;

	/**
	 * 
	 * @param spid
	 *            6位的企业代码
	 * @param password
	 *            6位的密码
	 */
	public CMPPConnect(String spid, String password) {
		this.spid = spid;
		this.password = password;
		this.version = 2;
		SimpleDateFormat formatter = new SimpleDateFormat("MMddHHmmss");
		Date currentTime = new Date();
		timestamp = formatter.format(currentTime);
	}

	public byte[] getRequestBody() {
		byte[] body = new byte[27];

		int cur = 0;
		byte[] sp = Common.getText(6, spid);
		System.arraycopy(sp, 0, body, cur, sp.length);
		cur += sp.length;

		byte[] authen = md5(spid);
		System.arraycopy(authen, 0, body, cur, authen.length);
		cur += authen.length;

		body[cur] = (byte) version;
		cur += 1;

		byte btimestamp[] = Common.intToBytes4(Integer.parseInt(timestamp));
		System.arraycopy(btimestamp, 0, body, cur, btimestamp.length);

		return body;
	}

	/**
	 * 得到md5后的16位byte型数组 
	 * 用于鉴别源地址。其值通过单向MD5 hash计算得出，表示如下：
	 * AuthenticatorSource =MD5（Source_Addr+9 字节的0 +shared secret+timestamp）
	 * Shared secret 由中国移动与源地址实体事先商定， timestamp格式为：MMDDHHMMSS，即月日时分秒，10位
	 * @param sp
	 * @return
	 */

	private byte[] md5(String spid) {
		byte sp[] = spid.getBytes();
		byte bzero[] = new byte[9];
		byte[] bSPpassword = password.getBytes();

		byte btimestamp[] = (timestamp).getBytes();
		byte bmd5[] = new byte[sp.length + 9 + bSPpassword.length
				+ btimestamp.length];
		int cur = 0;
		System.arraycopy(sp, 0, bmd5, cur, sp.length);
		cur += sp.length;
		System.arraycopy(bzero, 0, bmd5, cur, 9);
		cur += bzero.length;
		System.arraycopy(bSPpassword, 0, bmd5, cur, bSPpassword.length);
		cur += bSPpassword.length;
		System.arraycopy(btimestamp, 0, bmd5, cur, btimestamp.length);
		byte[] result = new byte[16];
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			md.update(bmd5);
			result = md.digest();
			if (log.isDebugEnabled()) {
				log.debug("md5散列码：" + Common.bytes2hex(result));
			}
		} catch (NoSuchAlgorithmException e) {
			log.error(e.toString());
		}
		return result;

	}
}