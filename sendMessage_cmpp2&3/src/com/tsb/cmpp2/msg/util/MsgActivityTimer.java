package com.tsb.cmpp2.msg.util;


/**
 * 接口调用
 * @author zzl
 * 2016-07-20 16:30
 *
 */
public class MsgActivityTimer {
	/**
	 * 短信接口长链接，定时进行链路检查
	 */
	public void executeInternal() {
			System.out.println("×××××××××××××开始链路检查××××××××××××××");
			int count=0;
			boolean result=MsgContainer.activityTestISMG();
			while(!result){
				count++;
				result=MsgContainer.activityTestISMG();
				if(count>=(MsgConfig.getConnectCount()-1)){//如果再次链路检查次数超过两次则终止连接
					MsgContainer.setFlag(false);
					MsgContainer.cancelISMG();
					MsgContainer.getSocketInstance();
				}
			}
			System.out.println("×××××××××××××链路检查结束××××××××××××××");
	}
}
