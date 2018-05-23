package com.tsb.cmpp;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsb.cmpp.model.CMPPActive;
import com.tsb.cmpp.model.CMPPActiveResp;
import com.tsb.cmpp.model.CMPPConnect;
import com.tsb.cmpp.model.CMPPConnectResp;
import com.tsb.cmpp.model.CMPPDeliver;
import com.tsb.cmpp.model.CMPPDeliverResp;
import com.tsb.cmpp.model.CMPPRequestPacket;
import com.tsb.cmpp.model.CMPPSubmit;
import com.tsb.cmpp.model.CMPPSubmitResp;
import com.tsb.cmpp.model.CMPPTerminate;
import com.tsb.cmpp.model.CMPPTerminateResp;
import com.tsb.cmpp.model.CommandID;
import com.tsb.cmpp.model.SubmitBody;



/**
 * cmpp协议api,通过调用此api实现cmpp协议的各个消息 <br>
 * 在调用此类之前,必须先调用CMPPSocket类的initialSock方法,实现和网关socket连接的初始化
 * 
 * @author David Yin
 * @version 1.0 (2013-11-21 8:59:06)
 */
public class CMPPService {
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(CMPPService.class);

	/**
	 * cmpp的socket连接
	 */
	private CMPPSocket socket;

	/**
	 * cmpp请求包体
	 */

	private CMPPRequestPacket packet;

	/**
	 * response消息的最大返回时间，单位为秒
	 */
	private int delayTime;

	/**
	 * 构造函数
	 * 
	 * @param socket
	 *            发送和接收cmpp消息的时候，使用的socket连接
	 * @param delayTime
	 *            接收response消息的最大返回时间
	 */
	public CMPPService(CMPPSocket socket, int delayTime) {

		log.info("CMPPService初始化赋值CMPPSocket/delayTime："+delayTime);
		this.socket = socket;
		log.info("CMPPService初始化CMPPRequestPacket：-----------");
		this.packet = new CMPPRequestPacket();
		this.delayTime = delayTime;

	}

	/**
	 * 构造函数,默认延迟时间为10秒
	 * 
	 * @param socket
	 */
	public CMPPService(CMPPSocket socket) {
		this.socket = socket;
		this.packet = new CMPPRequestPacket();
		this.delayTime = 10;// 默认时间为10秒
	}

	/**
	 * 和cmpp网关建立连接,实现cmpp3协议中的connect命令 <br>
	 * 发送完connect命令后,马上取得connectresp消息,以判断和网关连接的正确与否 如果5秒之内没有得到返回消息,返回-1
	 * 
	 * @param spid
	 *            企业代码
	 * @param password
	 *            密码
	 * @param version
	 *            版本号码,实现的是cmpp3.0协议,填3
	 * @return int 0：正确 <br>
	 *         1：消息结构错 <br>
	 *         2：非法源地址 <br>
	 *         3：认证错 <br>
	 *         4：版本太高 <br>
	 *         >5：其他错误 <br>
	 *         -1：输入流阻塞 <br>
	 * @throws IOException
	 */
	public int cmppConnect(String spid, String password) throws IOException {
		packet.setCommandID(CommandID.CMPP_CONNECT);
		packet.setSequenceID(getSequence());
		packet.setRequestBody(new CMPPConnect(spid, password));
		log.info("cmpp请求包体packet参数添加：setCommandID/setSequenceID："+CommandID.CMPP_CONNECT+"|"+getSequence());
		try {
			socket.write(packet);
//			socket.getOutStream().flush();
			CMPPConnectResp resp = new CMPPConnectResp();
			long begin = System.currentTimeMillis();
			// 循环等待10秒,超过10秒,认为这条短信发送失败
			while (true) {
				long now = System.currentTimeMillis();
				int available = socket.getInputStream().available();
				log.info("检查CmPPSocket中DataInputStream中数据文件大小:"+available);

			/*	int length = socket.read().length;
				int totalLength = resp.getTotalLength();
				log.info("检查CmPPSocket中DataInputStream中数据文件大小:"+available+"|||"+length+"||"+totalLength);
				byte[] read = socket.read();
				for (int i = 0; i <read.length ; i++) {
					System.out.print(read[i]+" ");
				}

				resp.parseResponseBody(socket.read());*/
//				log.info("检查CmPPSocket中DataInputStream中数据文件大小:"+"|||"+"||"+totalLength);
				if (socket.getInputStream().available() > 0) {
					if (log.isDebugEnabled())
						log.debug("读connectresp消息时输入流长度为:"
								+ socket.getInputStream().available());
					byte[] packetbytes = socket.read();
					if (packetbytes.length != resp.getTotalLength()) {

						begin = System.currentTimeMillis();// 时间重置
						continue;
					}
					resp.parseResponseBody(packetbytes);
					return resp.status;
				} else if (now - begin > delayTime * 1000) {
					log.warn("读取connectresp消息时阻塞,返回-1");
					return -1;
				}
				try {
					Thread.sleep(2000);
				}catch (Exception e){

				}
			}
		} catch (IOException e) {
			log.error("读取ConnectResp消息IO异常:" + e.toString());
			throw e;
		}

	}

