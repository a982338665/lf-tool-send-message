package com.tsb.cmpp.cmpp3;

import com.tsb.cmpp.CMPPSocket;
import com.tsb.ischool.msglog.model.SendMsgLog;
import com.tsb.ischool.msglog.service.impl.SendMsgSuccessQueue;
import com.tsb.ischool.sms.model.MobileMessage;
import com.tsb.ischool.sms.service.impl.SendMsgQueue;
import com.tsb.ischool.tmpmsg.model.tmpTubasicMsg;

@SuppressWarnings("ALL")
public class SendMsgThread3 extends Thread{

	public  boolean SendMsgThreadIswich=false;
	private CMPPSocket msgSocket;
	
	private String mobile;
	private String msg;
	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public SendMsgThread3(CMPPSocket msgSocket){
		this.msgSocket=msgSocket;
	}
	
	@Override
	public void run() {
		while(SendMsgThreadIswich){
			MobileMessage bean = null;
			try {
				if (SendMsgQueue.arrayPIR_Queue != null
						&& !SendMsgQueue.arrayPIR_Queue.isEmpty()
						&&msgSocket!=null&&msgSocket.getInputStream()!=null
						&&msgSocket.getOutStream()!=null) {
					bean = SendMsgQueue.arrayPIR_Queue.take();
				}
				if(bean==null){//arrayPIR_Queue队列为空时，发送arrayPIR_Queue_Slow_sending队列
					if (SendMsgQueue.arrayPIR_Queue_Slow_sending != null
							&& !SendMsgQueue.arrayPIR_Queue_Slow_sending.isEmpty()
							&&msgSocket!=null&&msgSocket.getInputStream()!=null
							&&msgSocket.getOutStream()!=null) {
						bean = SendMsgQueue.arrayPIR_Queue_Slow_sending.take();
					}
				}
			} catch (Exception e1) {
				bean = null;
			}
			if (bean != null) {
				MsgContainerNew3 instance = MsgContainerNew3.getInstance();
				instance.setMsgSocket(msgSocket);
				boolean result = instance.sendMsg(bean.getSequenceId(),bean.getMessageContent(),bean.getMobileNo());
				try {
					Thread.sleep(55);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(!result&&bean.getSendFailCount()<3){//失败3次，不在往队列放
					bean.setType("2");//设置成2的话，后面就不再插入send_msg_log表
					bean.setSendFailCount(bean.getSendFailCount()+1);
					if(bean!=null&&bean.getId()!=null&&"1".equals(bean.getId())){
						SendMsgQueue.getInstance().pushPrioritySending(bean);
					}else{
						SendMsgQueue.getInstance().pushDetry(bean);
					}
				}
				if(result){
					//如果是发送tmp_tubasic_msg表中的数据，并且发送成功的话
					if(bean!=null&&bean.getId()!=null&&"1".equals(bean.getId())){
						try {
							tmpTubasicMsg ttm = new tmpTubasicMsg();
							ttm.setMobile(bean.getMobileNo());
							ttm.setSendStatus(1);
							SendMsgSuccessQueue.getInstance().pushDetry_tmp(ttm);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if(bean!=null&&bean.getId()!=null&&!"1".equals(bean.getId())){
						SendMsgLog response = new SendMsgLog();
						response.setId(bean.getId());
						response.setSendStatus(1);
						SendMsgSuccessQueue.getInstance().pushDetry(response);
					}
				}
			}else{
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
