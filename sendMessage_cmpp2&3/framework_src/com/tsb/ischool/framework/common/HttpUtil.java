package com.tsb.ischool.framework.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

public class HttpUtil {

	private static org.apache.log4j.Logger logger = Logger.getLogger(HttpUtil.class.getName());
	/**
	 * 添加一个任务
	 */
//	public void setWork(){
//		String requestUrl = "http://10.0.0.189:50/TSB_ISCHOOL_WORKER/iworker/setwork";
//		for(String lesson : getLessons()){
//			C2SSetWorkQueueBean c = new C2SSetWorkQueueBean();
//			c.setModuleid(lesson);
//			c.setModuletype(200);
//			c.setLessonid(lesson);
//			sendRequest(requestUrl, "POST",JsonUtil.buildeJson(c));
//			
//		}
//	}
	
	
	
	private static void sendRequest(String url1,String requestMethod){
		sendRequest(url1, requestMethod, null, null,null);
	}
	
	public static void sendRequest(String url1,String requestMethod,String data){
		sendRequest(url1, requestMethod, data, null,null);
	}
	public static void sendRequest(String url1,String requestMethod,String data,String mediaType){
		sendRequest(url1, requestMethod, data, null,mediaType);
	}
	
	public static void sendRequest(String url1,String requestMethod,Map<String,String> headParamMap,String mediaType){
		sendRequest(url1, requestMethod, null, headParamMap,mediaType);
	}
	
	private static void sendRequest(String url1,String requestMethod,String data,Map<String,String> headParamMap,String mediaType){
		if(null == mediaType || mediaType.equals("")){
			mediaType = "application/json";
		}
		try {
			URL url = new URL(url1);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(requestMethod);
			conn.setRequestProperty("Accept", mediaType);
			
			if(headParamMap!=null && !headParamMap.isEmpty()){
				for(String key:headParamMap.keySet()){
					String value = headParamMap.get(key);
					conn.addRequestProperty(key, value);
				}
			}
			
			if(data!=null && data.trim().length()>0){
				conn.setDoOutput(true);
				conn.setRequestProperty("Content-Type", mediaType);
				String input = data;
				
				//System.out.println("outputString:"+input+"\n");
				logger.debug("DEBUG_PG"+" | "+"发出的数据包" + " | "+"字符串:" + input +"\n");

				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();
			}
			
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			
			String output;
			logger.debug("DEBUG_PG"+" | "+"接收的数据包" + " | "+"字符串:" +"\n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				logger.debug("DEBUG_PG"+" ----> " + output +"\n");
			}
			
			conn.disconnect();
		} catch (Exception e) {
			logger.error(url1+"--->"+e.getMessage());
		}
	}
	
	
	
	public static String postJsonData(String urlStr,String json){
		StringBuilder buffer=new StringBuilder();
		BufferedReader reader=null;
		PrintWriter out=null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("POST");
			http.setConnectTimeout(5000); 
			http.setReadTimeout(30000); 
			http.setInstanceFollowRedirects(true);
			//http.setRequestProperty("Content-Type","application/json;charset=utf-8");
//			http.addRequestProperty("Accept", "*/*");  
//			http.addRequestProperty("Accept-Language", "zh-cn");  
			http.addRequestProperty("Content-type", "text/plain");  
//			http.addRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727)");  
			http.setDefaultUseCaches(false);
			http.setDoOutput(true); 
			http.setDoInput(true); 
			out = new PrintWriter(http.getOutputStream());
			out.print(json);//传入参数
			out.close();
           // 读取返回数据 
			reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
			String line = null;
			while ((line=reader.readLine())!=null) {
				buffer.append(line+"\n");
			}
			if(buffer.length()>0){
				buffer.deleteCharAt(buffer.length()-1);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(out!=null)out.close();
				if(reader!=null)reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return buffer.toString();
	}
	
	
public static void main(String[] args) {
		
		try {
			JSONObject json=new JSONObject();
			json.put("time", 0);
			JSONArray userids=new JSONArray();
			userids.add(100155);
			userids.add(100188);
			json.put("userids",userids );
			json.put("clientype","0" );
			json.put("datatype","json" );
			json.put("command","push" );
			json.put("msgtype",102);
			
			
			JSONObject contentObj=new JSONObject();
			contentObj.put("title", "测试");
			contentObj.put("msgstr", "测试消息体");
			contentObj.put("createtime", "2014-10-15 12:00:00");
			
			json.put("content",contentObj);
			
			
			postJsonData("http://127.0.0.1:8020/push/", json.toString());
			//%7B%22content%22:%7B%22createtime%22:%222014-10-15 12:00:00%22,%22title%22:%22%E6%B5%8B%E8%AF%95%22,%22msgstr%22:%22%E6%B5%8B%E8%AF%95%E6%B6%88%E6%81%AF%E4%BD%93%22%7D,%22time%22:0,%22msgtype%22:102,%22command%22:%22push%22,%22datatype%22:%22json%22,%22clientype%22:%220%22,%22userids%22:%5B100155,100188%5D%7D
			//%7B%22content%22:%7B%22createtime%22:%222014-10-15+12:00:00%22,%22title%22:%22%E6%B5%8B%E8%AF%95%22,%22msgstr%22:%22%E6%B5%8B%E8%AF%95%E6%B6%88%E6%81%AF%E4%BD%93%22%7D,%22time%22:0,%22msgtype%22:102,%22command%22:%22push%22,%22datatype%22:%22json%22,%22clientype%22:%220%22,%22userids%22:%5B100155,100188%5D%7D
		} catch (JSONException e) {
			e.printStackTrace();
		} 
}
}