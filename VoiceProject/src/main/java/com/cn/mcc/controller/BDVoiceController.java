package com.cn.mcc.controller;

import com.alibaba.fastjson.JSON;
import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.talker.facade.Controller;
import com.baidu.aip.talker.facade.upload.LogBeforeUploadListener;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;
import com.cn.mcc.bean.Iat;
import com.cn.mcc.utils.BaseController;
import com.cn.mcc.utils.Constants;
import com.cn.mcc.utils.HttpUtil;
import com.cn.mcc.utils.Result;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.text.ParseException;
import java.util.Properties;

//import org.json.JSONObject;

/**
 * Created by mervin on 2018/6/25.
 */
@RestController
public class BDVoiceController extends BaseController {
    public  final  static Logger logger=Logger.getLogger(VoiceController.class);
    //设置APPID/AK/SK
    public static final String APP_ID = "11572625";
    public static final String API_KEY = "t5fTe2YN25P36FYMnBTQcUga";
    public static final String SECRET_KEY = "6SgSI55vVqCXOHTamtYwG3yE7A9uKvGX";
    private static String txts;
    // 音频文件路径
    // private static final String AUDIO_PATH = "./resource/test_1.pcm";

    // 官网获取的 API Key 更新为你注册的
    String clientId = "p3i3Sfhuls7LwDVrGtwr2VaF";
    // 官网获取的 Secret Key 更新为你注册的
    String clientSecret = "e9o8IcSOn9YKmGVofhnIeRj1UKwaDNkr";
    String url = "https://aip.baidubce.com/rest/2.0/antispam/v2/spam";
    String url1 = "https://aip.baidubce.com/rpc/2.0/nlp/v1/lexer";

    @RequestMapping(value="/voice/vysb",method = RequestMethod.POST)
    @ResponseBody
    public Result vysb(@RequestBody Iat iat) throws IOException,ParseException{
        String code="0";
        String msg="";
        String data="";
        try{
            System.out.println("请等待程序正常退出， 否则测试用户将导致10分钟内无法正常使用。");
          //  String dir = "src/test/resources/pcm";
            Controller controller = new Controller(new LogBeforeUploadListener(), new PrintAfterDownloadListener(), getProperties());

            // 选择开启 asrOne 或者 asrBoth ， asrOne即一个通话1路音频， asrBoth为1个通话，2路音频
            // 8k_test.pcm 较短  salesman.pcm 较长，客服  customer.pcm 用户
            //  BiccTest.asrOne(controller, dir + "/8k_test.pcm");
            // BiccTest.asrBoth(controller, "src/test/resources/pcm/8k_test.pcm", "src/test/resources/pcm/8k_test.pcm");

            // 请等待程序正常退出，即end包发送完成。否则测试用户将导致10分钟内无法正常使用。
            BiccTest.asrOne(controller,iat.getFilePath());
           // BiccTest.asrOne(controller,dir + "/8k_test.pcm");
            // BiccTest.asrBoth(controller, dir + "/salesman.pcm", dir + "/customer.pcm");

            controller.stop();

        }catch (Exception e){
            code= Constants.RESULT_MESSAGE_EXCEPTION;
            msg="转换失败";
        }
        return  result(code,msg,data);

    }

