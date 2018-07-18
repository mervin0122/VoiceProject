package com.cn.mcc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.mcc.bean.Employee;
import com.cn.mcc.bean.Iat;
import com.cn.mcc.bean.Tts;
import com.cn.mcc.service.EmployeeService;
import com.cn.mcc.utils.*;
import com.cn.mcc.utils.voice.VoiceUtil;
import com.cn.mcc.utils.voice.iat.FileUtil;
import com.cn.mcc.utils.voice.iat.HttpUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

/**
 * Created by mervin on 2018/6/25.
 */
@RestController
public class VoiceController extends BaseController{
   // public  final  static Logger logger=Logger.getLogger(VoiceController.class);

    //语音听写接口地址
    private static final String WEBIAT_URL = "http://api.xfyun.cn/v1/service/v1/iat";
    //语音合成接口地址
    private static final String WEBTTS_URL = "http://api.xfyun.cn/v1/service/v1/tts";

    /**
     * (音频文件)科大讯飞：语音(≤60秒)转文字
     * @param iat
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value="/voice/iat",method = RequestMethod.POST)
    @ResponseBody
    public Result iat(@RequestBody Iat iat) throws IOException,ParseException{
        String code="";
        String msg="";
        String data="";
        String analys="";
        try{
            Map<String, String> header = VoiceUtil.constructHeader("raw", "sms16k");
            // 读取音频文件，转二进制数组，然后Base64编码
            byte[] audioByteArray = FileUtil.read2ByteArray(iat.getFilePath());
            String audioBase64 = new String(Base64.encodeBase64(audioByteArray), "UTF-8");
            String bodyParam = "audio=" + audioBase64;
            String result = HttpUtil.doPost(WEBIAT_URL, header, bodyParam);
            JSONObject obj= JSON.parseObject(result);
            code=obj.getString("code");
            msg=obj.getString("desc");
            data=obj.getString("data");
            System.out.println(result);
            if (code.equals("0")){
                analys= Analysis.ansjSeg(data);
            }
        }catch (Exception e){
            code= Constants.RESULT_MESSAGE_EXCEPTION;
            msg="转换失败";
        }
        return  result(code,msg,data+"-->分析：【"+analys+"】");

    }
    /**
     * 音频流：将音频文件转二进制数组，然后Base64编码
     * @param iat
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value="/voice/iatb",method = RequestMethod.POST)
    @ResponseBody
    public Result iatb(@RequestBody Iat iat) throws IOException,ParseException{
        String code="";
        String msg="";
        String data="";
        try{
            Map<String, String> header = VoiceUtil.constructHeader("raw", "sms16k");
            String bodyParam = "audio=" + iat.getFilePath();
            String result = HttpUtil.doPost(WEBIAT_URL, header, bodyParam);
            JSONObject obj= JSON.parseObject(result);
            code=obj.getString("code");
            msg=obj.getString("desc");
            data=obj.getString("data");
            System.out.println(result);
        }catch (Exception e){
            code= Constants.RESULT_MESSAGE_EXCEPTION;
            msg="转换失败";
        }
        return  result(code,msg,data);

    }
    /**
     * 语音合成
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value="/voice/tts",method = RequestMethod.POST)
    @ResponseBody
    public Result tts(@RequestBody Tts tts) throws IOException,ParseException{
        String code="";
        String msg="";
        String data="";
        try{
             if (StringUtils.isEmpty(tts.getAue())){
                tts.setAue("raw");//raw（未压缩的pcm或wav格式），lame（mp3格式）
            }
            Map<String, String> header = VoiceUtil.getHeader("audio/L16;rate=16000", tts.getAue(), "xiaoyan", "50", "50", "", "text", "50");
            // String text = "山上五棵树，架上五壶醋，林中五只鹿，箱里五条裤。伐了山上树，搬下架上的醋，射死林中的鹿，取出箱中的裤。";
            Map<String, Object> resultMap = com.cn.mcc.utils.voice.tts.HttpUtil.doMultiPost(WEBTTS_URL, header, "text=" + tts.getTexts() );
            System.out.println(resultMap);
            // 合成成功
            if ("audio/mpeg".equals(resultMap.get("Content-Type"))) {
                com.cn.mcc.utils.voice.tts.FileUtil.save(Config.getInstance().get("TTS_URL"), resultMap.get("sid") + ".wav", (byte[]) resultMap.get("body"));
                System.out.println(resultMap.get("sid"));
                code="0";
                data=resultMap.get("sid").toString();
            } else { // 合成失败
                JSONObject obj= JSON.parseObject(resultMap.get("body").toString());
                code=obj.getString("code");
                msg=obj.getString("desc");
                System.out.println(resultMap.get("body").toString());
            }
        }catch (Exception e){
            code= Constants.RESULT_MESSAGE_EXCEPTION;
            msg="合成失败";
        }
        return  result(code,msg,data);

    }


}
