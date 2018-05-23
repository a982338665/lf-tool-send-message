package com.tsb.cmpp.cmpp3;

public class Msg_Head {
	private int msg_length;
	private int msg_command;
	private int msg_squence;
	public int getMsg_length() {
		return msg_length;
	}
	public void setMsg_length(int msg_length) {
		this.msg_length = msg_length;
	}
	public int getMsg_command() {
		return msg_command;
	}
	public void setMsg_command(int msg_command) {
		this.msg_command = msg_command;
	}
	public int getMsg_squence() {
		return msg_squence;
	}
	public void setMsg_squence(int msg_squence) {
		this.msg_squence = msg_squence;
	}
	
}
