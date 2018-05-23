/**
 * 
 */
package com.tsb.ischool.web;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.tsb.ischool.sms.model.C2SMobile;
import com.tsb.ischool.sms.model.MobileMessage;
import com.tsb.ischool.utils.JsonUtil;

/**
 * 接受客户端传参并提交到  web service发送短信的测试类
 * @author Dongrisheng
 * 
 */
public class SendConfigRestTest {
	private final String BASE_URL = "http://localhost:8080/TSB_ISCHOOL_SENDMSG_SERVER";//正式环境短信地址
	
	@Test
	public void sendmess() throws Exception {
		String requestUrl = BASE_URL + "/sms/sendmessage";
		C2SMobile c2sMobile = new C2SMobile();
		List<MobileMessage> list = new ArrayList();
		MobileMessage mobile = new MobileMessage();
		mobile.setMessageContent("【智动校园】:[作业]李欣依姌:练习册22、23页做完。（请家长指导检查）试卷一份做完。（请批阅打分）;[路芝梅★★]");
		mobile.setMobileNo("15101221992");
		list.add(mobile);

		c2sMobile.setList(list);
		 
		String s = JsonUtil.buildeJson(c2sMobile);
		System.out.println(s);
		sendRequest(requestUrl, "POST",s);
		
	}
	
	
	public void sendRequest(String url1, String requestMethod, String data) {
		sendRequest(url1, requestMethod, data, null);
	}

	public void sendRequest(String url1, String requestMethod, Map<String, String> headParamMap) {
		sendRequest(url1, requestMethod, null, headParamMap);
	}

	private void sendRequest(String url1, String requestMethod, String data, Map<String, String> headParamMap) {

		try {

			URL url = new URL(url1);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(requestMethod);
			conn.setRequestProperty("Accept", "application/json");

			if (headParamMap != null && !headParamMap.isEmpty()) {
				for (String key : headParamMap.keySet()) {
					String value = headParamMap.get(key);
					conn.addRequestProperty(key, value);
				}
			}

			if (data != null && data.trim().length() > 0) {
				conn.setDoOutput(true);
				conn.setRequestProperty("Content-Type", "application/json");
				String input = data;

				System.out.println("outputString:" + input + "\n");

				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();
			}

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
