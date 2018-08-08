package com.cn.mcc.controller.udp1;

import com.baidu.aip.talker.facade.Controller;
import com.baidu.aip.talker.facade.upload.LogBeforeUploadListener;
import com.cn.mcc.controller.BiccTest;
import com.cn.mcc.controller.PrintAfterDownloadListener;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;

/*
 * * Udp多线程回射服务器
 */
public class BDUdpServerThread extends Thread{
    private static DatagramSocket socket;
    //private byte[] infoBytes;
    static String info = null;
    private static int port;
    static InetAddress ip;
    Controller controller=null;
    private static String txts;

    public BDUdpServerThread(DatagramPacket packet, DatagramSocket socket, byte[] infoBytes,Controller controller) {
        this.socket = socket;
        this.info = new String(infoBytes, 0, packet.getLength());
        this.port = packet.getPort();
        this.ip = packet.getAddress();
        this.controller=controller;
    }
    @Override
    public void run() {
        super.run();
        //打印消息
        System.out.println("客户端-"+ ip + " port=" + port + "发送的消息："+info);
        //返回数据给客户端
        String str=info.substring(0,3);
        String feedback = "";
        if (str.equals("CRA")){
            if (controller==null){
                try{
                    controller = new Controller(new LogBeforeUploadListener(), new PrintAfterDownloadListener(), getProperties());
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            feedback =info.substring(3,19)+ "创建通话成功";
        }else if (str.equals("SED")){
            String AUDIO_PATH= info.substring(19,info.length());
            if (controller==null){
                try{
                    controller = new Controller(new LogBeforeUploadListener(), new PrintAfterDownloadListener(), getProperties());
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            BiccTest.asrOne(controller,AUDIO_PATH);
            System.out.println("================="+txts);
            feedback = "【语音数据】"+txts;
        }else if (str.equals("END")){
            feedback = "通话结束，释放接口";
            controller.stop();
        }
        responeSocket(feedback);
    }
    //返回消息给客户端
    public static void responeSocket(String message){
        // 构造响应数据包
        byte[] response = message.toString().getBytes();
        DatagramPacket dataPacket = new DatagramPacket(response, response.length, ip, port);
        try {// 发送
            socket.send(dataPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            responeSocket(txts);
            System.out.println(text);
            // return true;
        }
        // return false;
        return text;
    }
    private static Properties getProperties() throws Exception{
        //String fullFilename = System.getProperty("user.dir") + "/conf/sdk.properties";
        String fullFilename = "src/main/resources/application.properties";
        Properties properties = new Properties();
        FileInputStream is = null;
        try {
            is = new FileInputStream(fullFilename);
            properties.load(is);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (is != null) {
                try {
                    is.close();
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }
        return properties;
    }
   /* @Override
    public void run() {
        super.run();
        //打印消息
        System.out.println("客户端-"+ ip + " port=" + port + "说："+info);
        //回射
        byte[] echobuf = ("server:"+info).getBytes();
        DatagramPacket packet2 = new DatagramPacket(echobuf, echobuf.length, ip, port);

        try {
            socket.send(packet2);
        }catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }*/
}

