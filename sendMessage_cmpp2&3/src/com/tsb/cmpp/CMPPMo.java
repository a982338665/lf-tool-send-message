package com.tsb.cmpp;



/**   
 * @Title: CMPPMo.java 
 * @Package com.sxit.alarm.cmpp 
 * @Description:
 * @author A18ccms A18ccms_gmail_com   
 * @date 2013-11-28 下午09:17:44 
 * @version V1.0   
 */

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsb.cmpp.model.CMPPActive;
import com.tsb.cmpp.model.CMPPDeliver;
import com.tsb.cmpp.model.CMPPException;
import com.tsb.cmpp.model.CMPPReport;
import com.tsb.cmpp.model.CommandID;
import com.tsb.cmpp.model.CMPPSubmitResp;
import com.tsb.ischool.framework.common.SpringApplicationContext;
import com.tsb.ischool.msglog.model.SendMsgLog;
import com.tsb.ischool.msglog.service.ISendMsgLogService;
import com.tsb.ischool.msglog.service.impl.SendMsgReceiptQueue;



/**
 * @ClassName: CMPPMo
 * @Description:
 * @author A18ccms a18ccms_gmail_com
 * @date 2013-11-28 下午09:17:44
 * 
 */
@SuppressWarnings("ALL")
public class CMPPMo extends Thread {
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(CMPPMo.class);

	/**
	 * socket连接
	 */
	private static CMPPSocket socket;

	public static CMPPSocket getSocket() {
		return socket;
	}

	/**
	 * cmpp调用
	 */
	//todo
	private static CMPPService cmpp;

	private static String host = MsgConfig.getIsmgIp();

	private static int port = MsgConfig.getIsmgPort();

	private static String spid = MsgConfig.getSpId();

	private static String password = MsgConfig.getSpSharedSecret();
	/**
	 *  接收response消息的最大返回时间
	 */
	private static int delayTime = MsgConfig.getDelayTime();

//	public CMPPMo(String host, String spid, String password) {
//		this.host = host;
//		this.spid = spid;
//		this.password = password;
//	}
	
	private static boolean flag = false;
	
	private static long rec_returnheartTime = 0;
	private static long serverheartTimeOut = 30000;//相应服务器超时时间30秒
	/**
	 * 服务返回心跳超过30秒
	 * @return
	 */
	public static boolean testRecHeart(){
		long now = System.currentTimeMillis();
		if(rec_returnheartTime != 0 &&( now - rec_returnheartTime>serverheartTimeOut)){
			System.out.println("服务器没有相应时间:"+(now - rec_returnheartTime));
			return true;
		}
		return false;
	}
	// TODO  SendMsgThread3
	private SendMsgThreadHB sendThread;

	// 以上参数也可以从配置文件处取得