    public static void main(String[] args) throws Exception {

        System.out.println("请等待程序正常退出， 否则测试用户将导致10分钟内无法正常使用。");
        String dir = "msc";
        Controller controller = new Controller(new LogBeforeUploadListener(), new PrintAfterDownloadListener(), getProperties());

        // 选择开启 asrOne 或者 asrBoth ， asrOne即一个通话1路音频， asrBoth为1个通话，2路音频
        // 8k_test.pcm 较短  salesman.pcm 较长，客服  customer.pcm 用户
        //  BiccTest.asrOne(controller, dir + "/8k_test.pcm");
        // BiccTest.asrBoth(controller, "src/test/resources/pcm/8k_test.pcm", "src/test/resources/pcm/8k_test.pcm");

        // 请等待程序正常退出，即end包发送完成。否则测试用户将导致10分钟内无法正常使用。
        BiccTest.asrOne(controller,dir + "/8k_test.pcm");
        System.out.println("================="+txts);
        // BiccTest.asrOne(controller,dir + "/customer.pcm");
        // BiccTest.asrBoth(controller, dir + "/salesman.pcm", dir + "/customer.pcm");
        controller.stop();
    }
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
    /**
     * 文本审核
     * label:1	暴恐违禁; 2	文本色情; 3	政治敏感
             4	恶意推广; 5 低俗辱骂; 6	低质灌水
     */
    @RequestMapping(value="/voice/spam",method = RequestMethod.POST)
    @ResponseBody
    public Result spam(@RequestBody Iat iat) throws IOException,ParseException{
        String code="0";
        String msg="";
        String data="";
        try{
            String access_token= getAuth(clientId, clientSecret);
            //设置请求的编码
            String  param = "content="+URLEncoder.encode(iat.getFilePath(),"UTF-8");
            //发送并取得结果
            data = HttpUtil.post(url, access_token, param);
            System.out.println(data);
            com.alibaba.fastjson.JSONObject obj= JSON.parseObject(data);
            if (StringUtils.isNotEmpty(obj.getString("log_id"))){
                data=obj.get("result").toString();
                code="200";
            }else{
                data=obj.get("result").toString();
            }

        }catch (Exception e){
            code= Constants.RESULT_MESSAGE_EXCEPTION;
            msg="审核失败";
        }
        return  result(code,msg,data);
    }
    /**
     * 词法分析
     */
    @RequestMapping(value="/voice/lexer",method = RequestMethod.POST)
    @ResponseBody
    public Result lexer(@RequestBody Iat iat) throws IOException,ParseException{
        String code="0";
        String msg="";
        String data="";
        try{

            String access_token= getAuth(clientId, clientSecret);
            //设置请求的编码
            String  param = "{\"text\":\""+iat.getFilePath()+"\"}";
                  //  "text="+URLEncoder.encode(iat.getFilePath(),"UTF-8");
            //发送并取得结果
            data = HttpUtil.postNLP(url1+"?access_token="+access_token, param);
            System.out.println(data);
            com.alibaba.fastjson.JSONObject obj= JSON.parseObject(data);
            if (StringUtils.isNotEmpty(obj.getString("text"))){
                data=obj.get("items").toString();
                code="200";
            }else{
                data=obj.get("items").toString();
            }

        }catch (Exception e){
            code= Constants.RESULT_MESSAGE_EXCEPTION;
            msg="审核失败";
        }
        return  result(code,msg,data);
    }
    /**
     * 默认读取conf/sdk.properties, 您也可以用下面的构造方法，传入Properties类
     *
     * Controller controller =
     * new Controller(new LogBeforeUploadListener(), new PrintAfterDownloadListener(), getProperties());
     * @return
     * @throws Exception
     */
    private static Properties getProperties() throws Exception {
        //String fullFilename = System.getProperty("user.dir") + "/conf/sdk.properties";
        String fullFilename = "src/main/resources/application.properties";
        Properties properties = new Properties();
        FileInputStream is = null;
        try {
            is = new FileInputStream(fullFilename);
            properties.load(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return properties;
    }
    public static  String parseTxt(JsonNode node) throws IOException {
        //private boolean parseTxt(JsonNode node) throws IOException {
        String text="";
        if (node.has("roleCategory") && node.has("content")) {
            text = node.get("roleCategory").asText() + " ";
            if (node.has("extJson")) {
                JsonNode nodeExt = node.get("extJson");
                if (nodeExt.has("completed")) {
                    if (nodeExt.get("completed").asInt() == 1) {
                        text += "临时";
                    } else if (nodeExt.get("completed").asInt() == 3) {
                        text += "最终";
                    }
                }
            }
            text += "识别结果：" + node.get("content").asText();
            txts=text;
            System.out.println(text);
            // return true;
        }
        // return false;
        return text;
    }
    public String gettoken() {

        // 官网获取的 API Key 更新为你注册的
        String clientId = "p3i3Sfhuls7LwDVrGtwr2VaF";
        // 官网获取的 Secret Key 更新为你注册的
        String clientSecret = "e9o8IcSOn9YKmGVofhnIeRj1UKwaDNkr";
        return getAuth(clientId, clientSecret);
    }

    public static String getAuth(String ak, String sk) {
        //
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            //Map<String, List<String>> map = connection.getHeaderFields();
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
            JSONObject jsonObject = new JSONObject(result);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }
    public String get_text(String content,String url,String accessToken)
    {
        String param;
        String data;
        try {
            //设置请求的编码
            param = "content="+ URLEncoder.encode(content,"UTF-8");
            //发送并取得结果
            data = HttpUtil.post(url, accessToken, param);
            System.out.println(data);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
}
