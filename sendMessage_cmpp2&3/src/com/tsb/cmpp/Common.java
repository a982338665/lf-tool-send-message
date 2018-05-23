package com.tsb.cmpp;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Vector;

/**
 * 公用类
 * 
 * @author David Yin
 * @version 1.0 (2013-11-21 23:00:06)
 */
public final class Common {
	private static final char chars[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static String byte2hex(byte b) {
		char hex[] = new char[2];
		hex[0] = chars[(new Byte(b).intValue() & 0xf0) >> 4];
		hex[1] = chars[new Byte(b).intValue() & 0xf];
		return new String(hex);
	}

	public static String bytes2hex(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			sb.append(byte2hex(b[i]));
			sb.append(" ");
		}
		return sb.toString();
	}

	/**
	 * 将字符串转化成特定长度的byte[],如果value的长度小于idx,则右补零。比如
	 * getText(5,"1"),结果为{49,0,0,0,0}; 如果value的长度大于idx,则截取掉一部分。比如
	 * getText(2,"11111"),结果为{49,49};
	 * 
	 * @param idx
	 *            转化后byte[]的长度
	 * @param value
	 *            要转化的字符串
	 * @return byte[]
	 */
	public static byte[] getText(int idx, String value) {
		byte[] b1 = new byte[idx];
		int i = 0;
		if (value != null || !value.equals("")) {
			byte[] b2 = value.getBytes();
			while (i < b2.length && i < idx) {
				b1[i] = b2[i];
				i++;
			}
		}
		while (i < b1.length) {
			b1[i] = 0;
			i++;
		}
		return b1;
	}

	/**
	 * 8位的byte[]数组转换成long型
	 * 
	 * @param mybytes
	 * @return long
	 */
	public static long bytes8ToLong(byte mybytes[]) {
		long tmp = (0xff & mybytes[0]) << 56 | (0xff & mybytes[1]) << 48
				| (0xff & mybytes[2]) << 40 | (0xff & mybytes[3]) << 32
				| (0xff & mybytes[4]) << 24 | (0xff & mybytes[5]) << 16
				| (0xff & mybytes[6]) << 8 | 0xff & mybytes[7];
		return tmp;
	}

	/**
	 * long类型转化成8个字节
	 * 
	 * @param i
	 *            要转化的长整形
	 * @return byte[]
	 */
	public static byte[] longToBytes8(long i) {
		byte mybytes[] = new byte[8];
		mybytes[7] = (byte) (int) ((long) 255 & i);
		mybytes[6] = (byte) (int) (((long) 65280 & i) >> 8);
		mybytes[5] = (byte) (int) (((long) 0xff0000 & i) >> 16);
		mybytes[4] = (byte) (int) (((long) 0xff000000 & i) >> 24);
		int high = (int) (i >> 32);
		mybytes[3] = (byte) (0xff & high);
		mybytes[2] = (byte) ((0xff00 & high) >> 8);
		mybytes[1] = (byte) ((0xff0000 & high) >> 16);
		mybytes[0] = (byte) ((0xff000000 & high) >> 24);
		return mybytes;
	}

	/**
	 * int转化成4个字节的数组
	 * 
	 * @param i
	 *            要转化的整形变量
	 * @return byte[]
	 */

	public static byte[] intToBytes4(int i) {
		byte mybytes[] = new byte[4];
		mybytes[3] = (byte) (0xff & i);
		mybytes[2] = (byte) ((0xff00 & i) >> 8);
		mybytes[1] = (byte) ((0xff0000 & i) >> 16);
		mybytes[0] = (byte) ((0xff000000 & i) >> 24);
		return mybytes;
	}

	/**
	 * byte数组转化成int类型
	 * 
	 * @param mybytes
	 *            要转化的
	 * @return int
	 */
	public static int bytes4ToInt(byte mybytes[]) {
		int tmp = (0xff & mybytes[0]) << 24 | (0xff & mybytes[1]) << 16
				| (0xff & mybytes[2]) << 8 | 0xff & mybytes[3];
		return tmp;
	}
	
	/**
	 * UCS2编码
	 * 
	 * @param src
	 *            UTF-16BE编码的源串
	 * @return 编码后的UCS2串
	 * @throws Exception
	 */
	public static String EncodeUCS2(String src) {
		byte[] bytes = null;
		try {
			bytes = src.getBytes("UTF-16BE");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		StringBuffer reValue = new StringBuffer();
		StringBuffer tem = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			tem.delete(0, tem.length());
			tem.append(Integer.toHexString(bytes[i] & 0xFF));
			if (tem.length() == 1) {
				tem.insert(0, '0');
			}
			reValue.append(tem);
		}
		return reValue.toString().toUpperCase();
	}

	/**
	 * 构造随机码
	 * 
	 * @return
	 */
	public static int getRandInt() {
		Random random = new Random();
		return random.nextInt(255);
	}

	/**
	 * 长短信消息头
	 * 
	 * @param tpUdhiHead
	 * @param message
	 * @param i
	 * @param j
	 * @param udhi_length
	 * @return
	 */
	private static byte[] byteAdd(byte[] tpUdhiHead, byte[] message, int i,
			int j, int udhi_length) {
		byte[] msgb = new byte[j - i + udhi_length];
		System.arraycopy(tpUdhiHead, 0, msgb, 0, udhi_length);
		System.arraycopy(message, i, msgb, udhi_length, j - i);
		return msgb;
	}

	/**
	 * 字节数组截取工具
	 * 
	 * @param bytes
	 *            源字节数组
	 * @return 截取后的字节数组
	 */
	public static Vector<byte[]> SplitContent(String message, byte uid) {
		Vector<byte[]> v = new Vector<byte[]>();
		byte[] messageGBK = null;
		byte[] messageUCS2 = null;
		if (Function.isNotEmpty(message)) {
			try {
				messageGBK = message.getBytes("ISO-10646-UCS-2");
				int messageGBKLen = messageGBK.length;// 长短信长度
				// 20130315 yindayu insert begin[SVN上的源代码少了这句话，导致长短信无法下发]
				messageUCS2 = message.getBytes("ISO-10646-UCS-2");
				// 20130315 yindayu insert end
				int maxMessageLen = 140;
				int udhi_length = 6;// TP_udhi协议头长度
				int messageGBKCount = messageGBKLen
						/ (maxMessageLen - udhi_length) == 0 ? messageGBKLen
						/ (maxMessageLen - udhi_length) : messageGBKLen
						/ (maxMessageLen - udhi_length) + 1;// 长短信分为多少条发送

				byte[] tp_udhiHead = new byte[6];
				tp_udhiHead[0] = 0x05;
				tp_udhiHead[1] = 0x00;
				tp_udhiHead[2] = 0x03;
				tp_udhiHead[3] = uid;
				tp_udhiHead[4] = (byte) messageGBKCount;
				tp_udhiHead[5] = 0x01;// 默认为第一条
				for (int i = 0; i < messageGBKCount; i++) {
					tp_udhiHead[5] = (byte) (i + 1);
					byte[] msgContent;
					if (i != (messageGBKCount - 1)) {// 不为最后一条
						msgContent = byteAdd(tp_udhiHead, messageUCS2, i
								* (maxMessageLen - udhi_length), (i + 1)
								* (maxMessageLen - udhi_length), udhi_length);
						v.add(msgContent);
					} else {
						msgContent = byteAdd(tp_udhiHead, messageUCS2, i
								* (maxMessageLen - udhi_length), messageGBKLen,
								udhi_length);
						v.add(msgContent);
					}
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return v;
	}

}