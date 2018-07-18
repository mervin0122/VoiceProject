package com.cn.mcc.utils.voice.tts;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Http请求工具类
 */
public class HttpUtil {
	private HttpUtil() {

	}

	/**
	 * 发送post请求
	 * 
	 * @param url
	 * @param header
	 * @param body
	 * @return
	 */
	public static String doPost(String url, Map<String, String> header, String body) {
		String result = "";
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			// 设置 url
			URL realUrl = new URL(url);
			URLConnection connection = realUrl.openConnection();
			// 设置 header
			for (String key : header.keySet()) {
				connection.setRequestProperty(key, header.get(key));
			}
			// 设置请求 body
			connection.setDoOutput(true);
			connection.setDoInput(true);
			out = new PrintWriter(connection.getOutputStream());
			// 保存body
			out.print(body);
			// 发送body
			out.flush();
			// 获取响应body
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	/**
	 * 发送get请求
	 * 
	 * @param url
	 * @param header
	 * @return
	 */
	public static String doGet(String url, Map<String, String> header) {
		String result = "";
		BufferedReader in = null;
		try {
			// 设置 url
			URL realUrl = new URL(url);
			URLConnection connection = realUrl.openConnection();
			// 设置 header
			for (String key : header.keySet()) {
				connection.setRequestProperty(key, header.get(key));
			}
			// 设置请求 body
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			return null;
		}
		return result;
	}
	
	/**
	 * 发送post请求,根据Content-Type分别返回不同的返回值
	 * 
	 * @param url
	 * @param header
	 * @param body
	 * @return
	 */
	public static Map<String, Object> doMultiPost(String url, Map<String, String> header, String body) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		PrintWriter out = null;
		try {
			// 设置 url
			URL realUrl = new URL(url);
			URLConnection connection = realUrl.openConnection();
			// 设置 header
			for (String key : header.keySet()) {
				connection.setRequestProperty(key, header.get(key));
			}
			// 设置请求 body
			connection.setDoOutput(true);
			connection.setDoInput(true);
			out = new PrintWriter(connection.getOutputStream());
			// 保存body
			out.print(body);
			// 发送body
			out.flush();
			// 获取响应header
			String responseContentType = connection.getHeaderField("Content-Type");
			if ("audio/mpeg".equals(responseContentType)){
				// 获取响应body
				byte[] bytes = toByteArray(connection.getInputStream());
				resultMap.put("Content-Type", "audio/mpeg");
				resultMap.put("sid", connection.getHeaderField("sid"));
				resultMap.put("body", bytes);
				return resultMap;
			} else {
				// 设置请求 body
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				String result = "";
				while ((line = in.readLine()) != null) {
					result += line;
				}
				resultMap.put("Content-Type", "text/plain");
				resultMap.put("body", result);
				
				return resultMap;
			}
		} catch (Exception e) {
			return null;
		}
	}
	
	private static byte[] toByteArray(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024 * 4];
		int n = 0;
		while ((n = in.read(buffer)) != -1) {
			out.write(buffer, 0, n);
		}
		return out.toByteArray();
	}
}
