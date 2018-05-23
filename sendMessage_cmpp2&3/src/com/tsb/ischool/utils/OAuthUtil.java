package com.tsb.ischool.utils;


import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

/**
 * 授权工具类
 * @author admin
 */
public class OAuthUtil {
	/**
	 * 创建CODE
	 * 
	 * @param data 
	 * @param appKey
	 * @return
	 */
	public static String generateCode(String data,String appKey) throws Exception{
		//Des3.secretKey = appKey;
		return  new Des3(appKey).encode(data);
	}
	
	/**
	 * 判断返回的code是否是传递出去的code
	 * @param data
	 * @param appKey
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public static String analyseCode(String appKey,String code) throws Exception{
		//Des3.secretKey = appKey;
		return new Des3(appKey).decode(code);
	}
	
	/**
	 * 创建用户open_id,规则：用户open_id,userId,new Date().getTime(),StringUtil.getRandomStrId()
	 * @param data
	 * @param appKey
	 * @return
	 * @throws Exception
	 */
	public static String generateOpenId(long appId,long userId,String appKey) throws Exception{
		//Des3.secretKey = appKey;
		String open_id = "open_id," + appId + "," + userId + "," + new Date().getTime() + "," + StringUtil.getRandomStrId();
		System.out.println("generateOpenId ：" + open_id);
		return  StringUtil.md5(new Des3(appKey).encode(open_id));
	}
	
	/**
	 * 创建用户access_token,规则：用户access_token,userId,new Date().getTime(),StringUtil.getRandomStrId()
	 * @param data
	 * @param appKey
	 * @return
	 * @throws Exception
	 */
	public static String generateAccessToken(long appId,long userId,String appKey) throws Exception{
		//Des3.secretKey = appKey;
		String access_token = "access_token," + appId + "," + userId + "," + new Date().getTime() + "," + StringUtil.getRandomStrId();
		System.out.println("generateAccessToken ：" + access_token);
		return  StringUtil.md5(new Des3(appKey).encode(access_token));
	}

	
	/**
	 * 验证数字签名
	 * @param param
	 * @throws Exception
	 */
	public static boolean validate(String key,TreeMap param) throws Exception{
		try{
			boolean bol = false;
			String sig  = (String)param.get("sig");
			String str1 = joinParam(param,"sig");
			System.out.println("InterfaceService >> str1 :"+str1);
			String mac =  new BigInteger(Coder.encryptHMAC((str1).getBytes("utf-8"), key)).toString();
		    System.out.println("InterfaceService >> MAC :"+mac);
		    //验证签名
	 	    if(mac.equals(sig)){
	 	    	bol = true;
	 	    }
	 	    return bol;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	private static String joinParam(TreeMap param,String ignore) throws Exception{
		String str = "";
		//1 排序 2组串
		Set set = param.keySet();
		Iterator it = set.iterator();
		while(it.hasNext()){
			String key = (String)it.next();
			if(!key.equals(ignore)){
				str+=key+"="+param.get(key);
			}
		}
		return str;
	}
	
	private static String sigParam(TreeMap param){
		String str = "";
		//1 排序 2组串
		Set set = param.keySet();
		Iterator it = set.iterator();
		while(it.hasNext()){
			String key = (String)it.next();
			str+=key+"="+param.get(key);
		}
		return str;
	}
	
	public static String makeSig(TreeMap param,String key){
		try{
			String str = OAuthUtil.sigParam(param);
			System.out.println("InterfaceService >> str :"+str);
			String mac =  new BigInteger(Coder.encryptHMAC((str).getBytes("utf-8"), key)).toString();
		    System.out.println("InterfaceService >> MAC :"+mac);
		    return mac;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static String encode(String data,String key) throws Exception{
		//Des3.secretKey = key;
		return  new Des3(key).encode(data);
	}

	public static String decode(String data,String key) throws Exception{
		//Des3.secretKey = key;
		return  new Des3(key).decode(data);
	}
	
	public static void main(String[] args) {
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
