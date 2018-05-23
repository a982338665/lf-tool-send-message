package com.tsb.cmpp2.msg.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsb.cmpp2.msg.domain.MsgActiveTestResp;
import com.tsb.cmpp2.msg.domain.MsgCommand;
import com.tsb.cmpp2.msg.domain.MsgConnect;
import com.tsb.cmpp2.msg.domain.MsgConnectResp;
import com.tsb.cmpp2.msg.domain.MsgDeliver;
import com.tsb.cmpp2.msg.domain.MsgDeliverResp;
import com.tsb.cmpp2.msg.domain.MsgHead;
import com.tsb.cmpp2.msg.domain.MsgSubmit;
import com.tsb.cmpp2.msg.domain.MsgSubmitResp;

/**
 * 短信接口容器，单例获得链接对象
 * 
 * @author zzl 2016-07-20 16:30
 */
public class MsgContainer {
	private static Log log = LogFactory.getLog(MsgContainer.class);
	private static Socket msgSocket = null;
	private static  DataInputStream in;
	private static DataOutputStream out;

	public static DataInputStream getSocketDIS() {
		 try {
			 in = new DataInputStream(MsgContainer.getSocketInstance()
			 .getInputStream());
			 } catch (IOException e) {
			 in = null;
		 }
		return in;
	}

	public static DataOutputStream getSocketDOS() {
		 try {
			 out = new DataOutputStream(MsgContainer.getSocketInstance()
			 .getOutputStream());
			 } catch (IOException e) {
			 out = null;
		 }
		return out;
	}

	private static boolean flag = false;//控制登录包
	private static boolean initFlag = true;//控制心跳


	public static boolean isFlag() {
		return flag;
	}

	public static void setFlag(boolean flag) {
		MsgContainer.flag = flag;
	}

	public static Socket getSocketInstance() {
		log.info("msgSocket：" + msgSocket);
		if (msgSocket != null) {
			log.info("msgSocket.isClosed()：" + msgSocket.isClosed());
			log.info("msgSocket.isConnected()：" + msgSocket.isConnected());
		}
		try {
			// while (true) {
			if (null == msgSocket || msgSocket.isClosed()
					|| !msgSocket.isConnected()) {
				msgSocket = new Socket(MsgConfig.getIsmgIp(),
						MsgConfig.getIsmgPort());
				msgSocket.setKeepAlive(true);
				msgSocket.setSoTimeout(MsgConfig.getTimeOut());
				if (msgSocket != null) {
					in = new DataInputStream(msgSocket.getInputStream());
					out = new DataOutputStream(msgSocket.getOutputStream());
				}
//				if (!flag) {
					int count = 0;
					boolean result = connectISMG();
					while (!result) {
						count++;
						result = connectISMG();
						if (count >= (MsgConfig.getConnectCount() - 1)) {// 如果再次连接次数超过两次则终止连接
							break;
						}
					}
//				}
			}
			// heart();
			// }
		} catch (UnknownHostException e) {
			log.error("Socket链接短信网关端口号不正确：" + e.getMessage());
			// 链接短信网关
		} catch (IOException e) {
			log.error("Socket链接短信网关失败：" +MsgConfig.getIsmgIp()+":"+
					MsgConfig.getIsmgPort()+"  "+ e.getMessage());
		}

		return msgSocket;
	}

