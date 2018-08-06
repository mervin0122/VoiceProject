package com.cn.mcc.controller;

import com.baidu.aip.talker.facade.download.IAfterDownloadListener;
import com.baidu.aip.talker.facade.exception.LevelException;
import com.cn.mcc.controller.bdudp.UdpCallService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.IOException;

public class PrintAfterDownloadListener implements IAfterDownloadListener {

    private ObjectMapper objectMapper;
    private ObjectReader objectReader;

    public PrintAfterDownloadListener() {
        objectMapper = new ObjectMapper();
        objectReader = objectMapper.reader();
    }

    // 获取含有content和category字段的json
    // category TXT 实时识别结果
    // category INTENT 意图结果
    @Override
    public void onReceive(String json) {
        boolean isSuccessed = false;
        String text="";
        try {
            JsonNode node = objectReader.readTree(json);
            if (node.has("content")) {
                JsonNode contentNode = node.get("content");
                if (contentNode.has("category")) {
                    String category = contentNode.get("category").asText();
                    if (category.equals("TXT")) {
                       // text = EchoServer.parseTxt(contentNode); // 识别结果
                       //text = BDVoiceController.parseTxt(contentNode); // 识别结果
                        text =UdpCallService.parseTxt(contentNode);

                    } else if (category.equals("INTENT")) {
                        isSuccessed = parseIntent(contentNode); // 意图结果
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!isSuccessed) {
            System.out.println("RESULT receive success :" + json);

        }
    }

    // 配置文件中 app.scope = brain_ai_talker 生效

    // {"content": {"category": "INTENT", "content": "\u6765\u610f\u8bf4\u660e", "callId": "s1-ASR-34804-1-1530090052495",
    // "logId": "19D75D7A-0E28-4A40-B6F6-E9E24BB27CAC_24", "roleCategory": "AGENT", "triggerTime": 1530090071333,
    // "extJson": {"slot": {}, "say": "\u5b8c\u6210\u4efb\u52a1\uff1aCOME_SAY", "cmd": 0, "intent": "\u6765\u610f\u8bf4\u660e"},
    // "triggerTxt": "\u60a8\u5462\u8fd9\u8fb9\u662f\u4fe1\u7528\u5361\u4e2d\u5fc3"}, "name": "knowledge_content",
    // "callId": "s1-ASR-34804-1-1530090052495", "userId": 15137876, "logid": "a35209a0-79e8-11e8-b871-6c92bf139ec6",
    // "appId": 10811527}
    private boolean parseIntent(JsonNode node) {
        if (node.has("roleCategory") && node.has("content")) {
            String text = node.get("roleCategory").asText() + " ";
            text += "意图结果：" + node.get("content").asText();
            if (node.has("extJson")) {
                text += " , extJson:" + node.get("extJson").toString();
            }
            System.out.println(text);
            return true;
        }
        return false;
    }

    // {"content": {"category": "INTENT", "content": "\u4fe1\u606f\u786e\u8ba4", "callId": "s1-ASR-34804-1-1530090052495",
    // "logId": "19D75D7A-0E28-4A40-B6F6-E9E24BB27CAC_22", "roleCategory": "AGENT", "triggerTime": 1530090069734,
    // "extJson": {"slot": {}, "say": "\u5b8c\u6210\u4efb\u52a1\uff1aMSG_CONFIRM", "cmd": 0, "intent": "\u4fe1\u606f\u786e\u8ba4"},
    // "triggerTxt": "\u6270\u5230\u60a8\u5462\u8fd9\u8fb9\u662f\u4fe1\u7528\u5361"}, "name": "knowledge_content",
    // "callId": "s1-ASR-34804-1-1530090052495", "userId": 15137876, "logid": "a2815ab2-79e8-11e8-b871-6c92bf139ec6",
    // "appId": 10811527}
 /*   public static  String parseTxt(JsonNode node) throws IOException {
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
            System.out.println(text);
           // return true;
        }
       // return false;
        return text;
    }*/

    @Override
    public void onReceiveError(String json) {
        System.err.println("PrintAfterDownloadListener : RESULT receive success with error:" + json);
        System.exit(4);
    }

    @Override
    public void onRecieveLocalException(Exception exception, int level) {
        System.err.println("LogAfterDownloadListener Exception , level :" + level);
        if (level == LevelException.ERROR) {
            System.err.println("this is fatal error:");
        }
        exception.printStackTrace();
    }
}
