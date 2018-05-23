package com.tsb.cmpp.model;

/**
 * 返回的消息部分
 * 
 * @author David Yin
 * @version 1.0
 */
public abstract class CMPPResponsePacket {
	/**
	 * 包长度,包括包头和包体的总长度
	 */
	protected int totalLength;

	/**
	 * 发送包的命令字id
	 * 
	 * @link #commandID
	 */
	protected int commandID;

	/**
	 * 发送序列号，步长为1,达到最大值后循环使用
	 */
	protected int sequenceID;

	/**
	 * 得到解析后的命令字
	 * 
	 * @return
	 */
	public int getCommandID() {
		return this.commandID;
	}

	/**
	 * 得到解析后的序列号
	 * 
	 * @return
	 */
	public int getSequenceID() {
		return this.sequenceID;
	}

	/**
	 * 得到解析后的总长度
	 * 
	 * @return
	 */
	public int getTotalLength() {
		return this.totalLength;
	}

	/**
	 * 解析返回的输入流（字节形式）
	 * 
	 * @param body
	 */
	public abstract void parseResponseBody(byte[] body);
}