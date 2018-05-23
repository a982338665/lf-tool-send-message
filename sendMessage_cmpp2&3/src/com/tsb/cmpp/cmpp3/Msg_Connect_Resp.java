package com.tsb.cmpp.cmpp3;

public class Msg_Connect_Resp extends Msg_Head {
	private int       status;
	 private byte[]    authenticatorISMG;
	 private byte       version;
	 
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public  byte[] getAuthenticatorISMG() {
		return authenticatorISMG;
	}
	public void setAuthenticatorISMG(byte[] authenticatorISMG) {
		this.authenticatorISMG = authenticatorISMG;
	}
	public byte getVersion() {
		return version;
	}
	public void setVersion(byte version) {
		this.version = version;
	}
}
