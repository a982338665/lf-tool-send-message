package com.tsb.cmpp.cmpp3;

public class Msg_Connect extends Msg_Head {
	//Դ��ַ
	private String  source_Addr;
    //����Դ��ַ
	private byte[]  authenticatorSource;
	//�汾
	private byte version;
	//ʱ��
	private int timestamp;
	
	
	public String getSource_Addr() {
		return source_Addr;
	}
	public void setSource_Addr(String source_Addr) {
		this.source_Addr = source_Addr;
	}
	public byte[] getAuthenticatorSource() {
		return authenticatorSource;
	}
	public void setAuthenticatorSource(byte[] authenticatorSource) {
		this.authenticatorSource = authenticatorSource;
	}
	public byte getVersion() {
		return version;
	}
	public void setVersion(byte version) {
		this.version = version;
	}
	public int getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
	
	
	
}
