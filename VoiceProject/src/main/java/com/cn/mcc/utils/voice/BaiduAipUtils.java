package com.cn.mcc.utils.voice;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.util.Util;
import net.sf.json.JSON;
import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 * Created by yyzc on 2018/7/23.
 */
public class BaiduAipUtils {
    protected static Logger logger = Logger.getLogger(BaiduAipUtils.class);

    //设置APPID/AK/SK
    public static final String APP_ID = "11572625";
    public static final String API_KEY = "t5fTe2YN25P36FYMnBTQcUga";
    public static final String SECRET_KEY = "6SgSI55vVqCXOHTamtYwG3yE7A9uKvGX";


    public static String getBaiduAipResult(String saveUrt,String type) throws  Exception{
        logger.info("百度aip------------->BaiduAipUtils：url="+saveUrt);
        // 初始化一个FaceClient
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        if(type == null)
            type = "wav";

        if(saveUrt == null)
            return null;

        JSONObject res = client.asr(saveUrt, type, 16000, null);
        String works = "";
        if("0".equals(res.get("err_no").toString()) && "success.".equals(res.get("err_msg").toString())){
            works = res.getJSONArray("result").getString(0);
        }
        logger.info(res.toString(2));
        logger.info(works);
        return works;

    }
    /**
     * @throws Throwable
     */
    public static void main(String[] args) throws Throwable
   // public void synthesis(AipSpeech client) throws Throwable
    {     AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
        // 对本地语音文件进行识别
        //String path = "c:\\temp\\hts002d4c04@ch348b0eb0ece0477600.wav";
        String path = "c:\\temp\\test_1.pcm";
        JSONObject asrRes = client.asr(path, "pcm", 16000, null);
        System.out.println(asrRes);


        // 对语音二进制数据进行识别
        byte[] data = Util.readFileByBytes(path);     //readFileByBytes仅为获取二进制数据示例
        JSONObject asrRes2 = client.asr(data, "pcm", 16000, null);
        System.out.println(asrRes);


        // 对网络上音频进行识别
      //  String url = "http://somehost/res/16k_test.pcm";
      //  String callback = "http://callbackhost/aip/dump";
       // JSONObject res = client.asr(url, callback, "pcm", 16000, null);
        //System.out.println(res);
    }


}
