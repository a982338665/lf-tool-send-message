package com.tsb.cmpp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;

/**
 * 1:工具类
 * 
 * @author
 */
public class Utils {
	/**
	 * 解析连结上来的IP地址
	 * 
	 * @param sa
	 * @return
	 */
	public static String parseIP(String sa) {
		int start = sa.indexOf("/") + 1;
		int end = sa.indexOf(":");
		String ip = sa.substring(start, end);
		return ip;
	}

	/**
	 * 调试消息原始数据
	 * 
	 * @param dir
	 *            :消息发送方向说明
	 * @param data
	 *            :消息数据
	 */
	public static void debugData(String dir, byte[] data) {
		// logger.info("消息总长:" + data.length + " " + dir);
		int count = 0;
		for (int i = 0; i < data.length; i++) {
			int b = data[i];
			if (b < 0) {
				b += 256;
			}
			// 16进制如果不满2位则补零
			String hexString = Integer.toHexString(b);
			hexString = (hexString.length() == 1) ? "0" + hexString : hexString;
			// logger.info(hexString + "  ");
			count++;
			if (count % 4 == 0) {
				// logger.info(" ");
			}
			if (count % 16 == 0) {
				// logger.info("\r\n");
			}
		}
		// logger.info("\r\n");
	}

	/**
	 * 1:整数绝对值
	 * 
	 * @param value
	 *            整数
	 * @return 绝对值
	 */
	public static int getAbsoluteValue(int value) {
		if (value > 0) {
			return value;
		} else {
			return value * (-1);
		}
	}

