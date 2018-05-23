package com.tsb.cmpp.cmpp3;

import com.tsb.cmpp.CMPPSocket;
import com.tsb.cmpp2.msg.domain.MsgCommand;
import com.tsb.cmpp2.msg.domain.MsgHead;
import com.tsb.cmpp2.msg.domain.MsgSubmit;
import com.tsb.cmpp2.msg.util.CmppSender;
import com.tsb.cmpp2.msg.util.MsgConfig;
import com.tsb.cmpp2.msg.util.MsgUtils;
import com.tsb.ischool.framework.common.PropertyUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 短信接口容器，单例获得链接对象
 * 
 * @author zzl 2016-07-20 16:30
 */
@SuppressWarnings("ALL")
public class MsgContainerNew3 {
	private static Log log = LogFactory.getLog(MsgContainerNew3.class);
	private DataInputStream in;
	private DataOutputStream out;

	private static MsgContainerNew3 instance = null;
	private MsgContainerNew3() {
	}
	public static synchronized MsgContainerNew3 getInstance() {
		if (instance == null) {
			instance = new MsgContainerNew3();
		}
		return instance;
	}

	private CMPPSocket msgSocket;
	
	public CMPPSocket getMsgSocket() {
		return msgSocket;
	}
	public void setMsgSocket(CMPPSocket msgSocket) {
		this.msgSocket = msgSocket;
	}
	

	public DataInputStream getSocketDIS() {
		try {
			in = msgSocket.getInputStream();
		} catch (Exception e) {
			in = null;
		}
		return in;
	}

	public DataOutputStream getSocketDOS() {
		try {
			out = msgSocket.getOutStream();
		} catch (Exception e) {
			out = null;
		}
		return out;
	}
	
	public static void main(String[] args)  {
		String msg="12345校讯通大转型！服务升级不加价！现在下载智动校园APP还送100M流量: http://t.cn/RXnvz5O。详询10086。技术: 400-818-6656【中国移动】";
		System.out.println(msg.length());
	}

