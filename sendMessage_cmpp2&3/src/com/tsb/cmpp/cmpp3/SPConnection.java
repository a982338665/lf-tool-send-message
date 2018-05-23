package com.tsb.cmpp.cmpp3;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class SPConnection extends Thread{

	//生成一个连接线程
	private Socket socket;
	//输入输出流
	private DataInputStream din;
	private DataOutputStream dout;
	
	//全局的链路检测包和应答
	private Msg_Active_Test activeTest;
	private Msg_Active_Test_Resp activeTest_resp;
	//全局deliver 
	private Msg_Deliver_Resp deliver_resp;
	//连接是否打开
	public static boolean isrunning = false;
	
	
	
	//线程默认参数
	private int heart_s;
	private int heart_n;
	private int heart_c = 90;	//正常心跳包时间
	private int heart_t = 60;	//紧急心跳包时间
	private int msg_time = 30000;    //发送submit时间
	
	//启动SP  启动接收线程  启动心跳包线程  启动发送submit线程
	
	public static boolean isrecvstop = true;
	public static boolean isheartStartstop = true;
	public static boolean issendStartstop = true;
	public void startSP(){
		isrunning = true;
		initPara();
		this.recvStart();
		this.heartStart();
		this.sendStart();
	}
	
	
	//关闭SP   断开socket  isrunning为false 心跳  submit 都自动关闭
	public void closeSP(){
		isrunning = false;
		try {
			if(this.din != null){
				this.din.close();
				this.din = null;
			}
			if(this.dout != null){
				this.dout.close();
				this.dout = null;
			}
			if(this.socket != null){
				this.socket.close();
				this.socket = null;
			}
		} catch (IOException e) {
			Client.setText("SPConnection:关闭SP发生异常\n");
		}
	}
	
	
	/**
	 * 初始化socket io    每次init重新建立改参数
	 */
	public void init(String ip,int port,String spid,String pwd,byte version) {
		try {
			this.socket = new Socket(ip,port);
			this.din = new DataInputStream(this.socket.getInputStream());
			this.dout = new DataOutputStream(this.socket.getOutputStream());
			this.login(spid,pwd,version);
		} catch (Exception ex) {
			Client.setText("SPConnection:连接ISMG异常，可能是ISMG未启动\n");
		}
	}
	
	
	/**
	 * 初始化   心跳包   deliver_resp
	 */
	public void initPara(){
		if(null != Util.getProperties("heart_c"))
			this.heart_c = Integer.parseInt(Util.getProperties("heart_c"));
		if(null != Util.getProperties("heart_t"))
			this.heart_t = Integer.parseInt(Util.getProperties("heart_t"));
		if(null != Util.getProperties("msg_time"))
			this.msg_time = Integer.parseInt(Util.getProperties("msg_time")) * 1000;
		this.activeTest = new Msg_Active_Test();
		this.activeTest.setMsg_length(12);
		this.activeTest.setMsg_command(Msg_Command.CMPP_ACTIVE_TEST);
		this.activeTest_resp = new Msg_Active_Test_Resp();
		this.activeTest_resp.setMsg_length(13);
		this.activeTest_resp.setMsg_command(Msg_Command.CMPP_ACTIVE_TEST_RESP);
		this.activeTest_resp.setReserved((byte)0);
		this.deliver_resp = new Msg_Deliver_Resp();
		this.deliver_resp.setMsg_length(24);
		this.deliver_resp.setMsg_command(Msg_Command.CMPP_DELIVER_RESP);
		
	}
	
	/**
	 *  sp  登陆
	 * @param ip
	 * @param port
	 * @return
	 */
	public void login(String spid,String pwd,byte version){
		try {
			Msg_Connect msg = getCmpp_Connect(spid,pwd,version);
			byte[] connect_data = MsgPacket3.packMsg(msg);
			Util.debugData("��½��ݣ�", connect_data);
			this.sendMsg(connect_data);
			byte[] connect_resp_data = this.recvMsg();
			Msg_Connect_Resp connect_resp = (Msg_Connect_Resp) MsgPacket3.praseMsg(connect_resp_data);
			if(null != connect_resp){
				/**
				 *对认证串的认证
				 */
				boolean b = Util.byteEquals(Util.getMd5AuthIsmg(connect_resp.getStatus(),msg.getAuthenticatorSource(),pwd),connect_resp.getAuthenticatorISMG());
				if(!b){
					Client.setText("SPConnection:��֤ʧ�ܣ���ʱδ������\n");
				}
				//登陆成功  启动其他线程
				if(0 == connect_resp.getStatus()){
					this.startSP();
				}else{
					Client.setText("SPConnection:登陆失败......IMSG返回的结果："+connect_resp.getStatus()+"\n");
				}
			}
			//做出其他处理
		} catch (Exception e) {
			Client.setText("SPConnection:登陆时出现异常可能是ISMG未启动\n");
		}
	}
	
	
	/**
	 * 创建一个登陆消息
	 * @param id
	 * @param pwd
	 * @return
	 */
	public Msg_Connect getCmpp_Connect(String spid, String pwd, byte version) {
		Msg_Connect cmpp_connect = new Msg_Connect();
		cmpp_connect.setMsg_length(4 + 4 + 4 + 6 + 16 + 1 + 4);
		cmpp_connect.setMsg_command(Msg_Command.CMPP_CONNECT);
		cmpp_connect.setMsg_squence(Util.getSequence());
		cmpp_connect.setSource_Addr(spid);
		String timeStamp = Util.getMMDDHHMMSS();
		byte[] b = Util.getLoginMD5(spid,pwd,timeStamp);
		cmpp_connect.setAuthenticatorSource(b);
		cmpp_connect.setVersion(version);
		cmpp_connect.setTimestamp(Integer.parseInt(timeStamp));
		return cmpp_connect;
	}
	
	/**
	 * 1将字节流发送出去
	 * 
	 * @param data
	 *            数据
	 * @param output
	 *            发送出去
	 */
	public void sendMsg(byte[] data){
		try {
			synchronized(this.dout){
				if(null != data){
					this.dout.write(data);
					this.dout.flush();
				}
			}
		} catch (IOException e) {
			Client.setText("SPConnection:������ݷ����쳣\n");
			this.closeSP();
		}
	}
	
	
	// 接收消息时,肯定是要先读消息头,先取四个字节
	// 从输入流上接收消息
	public byte[] recvMsg(){
		try {
			synchronized(this.din){
				Client.setText("SPConnection:接收数据流等待...\n");
				int len = this.din.readInt();
				if(len == 0){
					return null;
				}else if(len > 0 && len < 500) {
					byte[] data = new byte[len - 4];
					this.din.readFully(data); // 如果超时了呢
					Util.debugData("数据", data);
					return data;
				}else{
					Client.setText("SPConnection:读数据发生异常\n");
					this.closeSP();
				}
			}
		} catch (Exception ef){
			Client.setText("SPConnection:读数据发生异常\n");
			this.closeSP();
		}
		return null;
	}
	
	/**
	 * 接收短信线程
	 */
	public void recvStart(){
		Runnable runner = new Runnable(){
			public void run(){
				isrecvstop = false;
				Client.setText("SPConnection:接收信息线程开启\n");
				while(isrunning){
					try{
						byte[] bb = recvMsg();
						//只要有消息接收 就代表连接是正常的   计时清0
						heart_s = 0;
						Msg_Head msg = null;
						if(bb != null){
							msg = MsgPacket3.praseMsg(bb);
						}
						if(null != msg){
							if(msg.getMsg_command() == Msg_Command.CMPP_ACTIVE_TEST_RESP){
								Client.setText("SPConnection:接收到心跳应答消息！\n");
								if(heart_n > 0){
									heart_n--;
								}
							}else if(msg.getMsg_command() == Msg_Command.CMPP_ACTIVE_TEST){
								Client.setText("SPConnection:接收到心跳消息！\n");
								activeTest_resp.setMsg_squence(msg.getMsg_squence());
								byte[] tb = MsgPacket3.packMsg(activeTest_resp);
								Client.setText("SPConnection:发送心跳应答消息！\n");
								sendMsg(tb);
							}else{
								//deliver消息
								if(msg.getMsg_command() == Msg_Command.CMPP_DELIVER){
									//正常deliver消息
									Msg_Deliver deliver = (Msg_Deliver)msg;
									if(deliver.getRegistered_Delivery() == 0){
										StringBuffer sb = new StringBuffer();
										sb.append("上行消息:");
										sb.append(" 手机号码:"+deliver.getSrc_terminal_Id());
										sb.append(" 长号码:"+deliver.getDest_Id());
										sb.append(" 业务标识："+deliver.getService_Id());
										sb.append(" 信息内容:"+deliver.getMsg_Content());
										sb.append(" LinkID:"+deliver.getLinkID()+"\n");
										Client.setText(sb.toString());
										/**
										 * 通过deliver生成submit
										 */
										test_deliver = deliver;
									   //消息报告
									}else{
										//将报告处理结果存库
										Client.setText("report stats = "+deliver.getStat()+" "+"report msgid = "+deliver.getMsg_Id_report()+" "+"submit msgid = "+test_submit.getMsg_Id()+" \n");
									}
									//回复应答
									deliver_resp.setMsg_squence(deliver.getMsg_squence());
									deliver_resp.setMsg_Id(deliver.getMsg_Id());
									deliver_resp.setResult(0);
									Client.setText("SPConnection:����deliverӦ����Ϣ\n");
									sendMsg(MsgPacket3.packMsg(deliver_resp));
								}else if(msg.getMsg_command() == Msg_Command.CMPP_SUBMIT_RESP){
									//通知已发送成功
									Msg_Submit_Resp sub_resp = (Msg_Submit_Resp)msg;
									//MsgUtils.busyness_Submit_Resp(sub_resp);
									test_submit.setMsg_Id(sub_resp.getMsg_Id());

								}else{
									Client.setText("SPConnection:其他类型消息接收,不做任何处理\n");
								}
							}
						}

					}catch(Exception e){
						Client.setText("SPConnection:接收信息发生异常\n"+e.toString());
						closeSP();
					}
				}
				isrecvstop = true;
				Client.setText("SPConnection:接收信息线程断开！\n");
			}

		};
		Thread t = new Thread(runner);
		t.start();
	}


	/**
	 * ����	��ʱ���������
	 *
	 */

	public void heartStart() {
		Runnable runner = new Runnable() {
			@SuppressWarnings({ "static-access" })
			public void run(){
				isheartStartstop = false;
				Client.setText("SPConnection:�����߳̿���\n");
				//���������

				try {
					activeTest.setMsg_squence(Util.getSequence());
					byte[] bs = MsgPacket3.packMsg(activeTest);
					Client.setText("SPConnection:����������Ϣ��\n");
					sendMsg(bs);
				} catch (Exception e1) {
					Client.setText("SPConnection:����������쳣\n");
					closeSP();
				}


				while(isrunning){
					try{
						Thread.currentThread().sleep(1000);
						//180��  ��3����
						heart_s++;

						if(heart_n >= 3){
							if(heart_s == heart_t){
								closeSP();
								break;
							}
						}
						if(heart_s == heart_t){
							if(heart_n > 0){
								//���������
								Client.setText("SPConnection:�������������\n ");
								activeTest.setMsg_squence(Util.getSequence());
								heart_s = 0;
								heart_n ++;
								byte[] b = MsgPacket3.packMsg(activeTest);
								sendMsg(b);
								//ʱ����0
								continue;
							}
						}
						if(heart_s == heart_c){
							//���������
							activeTest.setMsg_squence(Util.getSequence());
							heart_s = 0;
							heart_n ++;
							byte[] b = MsgPacket3.packMsg(activeTest);
							Client.setText("SPConnection:����������Ϣ��\n");
							sendMsg(b);
							//ʱ����0
						}

					}catch(Exception e){
						Client.setText("SPConnection:����������쳣\n");
						closeSP();
					}
				}
				isheartStartstop = true;
				Client.setText("SPConnection:���������߳� �Ͽ���\n");
			}
		};
		Thread t = new Thread(runner);
		t.start();
	}


	/**
	 * ����һ���̣߳����Ͷ���
	 */
	private Msg_Deliver test_deliver;
	private Msg_Submit test_submit;
	public void sendStart() {
		Runnable runner = new Runnable() {
			@SuppressWarnings({ "static-access" })
			public void run(){
				issendStartstop = false;
				Client.setText("SPConnection:������Ϣ�߳̿���\n");
				while(isrunning){
					try {
						Thread.currentThread().sleep(msg_time);
						if(null != test_deliver){
							test_submit = intoSubmit();
							
							byte[] b = MsgPacket3.packMsg(test_submit);
							sendMsg(b);
							test_deliver = null;
						}
					}catch (Exception e) {
						Client.setText("SPConnection:����submit��Ϣ�����쳣\n");
						closeSP();
					}
				}
				issendStartstop = true;
				Client.setText("SPConnection:������Ϣ�߳� �Ͽ���\n");
			}
		};
		Thread t = new Thread(runner);
		t.start();
	}
	
	
	
	
	
	
	
	public Msg_Submit intoSubmit(){
		Msg_Submit submit = new Msg_Submit();
		int SEQUENCE_ID = Util.getSequence();
		long MSG_ID = 0;
		byte PK_TOTAL = 1;
		byte PK_NUMBER = 1;
		byte REGISTERED_DELIVERY = 1;
		byte MSG_LEVEL = 1;
		String SERVICE_ID = "-DB";
		byte FEE_USERTYPE = 0;
		String FEE_TERMINAL_ID = "";
		byte FEE_TERMINAL_TYPE = 0;
		byte TP_PID = 0;
		byte TP_UDHI = 0;
		byte MSG_FMT = 15;
		String MSG_SRC = "901234";
		String FEETYPE = "02";
		String FEECODE = "00030";
		String VALID_TIME = "";
		String AT_TIME = "";
		String SRC_ID = "01850";
		byte DESTUSR_TL = 1;
		String DEST_TERMINAL_ID = "13012345678";
		byte DEST_TERMINAL_TYPE = 0;
		//��Ϣ����
		String MSG_CONTENT = "";
		MSG_CONTENT = "��ӭ��������л���СˬͶ��һƱ��������Ϣ��3�ǡ�";
		int msgLen = MSG_CONTENT.getBytes().length;
		String LINKID = "1";
		int totalLen = 12 + 8 + 1 + 1 + 1 + 1 + 10 + 1 + 32 + 1 + 1 + 1 + 1 + 6
		+ 2 + 6 + 17 + 17 + 21 + 1 + 32 + 1 + 1 + msgLen + 20;
		
		submit.setMsg_length(totalLen);
		submit.setMsg_squence(SEQUENCE_ID);
		submit.setMsg_command(Msg_Command.CMPP_SUBMIT);
		submit.setMsg_Id(MSG_ID);
		submit.setPk_total(PK_TOTAL);
		submit.setPk_number(PK_NUMBER);
		submit.setRegistered_Delivery(REGISTERED_DELIVERY);
		submit.setMsg_level(MSG_LEVEL);
		submit.setService_Id(SERVICE_ID);
		submit.setFee_UserType(FEE_USERTYPE);
		submit.setFee_terminal_Id(FEE_TERMINAL_ID);
		submit.setFee_terminal_type(FEE_TERMINAL_TYPE);
		submit.setTP_pId(TP_PID);
		submit.setTP_udhi(TP_UDHI);
		submit.setMsg_Fmt(MSG_FMT);
		submit.setMsg_src(MSG_SRC);
		submit.setFeeType(FEETYPE);
		submit.setFeeCode(FEECODE);
		submit.setValId_Time(VALID_TIME);
		submit.setAt_Time(AT_TIME);
		submit.setSrc_Id(SRC_ID);
		submit.setDestUsr_tl(DESTUSR_TL);
		submit.setDest_terminal_Id(DEST_TERMINAL_ID);
		submit.setDest_terminal_type(DEST_TERMINAL_TYPE);
		submit.setMsg_Length((byte)msgLen);
		submit.setMsg_Content(MSG_CONTENT);
		submit.setLinkID(LINKID);
		
		StringBuffer sb = new StringBuffer();
		sb.append("������Ϣ:");
		sb.append(" �ֻ����:"+submit.getDest_terminal_Id());
		sb.append(" ������:"+submit.getSrc_Id());
		sb.append(" ҵ���ʶ:"+submit.getService_Id());
		sb.append(" ��Ϣ����:"+MSG_CONTENT);
		sb.append(" LinkID:"+submit.getLinkID()+"\n");
		Client.setText(sb.toString());
		return submit;
	}
	
	
	
	
	
	
	
	
	
	
	
}
