package com.tsb.cmpp.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsb.cmpp.Common;

/**
 * submit返回消息包
 * 
 * @author HuaFeng
 * @version 1.0 (2005-3-30 13:27:03)
 */
public class CMPPSubmitResp extends CMPPResponsePacket {
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(CMPPSubmitResp.class);

	/**
	 * 8位,信息标识，生成算法如下，采用64位（8字节）的整数： <br>
	 * （1） 时间（格式为MMDDHHMMSS，即月日时分秒）：bit64~bit39，其中 bit64~bit61：月份的二进制表示；
	 * bit60~bit56：日的二进制表示； bit55~bit51：小时的二进制表示； bit50~bit45：分的二进制表示；
	 * bit44~bit39：秒的二进制表示； <br>
	 * （2） 短信网关代码：bit38~bit17，把短信网关的代码转换为整数填写到该字段中。 <br>
	 * （3） 序列号：bit16~bit1，顺序增加，步长为1，循环使用。 各部分如不能填满，左补零，右对齐。
	 * （SP根据请求和应答消息的Sequence_Id一致性就可得到CMPP_Submit消息的Msg_Id）
	 */
	public byte[] msg_Id;

	/**
	 * Unsigned Integer 结果 <br>
	 * 0：正确 <br>
	 * 1：消息结构错 <br>
	 * 2：命令字错 <br>
	 * 3：消息序号重复 <br>
	 * 4：消息长度错 <br>
	 * 5：资费代码错 <br>
	 * 6：超过最大信息长 <br>
	 * 7：业务代码错 <br>
	 * 8：流量控制错 <br>
	 * 9：本网关不负责服务此计费号码 <br>
	 * 10： Src_Id错误 <br>
	 * 11：Msg_src错误 <br>
	 * 12：Fee_terminal_Id错误 <br>
	 * 13：Dest_terminal_Id错误 ……
	 */
	public int result;

	public CMPPSubmitResp() {
		msg_Id = new byte[8];
		this.commandID = CommandID.CMPP_SUBMIT_RESP;
		this.totalLength = 21;
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
					+ CommandID.CMPP_SUBMIT_RESP);
		}

		byte[] seqid = new byte[4];
		System.arraycopy(packet, 8, seqid, 0, 4);
		this.sequenceID = Common.bytes4ToInt(seqid);

		System.arraycopy(packet, 12, msg_Id, 0, 8);

		// byte[] results = new byte[4];
		// System.arraycopy(packet, 20, results, 0, 4);
		// result = Common.bytes4ToInt(results);

		result = packet[20];// cmpp3.0是4个字节，2.0是1个字节
		log.info("submitResp消息解析成功,result=" + result + ",sequenceID="
				+ sequenceID+",commandId="+this.commandID+",totalLength="+totalLength+",msg_ID="+msg_Id);
	}
}