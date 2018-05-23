package com.tsb.cmpp.cmpp3;

/**
 * 启动一个线程   无限登陆ISMG
 * runSP控制是否关闭窗口
 * @author Administrator
 *
 */
public class Client {
	
	private String ip;
	private int port;
	private String spid;
	private String pwd;
	private byte version;
	private SPConnection conn;
	
	//客户端运行标记  全局标志  当停止后不可恢复的
	private boolean runSP;
	private int conect_t;  //重连SP时间
	//初始化client参数    全局只生成一次
	public Client(){
		getCCMPSPInfor();
		conn = new SPConnection();
		runSP = true;
		connect();
	}

	
	//连接到ISMG  启动线程
	public void connect(){
		
		Runnable runner = new Runnable() {
			
			@SuppressWarnings("static-access")
			public void run(){
				//初始runSP为true
				while(runSP){
					//只要  spconnection 没有连接到ISMG   就重新配置参数
					if(!conn.isrunning){
						//当3个线程都结束时才能重新生成SOCKET
						if(conn.isheartStartstop && conn.isrecvstop && conn.issendStartstop){
							try {
								setText("正在连接ISMG,请稍后...\n");
								conn.init("183.230.96.94",17890,"180107","180107",(byte)3);
							} catch (Exception e) {
								setText("客户端连接失败，5分钟后重新连接...\n");
							}
						}
					}
					//线程处理登陆   每5分钟判断一下连接是否断开
					try {
						Thread.currentThread().sleep(conect_t);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				
				//当关闭client 关闭连接   并关闭socket 和    所有线程
				if(!runSP){
					conn.closeSP();
				}
				
			}
		};
		Thread t = new Thread(runner);
		t.start();
	}
	
    
	public void getCCMPSPInfor(){
		
		if(null != Util.getProperties("ip"))
			this.ip = Util.getProperties("ip");
		if(null != Util.getProperties("port"))
			this.port = Integer.parseInt(Util.getProperties("port"));	
		if(null != Util.getProperties("spid"))
			this.spid = Util.getProperties("spid");		
		if(null != Util.getProperties("pwd"))
			this.pwd = Util.getProperties("pwd");
		if(null != Util.getProperties("version"))
			this.version = Byte.parseByte(Util.getProperties("version"));
		if(null != Util.getProperties("conect_t"))
			this.conect_t = Integer.parseInt(Util.getProperties("conect_t")) * 1000;
	}
	
	//当显示数据为1000条时清空
	static int num = 0;
    public static void setText(String str){
    	
//    	System.out.println(str);
    }

	public static void main(String[] args){
		new Client();
	}
	
}
