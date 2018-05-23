package com.tsb.cmpp2.msg.domain;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

/**
 * 
 * 链路检查消息结构定义
 * @author zzl
 * 2016-07-20 16:30
 */
public class MsgActiveTestResp extends MsgHead {
	private static Logger logger=Logger.getLogger(MsgActiveTestResp.class);
	private byte reserved;//
	public MsgActiveTestResp(byte[] data){
		try {
			logger.info("链路检查,解析数据包,内容为:"+new String(data,"utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		if(data.length==12||data.length==8){
			ByteArrayInputStream bins=new ByteArrayInputStream(data);
			DataInputStream dins=new DataInputStream(bins);
			try {
				this.setTotalLength(data.length+1);
				this.setCommandId(MsgCommand.CMPP_ACTIVE_TEST_RESP);
				this.setSequenceId(dins.readInt());
				this.reserved=(byte)0000;
				dins.close();
				bins.close();
			} catch (IOException e){}
		}else{
			logger.info("链路检查,解析数据包出错，包长度不一致。长度为:"+data.length);
		}
	}
	public byte getReserved() {
		return reserved;
	}

	public void setReserved(byte reserved) {
		this.reserved = reserved;
	}
}
