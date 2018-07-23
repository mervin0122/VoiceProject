package com.cn.mcc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.util.Util;
import com.cn.mcc.bean.Iat;
import com.cn.mcc.bean.Tts;
import com.cn.mcc.service.RTASRService;
import com.cn.mcc.utils.*;
import com.cn.mcc.utils.voice.DraftWithOrigin;
import com.cn.mcc.utils.voice.VoiceUtil;
import com.cn.mcc.utils.voice.iat.FileUtil;
import com.cn.mcc.utils.voice.iat.HttpUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by mervin on 2018/6/25.
 */
@RestController
public class BDVoiceController extends BaseController{
    public  final  static Logger logger=Logger.getLogger(VoiceController.class);

   //设置APPID/AK/SK
   public static final String APP_ID = "11572625";
    public static final String API_KEY = "t5fTe2YN25P36FYMnBTQcUga";
    public static final String SECRET_KEY = "6SgSI55vVqCXOHTamtYwG3yE7A9uKvGX";

    // 音频文件路径
   // private static final String AUDIO_PATH = "./resource/test_1.pcm";

    /**
     * (音频文件)科大讯飞：语音(≤60秒)转文字
     * @param iat
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value="/voice/vtt",method = RequestMethod.POST)
    @ResponseBody
    public Result iat(@RequestBody Iat iat) throws IOException,ParseException{
        String code="";
        String msg="";
        String data="";
        String analys="";
        try{
            AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
            // 对本地语音文件进行识别
            String path = "c:\\temp\\hts002d4c04@ch348b0eb0ece0477600.wav";
            org.json.JSONObject asrRes = client.asr(path, "wav", 16000, null);
            System.out.println(asrRes);

            // 对语音二进制数据进行识别
            byte[] datas = Util.readFileByBytes(path);     //readFileByBytes仅为获取二进制数据示例
            org.json.JSONObject asrRes2 = client.asr(datas, "pcm", 16000, null);
            System.out.println(asrRes2);
           // JSONObject obj= JSON.parseObject(asrRes2);
            System.out.println(asrRes2.getString("result"));
            System.out.println(asrRes2.getString("err_msg"));
            if (code.equals("success.")){
                data=asrRes2.getString("result");
            }

        }catch (Exception e){
            code= Constants.RESULT_MESSAGE_EXCEPTION;
            msg="转换失败";
        }
        return  result(code,msg,data);

    }

}
