package com.tsb.ischool.msglog.service.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;
import com.tsb.ischool.framework.common.SpringApplicationContext;
import com.tsb.ischool.msglog.model.SendMsgLog;
import com.tsb.ischool.msglog.service.ISendMsgLogService;
import com.tsb.ischool.tmpmsg.model.tmpTubasicMsg;
import com.tsb.ischool.tmpmsg.service.ItmpTubasicMsgService;

/**
 * 发送短信成功队列，更新其状态
 * @author wl
 *
 */
public class SendMsgSuccessQueue{

	private static final Logger logger = Logger
			.getLogger(SendMsgSuccessQueue.class);
	public static final ThreadPoolExecutor threadPollExcutor = new ThreadPoolExecutor(
			10, 30, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
	final static Lock lock = new ReentrantLock();
	private final BlockingQueue<SendMsgLog> SEND_SUCCESS_Queue = new LinkedBlockingQueue<SendMsgLog>();
	private final BlockingQueue<tmpTubasicMsg> SEND_SUCCESS_Queue_Tmp = new LinkedBlockingQueue<tmpTubasicMsg>();
	
	public SendMsgSuccessQueue() {
		popThreadMessage();
	}

	private static SendMsgSuccessQueue instance = new SendMsgSuccessQueue();

	public static synchronized SendMsgSuccessQueue getInstance() {
		if (instance == null) {
			instance = new SendMsgSuccessQueue();
		}
		return instance;
	}

	private void popThreadMessage() {
		// 初始化数据
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						SendMsgLog bean = null;
						try {
							if (SEND_SUCCESS_Queue != null
									&& !SEND_SUCCESS_Queue.isEmpty()) {
								bean = SEND_SUCCESS_Queue.take();
							}
						} catch (Exception e1) {
							bean = null;
						}
						if (bean != null) {
							processHandle(bean);
						}
						
						tmpTubasicMsg tmpTubasic = null;
						try {
							if (SEND_SUCCESS_Queue_Tmp != null
									&& !SEND_SUCCESS_Queue_Tmp.isEmpty()) {
								tmpTubasic = SEND_SUCCESS_Queue_Tmp.take();
							}
						} catch (Exception e1) {
							tmpTubasic = null;
						}
						if (tmpTubasic != null) {
							processHandle_Tmp(tmpTubasic);
						}
						
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
				}
			}
		});
		thread.setDaemon(true);
		thread.setName("SendMsgSuccessQueue");
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();

	}
	

	/**
	 * 
	 * 推送
	 */
	public boolean pushDetry(SendMsgLog response) {
		detryPir(response);
		return true;
	}
	
	
	/**
	 * 
	 * 推送
	 */
	public boolean pushDetry_tmp(tmpTubasicMsg response) {
		detryPir_tmp(response);
		return true;
	}

	/**
	 * 
	 * 调度任务
	 */
	private void detryPir(final SendMsgLog response) {
		try {
			SEND_SUCCESS_Queue.put(response);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	
	/**
	 * 
	 * 调度任务
	 */
	private void detryPir_tmp(final tmpTubasicMsg response) {
		try {
			SEND_SUCCESS_Queue_Tmp.put(response);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

	/**
	 * 
	 * 更新状态
	 * 
	 */
	private boolean processHandle(SendMsgLog bean) {
			try {
				ISendMsgLogService sendMsgLogService  = (ISendMsgLogService)SpringApplicationContext.getBean("sendMsgLogService");
				sendMsgLogService.update(bean);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("更新sendMsgLog状态失败:" + e.getMessage());
				return false;
			} 
			return true;
	}
	
	/**
	 * 
	 * 更新状态
	 * 
	 */
	private boolean processHandle_Tmp(tmpTubasicMsg bean) {
			try {
				ItmpTubasicMsgService tmpTubasicMsgService  = (ItmpTubasicMsgService)SpringApplicationContext.getBean("tmpTubasicMsgService");
				tmpTubasicMsgService.update(bean);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("更新tmpTubasicMsg状态失败:" + e.getMessage());
				return false;
			} 
			return true;
	}
	
	

}
