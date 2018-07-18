package com.cn.mcc.utils.voice.tts;

import io.netty.util.internal.StringUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public class TestWebTts {
	// 评测webapi接口地址
	private static final String WEBTTS_URL = "http://api.xfyun.cn/v1/service/v1/tts";
	// 测试应用ID
	private static final String TEST_APPID = "5b31b662";
	// 测试接口密钥
	private static final String TEST_API_KEY = "26fb56a1781611f8a30a6730cd327666";

	public static void main(String[] args) throws IOException {
		String aue = "raw";//raw（未压缩的pcm或wav格式），lame（mp3格式）
		Map<String, String> header = getHeader("audio/L16;rate=16000", aue, "xiaoyan", "50", "50", "", "text", "50");
		// text = "山上五棵树，架上五壶醋，林中五只鹿，箱里五条裤。伐了山上树，搬下架上的醋，射死林中的鹿，取出箱中的裤。";
		String text = "我今天非常荣幸的被邀请参观中国电信广东分公司啊";
		Map<String, Object> resultMap = HttpUtil.doMultiPost(WEBTTS_URL, header, "text=" + text);
		System.out.println(resultMap);
		// 合成成功
		if ("audio/mpeg".equals(resultMap.get("Content-Type"))) {
			FileUtil.save("c:\\temp\\", resultMap.get("sid") + ".wav", (byte[]) resultMap.get("body"));
			System.out.println(resultMap.get("sid"));
		} else { // 合成失败
			System.out.println(resultMap.get("body").toString());
		}
	}

	/**
	 * 组装http请求头
	 * 
	 * @param aue
	 * @param resultLevel
	 * @param language
	 * @param category
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static Map<String, String> getHeader(String auf, String aue, String voiceName, String speed, String volume, String engineType, String textType, String pitch) throws UnsupportedEncodingException {
		String curTime = System.currentTimeMillis() / 1000L + "";
		String param = "{\"auf\":\"" + auf + "\"";
		if (!StringUtil.isNullOrEmpty(aue)) {
			param += ",\"aue\":\"" + aue + "\"";
		}
		if (!StringUtil.isNullOrEmpty(voiceName)) {
			param += ",\"voice_name\":\"" + voiceName + "\"";
		}
		if (!StringUtil.isNullOrEmpty(speed)) {
			param += ",\"speed\":\"" + speed + "\"";
		}
		if (!StringUtil.isNullOrEmpty(volume)) {
			param += ",\"volume\":\"" + volume + "\"";
		}
		if (!StringUtil.isNullOrEmpty(pitch)) {
			param += ",\"pitch\":\"" + pitch + "\"";
		}
		if (!StringUtil.isNullOrEmpty(engineType)) {
			param += ",\"engine_type\":\"" + engineType + "\"";
		}
		if (!StringUtil.isNullOrEmpty(textType)) {
			param += ",\"text_type\":\"" + textType + "\"";
		}
		param += "}";

		String paramBase64 = new String(Base64.encodeBase64(param.getBytes("UTF-8")));
		String checkSum = DigestUtils.md5Hex(TEST_API_KEY + curTime + paramBase64);
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		header.put("X-Param", paramBase64);
		header.put("X-CurTime", curTime);
		header.put("X-CheckSum", checkSum);
		header.put("X-Real-Ip", "192.168.1.102");
		header.put("X-Appid", TEST_APPID);
		System.out.println(header);
		return header;
	}
}