	/**
	 * 1:读取协议对应长度的字节
	 * 
	 * @param input
	 *            输入字节流
	 * @param length
	 *            长度
	 */
	public static String readString(DataInputStream input, int length) {

		// 1.考虑，异常怎么处理？
		byte[] b = new byte[length];
		// 2.ins.read(b);
		try {
			input.readFully(b);
			// 3.编码方式是什么？？？
			String s = new String(b, "utf-8");
			s = s.trim();
			return s;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 1:md5加密
	 * 
	 * @param pid
	 *            1：sp端地址
	 * @param pwd
	 *            1：sp和移动协商的密码
	 * @param timeStamp
	 *            1:时间戳
	 * @return
	 */
	public static byte[] getMd5(String pid, String pwd, String timeStamp) {
		try {
			java.security.MessageDigest md5 = MessageDigest.getInstance("MD5");
			String authMd5 = pid + "\0\0\0\0\0\0\0\0\0" + pwd + timeStamp;
			byte[] data = authMd5.getBytes();
			byte[] md5_result = md5.digest(data);
			return md5_result;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 1:
	 * 
	 * @param status
	 *            状态
	 * @param authsource
	 * @param sercet
	 * @return
	 */
	public static byte[] getMd5AuthIsmg(String status, byte[] authsource,
			String sercet) {

		try {
			java.security.MessageDigest md5 = MessageDigest.getInstance("MD5");
			String auth = new String(authsource);
			String authMd5 = status + auth + sercet;
			byte[] data = authMd5.getBytes();
			byte[] md5_result = md5.digest(data);
			return md5_result;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 1:格式化时间
	 * 
	 * @return
	 */
	public static String getMMDDHHMMSS() {
		// 格式化日期格式为MMddhhmmss
		java.text.SimpleDateFormat fomart = new java.text.SimpleDateFormat(
				"MMddHHmmss");
		// 日期
		java.util.Date date = new java.util.Date();
		// 返回格式化后的日期
		return fomart.format(date);
	}

	/**
	 * 1:未满补零
	 * 
	 * @param dos
	 *            写出流
	 * @param length
	 *            长度
	 * @param input
	 *            写出的定长字符串
	 */
	public static void writeFully(DataOutputStream dos, int length, String input)
			throws Exception {
		byte[] bb = new byte[length];
		byte[] in = input.getBytes("utf-8");
		int len = in.length;
		if (len > bb.length) {
			throw new Exception("写入字符串过长");
		}
		try {
			dos.write(in);
			while (len < bb.length) {
				dos.writeByte(0);
				len++;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * 1:LONG MSG_ID
	 * 
	 * @param mmddhhss
	 * @param addr
	 *            sp代码
	 * @param sequenceId
	 *            序列
	 * @return
	 */
	public static long getMsg_id(int mmddhhss, String addr, int sequenceId) {
		java.lang.StringBuffer data = new StringBuffer();
		String time = writeFully(26, getBinary(mmddhhss));
		data.append(time);
		String add = writeFully(22, getBinary(Integer.parseInt(addr)));
		data.append(add);
		// logger.info("seq==========" + sequenceId);
		String seq = writeFully(16, getBinary(sequenceId));
		data.append(seq);
		// logger.info("msg_id" + data.toString());
		long dd = Long.parseLong(Long.valueOf(data.toString(), 2).toString());
		return dd;
	}

	/**
	 * 1:未写满左边补零
	 * 
	 * @param length
	 * @param bb
	 * @return
	 */
	private static String writeFully(int length, String bb) {
		while (bb.length() < length) {
			// logger.info(">>>>>>>>>>" + bb.length());
			bb = "0".concat(bb);
		}
		return bb;
	}

	public static String getBinary(int n) {
		java.lang.StringBuffer da = new StringBuffer();
		while (n != 0) {
			int data = n - (n / 100) * 100;
			n = n / 100;
			String data1 = Integer.toBinaryString(data);
			da.append(data1);
		}
		return da.toString();
	}

	/**
	 * 1:判断字节数组是否相等
	 * 
	 * @param auth1
	 *            字节数组1
	 * @param auth2
	 *            字节数组2
	 * @return
	 */
	public static boolean byteEquals(byte[] auth1, byte[] auth2) {
		int length1 = auth1.length;
		int length2 = auth2.length;
		if (length1 != length2) {
			return false;
		}
		for (int i = 0; i < length1; i++) {
			if (auth1[i] != auth2[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 1:从文件中读取MSG_ID
	 * 
	 * @return
	 */
	public static int getMsg_id() {
		try {
			boolean bool = true;
			FileInputStream fis = new FileInputStream("cnf/Msg_Id.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String msg_id = br.readLine();
			while (bool) {
				String msg_id_copy = br.readLine();
				if (msg_id_copy == null) {
					break;
				}
				msg_id = msg_id_copy;
			}
			br.close();
			fis.close();
			return Integer.parseInt(msg_id);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 1:生成新的MSG_ID
	 * 
	 * @param newMsg_id
	 */
	public static void write_msg_id() {
		try {
			int num = getMsg_id();
			java.io.FileWriter fw = new java.io.FileWriter("cnf/Msg_Id.txt",
					true);
			java.io.PrintWriter pw = new java.io.PrintWriter(fw);
			pw.print("\r\n");
			pw.print(num + 1);
			pw.close();
			fw.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static long BytesToInt(int[] buf) {
		long a1 = buf[0];
		long a2 = buf[1];
		long a3 = buf[2];
		long a4 = buf[3];
		long a = (a1 << 24) + (a2 << 16) + (a3 << 8) + a4;

		return a;
	}

	public static String changeIp(String sip) {
		int[] bip = new int[4];
		try {
			StringTokenizer st = new StringTokenizer(sip, ".");
			int i = 0;
			while (st.hasMoreTokens()) {
				bip[i] = Integer.parseInt(st.nextToken());
				i++;
			}
			return BytesToInt(bip) + "";
		} catch (Exception e) {
			System.out.println(e);
			return "";
		}
	}

	public static String unchangeIp(String str) {
		try {
			String temp = "";
			long ll = Long.parseLong(str);
			temp = (int) (ll >> 24) + "." + ((byte) (int) (ll >> 16) & 0xff)
					+ "." + ((byte) (int) (ll >> 8) & 0xff) + "."
					+ ((byte) (int) ll & 0xff);
			;
			return temp;
		} catch (Exception e) {
			System.out.println(e);
			return "";
		}
	}

	public static String writeFile(String filePath, String fileName,
			String fileContent, String charset) {
		OutputStreamWriter out = null;
		try {
			out = new OutputStreamWriter(new FileOutputStream(filePath
					+ fileName), charset);
			out.write(fileContent);
			out.flush();
			return filePath + fileName;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String writeZipFile(String filePath, String fileName,
			byte[] fileContent) {
		DataOutputStream out = null;
		try {
			out = new DataOutputStream(new FileOutputStream(new File(filePath
					+ fileName)));
			out.write(fileContent);
			out.flush();
			return filePath + fileName;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				out = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void main(String[] args) {
		// System.out.println(changeIp("127.0.0.1"));
		// System.out.println(unchangeIp("2130706433"));
		// logger.info(Utils.getBinary(1301770584));
		System.out.println(Integer.parseInt(Utils.getMMDDHHMMSS()));
	}
}
