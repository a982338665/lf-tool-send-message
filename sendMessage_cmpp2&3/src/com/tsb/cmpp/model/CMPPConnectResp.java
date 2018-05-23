
package com.tsb.cmpp.model;

import com.tsb.ischool.framework.common.PropertyUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsb.cmpp.Common;

/**
 * @ClassName: CMPPConnectResp
 * @Description: TODO(CMPPConnectResp消息包)
 * @author David Yin
 * @date 2013-11-21 上午09:41:33
 * 
 */
public class CMPPConnectResp extends CMPPResponsePacket {
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(CMPPConnectResp.class);

	/**
	 * 返回的状态,1位 Unsigned Integer 状态0：正确1：消息结构错 2：非法源地址 3：认证错 4：版本太高 5~ ：其他错误
	 */
	public int status;

	/**
	 * 返回的认证码 16 Octet String ISMG认证码，用于鉴别ISMG。 其值通过单向MD5
	 * hash计算得出，表示如下：AuthenticatorISMG =MD5（Status+AuthenticatorSource+shared
	 * secret），Shared secret
	 * 由中国移动与源地址实体事先商定，AuthenticatorSource为源地址实体发送给ISMG的对应消息CMPP_Connect中的值。
	 * 认证出错时，此项为空
	 */
	public String authenticaion;

	/**
	 * ismg返回的协议版本
	 */
	public int version;

	public CMPPConnectResp() {
		this.commandID = CommandID.CMPP_CONNECT_RESP;
		this.totalLength = Integer.parseInt(PropertyUtil.getProperty("cmpp"));
		log.info("CMPPConnectResp初始化commandID/totalLength:"+commandID+"|"+totalLength);

	}

	/**
	 * 解析从输入流得到的包体字节流
	 */
	public void parseResponseBody(byte[] packet) {
		byte[] length = new byte[4];
		System.arraycopy(packet, 0, length, 0, 4);
		this.totalLength = Common.bytes4ToInt(length);
		if (log.isDebugEnabled()) {
			log.debug("返回包长度解析后为:" + totalLength);
		}

		byte[] commandid = new byte[4];
		System.arraycopy(packet, 4, commandid, 0, 4);
		this.commandID = Common.bytes4ToInt(commandid);
		if (log.isDebugEnabled()) {
			log.debug("返回包命令字解析后=" + commandID + "，实际="
					+ CommandID.CMPP_CONNECT_RESP);
		}

		byte[] seqid = new byte[4];
		System.arraycopy(packet, 8, seqid, 0, 4);
		this.sequenceID = Common.bytes4ToInt(seqid);
		if (log.isDebugEnabled()) {
			log.debug("返回包序列号解析后为:" + sequenceID);
		}

		// byte stat[] = new byte[4];
		// System.arraycopy(packet, 12, stat, 0, 4);
		// status = Common.bytes4ToInt(stat);
		status = packet[12]; // cmpp3.0是4个字节，2.0只有1个字节

		byte[] authen = new byte[16];
		System.arraycopy(packet, 13, authen, 0, 16);
		this.authenticaion = new String(authen);

		version = packet[29];
		log.info("connectResp消息解析成功,status=" + status + ",sequenceID="
				+ sequenceID+",commandID"+commandID+",totalLength"+totalLength);
	}
}