	private static void heart() {
		log.info("开始心跳");
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						if (msgSocket != null) {
							in = new DataInputStream(msgSocket.getInputStream());
							out = new DataOutputStream(msgSocket.getOutputStream());
						}
						long now = System.currentTimeMillis();
						int available = in.available();
						if (available > 0) {
							long beginTime = System.currentTimeMillis();
							int commandID = MsgCommand.CMPP_ACTIVE_TEST;
							int len = in.readInt();
							log.info(Thread.currentThread().getName()+"心跳------------------||||"+len);

							if (null != in && 0 != len) {
								log.info("获取心跳信息:" + len);
								byte[] returnData = getInData();
								if(returnData!=null)
								log.info("returnData:" + returnData.length);
								if (returnData!=null&&returnData.length >= 8) {
									MsgHead head = new MsgHead(returnData);
									log.info("head.getCommandId():" + head.getCommandId());
									switch (head.getCommandId()) {
									case MsgCommand.CMPP_CONNECT_RESP:
										MsgConnectResp connectResp = new MsgConnectResp(
												returnData);
										log.info("CMPP_CONNECT_RESP heart,status :"
												+ connectResp.getStatus()
												+ " 序列号："
												+ connectResp.getSequenceId());
										break;
									case MsgCommand.CMPP_ACTIVE_TEST:
										MsgActiveTestResp activeResp = new MsgActiveTestResp(
												returnData);
										log.info("短信网关与短信网关进行连接检查2" + " 序列号："
												+ activeResp.getSequenceId());
										out.write(activeResp.toByteArray());// 发送服务器
										out.flush();
										break;
									case MsgCommand.CMPP_SUBMIT_RESP:
										MsgSubmitResp submitResp = new MsgSubmitResp(
												returnData);
										log.info("CMPP_SUBMIT_RESP,stauts : "
												+ submitResp.getResult()
												+ " 序列号："
												+ submitResp.getSequenceId());
										break;
									case MsgCommand.CMPP_TERMINATE_RESP:
										log.info("拆除与ISMG的链接" + " 序列号："
												+ head.getSequenceId());
										break;
									case MsgCommand.CMPP_CANCEL_RESP:
										log.info("CMPP_CANCEL_RESP 序列号："
												+ head.getSequenceId());
										break;
									case MsgCommand.CMPP_CANCEL:
										log.info("CMPP_CANCEL 序列号："
												+ head.getSequenceId());
										break;
									case MsgCommand.CMPP_DELIVER:
										MsgDeliver msgDeliver = new MsgDeliver(
												returnData);
										if (msgDeliver.getResult() == 0) {
											log.info("CMPP_DELIVER 序列号："
													+ head.getSequenceId()
													+ "，是否消息回复"
													+ (msgDeliver
															.getRegistered_Delivery() == 0 ? "不是,消息内容："
															+ msgDeliver
																	.getMsg_Content()
															: "是，目的手机号："
																	+ msgDeliver
																			.getDest_terminal_Id()));
										} else {
											log.info("CMPP_DELIVER 序列号："
													+ head.getSequenceId());
										}
										MsgDeliverResp msgDeliverResp = new MsgDeliverResp();
										msgDeliverResp
												.setTotalLength(12 + 8 + 4);
										msgDeliverResp
												.setCommandId(MsgCommand.CMPP_DELIVER_RESP);
										msgDeliverResp.setSequenceId(MsgUtils
												.getSequence());
										msgDeliverResp.setMsg_Id(msgDeliver
												.getMsg_Id());
										msgDeliverResp.setResult(msgDeliver
												.getResult());
										out.write(msgDeliverResp.toByteArray());// 发送服务器
										out.flush();
										break;
									case MsgCommand.CMPP_DELIVER_RESP:
										log.info("CMPP_DELIVER_RESP 序列号："
												+ head.getSequenceId());
										break;
									case MsgCommand.CMPP_QUERY:
										log.info("CMPP_QUERY 序列号："
												+ head.getSequenceId());
										break;
									case MsgCommand.CMPP_QUERY_RESP:
										log.info("CMPP_QUERY_RESP 序列号："
												+ head.getSequenceId());
										break;
									case MsgCommand.CMPP_TERMINATE:
										log.info("CMPP_TERMINATE 序列号："
												+ head.getSequenceId());
										break;
									case MsgCommand.CMPP_CONNECT:
										log.info("CMPP_CONNECT 序列号："
												+ head.getSequenceId());
										break;
									case MsgCommand.CMPP_ACTIVE_TEST_RESP:
										log.info("CMPP_ACTIVE_TEST 序列号："
												+ head.getSequenceId());
										break;
									case MsgCommand.CMPP_SUBMIT:
										log.info("CMPP_SUBMIT 序列号："
												+ head.getSequenceId());
										break;
									default:
										log.error("无法解析IMSP返回的包结构：heart 包长度为"
												+ head.getTotalLength());
										break;
									}
								}
							}// 如果1分钟之内没有输入流到达，则发送active包
							else if (commandID != MsgCommand.CMPP_ACTIVE_TEST
									&& now - beginTime >= 10 * 1000) {
								beginTime = now;
								log.info("send the active packet!");
								byte[] data = new byte[12];
								MsgActiveTestResp activeResp = new MsgActiveTestResp(
										data);
								activeResp.setTotalLength(13);
								activeResp.setCommandId(MsgCommand.CMPP_ACTIVE_TEST);
								activeResp.setReserved((byte) 0000);
								activeResp.setSequenceId(MsgUtils.getSequence());
								out.write(activeResp.toByteArray());// 发送服务器
								out.flush();
							} else {
								// 休眠50秒，等待下一个deliver的到来
								try {
									Thread.sleep(50);
								} catch (InterruptedException e) {
									log.error(e.toString());
								}
							}
						}
					} catch (Exception e) {
						log.info("心跳报错："+e.getMessage());
						getSocketInstance();
						flag=false;
					}
				}
			}
		}).start();
	}

	/**
	 * 创建Socket链接后请求链接ISMG
	 * 
	 * @return
	 */
	private static boolean connectISMG() {
		log.info("请求连接到ISMG...");
		MsgConnect connect = new MsgConnect();
		connect.setTotalLength(12 + 6 + 16 + 1 + 4);// 消息总长度，级总字节数:4+4+4(消息头)+6+16+1+4(消息主体)
		connect.setCommandId(MsgCommand.CMPP_CONNECT);// 标识创建连接
		connect.setSequenceId(MsgUtils.getSequence());// 序列，由我们指定
		connect.setSourceAddr(MsgConfig.getSpId());// 我们的企业代码
		String timestamp = MsgUtils.getTimestamp();
		connect.setAuthenticatorSource(MsgUtils.getAuthenticatorSource(
				MsgConfig.getSpId(), MsgConfig.getSpSharedSecret(), timestamp));// md5(企业代码+密匙+时间戳)
		connect.setTimestamp(Integer.parseInt(timestamp));// 时间戳(MMDDHHMMSS)
		connect.setVersion((byte) 0x20);// 版本号 高4bit为2，低4位为0
		List<byte[]> dataList = new ArrayList<byte[]>();
		dataList.add(connect.toByteArray());
		CmppSender sender = new CmppSender(getSocketDOS(), getSocketDIS(),
				dataList);
		try {
			boolean success = sender.start();
			if (success) {
				log.info("请求连接到ISMG...连接成功！" + success);
				flag = true;
				return true;
			} else {
				log.info("请求连接到ISMG...连接失败！" + success);
			}
			return false;
		} catch (Exception e) {
			log.info("请求连接到ISMG...连接失败！" + e.getMessage());
			// try {
			// out.close();
			// } catch (IOException e1) {
			// out=null;
			// }
			return false;
		}
	}

	public static boolean sendMsg(String msg, String cusMsisdn) {
		try {
			if (msg.length() < 70) {// 短短信
				log.info("开始短短信："+msg+"|"+cusMsisdn);
				boolean result = sendShortMsg(msg, cusMsisdn);
				int count = 0;
				while (!result) {
					count++;
					result = sendShortMsg(msg, cusMsisdn);
					if (count >= (MsgConfig.getConnectCount() - 1)) {// 如果再次连接次数超过两次则终止连接
						break;
					}
				}
				return result;
			} else {// 长短信
				log.info("开始长短信："+msg+"|"+cusMsisdn);
				boolean result = sendLongMsg2(msg, cusMsisdn);
				int count = 0;
				while (!result) {
					count++;
					result = sendLongMsg2(msg, cusMsisdn);
					if (count >= (MsgConfig.getConnectCount() - 1)) {// 如果再次连接次数超过两次则终止连接
						break;
					}
				}
				return result;
			}
		} catch (Exception e) {
			// try {
			// if(out!=null){
			// out.close();
			// }
			// } catch (IOException e1) {
			// out=null;
			// }
			return false;
		}
	}

	/**
	 * 发送web push短信
	 * 
	 * @param url
	 *            wap网址
	 * @param desc
	 *            描述
	 * @param cusMsisdn
	 *            短信
	 * @return
	 */
	public static boolean sendWapPushMsg(String url, String desc,
			String cusMsisdn) {
		try {
			int msgContent = 12 + 9 + 9 + url.getBytes("utf-8").length + 3
					+ desc.getBytes("utf-8").length + 3;
			if (msgContent < 140) {
				boolean result = sendShortWapPushMsg(url, desc, cusMsisdn);
				int count = 0;
				while (!result) {
					count++;
					result = sendShortWapPushMsg(url, desc, cusMsisdn);
					if (count >= (MsgConfig.getConnectCount() - 1)) {// 如果再次连接次数超过两次则终止连接
						break;
					}
				}
				return result;
			} else {
				boolean result = sendLongWapPushMsg(url, desc, cusMsisdn);
				int count = 0;
				while (!result) {
					count++;
					result = sendLongWapPushMsg(url, desc, cusMsisdn);
					if (count >= (MsgConfig.getConnectCount() - 1)) {// 如果再次连接次数超过两次则终止连接
						break;
					}
				}
				return result;
			}
		} catch (Exception e) {
			try {
				out.close();
			} catch (IOException e1) {
				out = null;
			}
			log.error("发送web push短信:" + e.getMessage());
			return false;
		}
	}

	
	/**
	 * 发送短短信
	 * 
	 * @return
	 */
	private static boolean sendShortMsg(String msg, String cusMsisdn) {
		try {
			log.info("+++发送短短信："+msg+"|"+cusMsisdn);

			int seq = MsgUtils.getSequence();
			byte[] msgByte = msg.getBytes("gb2312");
			MsgSubmit submit = new MsgSubmit();
			// 12+8+1+1+1+1+10+1+21+1+1+1+6+2+6+17+17+21+1+21+1+8
			submit.setTotalLength(159 + msgByte.length);
			submit.setCommandId(MsgCommand.CMPP_SUBMIT);
			submit.setSequenceId(seq);
			submit.setPkTotal((byte) 0x01);
			submit.setPkNumber((byte) 0x01);
			submit.setRegisteredDelivery((byte) 0x00);
			submit.setMsgLevel((byte) 0x01);
			submit.setFeeUserType((byte) 0x00);
			submit.setFeeTerminalId("");
			submit.setTpPId((byte) 0x00);
			submit.setTpUdhi((byte) 0x00);
			submit.setMsgFmt((byte) 0x0f);
			submit.setMsgSrc(MsgConfig.getSpId());
			submit.setSrcId(MsgConfig.getSpCode());
			submit.setDestTerminalId(cusMsisdn);
			submit.setMsgLength((byte) msgByte.length);
			submit.setMsgContent(msgByte);
//			submit.setFeeTerminalId(cusMsisdn);
//			submit.setServiceId(MsgConfig.getServiceId());// serviceId
															// 以及计费号FeeTerminalId
															// 由联创追踪发现少了这两个参数，by
															// Dongrisheng
															// 2016-10-11
			log.info("sendShortMsg mobile："+submit.toString());
			List<byte[]> dataList = new ArrayList<byte[]>();
			dataList.add(submit.toByteArray());

			msgSocket = MsgContainer.getSocketInstance();

			log.info("heart 开始");
			if (initFlag) {
				heart();
				initFlag = false;
			}
			log.info("heart 结束");
			
//			log.info("sendShortMsg msgSocket1：" + msgSocket);
			if (msgSocket != null) {
				log.info("sendShortMsg msgSocket1.isClosed()："
						+ msgSocket.isClosed());
				log.info("sendShortMsg msgSocket1.isConnected()："
						+ msgSocket.isConnected());
			}
			CmppSender sender = new CmppSender(getSocketDOS(), getSocketDIS(),
					dataList);
			log.info("向手机号码：" + cusMsisdn + "下发短短信，序列号为:" + seq);

			log.info("sendShortMsg msgSocket2：" + msgSocket);
			if (msgSocket != null) {
				log.info("sendShortMsg msgSocket2.isClosed()："
						+ msgSocket.isClosed());
				log.info("sendShortMsg msgSocket2.isConnected()："
						+ msgSocket.isConnected());
			}
			
			int count = 0;
			boolean result = activityTestISMG();
			while (!result) {
				count++;
				result = activityTestISMG();
				if (count >= (MsgConfig.getConnectCount() - 1)) {
					MsgContainer.setFlag(false);
					MsgContainer.cancelISMG();
					MsgContainer.getSocketInstance();
				}
			}
			boolean success = sender.start();
			if (success) {
				log.info("发送成功：" + cusMsisdn);
				return true;
			} else {
				log.info("发送失败：" + cusMsisdn);
			}
			return false;
		} catch (Exception e) {
			// try {
			// out.close();
			// } catch (IOException e1) {
			// out=null;
			// }
			log.error("1.发送短短信：" + e.getMessage());
			return false;
		}
	}

	/**
	 * 发送长短信
	 * 
	 * @return
	 */
	private static boolean sendLongMsg2(String msg, String cusMsisdn) {
		try {
			int msgLength = msg.length();// 短信字节数
			int maxLength = 70;// 标准短信最大字节数
			int msgSendCount = msgLength % (maxLength) == 0 ? msgLength
					/ (maxLength) : msgLength / (maxLength) + 1;// 短信分段数
			boolean b = false;
			log.info("===长短信短信字节数："+msgLength);
			log.info("===标准短信最大字节数："+maxLength);
			log.info("===长短信短信分段数："+msg+msgSendCount);
			for (int i = 0; i < msgSendCount; i++) {
				String needMsg = "";
				if (i != msgSendCount - 1) {
					int start = (maxLength) * i;
					int end = (maxLength) * (i + 1);
					needMsg = msg.substring(start, end);
				} else {
					int start = (maxLength) * i;
					int end = msg.length();
					needMsg = msg.substring(start, end);
				}

				// System.out.println(needMsg);
				b = sendShortMsg(needMsg, cusMsisdn);
			}
			log.info("===长短信sendShortMsg："+b);

			return b;
		} catch (Exception e) {
			// try {
			// out.close();
			// } catch (IOException e1) {
			// out=null;
			// }
			log.error("发送短短信" + e.getMessage());
			return false;
		}
	}

	/**
	 * 发送长短信
	 * 
	 * @return
	 */
	private static boolean sendLongMsg(String msg, String cusMsisdn) {
		try {
			byte[] allByte = msg.getBytes("iso-10646-ucs-2");
			// byte[] allByte = msg.getBytes("gb2312");
			// byte[] allByte=msg.getBytes("UTF-16BE");
			List<byte[]> dataList = new ArrayList<byte[]>();
			int msgLength = allByte.length;// 短信字节数
			int maxLength = 140;// 标准短信最大字节数
			int msgSendCount = msgLength % (maxLength - 6) == 0 ? msgLength
					/ (maxLength - 6) : msgLength / (maxLength - 6) + 1;// 短信分段数
			// 短信息内容头拼接
			byte[] msgHead = new byte[6];
			Random random = new Random();
			random.nextBytes(msgHead); // 为了随机填充msgHead[3]
			msgHead[0] = 0x05;
			msgHead[1] = 0x00;
			msgHead[2] = 0x03;
			msgHead[4] = (byte) msgSendCount;
			msgHead[5] = 0x01;// 默认为第一条
			int seqId = MsgUtils.getSequence();
			for (int i = 0; i < msgSendCount; i++) {
				// msgHead[3]=(byte)MsgUtils.getSequence();
				msgHead[5] = (byte) (i + 1);
				byte[] needMsg = null;
				// 消息头+消息内容拆分
				if (i != msgSendCount - 1) {
					int start = (maxLength - 6) * i;
					int end = (maxLength - 6) * (i + 1);
					needMsg = MsgUtils.getMsgBytes(allByte, start, end);
				} else {
					int start = (maxLength - 6) * i;
					int end = allByte.length;
					needMsg = MsgUtils.getMsgBytes(allByte, start, end);
				}
				int subLength = needMsg.length + msgHead.length;
				byte[] sendMsg = new byte[needMsg.length + msgHead.length];
				System.arraycopy(msgHead, 0, sendMsg, 0, 6);
				System.arraycopy(needMsg, 0, sendMsg, 6, needMsg.length);
				MsgSubmit submit = new MsgSubmit();
				// 12+8+1+1+1+1+10+1+21+1+1+1+6+2+6+17+17+21+1+21+1+8
				submit.setTotalLength(159 + subLength);
				submit.setCommandId(MsgCommand.CMPP_SUBMIT);
				submit.setSequenceId(seqId);
				submit.setPkTotal((byte) msgSendCount);
				submit.setPkNumber((byte) (i + 1));
				submit.setRegisteredDelivery((byte) 0x00);
				submit.setMsgLevel((byte) 0x01);
				submit.setFeeUserType((byte) 0x02);
				submit.setTpPId((byte) 0x00);
				submit.setTpUdhi((byte) 0x01);
				submit.setMsgFmt((byte) 0x08);
				submit.setMsgSrc(MsgConfig.getSpId());
				submit.setSrcId(MsgConfig.getSpCode());
				submit.setDestTerminalId(cusMsisdn);
				submit.setMsgLength((byte) subLength);
				submit.setMsgContent(sendMsg);
				submit.setFeeTerminalId(cusMsisdn);
				submit.setServiceId(MsgConfig.getServiceId());// serviceId
																// 以及计费号FeeTerminalId
																// 由联创追踪发现少了这两个参数，by
																// Dongrisheng
																// 2016-10-11
				dataList.add(submit.toByteArray());
			}
			CmppSender sender = new CmppSender(getSocketDOS(), getSocketDIS(),
					dataList);
			log.info("向" + cusMsisdn + "下发长短信，序列号为:" + seqId);
			boolean success = sender.start();
			if (success) {
				log.info("发送成功：" + cusMsisdn);
				return true;
			} else {
				log.info("发送失败：" + cusMsisdn);
			}
			return false;
		} catch (Exception e) {
			try {
				out.close();
			} catch (IOException e1) {
				out = null;
			}
			log.error("发送长短信" + e.getMessage());
			return false;
		}
	}

	/**
	 * 拆除与ISMG的链接
	 * 
	 * @return
	 */
	public static boolean cancelISMG() {
		try {
			MsgHead head = new MsgHead();
			head.setTotalLength(12);// 消息总长度，级总字节数:4+4+4(消息头)+6+16+1+4(消息主体)
			head.setCommandId(MsgCommand.CMPP_TERMINATE);// 标识创建连接
			head.setSequenceId(MsgUtils.getSequence());// 序列，由我们指定

			List<byte[]> dataList = new ArrayList<byte[]>();
			dataList.add(head.toByteArray());
			CmppSender sender = new CmppSender(getSocketDOS(), getSocketDIS(),
					dataList);
			sender.start();
			getSocketInstance().close();
			out.close();
			in.close();
			return true;
		} catch (Exception e) {
			try {
				out.close();
				in.close();
			} catch (IOException e1) {
				in = null;
				out = null;
			}
			log.error("拆除与ISMG的链接" + e.getMessage());
			return false;
		}
	}

	/**
	 * 链路检查
	 * 
	 * @return
	 */
	public static boolean activityTestISMG() {
		try {
			MsgHead head = new MsgHead();
			head.setTotalLength(12);// 消息总长度，级总字节数:4+4+4(消息头)+6+16+1+4(消息主体)
			head.setCommandId(MsgCommand.CMPP_ACTIVE_TEST);// 标识创建连接
			head.setSequenceId(MsgUtils.getSequence());// 序列，由我们指定

			List<byte[]> dataList = new ArrayList<byte[]>();
			dataList.add(head.toByteArray());
			CmppSender sender = new CmppSender(getSocketDOS(), getSocketDIS(),
					dataList);
			return sender.start();
		} catch (Exception e) {
			try {
				out.close();
			} catch (IOException e1) {
				out = null;
			}
			log.error("链路检查" + e.getMessage());
			return false;
		}
	}

	/**
	 * 发送web push 短短信
	 * 
	 * @param url
	 *            wap网址
	 * @param desc
	 *            描述
	 * @param cusMsisdn
	 *            短信
	 * @return
	 */
	private static boolean sendShortWapPushMsg(String url, String desc,
			String cusMsisdn) {
		try {
			// length 12
			byte[] szWapPushHeader1 = { 0x0B, 0x05, 0x04, 0x0B, (byte) 0x84,
					0x23, (byte) 0xF0, 0x00, 0x03, 0x03, 0x01, 0x01 };
			// length 9
			byte[] szWapPushHeader2 = { 0x29, 0x06, 0x06, 0x03, (byte) 0xAE,
					(byte) 0x81, (byte) 0xEA, (byte) 0x8D, (byte) 0xCA };
			// length 9
			byte[] szWapPushIndicator = { 0x02, 0x05, 0x6A, 0x00, 0x45,
					(byte) 0xC6, 0x08, 0x0C, 0x03 };
			// 去除了http://前缀的UTF8编码的Url地址"的二进制编码
			byte[] szWapPushUrl = url.getBytes("utf-8");
			// length 3
			byte[] szWapPushDisplayTextHeader = { 0x00, 0x01, 0x03 };
			// 想在手机上显示的关于这个URL的文字说明,UTF8编码的二进制
			byte szMsg[] = desc.getBytes("utf-8");
			// length 3
			byte[] szEndOfWapPush = { 0x00, 0x01, 0x01 };
			int msgLength = 12 + 9 + 9 + szWapPushUrl.length + 3 + szMsg.length
					+ 3;
			int seq = MsgUtils.getSequence();
			MsgSubmit submit = new MsgSubmit();
			submit.setTotalLength(12 + 8 + 1 + 1 + 1 + 1 + 10 + 1 + 32 + 1 + 1
					+ 1 + 1 + 6 + 2 + 6 + 17 + 17 + 21 + 1 + 32 + 1 + 1
					+ msgLength + 20);
			submit.setCommandId(MsgCommand.CMPP_SUBMIT);
			submit.setSequenceId(seq);
			submit.setPkTotal((byte) 0x01);
			submit.setPkNumber((byte) 0x01);
			submit.setRegisteredDelivery((byte) 0x00);
			submit.setMsgLevel((byte) 0x01);
			submit.setFeeUserType((byte) 0x00);
			submit.setFeeTerminalId("");
			submit.setTpPId((byte) 0x00);
			submit.setTpUdhi((byte) 0x01);
			submit.setMsgFmt((byte) 0x04);
			submit.setMsgSrc(MsgConfig.getSpId());
			submit.setSrcId(MsgConfig.getSpCode());
			submit.setDestTerminalId(cusMsisdn);
			submit.setMsgLength((byte) msgLength);
			submit.setFeeTerminalId(cusMsisdn);
			submit.setServiceId(MsgConfig.getServiceId());// serviceId
															// 以及计费号FeeTerminalId
															// 由联创追踪发现少了这两个参数，by
															// Dongrisheng
															// 2016-10-11
			byte[] sendMsg = new byte[12 + 9 + 9 + szWapPushUrl.length + 3
					+ szMsg.length + 3];
			System.arraycopy(szWapPushHeader1, 0, sendMsg, 0, 12);
			System.arraycopy(szWapPushHeader2, 0, sendMsg, 12, 9);
			System.arraycopy(szWapPushIndicator, 0, sendMsg, 12 + 9, 9);
			System.arraycopy(szWapPushUrl, 0, sendMsg, 12 + 9 + 9,
					szWapPushUrl.length);
			System.arraycopy(szWapPushDisplayTextHeader, 0, sendMsg,
					12 + 9 + 9 + szWapPushUrl.length, 3);
			System.arraycopy(szMsg, 0, sendMsg, 12 + 9 + 9
					+ szWapPushUrl.length + 3, szMsg.length);
			System.arraycopy(szEndOfWapPush, 0, sendMsg, 12 + 9 + 9
					+ szWapPushUrl.length + 3 + szMsg.length, 3);
			submit.setMsgContent(sendMsg);
			List<byte[]> dataList = new ArrayList<byte[]>();
			dataList.add(submit.toByteArray());
			CmppSender sender = new CmppSender(getSocketDOS(), getSocketDIS(),
					dataList);
			sender.start();
			log.info("向" + cusMsisdn + "下发web push短短信，序列号为:" + seq);
			return true;
		} catch (Exception e) {
			try {
				out.close();
			} catch (IOException e1) {
				out = null;
			}
			log.error("发送web push短短信" + e.getMessage());
			return false;
		}
	}

	/**
	 * 发送web push 长短信
	 * 
	 * @param url
	 *            wap网址
	 * @param desc
	 *            描述
	 * @param cusMsisdn
	 *            短信
	 * @return
	 */
	private static boolean sendLongWapPushMsg(String url, String desc,
			String cusMsisdn) {
		try {
			List<byte[]> dataList = new ArrayList<byte[]>();
			// length 12
			byte[] wdp = { 0x0B, 0x05, 0x04, 0x0B, (byte) 0x84, 0x23,
					(byte) 0xF0, 0x00, 0x03, 0x03, 0x01, 0x01 };
			// 需要拆分的部分
			// length 9
			byte[] wsp = { 0x29, 0x06, 0x06, 0x03, (byte) 0xAE, (byte) 0x81,
					(byte) 0xEA, (byte) 0x8D, (byte) 0xCA };
			// length 9
			byte[] szWapPushIndicator = { 0x02, 0x05, 0x6A, 0x00, 0x45,
					(byte) 0xC6, 0x08, 0x0C, 0x03 };
			// 去除了http://前缀的UTF8编码的Url地址"的二进制编码
			byte[] szWapPushUrl = url.getBytes("utf-8");
			// length 3
			byte[] szWapPushDisplayTextHeader = { 0x00, 0x01, 0x03 };
			// 想在手机上显示的关于这个URL的文字说明,UTF8编码的二进制
			byte szMsg[] = desc.getBytes("utf-8");
			// length 3
			byte[] szEndOfWapPush = { 0x00, 0x01, 0x01 };
			byte[] allByte = new byte[9 + 9 + szWapPushUrl.length + 3
					+ szMsg.length + 3];

			System.arraycopy(wsp, 0, allByte, 0, 9);
			System.arraycopy(szWapPushIndicator, 0, allByte, 9, 9);
			System.arraycopy(szWapPushUrl, 0, allByte, 18, szWapPushUrl.length);
			System.arraycopy(szWapPushDisplayTextHeader, 0, allByte,
					18 + szWapPushUrl.length, 3);
			System.arraycopy(szMsg, 0, allByte, 18 + szWapPushUrl.length + 3,
					szMsg.length);
			System.arraycopy(szEndOfWapPush, 0, allByte, 18
					+ szWapPushUrl.length + 3 + szMsg.length, 3);
			int msgMax = 140;
			int msgCount = allByte.length % (msgMax - wdp.length) == 0 ? allByte.length
					/ (msgMax - wdp.length)
					: allByte.length / (msgMax - wdp.length) + 1;
			wdp[10] = (byte) msgCount;
			int seqId = MsgUtils.getSequence();
			for (int i = 0; i < msgCount; i++) {
				wdp[11] = (byte) (i + 1);
				byte[] needMsg = null;
				// 消息头+消息内容拆分
				if (i != msgCount - 1) {
					int start = (msgMax - wdp.length) * i;
					int end = (msgMax - wdp.length) * (i + 1);
					needMsg = MsgUtils.getMsgBytes(allByte, start, end);
				} else {
					int start = (msgMax - wdp.length) * i;
					int end = allByte.length;
					needMsg = MsgUtils.getMsgBytes(allByte, start, end);
				}
				int msgLength = needMsg.length + wdp.length;
				MsgSubmit submit = new MsgSubmit();
				submit.setTotalLength(12 + 8 + 1 + 1 + 1 + 1 + 10 + 1 + 32 + 1
						+ 1 + 1 + 1 + 6 + 2 + 6 + 17 + 17 + 21 + 1 + 32 + 1 + 1
						+ msgLength + 20);
				submit.setCommandId(MsgCommand.CMPP_SUBMIT);
				submit.setSequenceId(seqId);
				submit.setPkTotal((byte) msgCount);
				submit.setPkNumber((byte) (i + 1));
				submit.setRegisteredDelivery((byte) 0x00);
				submit.setMsgLevel((byte) 0x01);
				submit.setFeeUserType((byte) 0x00);
				submit.setFeeTerminalId("");
				submit.setTpPId((byte) 0x00);
				submit.setTpUdhi((byte) 0x01);
				submit.setMsgFmt((byte) 0x04);
				submit.setMsgSrc(MsgConfig.getSpId());
				submit.setSrcId(MsgConfig.getSpCode());
				submit.setDestTerminalId(cusMsisdn);
				submit.setMsgLength((byte) msgLength);
				submit.setFeeTerminalId(cusMsisdn);
				submit.setServiceId(MsgConfig.getServiceId());// serviceId
																// 以及计费号FeeTerminalId
																// 由联创追踪发现少了这两个参数，by
																// Dongrisheng
																// 2016-10-11
				byte[] sendMsg = new byte[wdp.length + needMsg.length];
				System.arraycopy(wdp, 0, sendMsg, 0, wdp.length);
				System.arraycopy(needMsg, 0, sendMsg, wdp.length,
						needMsg.length);
				submit.setMsgContent(sendMsg);
				dataList.add(submit.toByteArray());
			}
			CmppSender sender = new CmppSender(getSocketDOS(), getSocketDIS(),
					dataList);
			sender.start();
			log.info("向" + cusMsisdn + "下发web pus长短信，序列号为:" + seqId);
			return true;
		} catch (Exception e) {
			try {
				out.close();
			} catch (IOException e1) {
				out = null;
			}
			log.error("发送web push长短信" + e.getMessage());
			return false;
		}
	}

	private static byte[] getInData() throws IOException {
		try {
			int len = in.readInt();
			System.out.println("LEN++++++"+len);
			if (null != in && 0 != len) {
				byte[] data = new byte[len - 4];
				in.read(data);
				return data;
			} else {
				log.info("getInData heart is null");
				return null;
			}
		} catch (NullPointerException ef) {
			log.error("在本连结上接受字节消息:无流输入");
			return null;
		} catch (EOFException eof) {
			log.error("在本连结上接受字节消息:" + eof.getMessage());
			return null;
		}
	}
}
