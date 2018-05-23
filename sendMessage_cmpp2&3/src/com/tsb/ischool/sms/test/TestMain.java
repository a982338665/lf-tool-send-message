package com.tsb.ischool.sms.test;

import com.tsb.cmpp2.msg.util.MsgContainer;

public class TestMain {
	
	public static void main(String[] args) {
		MsgContainer.sendMsg("您好！这里是中国移动客服，欢迎致电。。。", "15910837285");
	}

}
