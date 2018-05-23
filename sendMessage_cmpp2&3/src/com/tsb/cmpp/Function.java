package com.tsb.cmpp;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Function {
	public static final char chars[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * 去掉byte数组中右补的二进制0
	 * 
	 * @param b
	 * @return
	 */
	public static String trimZero(byte b[]) {
		if (b == null) {
			throw new NullPointerException();
		}
		int i = 0;
		for (i = 0; i < b.length; i++) {
			if (b[i] == 0) {
				break;
			}
		}
		byte temp[] = new byte[i];
		System.arraycopy(b, 0, temp, 0, temp.length);
		return new String(temp);
	}

	public static byte[] getText(String value, int idx) {

		byte[] b1 = new byte[idx];
		int i = 0;
		if (value != null && !value.equals("")) {

			byte[] b2 = value.getBytes();

			while (i < b2.length && i < idx) {
				b1[i] = b2[i];
				i++;
			}
		}
		return b1;
	}

	/**
	 * 将1个byte转化成16进制的形式。比如byte类型24为0x18,-1为0xff
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte b) {
		char hex[] = new char[2];
		hex[0] = chars[(new Byte(b).intValue() & 0xf0) >> 4];
		hex[1] = chars[new Byte(b).intValue() & 0xf];
		return new String(hex);
	}

	/**
	 * 将byte数组转化成16进制的形式,[-1,-1]变为ff,ff
	 * 
	 * @param b
	 * @return
	 */
	public static String bytes2hex(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			sb.append(byte2hex(b[i]));
			sb.append("");
		}
		return sb.toString();
	}

	/**
	 * 8位的byte[]数组转换成long型
	 * 
	 * @param mybytes
	 * @return long
	 */
	public static long bytes2long(byte mybytes[]) {
		long tmp = (0xff & mybytes[0]) << 56 | (0xff & mybytes[1]) << 48
				| (0xff & mybytes[2]) << 40 | (0xff & mybytes[3]) << 32
				| (0xff & mybytes[4]) << 24 | (0xff & mybytes[5]) << 16
				| (0xff & mybytes[6]) << 8 | 0xff & mybytes[7];
		return tmp;
	}

	/**
	 * long类型转化成8个字节的byte[]数组
	 * 
	 * @param i
	 *            要转化的长整形
	 * @return byte[]
	 */
	public static byte[] long2bytes(long i) {
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
	 * int转化成4个字节的byte数组
	 * 
	 * @param i
	 *            要转化的整形变量
	 * @return byte[]
	 */

	public static byte[] int2bytes(int i) {

		byte mybytes[] = new byte[4];
		mybytes[3] = (byte) (0xff & i);
		mybytes[2] = (byte) ((0xff00 & i) >> 8);
		mybytes[1] = (byte) ((0xff0000 & i) >> 16);
		mybytes[0] = (byte) ((0xff000000 & i) >> 24);
		return mybytes;
	}

	/**
	 * 4个字节的byte数组转化成int类型
	 * 
	 * @param mybytes
	 *            要转化的长度为4的byte数组
	 * @return int
	 */
	public static int bytes2int(byte mybytes[]) {

		int tmp = (0xff & mybytes[0]) << 24 | (0xff & mybytes[1]) << 16
				| (0xff & mybytes[2]) << 8 | 0xff & mybytes[3];
		return tmp;
	}

	/**
	 * 2个字节的byte数组转化成short类型
	 * 
	 * @param mybytes
	 *            要转化的长度为2的byte数组
	 * @return int
	 */
	public static short bytes2short(byte mybytes[]) {

		short tmp = (short) ((0xff & mybytes[0]) << 8 | 0xff & mybytes[1]);
		return tmp;
	}

	/**
	 * short转化成2个字节的byte数组
	 * 
	 * @param i
	 *            要转化的short整形变量
	 * @return byte[]
	 */

	public static byte[] short2bytes(short i) {

		byte mybytes[] = new byte[2];
		mybytes[1] = (byte) (0xff & i);
		mybytes[0] = (byte) ((0xff00 & i) >> 8);
		return mybytes;
	}

	public static byte getInteger1(int value) {
		byte b = (byte) (value & 0xff);
		return b;
	}

	public static byte[] getInteger4(int value) {
		byte[] b1 = new byte[4];
		b1[0] = (byte) ((value >>> 24) & 0xff);
		b1[1] = (byte) ((value >>> 16) & 0xff);
		b1[2] = (byte) ((value >>> 8) & 0xff);
		b1[3] = (byte) ((value) & 0xff);
		return b1;
	}

	// 50000.0
	public static byte[] getText(int idx, String value) {
		byte[] b1 = new byte[idx];
		int i = 0;
		if (value != null && !value.equals("")) {
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

	public static byte[] getText(int idx, String[] value) {
		int len = value.length;
		int i = 0;
		byte[] b1 = new byte[idx * len];
		while (i < len) {
			byte[] b2 = value[i].getBytes();
			int j = 0;
			while (j < b2.length) {
				b1[i * idx + j] = b2[j];
				j++;
			}
			while (j < idx) {
				b1[i * idx + j] = 0;
				j++;
			}
			i++;
		}
		return b1;
	}

	public static String bytetohex(Byte b) {
		char hex[] = new char[2];
		hex[0] = chars[(b.intValue() & 0xf0) >> 4];
		hex[1] = chars[b.intValue() & 0xf];
		return new String(hex);
	}

	public static String byte2hex(byte bytes[]) // 二进制转字符串
	{
		String hexstring = "";
		for (int i = 0; i < bytes.length; i++) {
			Byte b = new Byte(bytes[i]);
			hexstring = String.valueOf(hexstring)
					+ String.valueOf(bytetohex(b));
			if (i < bytes.length)
				hexstring = String.valueOf(hexstring);
		}
		return hexstring;
	}

	public static String hextobyte(Byte b) {
		char hex[] = new char[2];
		hex[0] = chars[(b.intValue() & 0xf0) >> 4];
		hex[1] = chars[b.intValue() & 0xf];
		return new String(hex);
	}

	public static byte[] hex2byte(String aa) // 字符串转二进制
	{

		byte[] cc = aa.getBytes();

		int size = cc.length;

		byte[] cc1 = new byte[size / 2];
		int i = 0;
		while (i < size) {
			if (cc[i] > 57)
				cc[i] = (byte) (cc[i] - 55);
			else
				cc[i] = (byte) (cc[i] - 48);
			i++;
		}
		i = 0;
		int j = 0;
		while (i < size) {
			cc1[j] = (byte) (((cc[i] & 0x0f) << 4) | (cc[i + 1] & 0xff));
			j++;
			i = i + 2;
		}
		return cc1;
	}

	public static String backformatstring(String temp, int strleng, String chars) {
		int addlen = 0;
		if (temp != null)
			addlen = strleng - temp.length();
		for (int i = 0; i < addlen; i++)
			temp = temp + chars;
		return temp;
	}

	public static String formatstring(String temp, int strleng) {
		int addlen = 0;
		if (temp != null)
			addlen = strleng - temp.length();
		for (int i = 0; i < addlen; i++)
			temp = "0" + temp;
		return temp;
	}

	public static String formatstring2(String temp, int strleng) {
		int addlen = 0;
		if (temp != null)
			addlen = strleng - temp.length();
		for (int i = 0; i < addlen; i++)
			temp = " " + temp;
		return temp;
	}

	private static int sequence = 0;

	public static int getSequence() {
		sequence++;
		if (sequence > 0x7fffffff)
			sequence = 2;
		return sequence;
	}

	public static String getTimeStamp(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date currentTime = new Date();
		return formatter.format(currentTime);
	}

	public static long IPtoNumber(String ip) {
		String[] ipstr = ip.split("\\.");
		long[] ipint = new long[ipstr.length];
		for (int i = 0; i < ipstr.length; i++) {
			ipint[i] = Long.parseLong(ipstr[i]);
		}
		long number = 16777216 * ipint[0] + 65536 * ipint[1] + 256 * ipint[2]
				+ ipint[3];
		return number;
	}

	public static String NumbertoIp(String number) {
		long l = Long.parseLong(number);
		String ip = long2ip(l);
		return ip;
	}

	public static String long2ip(long l) {
		int[] b = new int[4];
		b[0] = (int) ((l >> 24) & 0xff);
		b[1] = (int) ((l >> 16) & 0xff);
		b[2] = (int) ((l >> 8) & 0xff);
		b[3] = (int) (l & 0xff);
		String ip = Integer.toString(b[0]) + "." + Integer.toString(b[1]) + "."
				+ Integer.toString(b[2]) + "." + Integer.toString(b[3]);
		return ip;
	}

	public static String toBlank(Object org) {
		if (org == null)
			return "";
		return org.toString();
	}

	public static long DatetoLong(String strDate) {
		try {
			java.text.SimpleDateFormat sfarmat = new java.text.SimpleDateFormat(
					"yyyy-MM-dd");
			java.util.Date date = sfarmat.parse(strDate);

			return date.getTime() / 1000;
		} catch (Exception e) {
			System.out.println(e);
			return 0;
		}
	}

	public static long DatetoLong(String strDate, String format) {
		try {
			java.text.SimpleDateFormat sfarmat = new java.text.SimpleDateFormat(
					format);
			java.util.Date date = sfarmat.parse(strDate);

			return date.getTime() / 1000;
		} catch (Exception e) {
			System.out.println(e);
			return 0;
		}
	}

	public static String LongtoDate(String longString, String format) {
		try {
			if (longString == null || "".equals(longString.trim()))
				return "";
			long llong = Long.parseLong(longString);
			java.util.Date dlong = new java.util.Date();
			dlong.setTime(llong * 1000);
			java.text.SimpleDateFormat sfarmat = new java.text.SimpleDateFormat(
					format);
			String strDateTime = sfarmat.format(dlong);
			return strDateTime;
		} catch (Exception e) {
			System.out.println(e);
			return "";
		}
	}

	/**
	 * 实现字符串财分
	 * 
	 * @param destMobile
	 * @param length
	 * @return
	 */
	public static String[] split(String destMobile, int length) {
		Vector<String> vector = new Vector<String>();
		java.io.ByteArrayInputStream bais = null;
		java.io.DataInputStream dis = null;
		try {
			bais = new java.io.ByteArrayInputStream(destMobile.getBytes());
			dis = new java.io.DataInputStream(bais);
			while (bais.available() > length) {
				vector.add(Utils.readString(dis, length));
			}
			if (bais.available() > 0) {
				vector.add(Utils.readString(dis, bais.available()));
			}
		} catch (Exception e) {

		} finally {
			try {
				if (dis != null) {
					dis.close();
					dis = null;
				}
				if (bais != null) {
					bais.close();
					bais = null;
				}
			} catch (Exception e) {
			}
		}
		String[] result = new String[vector.size()];
		vector.copyInto(result);
		return result;
	}

	/**
	 * 实现字符串合并
	 * 
	 * @param destMobile
	 * @param length
	 * @return
	 */
	public static byte[] combo(String[] receiveList, int length) {
		java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
		java.io.DataOutputStream dos = new java.io.DataOutputStream(bos);
		try {
			for (int i = 0; i < receiveList.length; i++) {
				Utils.writeFully(dos, length, receiveList[i]);
			}
			dos.flush();
			return bos.toByteArray();

		} catch (Exception e) {

		} finally {
			try {
				if (dos != null) {
					dos.close();
					dos = null;
				}
				if (bos != null) {
					bos.close();
					bos = null;
				}
			} catch (Exception e) {
			}
		}
		return null;
	}

	public static int validateLength(String srcString) {
		int flag = 0;
		if (srcString != null) {
			char[] a = srcString.toCharArray();
			for (int i = 0; i < a.length; i++) {
				char c = a[i];
				if ((int) c >= 0 && (int) c <= 255)
					flag += 1;
				else
					flag += 2;
			}
		}
		return flag;
	}

	public static boolean checkDir(String file) {
		File dir = new File(file);
		boolean ret = false;
		try {
			if (dir.isDirectory()) {
				return true;
			} else if (dir.isFile()) {
				String fileName = dir.getName();
				if (fileName.indexOf(".") > 0) {
					String ext = fileName.substring(fileName.indexOf(".") + 1);
					if (ext.equalsIgnoreCase("zip")) {
						return true;
					}
				}
			}
		} finally {
		}
		return ret;
	}

	public static boolean checkMobile(String msisdn) {
		Pattern pattern = Pattern.compile("^1[3,4,5,8]{1}[0-9]{9}$");
		Matcher isNum = pattern.matcher(msisdn);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public static long getFileSize(String file) {
		File dir = new File(file);
		long fileSize = 0;
		try {
			if (dir.isDirectory()) {
				File[] fileList = dir.listFiles();
				for (File f : fileList) {
					fileSize += f.length();
				}
			} else if (dir.isFile()) {
				fileSize += dir.length();
			}
		} finally {
			file = null;
		}
		return fileSize;
	}

	public static boolean isEmpty(String s) {
		if (s == null || "".equals(s))
			return true;
		s = s.trim();
		return "".equals(s);
	}

	public static boolean isNotEmpty(String s) {
		return !isEmpty(s);
	}

	public static boolean checkIp(String ip) {
		if (Function.isNotEmpty(ip)
				&& ip.matches("\\b((\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])(\\b|\\.)){4}")) {
			return true;
		} else
			return false;
	}

	public static boolean checkAccount(String account) {
		if (Function.isNotEmpty(account) && account.matches("^[0-9]{1,6}$")) {
			return true;
		} else
			return false;
	}

	public static Vector<byte[]> SplitContent(byte[] content, int length) {
		ByteArrayInputStream buf = new ByteArrayInputStream(content);
		Vector<byte[]> tmpv = new Vector<byte[]>();

		int msgCount = (int) (content.length / length + 1);
		int LeftLen = content.length;
		for (int i = 0; i < msgCount; i++) {
			byte[] tmp = new byte[length];
			if (LeftLen < length)
				tmp = new byte[LeftLen];
			try {
				buf.read(tmp);
				tmpv.add(tmp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			LeftLen = LeftLen - tmp.length;
		}
		return tmpv;
	}

	public static void main(String[] args) {
		// System.out.println(Integer.MAX_VALUE);
		// String ip = "192.168.0.106";
		// long number = Function.IPtoNumber(ip);
		// System.out.println(number);
		// System.out.println(Function.NumbertoIp("" + number));
	}
}
