package com.cn.mcc.utils.voice;

import com.alibaba.fastjson.JSON;
import com.cn.mcc.bean.Recognizer.Cw;
import com.cn.mcc.bean.Recognizer.Root;
import com.cn.mcc.bean.Recognizer.Ws;
import com.iflytek.cloud.speech.RecognizerListener;
import com.iflytek.cloud.speech.RecognizerResult;
import com.iflytek.cloud.speech.SpeechError;

import java.util.Iterator;

/**
 * Created by yyzc on 2018/6/13.
 */
public class Recognizer implements RecognizerListener {
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

    //听写结果回调接口:一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加
    //isLast等于true时会话结束。
    public void onResult(RecognizerResult results, boolean isLast){
        System.out.print(results.getResultString());
        StringBuilder text = new StringBuilder();
        Root root= JSON.parseObject(results.getResultString(), Root.class);
        Iterator<Ws> list=root.getWs().iterator();
        while(list.hasNext()){
            Iterator<Cw> listCw=list.next().getCw().iterator();
            while(listCw.hasNext()){
                text.append(listCw.next().getW());
            }
        }
        System.out.print(text);
        //结果实例:Result:{"sn":1,"ls":false,"bg":0,"ed":0,"ws":[{"bg":0,"cw":[{"sc":0.00,"w":"你好"}]}]}
        //sn:第几句,ls:是否是第一句,bg：开始,ed:结束,ws:词语,cw:中文分词,w:单词,sc:分数
    }
    //结束录音
    public void onEndOfSpeech() {
        System.out.println("================录音结束================");
    }
    //会话发生错误回调接口
    public void onError(SpeechError error) {
        // error.getPlainDescription(true); //获取错误码描述
        System.out.println(error.getErrorDesc());
    }
}
