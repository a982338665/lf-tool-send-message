package com.tsb.cmpp.model;

/**
 * cmpp协议各命令对应id
 * 
 * @author David Yin
 * @version 1.0 (2013-11-21 23:06:50)
 */
public interface CommandID {
	/**
	 * connect命令字,sp->ismg
	 */
	public final static int CMPP_CONNECT = 0x00000001; // 请求连接

	/**
	 * connectresp命令字id,ismg->sp
	 */

	public final static int CMPP_CONNECT_RESP = 0x80000001; // 请求连接应答

	/**
	 * terminate命令,sp->ismg或ismg->sp
	 */

	public final static int CMPP_TERMINATE = 0x00000002; // 终止连接

	/**
	 * terminateresp命令,sp->ismg或ismg->sp
	 */
	public final static int CMPP_TERMINATE_RESP = 0x80000002; // 终止连接应答

	/**
	 * submit命令,sp->ismg
	 */
	public final static int CMPP_SUBMIT = 0x00000004; // 提交短信

	/**
	 * submitresp命令,ismg->sp
	 */
	public final static int CMPP_SUBMIT_RESP = 0x80000004; // 提交短信应答

	/**
	 * deliver命令,ismg->sp
	 */
	public final static int CMPP_DELIVER = 0x00000005; // 短信下发

	/**
	 * deliverresp命令,sp->ismg
	 */
	public final static int CMPP_DELIVER_RESP = 0x80000005; // 下发短信应答

	public final static int CMPP_QUERY = 0x00000006; // 发送短信状态查询
	public final static int CMPP_QUERY_RESP = 0x80000006; // 发送短信状态查询应答
	public final static int CMPP_CANCEL = 0x00000007; // 删除短信
	public final static int CMPP_CANCEL_RESP = 0x80000007; // 请求连接应答

	/**
	 * active命令,sp->ismg或ismg->sp
	 */
	public final static int CMPP_ACTIVE_TEST = 0x00000008; // 激活测试

	/**
	 * activeresp命令,sp->ismg或ismg->sp
	 */
	public final static int CMPP_ACTIVE_TEST_RESP = 0x80000008; // 激活测试应答
}