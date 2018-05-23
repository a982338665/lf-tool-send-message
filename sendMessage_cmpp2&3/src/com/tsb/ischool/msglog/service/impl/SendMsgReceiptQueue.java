package com.tsb.ischool.msglog.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;

import com.tsb.ischool.framework.common.HttpUtil;
import com.tsb.ischool.framework.common.PropertyUtil;
import com.tsb.ischool.framework.common.SpringApplicationContext;
import com.tsb.ischool.msglog.model.SendMsgLog;
import com.tsb.ischool.msglog.service.ISendMsgLogService;
import com.tsb.ischool.sms.model.SmsReceipt;
import com.tsb.ischool.tmpmsg.model.tmpTubasicMsg;
import com.tsb.ischool.tmpmsg.service.ItmpTubasicMsgService;
import com.tsb.ischool.utils.JsonUtil;

/**
 * 发送短信回执
 * @author wl
 *
 */
public class SendMsgReceiptQueue{

	private static final Logger logger = Logger
			.getLogger(SendMsgReceiptQueue.class);
	final static Lock lock = new ReentrantLock();
	private final BlockingQueue<SendMsgLog> SEND_RECEIPT_QUEUE = new LinkedBlockingQueue<SendMsgLog>();
	
	public SendMsgReceiptQueue() {
		popThreadMessage();
	}

	private static SendMsgReceiptQueue instance = new SendMsgReceiptQueue();

	public static synchronized SendMsgReceiptQueue getInstance() {
		if (instance == null) {
			instance = new SendMsgReceiptQueue();
		}
		return instance;
	}

	//private volatile boolean noStopRequested=true;  
	
	private void popThreadMessage() {
		// 初始化数据
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				SendMsgLog bean = null;
				while (true) {
					try {
						try {
							if (SEND_RECEIPT_QUEUE != null
									&& !SEND_RECEIPT_QUEUE.isEmpty()) {
								bean = SEND_RECEIPT_QUEUE.take();
								processHandle(bean);
							}
						} catch (Exception e1) {
							bean = null;
						}
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
				}
			}
		});
		thread.setDaemon(true);
		thread.setName("SendMsgReceiptQueue");
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
	 * 调度任务
	 */
	private void detryPir(final SendMsgLog response) {
		try {
		   SEND_RECEIPT_QUEUE.put(response);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	private static String httpUrlReceipt = PropertyUtil.getProperty("update_sms_receipt");//学酷更新回执接口

	/**
	 * 
	 * 更新状态
	 * 
	 */
	private boolean processHandle(SendMsgLog bean) {
			try {
			    List<SmsReceipt> list = new ArrayList<SmsReceipt>();
			    SmsReceipt smsReceipt = new SmsReceipt();
			    smsReceipt.setRunningnumber(bean.getSequenceId());
			    if(bean.getReturnResult()==0){
			    	smsReceipt.setStatus(1);//发送成功
			    }else{
			    	smsReceipt.setStatus(2);//发送失败
			    }
			    list.add(smsReceipt);
			    String json=JsonUtil.buildeJson(list);
			     //通知学酷，短信发送状态
				HttpUtil.sendRequest(httpUrlReceipt, "POST", json);
			} catch (Exception e) {
//				e.printStackTrace();
				logger.error("更新sendMsgLog状态失败:" + e.getMessage()+"//TSB_ISCHOOL_MOOC_SERVER/sendmsg/updatesmsreceipt");
				return false;
			} 
			return true;
	}
}
