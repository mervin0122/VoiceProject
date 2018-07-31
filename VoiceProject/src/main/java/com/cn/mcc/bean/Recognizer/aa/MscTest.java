package com.cn.mcc.bean.Recognizer.aa;

import com.cn.mcc.utils.voice.Recognizer;
import com.iflytek.cloud.speech.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MscTest {

    private static final String APPID = "5b1f775f";

    private StringBuffer mResult = new StringBuffer();

    /** 最大等待时间， 单位ms */
    private int maxWaitTime = 500000;
    /** 每次等待时间 */
    private int perWaitTime = 100;
    /** 出现异常时最多重复次数 */
    private int maxQueueTimes = 3;
    /** 音频文件 */
    private String fileName = "";

    static {
        Setting.setShowLog( false );
        SpeechUtility.createUtility("appid=" + APPID);
    }

    public String voice2words(String fileName) throws InterruptedException {
        return voice2words(fileName, true);
    }

    /**
     *
     * @desc: 工具类，在应用中有一个实例即可， 但是该实例是有状态的， 因此要消除其他调用对状态的修改，所以提供一个init变量
     * @auth: zona
     * 2017年1月4日 下午4:38:45
     * @param fileName
     * @param init 是否初始化最大等待时间。
     * @return
     * @throws InterruptedException
     */
    public String voice2words(String fileName, boolean init) throws InterruptedException {
        if(init) {
            maxWaitTime = 500000;
            maxQueueTimes = 3;
        }
        if(maxQueueTimes <= 0) {
            mResult.setLength(0);
            mResult.append("解析异常！");
            return mResult.toString();
        }
        this.fileName = fileName;

        return recognize();
    }


    // *************************************音频流听写*************************************

    /**
     * 听写
     * @return
     * @throws InterruptedException
     */
    private String recognize() throws InterruptedException {
        if (SpeechRecognizer.getRecognizer() == null)
            SpeechRecognizer.createRecognizer();
        return RecognizePcmfileByte();
    }

    /**
     * 自动化测试注意要点 如果直接从音频文件识别，需要模拟真实的音速，防止音频队列的堵塞
     * @throws InterruptedException
     */
    private String RecognizePcmfileByte() throws InterruptedException {
        // 1、读取音频文件
        FileInputStream fis = null;
        byte[] voiceBuffer = null;
        try {
            fis = new FileInputStream(new File(fileName));
            voiceBuffer = new byte[fis.available()];
            fis.read(voiceBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fis) {
                    fis.close();
                    fis = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 2、音频流听写
        if (0 == voiceBuffer.length) {
            mResult.append("no audio avaible!");
        } else {
            //解析之前将存出结果置为空
            mResult.setLength(0);
          /*  SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
            recognizer.setParameter(SpeechConstant.DOMAIN, "iat");
            recognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            recognizer.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
            //写音频流时，文件是应用层已有的，不必再保存
//			recognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH,
//					"./iflytek.pcm");
            recognizer.setParameter( SpeechConstant.RESULT_TYPE, "plain" );
            recognizer.startListening(recListener);
            ArrayList<byte[]> buffers = splitBuffer(voiceBuffer,
                    voiceBuffer.length, 4800);
            for (int i = 0; i < buffers.size(); i++) {
                // 每次写入msc数据4.8K,相当150ms录音数据
                recognizer.writeAudio(buffers.get(i), 0, buffers.get(i).length);
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            recognizer.stopListening();*/
            SpeechRecognizer mIat= SpeechRecognizer.createRecognizer( );
            //2.设置听写参数，详见《MSC Reference Manual》SpeechConstant类
            mIat.setParameter(SpeechConstant.DOMAIN, "iat"); //领域短信和日常用语：iat (默认)；视频：video；地图：poi；音乐：music
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");//简体中文：zh_cn（默认）；美式英文：en_us
            mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");//方言普通话：mandarin(默认);粤 语：cantonese四川话：lmz;河南话：henanese
            //识别完成后在本地保存一个音频文件（目前保存为Windows pcm）
            mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, "c:\\temp\\655.pcm");
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

            // 在原有的代码基础上主要添加了这个while代码等待音频解析完成，recognizer.isListening()返回true，说明解析工作还在进行
            while(mIat.isListening()) {
                if(maxWaitTime < 0) {
                    mResult.setLength(0);
                    mResult.append("解析超时！");
                    break;
                }
                Thread.sleep(perWaitTime);
                maxWaitTime -= perWaitTime;
            }
        }
        return mResult.toString();
    }

    /**
     * 将字节缓冲区按照固定大小进行分割成数组
     *
     * @param buffer
     *            缓冲区
     * @param length
     *            缓冲区大小
     * @param spsize
     *            切割块大小
     * @return
     */
    private ArrayList<byte[]> splitBuffer(byte[] buffer, int length, int spsize) {
        ArrayList<byte[]> array = new ArrayList<byte[]>();
        if (spsize <= 0 || length <= 0 || buffer == null
                || buffer.length < length)
            return array;
        int size = 0;
        while (size < length) {
            int left = length - size;
            if (spsize < left) {
                byte[] sdata = new byte[spsize];
                System.arraycopy(buffer, size, sdata, 0, spsize);
                array.add(sdata);
                size += spsize;
            } else {
                byte[] sdata = new byte[left];
                System.arraycopy(buffer, size, sdata, 0, left);
                array.add(sdata);
                size += left;
            }
        }
        return array;
    }

    /**
     * 听写监听器
     */
    private RecognizerListener recListener = new RecognizerListener() {

        public void onBeginOfSpeech() { }

        public void onEndOfSpeech() { }

        public void onVolumeChanged(int volume) { }

        public void onResult(RecognizerResult result, boolean islast) {
            mResult.append(result.getResultString());
        }

        public void onError(SpeechError error) {
            try {
                voice2words(fileName);
                maxQueueTimes--;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }

        public void onEvent(int eventType, int arg1, int agr2, String msg) { }

    };

}