	/**
	 * 发送submit消息,发送完毕后等待获得submitresp,以判断发送的正确与否
	 * 
	 * @param sb
	 *            submit包体
	 * @param sbresp
	 *            返回的submitresp包体,通过它得到sequenceid以及msgid
	 * @return int >0表示网关已读到submit消息,返回结果为submiresp的result字段,
	 *         <0表示发送或者读取的问题,如果5秒之内没有读到submitresp消息,返回-1 <br>
	 *         0：正确 <br>
	 *         1：消息结构错 <br>
	 *         2：命令字错 <br>
	 *         3：消息序号重复 <br>
	 *         4：消息长度错 <br>
	 *         5：资费代码错 <br>
	 *         6：超过最大信息长 <br>
	 *         7：业务代码错 <br>
	 *         8：流量控制错 <br>
	 *         9：本网关不负责服务此计费号码 <br>
	 *         10：Src_Id错误 <br>
	 *         11：Msg_src错误 <br>
	 *         12：Fee_terminal_Id错误 <br>
	 *         13：Dest_terminal_Id错误 <br>
	 *         -1：输入流阻塞 <br>
	 * @throws IOException
	 */
	public int cmppSubmit(SubmitBody sb, CMPPSubmitResp sbresp)
			throws IOException {
		packet.setCommandID(CommandID.CMPP_SUBMIT);
		packet.setSequenceID(getSequence());
		packet.setRequestBody(new CMPPSubmit(sb));
		try {
			socket.write(packet);
			long begin = System.currentTimeMillis();
			// 循环等待10秒,超过10秒,认为这条短信发送失败
			while (true) {
				long now = System.currentTimeMillis();
				if (socket.getInputStream().available() > 0) {
					if (log.isDebugEnabled())
						log.debug("读submitresp时输入流可读长度为:"
								+ socket.getInputStream().available());
					byte[] packetbytes = socket.read();
					if (packetbytes.length != sbresp.getTotalLength()) {
						begin = System.currentTimeMillis();// 时间重置
						continue;
					}
					sbresp.parseResponseBody(packetbytes);
					return sbresp.result;
				} else if (now - begin > delayTime * 1000) {
					log.warn("读取输入流时租塞,返回-1");
					return -1;
				}
			}
		} catch (IOException e) {
			log.error("submit返回消息IO错误:" + e.toString());
			throw e;
		}
	}

