package com.tsb.cmpp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsb.cmpp.model.CMPPSubmitResp;
import com.tsb.cmpp.model.SubmitBody;
import com.tsb.cmpp2.msg.util.MsgConfig;

public class CmppAPI {
	private static final Log log = LogFactory.getLog(CmppAPI.class);
	public boolean sendMessage(String msg,String phonenumber) throws Exception{
	
		CMPPSocket socket = new CMPPSocket(MsgConfig.getIsmgIp(), MsgConfig.getIsmgPort());
		boolean  flag=false;

		// 将socket连接注册到CMPP api中,获得response消息的最长时间为10秒
		CMPPService cmpp = null;
		// 建立和网关的connect连接
		while (true) {
			try {
				socket.initialSock();
				// 将socket连接注册到CMPP api中
				cmpp = new CMPPService(socket);
				// 建立和网关的connect连接
				int status = cmpp.cmppConnect(MsgConfig.getSpId(), MsgConfig.getSpSharedSecret());
				log.info("和网关建立的连接返回结果:" + status);

				if (status == 0) {
					log.info("连接建立成功！\n");
					break;
				} else {
					log.warn("连接建立不成功,结果为:" + status + "，重新连接\n");
					Thread.sleep(5000L);
					socket.closeSock();
				}
			} catch (Exception e) {
				try {
					socket.closeSock();
					log.error("建立连接异常,休眠5秒后再次建立连接!");
					Thread.sleep(5000L);
				} catch (Exception ee) {
					log.error("关闭连接异常:" + ee.toString());
					flag=false;
				}
				log.error(e.toString());
				flag=false;
			}
		}

		// submit消息,封装submit包,各字段意义请查阅doc文档
		SubmitBody submit = new SubmitBody(); // submit.msgID = 12;
		submit.ucPkTotal = 1;
		submit.ucPkNumber = 1;
		submit.ucRegister = 1;
		submit.ucMsgLevel = 1;
		submit.sServiceId = MsgConfig.getServiceId();
		submit.ucFeeUserType = 1;
		submit.sFeeTermId = "";
		submit.ucTpPid = 0;
		submit.ucTpUdhi = 0;
		submit.ucMsgFmt = 15;
		//submit.sMsgSrc = "922095";
		submit.sMsgSrc = MsgConfig.getSpId();
		submit.sFeeType = "01";
		submit.sFeeCode = "0";
		submit.sValidTime = "";
		submit.sAtTime = "";
		submit.sSrcTermId = MsgConfig.getSpCode();

		// 可以支持多个发送,cmpp2.0暂时只支持一个接收号码
		submit.sDstTermId = phonenumber;
		// 也可以设定只有一个
		// submit.sDstTermId = "13635423870";
		

		submit.ucMsgContent = msg.getBytes("gb2312");
		submit.reserver = ""; // cmpp.cmppSubmit(submit); //

		int i = 0;
		while (true) {
			long begin = System.currentTimeMillis();
			
			// 同时获得submitresp消息
			CMPPSubmitResp resp = new CMPPSubmitResp();
			cmpp.cmppSubmit(submit, resp);
			//log.info("msgid=" + Common.bytes8ToLong(resp.msg_Id) + "--seqid="
			//		+ resp.getSequenceID() + "--result=" + resp.result);
			//log.info("msgid=" + bytes2hex(resp.msg_Id));
			long now = System.currentTimeMillis();
			if (now - begin >= 1000) {
				log.info("发送这条信息花了" + (now - begin) + "时间,睡"
						+ (100 - now + begin) + "这么惨\n\n");
				Thread.sleep(100 - now + begin);
			}
			if (++i == 1) {
				flag=true;
				break;
			}

		}
		// 发送链路检测包
		int iii = cmpp.cmppActiveTest();
		log.info("发送检测包成功=" + iii);
		// 记住要关闭连接
		socket.closeSock();
		return flag;
	}
	
	private static String bytes2hex(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			sb.append(Common.byte2hex(b[i]) + " ");
		return sb.toString();
	}
	
	public static void main(String args[]) throws Exception {
//		CmppAPI cmpp=new CmppAPI();
//		boolean bl=cmpp.sendMessage("妹妹好看不3452", "15320588278");
//		System.out.println(bl);
		/*String msg="我来个去呀我来个去呀我来个去呀我来个去呀我来个去呀我来个去呀我来个去呀我来个去呀我来个去呀我来个去呀我来个去呀我来个去呀!!";
		
		System.out.println(msg.getBytes("gb2312").length);
		byte[] mm=msg.getBytes("gb2312");
		byte[] dd=new byte[3000];
		System.arraycopy(mm, 0, dd, 0, 120);
		String s = new String(dd, "GB2312");
		System.out.println(s);
		*/
	}
	

}