	/**
	 * 发送短信
	 * 如果有长内容短信，是循环调用发送
	 * @param sequenceId
	 * @param msg
	 * @param cusMsisdn
	 * @return
	 */
	public boolean sendMsg(Integer sequenceId,String msg, String cusMsisdn) {
		try {
			if (msg.length() < 90) {// 短短信
				boolean result = sendShortMsg(sequenceId, msg, cusMsisdn);
				int count = 0;
				while (!result) {
					count++;
					result = sendShortMsg(sequenceId, msg, cusMsisdn);
					if (count >= (MsgConfig.getConnectCount() - 1)) {// 如果再次连接次数超过两次则终止连接
						break;
					}
				}
				return result;
			} else {// 长短信
				boolean result = sendLongMsg2(sequenceId, msg, cusMsisdn);
				int count = 0;
				while (!result) {
					count++;
					result = sendLongMsg2(sequenceId, msg, cusMsisdn);
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
	 * 支持短短信和长短信，目前还没有测试，线上未使用
	 * @param sequenceId
	 * @param msg
	 * @param cusMsisdn
	 * @return
	 */
	public  boolean sendMsgNew(Integer sequenceId,String msg,String cusMsisdn){
		try{
			if(msg.getBytes("iso-10646-ucs-2").length<140){//短短信
				boolean result=sendShortMsg(sequenceId,msg,cusMsisdn);
				int count=0;
				while(!result){
					count++;
					result=sendShortMsg(sequenceId,msg,cusMsisdn);
					if(count>=(MsgConfig.getConnectCount()-1)){//如果再次连接次数超过两次则终止连接
						break;
					}
				}
				return result;
			}else{//长短信
				boolean result=sendLongMsg(sequenceId,msg,cusMsisdn);
				int count=0;
				while(!result){
					count++;
					result=sendLongMsg(sequenceId,msg,cusMsisdn);
					if(count>=(MsgConfig.getConnectCount()-1)){//如果再次连接次数超过两次则终止连接
						break;
					}
				}
				return result;
			}
		}catch(Exception e){
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
	public boolean sendWapPushMsg(String url, String desc, String cusMsisdn) {
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
	private boolean sendShortMsg(Integer sequenceId,String msg, String cusMsisdn) {
		try {
			int seq=0;
			if (sequenceId==null || sequenceId == 0) {
				 seq = MsgUtils.getSequence();//发送序列号
			}else{
				seq=sequenceId;
			}
			byte[] msgByte = msg.getBytes("GB18030");
			/*MsgSubmit submit = new MsgSubmit();


			// 12+8+1+1+1+1+10+1+21+1+1+1+6+2+6+17+17+21+1+21+1+8
			submit.setTotalLength(159 + msgByte.length);
			submit.setCommandId(MsgCommand.CMPP_SUBMIT);
			submit.setSequenceId(seq);
			submit.setPkTotal((byte) 0x01);
			submit.setPkNumber((byte) 0x01);
			submit.setRegisteredDelivery((byte) 0x00);
			submit.setMsgLevel((byte) 0x01);
			submit.setFeeUserType((byte) 0x02);
			submit.setFeeTerminalId("");
			submit.setTpPId((byte) 0x00);
			submit.setTpUdhi((byte) 0x00);
			submit.setMsgFmt((byte) 0x0f);
			submit.setMsgSrc(MsgConfig.getSpId());
			submit.setSrcId(MsgConfig.getSpCode());
			submit.setDestTerminalId(cusMsisdn);
			submit.setMsgLength((byte) msgByte.length);
			submit.setMsgContent(msgByte);
			submit.setFeeTerminalId(cusMsisdn);
			submit.setServiceId(MsgConfig.getServiceId());// serviceId
															// 以及计费号FeeTerminalId
															// 由联创追踪发现少了这两个参数，by
															// Dongrisheng
															// 2016-10-11*/
			//初始化短信提交消息
			Msg_Submit msg_submit = intoSubmit2( sequenceId, msg,  cusMsisdn);
			byte[] b = MsgPacket3.packMsg(msg_submit);
			//发送字节包到网关
			boolean success = sendMsg2(b);
			System.out.println("短信已经发送出去");
//			isrunning = true;


//			log.info("sendShortMsg mobile：" + submit.toString());
//			List<byte[]> dataList = new ArrayList<byte[]>();
//			dataList.add(submit.toByteArray());
//
//			CmppSender sender = new CmppSender(getSocketDOS(), getSocketDIS(),
//					dataList);
//			log.info("向手机号码：" + cusMsisdn + "下发短短信，序列号为:" + seq);
//			boolean success = sender.start();
			if (success) {
				log.info("发送成功：" + cusMsisdn);
				return true;
			} else {
				log.info("发送失败：" + cusMsisdn);
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			// try {
			// out.close();
			// } catch (IOException e1) {
			// out=null;
			// }
			log.error("MsgContainerNew3  发送短短信：" + e.getMessage());
//			try {
//				msgSocket.closeSock();
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
			return false;
		}
	}

	/**
	 * 发送字节消息到网关
	 *
	 * @param data 发送的字节数据
	 */
	public boolean sendMsg2(byte[] data) {
		DataOutputStream socketDOS = getSocketDOS();
		try {
			synchronized (socketDOS) {
				if (null != data) {
					socketDOS.write(data);
					socketDOS.flush();
					return true;
				}
			}
		} catch (IOException e) {
			return false;
		}
		return false;
	}
	/**
	 * 发送长短信
	 * 
	 * @return
	 */
	private boolean sendLongMsg2(Integer sequenceId,String msg, String cusMsisdn) {
		try {
			int msgLength = msg.length();// 短信字节数
			int maxLength = 90;// 标准短信最大字节数
			int msgSendCount = msgLength % (maxLength) == 0 ? msgLength
					/ (maxLength) : msgLength / (maxLength) + 1;// 短信分段数
			boolean b = false;
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
				b = sendShortMsg(sequenceId, needMsg, cusMsisdn);
			}
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
	 * 初始化短信提交到网关信息
	 * @return
	 */
	public Msg_Submit intoSubmit2(Integer sequenceId, String msg, String cusMsisdn) {
		Msg_Submit submit = new Msg_Submit();
		// 信息内容
		String MSG_CONTENT = msg;
		int msgLen = MSG_CONTENT.getBytes().length;

		submit.setMsg_length(12 + 8 + 1 + 1 + 1 + 1 + 10 + 1 + 32 + 1 + 1 + 1 + 1 + 6
				+ 2 + 6 + 17 + 17 + 21 + 1 + 32 + 1 + 1 + msgLen + 20);
		submit.setMsg_squence(Util.getSequence());
		submit.setMsg_command(Msg_Command.CMPP_SUBMIT);

		submit.setMsg_Id(0);

		submit.setPk_total((byte)1);
		submit.setPk_number((byte)1);
		submit.setRegistered_Delivery((byte)Integer.parseInt(PropertyUtil.getProperty("isDelivery").trim()));
		submit.setMsg_level((byte)1);

		submit.setService_Id(MsgConfig.getServiceId());

		submit.setFee_UserType((byte)0x00);
		submit.setFee_terminal_Id(cusMsisdn);
		submit.setFee_terminal_type((byte)0);
		submit.setTP_pId((byte)0);
		submit.setTP_udhi((byte)0);
		submit.setMsg_Fmt((byte)0);
		submit.setMsg_src(MsgConfig.getSpId());

		submit.setFeeType("01");
		submit.setFeeCode("00030");
		submit.setValId_Time("");
		submit.setAt_Time("");

		submit.setSrc_Id(MsgConfig.getSpCode());

		submit.setDestUsr_tl((byte)1);
		submit.setDest_terminal_Id(cusMsisdn);
		submit.setDest_terminal_type((byte)0);
		submit.setMsg_Length((byte) msgLen);
		submit.setMsg_Content(MSG_CONTENT);
		submit.setLinkID("1");

		return submit;
	}
	/**
	 * 发送长短信
	 * 
	 * @return
	 */
	private boolean sendLongMsg(Integer sequenceId,String msg, String cusMsisdn) {
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
			
			int seqId=0;
			if (sequenceId==null || sequenceId == 0) {
				seqId = MsgUtils.getSequence();//发送序列号
			}else{
				seqId=sequenceId;
			}
			
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
				submit.setFeeTerminalId("");
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
	public boolean cancelISMG() {
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
			// getSocketInstance().close();
			// out.close();
			// in.close();
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
	private boolean sendShortWapPushMsg(String url, String desc,
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
	private boolean sendLongWapPushMsg(String url, String desc, String cusMsisdn) {
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

}