	public void run() {
		socket = new CMPPSocket(host, port);
		// Thread thread = null;
		byte[] resppacket = null;
		int status = -1;
		// 初始化socket连接
		while (true) {
			try {
				Thread.sleep(1000);
				boolean initialSock = socket.initialSock();
				if(initialSock){
					// 将socket连接注册到CMPP api中
					// TODO  CMPPService3
					cmpp = new CMPPService(socket, delayTime);
					// 建立和网关的connect连接
					status = cmpp.cmppConnect(spid, password);
				}
				log.info("登陆包返回状态："+ status);
				log.info("the resoponse of connect to ismg:" + status);
				
				if (status == 0) {
					rec_returnheartTime=0;
					log.info("connect successfully!\n");
					flag=true;
					
					sendThread = new SendMsgThreadHB(socket);
					sendThread.SendMsgThreadIswich=true;
					sendThread.start();
				} else {
					log.warn("connect failer,result is:" + status
							+ "，reconnect!\n");
				}
				
				// 发送链路检测包
//				int iii = cmpp.cmppActiveTest();
//				log.info("发送检测包成功=" + iii);
//				log.info("发送检测包成功=" + iii);
			} catch (Exception e) {
				try {
					socket.closeSock();
					log.error("connect exception!sleep 5 seconds");
					log.info("connect exception!sleep 5 seconds");
					Thread.sleep(5000L);
				} catch (Exception ee) {
					log.error("connect close exception:" + ee.toString());
				}
				log.error("TEST"+e.toString());
			}
		
		
		
		

		// 正式接收网关发送过来的
		int count = 0;
		long beginTime = System.currentTimeMillis();
		int commandID = CommandID.CMPP_ACTIVE_TEST;
		while (flag) {
			if(testRecHeart()){
				status = -1;
				flag=false;
				try {
					sendThread.SendMsgThreadIswich=false;
					socket.closeSock();
					break;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// 有数据就读出来，没有数据的话，等待50毫秒
			long now = System.currentTimeMillis();
			try {
				int available = socket.getInputStream().available();
				log.info("available=" + available);
				if (true) {
					resppacket = socket.read(); // 得到输入流字节形式
					if (log.isDebugEnabled()) {
						log.debug("the response body length is："
								+ resppacket.length);
					}
					byte[] commandid = new byte[4];
					System.arraycopy(resppacket, 4, commandid, 0, 4);
					commandID = Common.bytes4ToInt(commandid);
					log.info("commandID:"+commandID);
					switch (commandID) {
					case CommandID.CMPP_DELIVER:
						log.info("revc deliver message");
						deliver(resppacket);
						break;
					case CommandID.CMPP_ACTIVE_TEST:
						rec_returnheartTime =System.currentTimeMillis();//服务器返回心跳时间
						log.info("revc cmppactive message");
						active(resppacket);
						break;
					case CommandID.CMPP_ACTIVE_TEST_RESP:
						rec_returnheartTime =System.currentTimeMillis();//服务器返回心跳时间
						log.info("revc cmppactive resp message");
						break;
					case CommandID.CMPP_DELIVER_RESP:
						log.info("unresonable message");
						break;
					case CommandID.CMPP_SUBMIT_RESP:
						log.info("revc submitresp");
				          CMPPSubmitResp submitResp = new CMPPSubmitResp();
				          if (resppacket.length != submitResp.getTotalLength()) {
				            log.error("CMPP_SUBMIT_RESP 返回包长度不对");
				          }else {
				            submitResp.parseResponseBody(resppacket);
				            if (submitResp.getSequenceID() > 255) {
				                SendMsgLog response = new SendMsgLog();
				                response.setSequenceId(submitResp.getSequenceID());
				                response.setReturnResult(submitResp.result);
								//新增短信服务库的cmpp发送回执状态
							    ISendMsgLogService sendMsgLogService = (ISendMsgLogService)SpringApplicationContext.getBean("sendMsgLogService");
							    sendMsgLogService.insertSequence(response);
							    //通知学酷，短信发送状态
								SendMsgReceiptQueue.getInstance().pushDetry(response);
				            }
				            log.info("CMPP_SUBMIT_RESP,stauts : " + submitResp.result + 
				              " msg_Id：" + Common.bytes4ToInt(submitResp.msg_Id) + 
				              " 序列号sequenceID：" + submitResp.getSequenceID());
				          }
				        break;
					default:
						log.error("wrong commandid:" + commandID);
						break;
					}
				}
				//每隔10秒发送一次
				if (now - beginTime >= 10 * 1000) {
					beginTime = now;
					log.info("send the active packet!");
					int active = cmpp.cmppActiveTestNoResp();
					log.info("the result of sending active packet"
							+ (active == 0 ? "success" : "failer,active="
									+ active));
				} else {
					// 休眠50秒，等待下一个deliver的到来
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						log.error(e.toString());
					}
				}
			}// 如果异常的引起原因是连接关掉了等，则重新建立起连接
			catch (SocketException e) {
				log.error("test1"+e.toString());
				try {
					socket.closeSock();// 关闭连接后在建立连接
					status = -1;
					flag=false;
					sendThread.SendMsgThreadIswich=false;
				} catch (Exception ee) {
					log.error("test12"+ee.toString());
				}
			}catch (CMPPException e) {
				log.error("test2"+e.toString());
			}catch (IOException e) {
				log.error("test4"+e.toString());
			}catch (Exception e) {
				log.error("test"+e.toString());
			}
		}
		
		}

	}

	private static void deliver(byte[] resppacket) throws IOException {
		// 可以在此定义一private的方法，该方法可以为一处理的线程
		// 对的进行处理
		CMPPDeliver deliver = new CMPPDeliver();
		deliver.parseResponseBody(resppacket);

		// 发送deliverresp
		//cmpp.cmppDeliverResp(deliver);
		log.info("deliverresp sending successfully");

		System.out.println("            ID:"
				+ Common.bytes8ToLong(deliver.msg_Id));
		System.out.println("          dest:" + deliver.dest_Id);
		System.out.println("           msg:" + bytes2str(deliver.msg_Content));
		System.out.println("        format:" + deliver.msg_Fmt);
		System.out.println("deliver length:" + deliver.msg_Length);
		System.out.println("      isreport:" + deliver.registered_Delivery);
		System.out.println("      sequence:" + deliver.getSequenceID());
		System.out.println("        report:" + deliver.report);
		System.out.println("            ID:" + bytes2hex(deliver.msg_Id));
		if (deliver.registered_Delivery == 1) {
			CMPPReport report = deliver.report;
			System.out.println("dest_terminal_Id=" + report.dest_terminal_Id);
			System.out.println("          msg_Id=" + report.msg_Id);
			System.out.println("            stat=" + report.stat);
			System.out.println("     submit_time=" + report.submit_time);
			System.out.println("       done_time=" + report.done_time);
			System.out.println("   smsc_sequence=" + report.smsc_sequence);
		}

	}

	private static String bytes2str(byte b[]) {
		if (b == null || b.length == 0)
			return "";
		String str = "";
		for (int i = 0; i < b.length; i++)
			str += b[i] + " ";
		return str;

	}

	private static void active(byte[] resppacket) throws IOException {
		CMPPActive active = new CMPPActive();
		active.parseResponseBody(resppacket);

		log.info("send activeresp");
		cmpp.cmppActiveResp(active);
		log.info("activeresp send successfully");

	}

	private static String bytes2hex(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			sb.append(Common.byte2hex(b[i]) + " ");
		return sb.toString();
	}

	public static void main(String[] args){
//		String url = "218.206.27.231";
//		String spid = "100248";
//		String passwd = "765131";
		CMPPMo cmppMo = new CMPPMo();
		cmppMo.start();
	}

}

