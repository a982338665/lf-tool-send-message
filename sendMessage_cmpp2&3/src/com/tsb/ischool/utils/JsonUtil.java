package com.tsb.ischool.utils;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tsb.ischool.framework.common.ErrorCode;
import com.tsb.ischool.framework.exception.ISchoolException;

/**
 * 
 * @author zkl
 * @date 2014-7-17 下午3:49:15
 * @version 1.0
 */
public class JsonUtil {
	static JsonConfig cfg = new JsonConfig();
	static {
		cfg.registerJsonValueProcessor(Timestamp.class, new JsonValueProcessor() {
			private final String format = "yyyy-MM-dd HH:mm:ss";

			public Object processObjectValue(String key, Object value, JsonConfig arg2) {
				if (value == null || value.equals(""))
					return "";
				if (value instanceof Date) {
					String str = new SimpleDateFormat(format).format((Date) value);
					return str;
				}
				return value.toString();
			}

			public Object processArrayValue(Object value, JsonConfig arg1) {
				return null;
			}
		});
	}

	public static Object buildeBean(Object obj, Class clazz) throws ISchoolException {
		try {
			JSONObject jsonObject = JSONObject.fromObject(obj);
			// String[] dateFormats = new String[] {"yyyy-MM-dd HH:mm:dd"};
			// JSONUtils.getMorpherRegistry().registerMorpher(new
			// DateMorpher(dateFormats));
			return JSONObject.toBean(jsonObject, clazz, cfg);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ISchoolException(ErrorCode.ISCHOOL_REQJSON_PARSEMSG_EXCEPTION);
		}
	}

	public static Object buildeBean(Object obj, Class clazz, Map classMap) throws ISchoolException {
		try {
			JSONObject jsonObject = JSONObject.fromObject(obj);
			// String[] dateFormats = new String[] {"yyyy/MM/dd"};
			// JSONUtils.getMorpherRegistry().registerMorpher(new
			// DateMorpher(dateFormats));
			return JSONObject.toBean(jsonObject, clazz, classMap);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ISchoolException(ErrorCode.ISCHOOL_REQJSON_PARSEMSG_EXCEPTION);
		}
	}

	public static String buildeJson(Object obj) {
		String _resultjson = "";
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		_resultjson = gson.toJson(obj);
		return _resultjson;
	}

	public static String buildeJsonComm(Object obj) {
		String _resultjson = "";
		JSONObject jsonObject = JSONObject.fromObject(obj, cfg);
		_resultjson = jsonObject.toString();
		return _resultjson;
	}

	public static Object toObject(String json, Type typeOfT) {

		if (json == null)
			return null;

		return new Gson().fromJson(json, typeOfT);
	}

	public static void main(String[] args) {
		
	}
	
	/**
	 * 将json格式的字符串解析成Map对象 <li>
	 * json格式：{"name":"admin","retries":"3fff","testname":"ddd","testretries":"fffffffff"}
	 */
	public static HashMap<String, String> toHashMap(Object object) {
		HashMap<String, String> data = new HashMap<String, String>();
		// 将json字符串转换成jsonObject
		JSONObject jsonObject = JSONObject.fromObject(object);
		Iterator it = jsonObject.keys();
		// 遍历jsonObject数据，添加到Map对象
		while (it.hasNext()) {
			String key = String.valueOf(it.next());
			String value = (String) jsonObject.get(key);
			data.put(key, value);
		}
		return data;
	}
	
}


