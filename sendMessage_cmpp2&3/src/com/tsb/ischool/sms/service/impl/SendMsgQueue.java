package com.tsb.ischool.sms.service.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.tsb.cmpp2.msg.util.MsgContainer;
import com.tsb.cmpp2.msg.util.MsgContainerNew;
import com.tsb.ischool.framework.common.SpringApplicationContext;
import com.tsb.ischool.msglog.model.SendMsgLog;
import com.tsb.ischool.msglog.service.ISendMsgLogService;
import com.tsb.ischool.sms.model.MobileMessage;
import com.tsb.ischool.tmpmsg.service.ItmpTubasicMsgService;

/**
 * 发送短信队列---单例
 */
public class SendMsgQueue{


	private static final Logger logger = Logger.getLogger(SendMsgQueue.class);
	public static final ThreadPoolExecutor threadPollExcutor = new ThreadPoolExecutor(
			10, 30, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
	//优先发送
	public static final BlockingQueue<MobileMessage> arrayPIR_Queue = new LinkedBlockingQueue<MobileMessage>();
	//批量发送（慢于优先）
	public static final BlockingQueue<MobileMessage> arrayPIR_Queue_Slow_sending = new LinkedBlockingQueue<MobileMessage>();

	public SendMsgQueue() {
	}

	/**
	 * 构造方法私有化-饿汉式单例
	 */
	private static SendMsgQueue instance = new SendMsgQueue();

	public static synchronized SendMsgQueue getInstance() {
		if (instance == null) {
			instance = new SendMsgQueue();
		}
		return instance;
	}

	/**
	 * 
	 * 高优先级推送
	 */
	public boolean pushDetry(MobileMessage response) {
//		logger.info("put短信到队列，手机号为："+response.getMobileNo()+",发送内容为："+response.getMessageContent());
//		logger.debug("put短信到队列，手机号为："+response.getMobileNo()+",发送内容为："+response.getMessageContent());
		if(arrayPIR_Queue!=null){
//			logger.info("put短信到队列，目前队列数为："+arrayPIR_Queue.size());
			logger.debug("put短信到队列，目前队列数为："+arrayPIR_Queue.size());
		}
		detryPir(response);
		return true;
	}
	
	/**
	 * 
	 * 低优先级推送
	 */
	public boolean pushPrioritySending(MobileMessage response) {
		logger.info("put批量短信到队列，手机号为："+response.getMobileNo()+",发送内容为："+response.getMessageContent());
		if(arrayPIR_Queue_Slow_sending!=null){
			logger.info("put短信到队列，目前队列数为："+arrayPIR_Queue_Slow_sending.size());
		}
		detrySlowSending(response);
		return true;
	}

	/**
	 * 
	 * 调度任务
	 */
	private void detryPir(final MobileMessage response) {
		try {
			insertMsgLog(response);
			logger.info("添加短信日志结束："+arrayPIR_Queue.size());
			arrayPIR_Queue.put(response);
			logger.info("put短信到队列完成,目前队列数为："+arrayPIR_Queue.size());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	/**
	 * 
	 * 调度任务
	 */
	private void detrySlowSending(final MobileMessage response) {
		try {
			arrayPIR_Queue_Slow_sending.put(response);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	private void insertMsgLog(MobileMessage response){
		try {
			if(!"1".equals(response.getId())&&"1".equals(response.getType())){
				ISendMsgLogService sendMsgLogService  = (ISendMsgLogService)SpringApplicationContext.getBean("sendMsgLogService");
				SendMsgLog bean = new SendMsgLog();
				bean.setId(response.getId());
				bean.setSendStatus(0);
				bean.setMobile(response.getMobileNo());
				bean.setContextWs(response.getMessageContent());
				bean.setSequenceId(response.getSequenceId());
				sendMsgLogService.insert(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 暂时记录，没有token的用户发短信失败
	 * @param response
	 */
	public void pushFail(MobileMessage response) {
		try {
			//没有token存2
			if(!"1".equals(response.getId())&&"1".equals(response.getType())){
				ISendMsgLogService sendMsgLogService  = (ISendMsgLogService)SpringApplicationContext.getBean("sendMsgLogService");
				SendMsgLog bean = new SendMsgLog();
				bean.setId(response.getId());
				bean.setSendStatus(2);
				bean.setMobile(response.getMobileNo());
				bean.setContextWs(response.getMessageContent());
				bean.setSequenceId(response.getSequenceId());
				sendMsgLogService.insert(bean);
			//没有对应的联创学士id存3
			}else
			if(!"1".equals(response.getId())&&"2".equals(response.getType())){
				ISendMsgLogService sendMsgLogService  = (ISendMsgLogService)SpringApplicationContext.getBean("sendMsgLogService");
				SendMsgLog bean = new SendMsgLog();
				bean.setId(response.getId());
				bean.setSendStatus(3);
				bean.setMobile(response.getMobileNo());
				bean.setContextWs(response.getMessageContent());
				bean.setSequenceId(response.getSequenceId());
				sendMsgLogService.insert(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
