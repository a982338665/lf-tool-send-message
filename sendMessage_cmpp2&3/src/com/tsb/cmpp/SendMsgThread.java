package com.tsb.cmpp;

import java.util.TreeMap;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.StringUtils;

import com.tsb.ischool.msglog.model.SendMsgLog;
import com.tsb.ischool.msglog.service.impl.SendMsgSuccessQueue;
import com.tsb.ischool.sms.model.MobileMessage;
import com.tsb.ischool.sms.service.impl.SendMsgQueue;
import com.tsb.ischool.tmpmsg.model.tmpTubasicMsg;
import com.tsb.ischool.utils.ConstantParam;
import com.tsb.ischool.utils.Encrypt;
import com.tsb.ischool.utils.HttpUrlClient;
import com.tsb.ischool.utils.OAuthUtil;


public class SendMsgThread extends Thread{

	public  boolean SendMsgThreadIswich=false;

	private String mobile;
	private String msg;
	
	public SendMsgThread(CMPPSocket cmpp) {
		
	}
	
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

	public SendMsgThread(){
	}
	
	private ObjectMapper jsonMapper = new ObjectMapper();
	
	@Override
	public void run() {
		while(SendMsgThreadIswich){
			MobileMessage bean = null;
			try {
				if (SendMsgQueue.arrayPIR_Queue != null
						&& !SendMsgQueue.arrayPIR_Queue.isEmpty()
						) {
					bean = SendMsgQueue.arrayPIR_Queue.take();
				}
				if(bean==null){//arrayPIR_Queue队列为空时，发送arrayPIR_Queue_Slow_sending队列
					if (SendMsgQueue.arrayPIR_Queue_Slow_sending != null
							&& !SendMsgQueue.arrayPIR_Queue_Slow_sending.isEmpty()
							) {
						bean = SendMsgQueue.arrayPIR_Queue_Slow_sending.take();
					}
				}
				System.out.println("从队列中获取的待发送短信信息为 ： " + jsonMapper.writeValueAsString(bean));
			} catch (Exception e1) {
				bean = null;
			}
			if (bean != null) {
				//TODO 包装数据调用联创短信发送接口
				String token = null;
				String openid = null;
	  			TreeMap<String, String> treeMap = new TreeMap<String, String>();
	  			try {
		  			 token = Encrypt.encryptDESCBC(bean.getAccessToken(), ConstantParam.APP_KEY, ConstantParam.IV);
					 openid = Encrypt.encryptDESCBC(bean.getOpenId(), ConstantParam.APP_KEY, ConstantParam.IV);
				} catch (Exception e3) {
					e3.printStackTrace();
				}

	  			treeMap.put("access_token", token);
	  			treeMap.put("open_id", openid);
	 			treeMap.put("client_id", ConstantParam.APP_ID);
	  			treeMap.put("format","json");
	 			treeMap.put("msg_type", "14");
	 			treeMap.put("send_mode", "0");
	 			treeMap.put("receive_id",bean.getReceiveId());
	 			treeMap.put("message_content",bean.getMessageContent());
	 			treeMap.put("state", String.valueOf(System.currentTimeMillis()));
	 			String sig = OAuthUtil.makeSig(treeMap, ConstantParam.APP_KEY);
				treeMap.put("sig", sig);

				try {
					System.out.println("调用远程接口参数为 ： " + jsonMapper.writeValueAsString(treeMap));
				} catch (Exception e2) {
					e2.printStackTrace();
				}

				String str = HttpUrlClient.doPost(ConstantParam.AUTH_URL, treeMap);






				// System.out.println(str);
				String result = null;
				try {
					result = Encrypt.decryptDESCBC(str, ConstantParam.APP_KEY);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				System.out.println("调用远程接口进行短信发送，返回值为：" + result);

//				MsgContainerNew instance = MsgContainerNew.getInstance();
//				instance.setMsgSocket(msgSocket);
//				boolean result = instance.sendMsg(bean.getSequenceId(),bean.getMessageContent(),bean.getMobileNo());
				try {
					Thread.sleep(55);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(StringUtils.isEmpty(result)&&bean.getSendFailCount()<3){//失败3次，不在往队列放
					bean.setType("2");//设置成2的话，后面就不再插入send_msg_log表
					bean.setSendFailCount(bean.getSendFailCount()+1);
					if(bean!=null&&bean.getId()!=null&&"1".equals(bean.getId())){
						SendMsgQueue.getInstance().pushPrioritySending(bean);
					}else{
						SendMsgQueue.getInstance().pushDetry(bean);
					}
				}
				if(!StringUtils.isEmpty(result)){
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
