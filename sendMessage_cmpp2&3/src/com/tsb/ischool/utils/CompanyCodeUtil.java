package com.tsb.ischool.utils;

/**
*    
* 类名称：CompanyCodeUtil   
* 类描述：   
* 创建人：Guochangqing
* 创建时间：2014-10-23 下午5:56:38     
* 版本号： 1.0  
*
 */
public class CompanyCodeUtil {
	/**
	 * 
	 * @param companycode
	 * @return   1代表省 2 代表市 3 代表区 4 代表 校  0 代表error
	 */
	public static long getCodeRealType(String companycode){
		if(companycode==null||"".equals(companycode)){
			return 0;
		}else if(companycode.length()!=10){
			return 0;
		}else if(!companycode.endsWith("0000")){
			return 4;
		}else if(!companycode.substring(0, 6).endsWith("00")){
			return 3;
		}else if(!companycode.substring(0, 4).endsWith("00")){
			return 2;
		}else if(!companycode.substring(0, 2).endsWith("00")){
			return 1;
		}
		return 0;
	}
}