	/**
	 * 发送链路检测包
	 * 
	 * @return int 0：成功 <br>
	 *         -1：获得返回包延迟阻塞 <br>
	 * @throws IOException
	 */
	public int cmppActiveTest() throws IOException {
		packet.setCommandID(CommandID.CMPP_ACTIVE_TEST);
		packet.setSequenceID(getSequence());
		packet.setRequestBody(new CMPPActive());
		try {
			socket.write(packet);
			CMPPActiveResp resp = new CMPPActiveResp();
			long begin = System.currentTimeMillis();
			// 循环等待10秒,超过10秒,认为这条短信发送失败
			while (true) {
				long now = System.currentTimeMillis();
				if (socket.getInputStream().available() > 0) {
					if (log.isDebugEnabled())
						log.debug("读activeresp时输入流可读长度为:"
								+ socket.getInputStream().available());
					byte[] packetbytes = socket.read();
					if (packetbytes.length != resp.getTotalLength()) {
						begin = System.currentTimeMillis();// 时间重置
						continue;
					}
					resp.parseResponseBody(packetbytes);
					return 0;
				} else if (now - begin > delayTime * 1000) {
					log.warn("读取输入流时阻塞,返回-1");
					return -1;
				}
			}
		} catch (IOException e) {
			log.error("active消息IO错误:" + e.toString());
			throw e;
		}
	}

	/**
	 * 发送链路检测包，不需要返回信息
	 * 
	 * @return int 0：成功 <br>
	 * @throws IOException
	 */
	public int cmppActiveTestNoResp() throws IOException {
		packet.setCommandID(CommandID.CMPP_ACTIVE_TEST);
		packet.setSequenceID(getSequence());
		packet.setRequestBody(new CMPPActive());
		try {
			socket.write(packet);
			return 0;
		} catch (IOException e) {
			log.error("active消息IO错误:" + e.toString());
			throw e;
		}
	}

	/**
	 * 发送断开连接命令.至于socket连接的关闭，则由用户自己调用CMPPSocket的closeSocket()方法
	 * 
	 * @return int <br>
	 *         0:成功断开连接 <br>
	 *         -1:阻塞延迟 <br>
	 * @throws IOException
	 */
	public int cmppTerminate() throws IOException {
		packet.setCommandID(CommandID.CMPP_TERMINATE);
		packet.setSequenceID(getSequence());
		packet.setRequestBody(new CMPPTerminate());
		try {
			socket.write(packet);

			CMPPTerminateResp resp = new CMPPTerminateResp();
			long begin = System.currentTimeMillis();
			// 循环等待10秒,超过10秒,认为这条短信发送失败
			while (true) {
				long now = System.currentTimeMillis();
				if (socket.getInputStream().available() > 0) {
					if (log.isDebugEnabled())
						log.debug("读submitresp时输入流可读长度为:"
								+ socket.getInputStream().available());
					byte[] packetbytes = socket.read();
					if (packetbytes.length != resp.getTotalLength()) {
						begin = System.currentTimeMillis();// 时间重置
						continue;
					}
					resp.parseResponseBody(packetbytes);
					return 0;
				} else if (now - begin > delayTime * 1000) {
					log.warn("读取输入流时租塞,返回-1");
					return -1;
				}
			}
		} catch (IOException e) {
			log.error("terminate消息IO错误:" + e.toString());
			throw e;
		}
	}

