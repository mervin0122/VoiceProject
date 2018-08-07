package com.cn.mcc.controller;

import com.cn.mcc.utils.voice.Recognizer;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechRecognizer;
import com.iflytek.cloud.speech.SpeechUtility;


// 科大讯飞---语音听写.




public class XfyunVoiceListening {
    private static final String APPID = "5b1f775f";
    public static void main(String[] args) {
        SpeechUtility.createUtility("appid=" + APPID);
        //1.创建SpeechRecognizer对象
        SpeechRecognizer mIat= SpeechRecognizer.createRecognizer( );
        //2.设置听写参数，详见《MSC Reference Manual》SpeechConstant类
        mIat.setParameter(SpeechConstant.DOMAIN, "iat"); //领域短信和日常用语：iat (默认)；视频：video；地图：poi；音乐：music
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");//简体中文：zh_cn（默认）；美式英文：en_us
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");//方言普通话：mandarin(默认);粤 语：cantonese四川话：lmz;河南话：henanese
        //识别完成后在本地保存一个音频文件（目前保存为Windows pcm）
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, "c:\\temp\\0808.pcm");
        //如果不写默认是“1”，“1”是从麦克风读取声音，“-1”是从.pcm音频文件读取声音
        mIat.setParameter(SpeechConstant.AUDIO_SOURCE,"1");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "4000");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");
        Recognizer recognizer =new Recognizer();
        mIat.startListening (recognizer);
    }
}
