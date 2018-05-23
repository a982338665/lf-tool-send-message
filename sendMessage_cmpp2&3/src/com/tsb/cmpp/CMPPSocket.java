package com.tsb.cmpp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsb.cmpp.model.CMPPException;
import com.tsb.cmpp.model.CMPPRequestPacket;
import com.tsb.cmpp2.msg.util.MsgContainerNew;




/**
 * 建立和网关的socket连接
 * 
 * @author David Yin
 * @version 1.0 (2013-11-21 22:52:58)
 */
public class CMPPSocket {
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(CMPPSocket.class);

	/**
	 * 网关主机地址
	 */
	private String host;

	/**
	 * 监听端口
	 */
	private int port;

	/**
	 * 建立起的socket端
	 */
	private Socket socket;

	/**
	 * 输出流
	 */
	private DataOutputStream out;

	/**
	 * 输入流
	 */
	private DataInputStream is;

	/**
	 * 构造函数
	 * 
	 * @param host
	 *            网关主机地址
	 * @param port
	 *            监听端口
	 */
	public CMPPSocket(String host, int port) {
		this.host = host;
		this.port = port;
	}

	/**
	 * 得到输入流
	 * 
	 * @return DataInputStream
	 */
	public DataInputStream getInputStream() {
		return this.is;

	}

	public DataOutputStream getOutStream() {
		return out;
	}

	/**
	 * 初始化socket连接,设定超时时间为5秒 <br>
	 * 使用cmpp协议各命令之前,必须调用此方法
	 * 
	 * @throws CMPPException
	 *             封装连接时抛出的UnknownHostException以及IOException
	 */
	public boolean initialSock()  {
		try {
			if (socket == null) {
				socket = new Socket(host, port);
			}
			log.info("CMPPSocket设置socket超时时间："+30*1000);
			socket.setSoTimeout(30 * 1000);
			log.info("CMPPSocket包装数据输入输出流DataOutputStream/DataInputStream");
			out = new DataOutputStream(socket.getOutputStream());
			is = new DataInputStream(socket.getInputStream());
			log.info("CMPPSocket成功建立起和网关的socket连接");
			if(socket.getInputStream()!=null){
				return true;
			}
		} catch (UnknownHostException e) {
			log.error("地址\"" + host + "\"未知" + e.toString());
			
		} catch (IOException e) {
			log.error("建立socket IO异常:" + e.toString());
		}
		return false;

	}

	/**
	 * 关闭socket连接
	 * 
	 * @throws CMPPException
	 *             封装关闭连接时抛出的IOException
	 */
	public void closeSock() throws IOException {
		try {
			if (socket != null) {
				socket.close();
				if (out != null)
					out.close();
				if (is != null)
					is.close();
			}
			socket = null;
			out = null;
			is = null;
			log.info("socket连接关闭成功");
		} catch (IOException e) {
			log.error("socket关闭异常:" + e.toString());
			throw e;
		}

	}

	// /**
	// * 判断连接是否关闭
	// *
	// * @return boolean
	// */
	// public boolean isClosed() {
	// return socket.isClosed();
	//
	// }
	//
	// /**
	// * 判断连接是否正确建立成功
	// *
	// * @return boolean
	// */
	// public boolean isConnected() {
	// return socket.isConnected();
	// }
	//
	// public boolean isInputShutdown() {
	// return socket.isInputShutdown();
	// }
	//
	// public boolean isOutputShutdown() {
	// return socket.isOutputShutdown();
	// }

	/**
	 * socket连接上发送请求包
	 * 
	 * @param
	 * @throws IOException
	 */
	public void write(CMPPRequestPacket cmpppacket) throws IOException {
		byte[] packets = cmpppacket.getRequestPacket();

		out.write(packets);
		out.flush();

		log.info(cmpppacket.getRequestBody().getClass().getName() + " 发送包体成功");
	}

	/**
	 * socket连接上读取输入流
	 * 
	 * @return 输入流的字节形式
	 * @throws IOException
	 */
	public byte[] read() throws CMPPException, IOException {
		// 包头
		byte[] head = new byte[12];
		byte[] packet = null;
		int reads = -1;
		// 应该这样读,先读12个字节,然后解析后得到包长,再读包长的长度
		// try {
		reads = is.read(head, 0, 12);
		// 没有读到的话
		if (reads == -1) {
			throw new CMPPException("读包头时输入流已空,没有读到数据!");
		}
		// 得到输入流的总长度，应该和is.available的一样
		byte[] length = new byte[4];
		System.arraycopy(head, 0, length, 0, 4);
		int packetlen = Common.bytes4ToInt(length);

		// 整个输入流的字节形式
		packet = new byte[packetlen];
		// 将包头独到整个输入流的字节形式中
		System.arraycopy(head, 0, packet, 0, 12);
		// 如果输入流还有包体
		if (packetlen - 12 != 0) {
			is.readFully(packet, 12, packetlen - 12);
			// if (reads == -1) {
			// throw new CMPPException("读包体时输入流已空,没有读到数据!");
			// }
		}
		log.info("本次输入流读取完毕,totalLength=" + packetlen);
		return packet;
		// }
		// catch (IOException o) {
		// log.error(o.getMessage());
		// throw new CMPPException(o.getMessage());
		// }
	}
}