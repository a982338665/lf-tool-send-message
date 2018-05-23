package com.tsb.ischool.utils;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

/**
 * @ClassName: util
 * @Description: 字符、整形、byte转换类
 * @author: m_lm@163.com
 * @date:2012-8-26 下午09:51:21
 */
public class ByteUtil {
	/**
	 * @Title: Str2Hex
	 * @Description: 字符串转换为16进制
	 * @param @param str
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String Str2Hex(String str) {
		if (str == null)
			return null;
		String content = str;
		String Digital = "0123456789ABCDEF";
		StringBuffer sb = new StringBuffer("");
		byte[] bs = content.getBytes();
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(Digital.substring(bit, bit + 1));
			bit = bs[i] & 0x0f;
			sb.append(Digital.substring(bit, bit + 1));
		}
		return sb.toString();
	}

	/**
	 * @Title: Hex2Str
	 * @Description: 16进制转换字符串
	 * @param @param hex
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String Hex2Str(String hex) {
		String digital = "0123456789ABCDEF";
		char[] hex2char = hex.toCharArray();
		byte[] bytes = new byte[hex.length() / 2];
		int temp;
		for (int i = 0; i < bytes.length; i++) {
			temp = digital.indexOf(hex2char[2 * i]) * 16;
			temp += digital.indexOf(hex2char[2 * i + 1]);
			bytes[i] = (byte) (temp & 0xff);
		}
		return new String(bytes);
	}

	/**
	 * @Title: bytesToHexString
	 * @Description: byte字节转换为16进制字符串
	 * @param @param src
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String bytesToHexStr(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * @Title: bytesToHexStr
	 * @Description: 方法二 byte字节转换为16进制字符串
	 * @param @param b
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String bytes2HexStr(byte[] b) {
		String ret = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex.toUpperCase();
		}
		return ret;
	}

	/**
	 * @Title: Hex2Bytes
	 * @Description: 16进制字符串转换为byte字节
	 * @param @param hexString
	 * @param @return
	 * @return byte[]
	 * @throws
	 */
	public static byte[] Hex2Bytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * @Title: charToByte
	 * @Description: 字符转字节
	 * @param @param c
	 * @param @return
	 * @return byte
	 * @throws
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * @Title: printHexString
	 * @Description: 将指定byte数组以16进制的形式打印到控制台
	 * @param @param b
	 * @return void
	 * @throws
	 */
	public static void printHexString(byte[] b) {
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			System.out.print(hex.toUpperCase());
		}
		System.out.println();
	}

	/**
	 * @Title: Int2Byte4
	 * @Description: 整形转为4字节byte数组
	 * @param @param i
	 * @param @return
	 * @return byte[]
	 * @throws
	 */
	public static byte[] Int2Byte4(int i) {
		byte[] result = new byte[4];
		result[0] = (byte) ((i >> 24) & 0xFF);
		result[1] = (byte) ((i >> 16) & 0xFF);
		result[2] = (byte) ((i >> 8) & 0xFF);
		result[3] = (byte) (i & 0xFF);
		return result;
	}
	
    /**
     * @Title: Int2Byte
     * @Description: 整形转为单字节byte数组
     * @param @param i
     * @param @return
     * @return byte[]
     * @throws
     */
    public static byte[] Int2Byte(int i) {
        byte[] result = new byte[1];
                
        result[0] = (byte) ((i >> 8) & 0xFF);
        
        return result;
    }	
	
	/**
	 * @Title: IntToByteArray 
	 * @Description: 整形转为byte数组
	 * @param @param i
	 * @param @return
	 * @param @throws Exception
	 * @return byte[]
	 * @throws
	 */
	public static byte[] IntToByteArray(int i) throws Exception {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(buf);
		out.writeInt(i);
		byte[] b = buf.toByteArray();
		out.close();
		buf.close();
		return b;
	}

	/**
	 * @Title: Hex2Int
	 * @Description: 16进制转换为整形
	 * @param @param hex
	 * @param @return
	 * @return int
	 * @throws
	 */
	public static int Hex2Int(String hex) {
		return Integer.valueOf(hex, 16);
	}	
	
	/**
	 * @Title: Int2Hex 
	 * @Description: 整形转16进制字符串格式
	 * @param @param i
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String Int2Hex(int i){
		return ByteUtil.bytes2HexStr(ByteUtil.Int2Byte4(i));
	}
	
	public static byte[] LinkByte(byte[] b1, byte[] b2, byte[] b3) throws Exception{
		int b1len = 0;
		int b2len = 0;
		int b3len = 0;
		
		if (b1!=null){ b1len = b1.length;}
		if (b2!=null){ b2len = b2.length;}
		if (b3!=null){ b3len = b3.length;}
		
		byte[] nb = new byte[b1len+b2len+b3len];
				
		if (b1!=null){
			System.arraycopy(b1, 0, nb, 0, b1.length);
		}
		else{
			throw new Exception("byte canot null.");
		}
		if (b2!=null){
			System.arraycopy(b2, 0, nb, b1len, b2.length);
		}
		if (b3!=null){
			System.arraycopy(b3, 0, nb, b1len+b2len, b3.length);
		}
		
		return nb;
	}
	
	@SuppressWarnings("static-access")
	public static void main(String[] args){
		ByteUtil ut = new ByteUtil();
		int i= 69;
		String str = "<Test>";		
		byte[] b = new byte[4];
		b[0] = 0x00; 
		b[1] = 0x00;
		b[2] = 0x00;
		b[3] = 0x45;
		
		System.out.println("Int to byte[69]: " + ut.bytes2HexStr(ut.Int2Byte4(i)));
		System.out.println("byte to int[0x00000045]: " + ut.Hex2Int(ut.bytes2HexStr(b)));
		System.out.println("str to hex[<Test>]: " + ut.Str2Hex(str));
		System.out.print("hex to byte print: " );
		ut.printHexString(ut.Hex2Bytes(ut.Str2Hex(str)));
		
//			byte[] body = new byte[ut.Hex2Bytes(ut.Str2Hex(str)).length];
//			body = ut.Hex2Bytes(ut.Str);
	}
}
