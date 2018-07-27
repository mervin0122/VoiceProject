package com.cn.mcc.controller;

import com.baidu.aip.speech.AipSpeech;
import com.cn.mcc.bean.Iat;
import com.cn.mcc.utils.BaseController;
import com.cn.mcc.utils.Constants;
import com.cn.mcc.utils.Result;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

//import org.json.JSONObject;

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
     * 百度长语音转换
     * @param iat
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value="/voice/vtt",method = RequestMethod.POST)
    @ResponseBody
    public Result iat(@RequestBody Iat iat) throws IOException,ParseException{
        String code="0";
        String msg="";
        String data="";
        String analys="";
        try{
            AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
            // 对本地语音文件进行识别
           // String path = "c:\\temp\\hts002d4c04@ch348b0eb0ece0477600.wav";
            JSONObject asrRes = client.asr(iat.getFilePath(), "pcm", 16000, null);
            System.out.println(asrRes);

            // 对语音二进制数据进行识别
            //byte[] datas = Util.readFileByBytes(iat.getFilePath());     //readFileByBytes仅为获取二进制数据示例
            //org.json.JSONObject asrRes2 = client.asr(datas, "pcm", 16000, null);
         //  System.out.println(asrRes2);
           // JSONObject obj= JSON.parseObject(asrRes2);
           // System.out.println(asrRes2.getString("result"));
           //System.out.println(asrRes2.getString("err_msg"));
            if (asrRes.getString("err_msg").equals("success.")){
                data=asrRes.get("result").toString();
                code="200";
            }else{
                data=asrRes.get("result").toString();
            }

        }catch (Exception e){
            code= Constants.RESULT_MESSAGE_EXCEPTION;
            msg="转换失败";
        }
        return  result(code,msg,data);

    }

}
