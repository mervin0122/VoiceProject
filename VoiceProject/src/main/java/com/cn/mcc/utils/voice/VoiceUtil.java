package com.cn.mcc.utils.voice;

import com.cn.mcc.utils.Config;
import io.netty.util.internal.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yyzc on 2018/6/27.
 */
public class VoiceUtil {
    /**
     * 语音听写:组装http请求头
     *
     * @param aue
     * @param engineType
     * @return
     * @throws UnsupportedEncodingException
     * @throws ParseException
     */
     public static Map<String, String> constructHeader(String aue, String engineType) throws UnsupportedEncodingException, ParseException {
        // 系统当前时间戳
        String X_CurTime = System.currentTimeMillis() / 1000L + "";
        // 业务参数
         // aue：音频编码，可选值：raw（未压缩的pcm或wav格式）、speex（speex格式）、speex-wb（宽频speex格式）
         //engine_type:引擎类型，可选值：sms16k（16k采样率普通话音频）、sms8k（8k采样率普通话音频）等
        String param = "{\"aue\":\""+aue+"\""+",\"engine_type\":\"" + engineType + "\"}";
        String X_Param = new String(Base64.encodeBase64(param.getBytes("UTF-8")));
        // 接口密钥
        String apiKey = Config.getInstance().get("IAT_API_KEY");
        // 讯飞开放平台应用ID
        String X_Appid = Config.getInstance().get("APPID");
        // 生成令牌
        String X_CheckSum = DigestUtils.md5Hex(apiKey + X_CurTime + X_Param);

        // 组装请求头
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        header.put("X-Param", X_Param);
        header.put("X-CurTime", X_CurTime);
        header.put("X-CheckSum", X_CheckSum);
        header.put("X-Appid", X_Appid);
        return header;
    }
    /**
     * 语音合成:组装http请求头
     *
     * @param aue
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Map<String, String> getHeader(String auf, String aue, String voiceName, String speed, String volume, String engineType, String textType, String pitch) throws UnsupportedEncodingException {
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
        String checkSum = DigestUtils.md5Hex(Config.getInstance().get("TTS_API_KEY") + curTime + paramBase64);
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        header.put("X-Param", paramBase64);
        header.put("X-CurTime", curTime);
        header.put("X-CheckSum", checkSum);
        header.put("X-Real-Ip", "192.168.1.102");
        header.put("X-Appid", Config.getInstance().get("APPID"));
        System.out.println(header);
        return header;
    }
}
