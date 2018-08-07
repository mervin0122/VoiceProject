package com.cn.mcc.controller;

import com.alibaba.fastjson.JSON;
import com.cn.mcc.bean.Recognizer.Cw;
import com.cn.mcc.bean.Recognizer.Root;
import com.cn.mcc.bean.Recognizer.Ws;
import com.cn.mcc.utils.BaseController;
import com.cn.mcc.utils.Constants;
import com.cn.mcc.utils.Result;
import com.cn.mcc.utils.voice.Recognizer;
import com.iflytek.cloud.speech.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;

/**
 * Created by mervin on 2018/6/25.
 */
@RestController
public class VoiceListeningController extends BaseController{
   // public  final  static Logger logger=Logger.getLogger(VoiceController.class);
    private static final String APPID = "5b1f775f";
    public  String texts = "";



    /**
     * 语音听写
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value="/voice/listening",method = RequestMethod.POST)
    @ResponseBody
    public Result listening(){
        String code="";
        String msg="";
        String data="";
        try{
            SpeechUtility.createUtility("appid=" + APPID);
            //1.创建SpeechRecognizer对象
            SpeechRecognizer mIat= SpeechRecognizer.createRecognizer( );
            //2.设置听写参数，详见《MSC Reference Manual》SpeechConstant类
            mIat.setParameter(SpeechConstant.DOMAIN, "iat"); //领域短信和日常用语：iat (默认)；视频：video；地图：poi；音乐：music
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");//简体中文：zh_cn（默认）；美式英文：en_us
            mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");//方言普通话：mandarin(默认);粤 语：cantonese四川话：lmz;河南话：henanese
            //识别完成后在本地保存一个音频文件（目前保存为Windows pcm）
            mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, "c:\\temp\\1234.pcm");
            //如果不写默认是“1”，“1”是从麦克风读取声音，“-1”是从.pcm音频文件读取声音
            mIat.setParameter(SpeechConstant.AUDIO_SOURCE,"1");
            // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
            mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
            // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
            mIat.setParameter(SpeechConstant.VAD_EOS, "4000");
            // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
            mIat.setParameter(SpeechConstant.ASR_PTT, "0");
           // VoiceListeningController recognizer =new VoiceListeningController();
            mIat.startListening (new Recognizer(){
                //开始录音
                public void onBeginOfSpeech() {
                    System.out.println("================开始录音================");
                }

                //音量值0~30
                public void onVolumeChanged(int volume){
                    //System.out.println("================当前音量："+volume);
                }

                //扩展用接口
                public void onEvent(int eventType,int arg1,int arg2,String msg) {}

                public void onResult(RecognizerResult results, boolean isLast) {
                   // System.out.print(results.getResultString());
                    StringBuilder text = new StringBuilder();
                    Root root = JSON.parseObject(results.getResultString(), Root.class);
                    Iterator<Ws> list = root.getWs().iterator();
                    while (list.hasNext()) {
                        Iterator<Cw> listCw = list.next().getCw().iterator();
                        while (listCw.hasNext()) {
                            text.append(listCw.next().getW());
                        }
                    }
                    System.out.print(text);
                    texts=text.toString();
                }

                //结束录音
                public void onEndOfSpeech() {
                    System.out.println("================录音结束================");
                }
                //会话发生错误回调接口
                public void onError(SpeechError error) {
                    System.out.println(error.getErrorDesc());
                }
            } );
            data=texts;
        }catch (Exception e){
            code= Constants.RESULT_MESSAGE_EXCEPTION;
            msg="转换失败";
        }
        return  result(code,msg,data);

    }


}
