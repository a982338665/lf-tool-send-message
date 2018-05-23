package com.tsb.cmpp.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsb.cmpp.Common;

/**
 * @ClassName: CMPPDeliver
 * @Description: CMPP_DELIVER操作的目的是ISMG把从短信中心或其它ISMG转发来的
 *               短信送交SP，SP以CMPP_DELIVER_RESP消息回应
 * @author David Yin
 * @date 2013-11-21 上午09:42:44
 * 
 */
public class CMPPDeliver extends CMPPResponsePacket {
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(CMPPDeliver.class);

	/**
	 * 8位,信息标识 生成算法如下： 采用64位（8字节）的整数： （1）
	 * 时间（格式为MMDDHHMMSS，即月日时分秒）：bit64~bit39，其中 bit64~bit61：月份的二进制表示；
	 * bit60~bit56：日的二进制表示； bit55~bit51：小时的二进制表示； bit50~bit45：分的二进制表示；
	 * bit44~bit39：秒的二进制表示； （2） 短信网关代码：bit38~bit17，把短信网关的代码转换为整数填写到该字段中。 （3）
	 * 序列号：bit16~bit1，顺序增加，步长为1，循环使用。 各部分如不能填满，左补零，右对齐。
	 */
	public byte msg_Id[] = new byte[8];

	/**
	 * 21位, 目的号码 SP的服务代码，一般4--6位，或者是前缀为服务代码的长号码；该号码是手机用户短消息的被叫号码。
	 */
	public String dest_Id;

	/**
	 * 10位, 业务类型，是数字、字母和符号的组合。
	 */
	public String service_Id;

	/**
	 * 1位, GSM协议类型。详细解释请参考GSM03.40中的9.2.3.9
	 */
	public byte tp_pid;

	/**
	 * 1位, GSM协议类型。详细解释请参考GSM03.40中的9.2.3.23，仅使用1位，右对齐
	 */
	public byte tp_udhi;

	/**
	 * 1位, 信息格式 0：ASCII串 3：短信写卡操作 4：二进制信息 8：UCS2编码 15：含GB汉字
	 */
	public byte msg_Fmt;

	/**
	 * 21位, 源终端MSISDN号码（状态报告时填为CMPP_SUBMIT消息的目的终端号码）
	 */
	public String src_terminal_Id;

	/**
	 * 1 位, 是否为状态报告 0：非状态报告 1：状态报告
	 */
	public byte registered_Delivery;

	/**
	 * 1位, 消息长度
	 */
	public int msg_Length;

	/**
	 * Msg_length 位, 消息内容字节流形式，如果是deliver消息，用户需自己将其转成字串
	 */
	public byte[] msg_Content;

	/**
	 * 状态报告对象
	 */
	public CMPPReport report;

	/**
	 * 8位, 保留项,这是cmpp2.0中定义的
	 */
	public String reserved;

	// /**
	// * 20位，点播业务使用的LinkID，非点播类业务的MT流程不使用该字段。
	// */
	// public String linkID;

	public CMPPDeliver() {
		msg_Id = new byte[8];
	}

	/**
	 * 解析从输入流得到的包体字节流
	 */
	public void parseResponseBody(byte[] packet) {
		byte[] length = new byte[4];
		// 此方位为native方法。
		// public static native void arraycopy(
		// Object src, int srcPos, Object dest,
		// int destPos, int length);
		//// 把从索引src从srcPos开始的length个数字复制到dest为destPos的位置上

		System.arraycopy(packet, 0, length, 0, 4);
		this.totalLength = Common.bytes4ToInt(length);
		if (log.isDebugEnabled()) {
			log.debug("返回包长度解析后为:" + totalLength);
		}

		byte[] commandid = new byte[4];
		System.arraycopy(packet, 4, commandid, 0, 4);
		this.commandID = Common.bytes4ToInt(commandid);
		if (log.isDebugEnabled()) {
			log
					.debug("返回命令字解析后=" + commandID + "，实际="
							+ CommandID.CMPP_DELIVER);
		}

		byte[] seqid = new byte[4];
		System.arraycopy(packet, 8, seqid, 0, 4);
		this.sequenceID = Common.bytes4ToInt(seqid);
		if (log.isDebugEnabled()) {
			log.debug("返回包序列号解析后为:" + sequenceID);
		}

		System.arraycopy(packet, 12, msg_Id, 0, 8);

		byte[] dest = new byte[21];
		System.arraycopy(packet, 20, dest, 0, 21);
		this.dest_Id = new String(dest);

		byte[] service = new byte[10];
		System.arraycopy(packet, 41, service, 0, 10);
		this.service_Id = new String(service);

		this.tp_pid = packet[51];
		this.tp_udhi = packet[52];
		this.msg_Fmt = packet[53];

		// byte[] src = new byte[32];
		// System.arraycopy(packet, 42 + 12, src, 0, 32);
		// this.src_terminal_Id = new String(src);
		byte[] src = new byte[21];
		System.arraycopy(packet, 54, src, 0, 21);
		this.src_terminal_Id = new String(src);

		this.registered_Delivery = packet[75];
		this.msg_Length = packet[76];
		if (msg_Length < 0)
			msg_Length = 256 + msg_Length;

		byte[] content = new byte[this.msg_Length];
		if (log.isDebugEnabled())
			log.debug("deliver的内容长度为:" + content.length);
		System.arraycopy(packet, 77, content, 0, content.length);

		if (this.registered_Delivery == 1) {
			report = new CMPPReport();
			System.arraycopy(content, 0, report.msg_Id, 0, 8);
			log.info("返回的是状态报告,对应msgid=" + Common.bytes8ToLong(report.msg_Id));

			byte[] state = new byte[7];
			System.arraycopy(content, 8, state, 0, 7);
			report.stat = new String(state);

			byte[] subtime = new byte[10];
			System.arraycopy(content, 15, subtime, 0, 10);
			report.submit_time = new String(subtime);

			byte[] donetime = new byte[10];
			System.arraycopy(content, 25, donetime, 0, 10);
			report.done_time = new String(donetime);

			// byte[] destre = new byte[32];
			// System.arraycopy(content, 35, destre, 0, 32);
			// report.dest_terminal_Id = new String(destre);
			byte[] destre = new byte[21];
			System.arraycopy(content, 35, destre, 0, 21);
			report.dest_terminal_Id = new String(destre);

			byte[] seqre = new byte[4];
			System.arraycopy(content, 56, destre, 0, 4);
			report.smsc_sequence = Common.bytes4ToInt(seqre);
		} else {
			// try {
			// this.msg_Content = new String(content, "gb2312");
			// }
			// catch (UnsupportedEncodingException e) {
			// log.error("系统不支持gb2312字符集,采用系统默认字符集");
			// this.msg_Content = new String(content);
			// }
			this.msg_Content = content;
		}

		byte[] reserver = new byte[8];
		// System.arraycopy(packet, 78 + content.length , linkid, 0, 20);
		// linkID = new String(linkid);
		System.arraycopy(packet, 77 + content.length, reserver, 0, 8);
		reserved = new String(reserver);

		log.info("deliver消息解析成功,msgid=" + bytes2hex(msg_Id));
	}

	private static String bytes2hex(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			sb.append(Common.byte2hex(b[i])).append(" ");
		return sb.toString();
	}
}