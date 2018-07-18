package com.cn.mcc.controller;

import com.alibaba.fastjson.JSON;
import com.cn.mcc.bean.Iat;
import com.cn.mcc.utils.BaseController;
import com.cn.mcc.utils.Result;
import com.iflytek.msp.cpdb.lfasr.client.LfasrClientImp;
import com.iflytek.msp.cpdb.lfasr.exception.LfasrException;
import com.iflytek.msp.cpdb.lfasr.model.LfasrType;
import com.iflytek.msp.cpdb.lfasr.model.Message;
import com.iflytek.msp.cpdb.lfasr.model.ProgressStatus;
import net.sf.json.JSONArray;
import org.springframework.web.bind.annotation.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

/**
 * Created by mervin on 2018/6/25.
 */
@RestController
public class VoiceIdentifyController extends BaseController{
   // public  final  static Logger logger=Logger.getLogger(VoiceIdentifyController.class);
    private static final LfasrType type = LfasrType.LFASR_STANDARD_RECORDED_AUDIO;

    private static int sleepSecond = 20;

    /**
     * 长语音转文字
     * @param iat
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value="/voice/vic",method = RequestMethod.POST)
    @ResponseBody
    public Result iat(@RequestBody Iat iat) throws IOException,ParseException{
        String code="";
        String msg="";
        String data="";
        /*System.setProperty("http.proxyHost", "proxy.sha.sap.corp");
        System.setProperty("http.proxyPort", "8080");
        System.setProperty("https.proxyHost", "proxy.sha.sap.corp");
        System.setProperty("https.proxyPort", "8080");*/
        Logger logger = Logger.getLogger(VoiceIdentifyController.class);
        logger.error("Jerry check whether log configuration file works");
        // System.exit(-1);
        // PropertyConfigurator.configure("log4j.properties");
        LfasrClientImp lc = null;
        try {
            lc = LfasrClientImp.initLfasrClient();
        } catch (LfasrException e) {
            Message initMsg = JSON.parseObject(e.getMessage(), Message.class);
            System.out.println("ecode=" + initMsg.getErr_no());
            System.out.println("failed=" + initMsg.getFailed());
        }
        // get upload task id
        String task_id = "";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("has_participle", "true");
        try {
            Message uploadMsg = lc.lfasrUpload(iat.getFilePath(), type, params);
            int ok = uploadMsg.getOk();
            if (ok == 0) {
                task_id = uploadMsg.getData();
                System.out.println("task_id=" + task_id);
            } else {
                System.out.println("ecode=" + uploadMsg.getErr_no());
                System.out.println("failed=" + uploadMsg.getFailed());
            }
        } catch (LfasrException e) {
            Message uploadMsg = JSON.parseObject(e.getMessage(), Message.class);
            System.out.println("ecode=" + uploadMsg.getErr_no());
            System.out.println("failed=" + uploadMsg.getFailed());
        }
        while (true) {
            try {
                Thread.sleep(sleepSecond * 1000);
                System.out.println("waiting ...");
            } catch (InterruptedException e) {
            }
            try {
                Message progressMsg = lc.lfasrGetProgress(task_id);
                if (progressMsg.getOk() != 0) {
                    System.out.println("task was fail. task_id:" + task_id);
                    System.out.println("ecode=" + progressMsg.getErr_no());
                    System.out.println("failed=" + progressMsg.getFailed());
                    continue;
                } else {
                    ProgressStatus progressStatus = JSON.parseObject(progressMsg.getData(), ProgressStatus.class);
                    if (progressStatus.getStatus() == 9) {
                        System.out.println("task was completed. task_id:" + task_id);
                        break;
                    } else {
                        System.out.println("task was incomplete. task_id:" + task_id + ", status:" + progressStatus.getDesc());
                        continue;
                    }
                }
            } catch (LfasrException e) {
                Message progressMsg = JSON.parseObject(e.getMessage(), Message.class);
                System.out.println("ecode=" + progressMsg.getErr_no());
                System.out.println("failed=" + progressMsg.getFailed());
            }
        }
        try {
            Message resultMsg = lc.lfasrGetResult(task_id);
            System.out.println(resultMsg.getData());
            if (resultMsg.getOk() == 0) {
                System.out.println(resultMsg.getData());
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(resultMsg.getData())) {
                    JSONArray jsStr = JSONArray.fromObject(resultMsg.getData());
                    if(jsStr.size()>0){
                        for(int i=0;i<jsStr.size();i++){
                            net.sf.json.JSONObject job = jsStr.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                            System.out.println(job.get("onebest")) ;  // 得到 每个对象中的属性值
                            code="0";
                            msg=job.get("onebest").toString();
                        }
                    }
                }
            } else {
                code=resultMsg.getErr_no()+"";
                msg=resultMsg.getFailed();
                System.out.println("ecode=" + resultMsg.getErr_no());
                System.out.println("failed=" + resultMsg.getFailed());
            }
        } catch (LfasrException e) {
            Message resultMsg = JSON.parseObject(e.getMessage(), Message.class);
            code=resultMsg.getErr_no()+"";
            msg=resultMsg.getFailed();
            System.out.println("ecode=" + resultMsg.getErr_no());
            System.out.println("failed=" + resultMsg.getFailed());
        }
        return  result(code,msg,data);
    }

}
