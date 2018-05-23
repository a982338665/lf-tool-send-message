package com.tsb.ischool.framework.common;
/**
 * 
 * @author zkl
 * @date 2014-7-17 下午3:47:07
 * @version 1.0
 */
public class ErrorMsgManager {

	
	public static String GetErrorMsg(long errorCode){
		String _errorMsg = "";
		
		if(errorCode == ErrorCode.ISCHOOL_REQJSON_IO_EXCEPTION){
			_errorMsg = "输入输出异常！";
		}
		else if(errorCode == ErrorCode.ISCHOOL_REQJSON_CHARACTERCODING_EXCEPTION){
			_errorMsg = "输入输出异常！";
		}
		else if(errorCode == ErrorCode.ISCHOOL_REQJSON_PARSEMSG_EXCEPTION){
			_errorMsg = "输入输出异常,协议体结构错误！";
		}
		else if(errorCode == ErrorCode.ISCHOOL_REQJSON_C2SBASEMSG_ISNULL){
			_errorMsg = "输入输出异常！";
		}
		else if(errorCode == ErrorCode.ISCHOOL_REQBODY_INVALID_MSGTYPE){
			_errorMsg = "参数错误，不合理的请求协议类型！";
		}
		else if(errorCode == ErrorCode.ISCHOOL_SERVICE_EXCEPTION){
			_errorMsg = "自定义服务相关异常！";
		}
		return _errorMsg;
	}
	
	
}