	/**
	 * 发送查询消息，得到查询结果
	 * 
	 * @param date
	 *            查询时间，精确至日
	 * @param queryType
	 *            查询类别,0：总数查询,1：按业务类型查询
	 * @param queryCode
	 *            查询码.当Query_Type为0时，此项无效(此处填null)；当Query_Type为1时，此项填写业务类型Service_Id
	 *            .
	 * @param reserve
	 *            保留
	 * @return CMPPQueryResp 返回查询结果，如果延迟或者抛出异常，返回null
	 * @throws IOException
	 */
	// public CMPPQueryResp cmppQuery(Date date, int queryType, String
	// queryCode, String reserve) throws IOException {
	// packet.setCommandID(CommandID.CMPP_QUERY);
	// packet.setSequenceID(getSequence());
	// packet.setRequestBody(new CMPPQuery(date, queryType, queryCode,
	// reserve));
	// try {
	// // synchronized (socket) {
	// socket.write(packet);
	// // long beginTime = System.currentTimeMillis();
	// CMPPQueryResp resp = new CMPPQueryResp();
	// long begin = System.currentTimeMillis();
	// // 循环等待10秒,超过10秒,认为这条短信发送失败
	// while (true) {
	// long now = System.currentTimeMillis();
	// if (socket.getInputStream().available() > 0) {
	// if (log.isDebugEnabled())
	// log.debug("读cmppqueryResp时输入流可读长度为:" +
	// socket.getInputStream().available());
	// byte[] packetbytes = socket.read();
	//
	// resp.parseResponseBody(packetbytes);
	// return resp;
	// }
	// else if (now - begin > delayTime * 1000) {
	// log.warn("读取输入流时租塞,返回-1");
	// return null;
	// }
	// }
	// // }
	// }
	//
	// catch (IOException e) {
	// log.error("terminate消息IO错误:" + e.toString());
	// throw e;
	// }
	// }
	/**
	 * 删除msg_Id在网关的消息
	 * 
	 * @param msg_Id
	 *            submitresp返回来的消息id
	 * @return 0:删除成功 <br>
	 *         -1:读response消息延迟 <br>
	 * @throws IOException
	 */
	// public int cmppCancel(byte[] msg_Id) throws IOException {
	//
	// packet.setCommandID(CommandID.CMPP_QUERY);
	// packet.setSequenceID(getSequence());
	// packet.setRequestBody(new CMPPCancel(msg_Id));
	// try {
	// // synchronized (socket) {
	// socket.write(packet);
	// // long beginTime = System.currentTimeMillis();
	//
	// long begin = System.currentTimeMillis();
	// // 循环等待10秒,超过10秒,认为这条短信发送失败
	// while (true) {
	// long now = System.currentTimeMillis();
	// if (socket.getInputStream().available() > 0) {
	// if (log.isDebugEnabled())
	// log.debug("读cmppqueryResp时输入流可读长度为:" +
	// socket.getInputStream().available());
	// byte[] packetbytes = socket.read();
	//
	// return packetbytes[12];
	// }
	// else if (now - begin > delayTime * 1000) {
	// log.warn("读取输入流时租塞,返回-1");
	// return -1;
	// }
	// }
	// // }
	// }
	//
	// catch (IOException e) {
	// log.error("terminate消息IO错误:" + e.toString());
	// // return -2;
	// throw e;
	// }
	//
	// }
	/**
	 * 发送deliverresp消息
	 * 
	 * @param deliver
	 * @param mysocket
	 */
	public void cmppDeliverResp(CMPPDeliver deliver) throws IOException {
		packet.setCommandID(CommandID.CMPP_DELIVER_RESP);
		packet.setSequenceID(deliver.getSequenceID());
		packet.setRequestBody(new CMPPDeliverResp(deliver.msg_Id, 0));
		try {
			socket.write(packet);
		} catch (IOException e) {
			log.error("deliverResp消息IO错误:" + e.toString());
			throw e;
		}
	}

	/**
	 * 发送activeresp消息
	 * 
	 * @param active
	 */
	public void cmppActiveResp(CMPPActive active) throws IOException {
		packet.setCommandID(CommandID.CMPP_ACTIVE_TEST_RESP);
		packet.setSequenceID(active.getSequenceID());
		packet.setRequestBody(new CMPPActiveResp());
		try {
			socket.write(packet);
		} catch (IOException e) {
			log.error("cmppActiveResp消息IO错误:" + e.toString());
			throw e;
		}
	}

	/**
	 * 发送terminateresp消息
	 * 
	 * @param terminate
	 */
	public void cmppTerminateResp(CMPPTerminate terminate) throws IOException {
		packet.setCommandID(CommandID.CMPP_TERMINATE_RESP);
		packet.setSequenceID(terminate.getSequenceID());
		packet.setRequestBody(new CMPPTerminateResp());
		try {
			socket.write(packet);
		} catch (IOException e) {
			log.error("terminate消息IO错误:" + e.toString());
			throw e;
		}
	}

	private int sequence = 0;

	/**
	 * 取得每次操作的序列号,步长为1,重复使用
	 * 
	 * @return int
	 */
	private int getSequence() {
		sequence++;
		if (sequence > 0x7fffffff)
			sequence = 2;
		return sequence;
	}
}