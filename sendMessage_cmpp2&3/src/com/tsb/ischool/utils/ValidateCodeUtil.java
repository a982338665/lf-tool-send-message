package com.tsb.ischool.utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ValidateCodeUtil {

	public static String getValidateCode() {
		Random random = new Random();
		Integer x = random.nextInt(899999);
		x += 100000;
		String code = x.toString();
		return code;
	}
	
	public static String getId() {
		String val = ValidateCodeUtil.getDateString()+ValidateCodeUtil.getRandom();
		return val.substring(2, val.length());
	}
	
	public static String getDateString() {
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String val = df.format(new Date());
		return val;
	}
	
	public static String getRandom(){
		Random random = new Random();
		Integer x = random.nextInt(900)+100;
		String code = x.toString();
		return code;
	}
	

	public static void main(String[] args) {
		System.out.println(ValidateCodeUtil.getValidateCode());
		
		for(int i=0;i<10;i++){
		    System.out.println(ValidateCodeUtil.getId());
		}
	}

}
