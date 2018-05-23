package com.tsb.cmpp;

import java.io.IOException;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsb.cmpp.model.CMPPConnectStatus;
import com.tsb.cmpp.model.CMPPSubmitResp;
import com.tsb.cmpp.model.CMPPSubmitStatus;
import com.tsb.cmpp.model.SubmitBody;


/**
 * @ClassName: CMPPClient
 * @Description: cmpp客户端调用
 * @author David Yin
 * @date 2013-11-22 下午01:56:48
 * 
 */
public class CMPPClient {
	protected static final Log log = LogFactory.getLog(CMPPClient.class);

	private String ip; // 服务端ip
	private int port = 7890; // 服务端口
	private String spid;// 鉴权账号
	private String password;// 鉴权密码
	private String msgsrc;// 企业代码

	// 构造方法
	public CMPPClient(String ip, int port, String spid, String password,
			String msgsrc) {
		this.ip = ip;
		this.port = port;
		this.spid = spid;
		this.password = password;
		this.msgsrc = msgsrc;
	}

	// 发送通知短信
	/**
	 * 
	 */
	public void sendNotifySms(String servicenumber, String msgContent,
			String[] receiveList) {
		// 新建CMPP封装的SOCKET
		CMPPSocket cmppSocket = new CMPPSocket(this.ip, this.port);
		// 初始化SOCKET
		try {
			cmppSocket.initialSock();

			CMPPService cmppService = new CMPPService(cmppSocket);
			int connStatus = cmppService.cmppConnect(this.spid, this.password);
			if (connStatus == 0) {
				int testStatus = cmppService.cmppActiveTest();
				// 链路测试成功
				if (testStatus == 0) {
					// 考虑长短信的问题
					if (msgContent.getBytes("ISO-10646-UCS-2").length > 140) {
						byte uid = (byte) Common.getRandInt();
						Vector<byte[]> messageList = Common.SplitContent(
								msgContent, uid);
						for (int i = 0; i < messageList.size(); i++) {
							SubmitBody submitBody = getSubmit(this.msgsrc,
									servicenumber, receiveList, messageList
											.get(i), true, 1);

							CMPPSubmitResp sbresp = new CMPPSubmitResp();
							int submitStatus = cmppService.cmppSubmit(
									submitBody, sbresp);
							if (submitStatus != 0) {
								log.error(CMPPSubmitStatus
										.getDesByCode(submitStatus));
							}
						}
					} else {
						SubmitBody submitBody = getSubmit(this.msgsrc,
								servicenumber, receiveList, msgContent
										.getBytes("gb2312"), true, 0);
						CMPPSubmitResp sbresp = new CMPPSubmitResp();
						int submitStatus = cmppService.cmppSubmit(submitBody,
								sbresp);
						if (submitStatus != 0) {
							log.error(CMPPSubmitStatus
									.getDesByCode(submitStatus));
						}
					}
				}
			} else {
				log.error(CMPPConnectStatus.getDesByCode(connStatus));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				cmppSocket.closeSock();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 构造SUBMIT包
	public static SubmitBody getSubmit(String spid, String srcid,
			String[] receiveList, byte[] content, boolean reportFlag, int udhi) {
		// 初始化Submit信息
		SubmitBody msgSubmit = new SubmitBody();

		msgSubmit.setUcPkTotal((byte) 1);
		msgSubmit.setUcPkNumber((byte) 1);
		// 是否要求返回状态确认报告
		if (reportFlag) {
			msgSubmit.setUcRegister((byte) 1);
		} else {
			msgSubmit.setUcRegister((byte) 0);
		}
		msgSubmit.setUcMsgLevel((byte) 1);// 信息级别
		msgSubmit.setsServiceId("CQQXT001");// 业务类型，是数字、字母和符号的组合
		msgSubmit.setUcFeeUserType((byte) 2);// 计费用户类型字段 3表示本子段无效
		msgSubmit.setsFeeTermId("");// 计费号码[UcFeeUserType为3，这个字段非要有值，否则CMPP提交会报103错误或者是提交后收不到短信]
		msgSubmit.setUcTpPid((byte) 0);
		msgSubmit.setUcTpUdhi((byte) udhi);// 是否长短信标志
		if (udhi == 1) {
			msgSubmit.setUcMsgFmt((byte) 8);// 长短信编码规则
		} else {
			msgSubmit.setUcMsgFmt((byte) 15);// 普通短信编码规则
		}
		msgSubmit.setsMsgSrc(spid);
		msgSubmit.setsFeeType("01");
		msgSubmit.setsFeeCode("0");
		msgSubmit.setsValidTime("");
		msgSubmit.setsAtTime("");
		msgSubmit.setsSrcTermId(srcid);
		msgSubmit.setUcDestUsrTl((byte) receiveList.length);
		msgSubmit.setsDstTermId(StringUtils.join(receiveList, ","));

		msgSubmit.setUcMsgLen((byte) content.length);
		msgSubmit.setUcMsgContent(content);

		msgSubmit.setReserver("");
		return msgSubmit;
	}

	public static void main(String[] args) {
		String url = "218.206.27.231";
		int port = 7890;
		String spid = "耦合帐号";
		String passwd = "耦合密码";
		String msgsrc = "企业代码";
		String servicenumber = "服务代码";
		String[] receiveList = { "手机号码" };

		CMPPClient cmppClient = new CMPPClient(url, port, spid, passwd, msgsrc);
		cmppClient.sendNotifySms(servicenumber, "短信内容[企业签名]",
				receiveList);
		// 启动获取状态报告进程
//		CMPPMo cmppMo = new CMPPMo(url, spid, passwd);
//		cmppMo.start();
	}

}
