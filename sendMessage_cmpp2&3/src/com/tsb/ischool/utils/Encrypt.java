package com.tsb.ischool.utils;

/**   
 * @FileName: MyEncrypt.java 
 * @Package:cn.megaline.server.util 
 * @Description: TODO
 * @author: megaline m_lm@163.com  
 * @date:2013-9-22 下午01:33:55 
 * @version V1.0 
 * Copyright (c) 2013 megaline,Inc. All Rights Reserved.
 */

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * @ClassName: MyEncrypt 
 * @Description: TODO
 * @author: GZSOFT m_lm@163.com
 * @date:2013-9-22 下午01:33:55 
 */
public class Encrypt
{

    public static byte[] Key = new byte[] { 0x31, 0x32, 0x33, 0x34, 0x35, 0x36 };

    public static byte[] MyEncryptAll(byte[] Message, byte[] TimeStamp)
    {
        byte[] key = MyEncrypt(TimeStamp);
        return MyEncrypt(Message, key);
    }

    public static byte[] MyEncrypt(byte[] Message, byte[] Key)
    {
        byte[] bsResult = new byte[Message.length];
        for (int i = 0, j = 0; i < Message.length; i++, j++)
        {
            bsResult[i] = (byte)(Message[i] ^ Key[j % Key.length]);
        }
        return bsResult;
    }

    public static byte[] MyEncrypt(byte[] Message)
    {
        byte[] bsResult = new byte[Message.length];
        for (int i = 0, j = 0; i < Message.length; i++, j++)
        {
            bsResult[i] = (byte)(Message[i] ^ Key[j % Key.length]);
        }
        return bsResult;
    }
    
    public static void main(String[] args) throws UnsupportedEncodingException
    {
//    	 String TimeStamp = "20130922132108";
         byte[] bsTS=new byte[]{0x20,0x13,0x09,0x22,0x13,0x21,0x08};
         
         String Message = "TestMsg123456中文";
         byte[] key = MyEncrypt(bsTS);
         byte[] Encode = MyEncrypt(Message.getBytes("UTF-8"), key);
         
         System.out.print("源字符串                  :");
         System.out.println(Message);
         System.out.print("源字符串Byte   :");
         System.out.println(ByteUtil.bytes2HexStr(Message.getBytes()));
         System.out.print("时间戳Byte     :");
         System.out.println(ByteUtil.bytes2HexStr(bsTS));
         System.out.print("一次加密后Byte :");
         System.out.println(ByteUtil.bytes2HexStr(key));
         System.out.print("二次加密后Byte :");
         System.out.println(ByteUtil.bytes2HexStr(Encode));
         
         //byte[] key = MyEncrypt(bsTS);
         byte[] Decode = MyEncrypt(Encode, key);
         System.out.print("解密后Byte     :");
         System.out.println(ByteUtil.bytes2HexStr(Decode));
         System.out.print("解密后字符传        :");
         System.out.println(new String(Decode,"UTF-8"));
    }
    
    public static String encryptDESCBC(String src, String key, String iv)
			throws Exception {

		// --生成key,同时制定是des还是DESede,两者的key长度要求不同
		DESedeKeySpec dks = new DESedeKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory
				.getInstance("desede");
		SecretKey secretKey = keyFactory.generateSecret(dks);

		// --加密向量
		IvParameterSpec ips = new IvParameterSpec(iv.getBytes("UTF-8"));

		// --通过Chipher执行加密得到的是一个byte的数组,Cipher.getInstance("DES")就是采用ECB模式,cipher.init(Cipher.ENCRYPT_MODE,
		// secretKey)就可以了.
		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, ips);
		byte[] b = cipher.doFinal(src.getBytes("UTF-8"));

		// --通过base64,将加密数组转换成字符串

		return Base64.encode(b);
	}
    
    public static String decryptDESCBC(final String src, final String key)
			throws Exception {
		final byte[] bytesrc = Base64.decode(src);

		final DESedeKeySpec desKeySpec = new DESedeKeySpec(
				key.getBytes("UTF-8"));
		final SecretKeyFactory keyFactory = SecretKeyFactory
				.getInstance("DESede");
		final SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

		final IvParameterSpec iv = new IvParameterSpec("01234567".getBytes());

		final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
		final byte[] retByte = cipher.doFinal(bytesrc);

		return new String(retByte , "UTF-8");

	}
    
    /**
	 * 使用 签名方法对对encryptText进行签名
	 * 
	 * @param encryptText
	 *            被签名的字符串
	 * @param encryptKey
	 *            密钥
	 * @return
	 * @throws Exception
	 */
    public static String HmacSHA1Encrypt(String encryptText, String encryptKey,String macName)
			throws Exception {
		byte[] data = encryptKey.getBytes("UTF-8");
		// 根据给定的字节数组构造一个密钥
		SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
		Mac mac = Mac.getInstance(macName);
		mac.init(secretKey);
		byte[] text = encryptText.getBytes("UTF-8");
		// 完成 Mac 操作
		byte[] arr = mac.doFinal(text);
		return Base64.encode(arr);
	}
}