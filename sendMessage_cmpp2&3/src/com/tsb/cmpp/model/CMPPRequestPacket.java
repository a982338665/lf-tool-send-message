package com.tsb.cmpp.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsb.cmpp.Common;

/**
 * cmpp请求包
 * 
 * @author David Yin
 * @version 1.0
 */
public class CMPPRequestPacket {
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(CMPPRequestPacket.class);

	/**
	 * Unsigned Integer 消息总长度(含消息头及消息体)
	 */
	private int totalLength;

	/**
	 * 发送包的命令字id
	 * 
	 * Unsigned Integer 命令或响应类型
	 */
	private int commandID;

	/**
	 * Unsigned Integer
	 * 消息流水号,顺序累加,步长为1,循环使用（一对请求和应答消息的流水号必须相同）
	 */
	private int sequenceID;

	/**
	 * CMPP包体
	 */
	private CMPPRequestBody body;

	/**
	 * 设置命令字
	 * 
	 * @param commandID
	 */
	public void setCommandID(int commandID) {
		this.commandID = commandID;
	}

	/**
	 * 设置序列号
	 * 
	 * @param sequenceID
	 */
	public void setSequenceID(int sequenceID) {
		this.sequenceID = sequenceID;
	}

	/**
	 * 设置包体
	 * 
	 * @param body
	 */
	public void setRequestBody(CMPPRequestBody body) {
		this.body = body;
	}

	/**
	 * 取得包体
	 * 
	 * @return
	 */
	public CMPPRequestBody getRequestBody() {
		return this.body;
	}

	/**
	 * 取得整个cmpp请求包的字节形式
	 * 
	 * @return
	 */
	public byte[] getRequestPacket() {
		log.info(body.getClass().getName() + " 消息处理,sequenceID=" + sequenceID);
		byte[] bodybytes = body.getRequestBody();
		this.totalLength = 12 + bodybytes.length;

		byte[] requestPacket = new byte[totalLength];
		System.arraycopy(Common.intToBytes4(totalLength), 0, requestPacket, 0,
				4);
		System.arraycopy(Common.intToBytes4(commandID), 0, requestPacket, 4, 4);
		System
				.arraycopy(Common.intToBytes4(sequenceID), 0, requestPacket, 8,
						4);
		System.arraycopy(bodybytes, 0, requestPacket, 12, bodybytes.length);

		return requestPacket;
	}
}