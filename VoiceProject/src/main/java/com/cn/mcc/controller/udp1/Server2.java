package com.cn.mcc.controller.udp1;

import com.baidu.aip.talker.facade.Controller;
import com.baidu.aip.talker.facade.upload.LogBeforeUploadListener;
import com.cn.mcc.controller.PrintAfterDownloadListener;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Properties;
import java.util.concurrent.Executors;

public class Server2{
    Logger logger = Logger.getLogger(Server2.class);
    private static final int PORT = 9960;
    DatagramSocket socket;
    Controller controller=null;
    public Server2() throws Exception{
        try {
            socket = new DatagramSocket(PORT);
            logger.info("端口号为" + PORT + "的服务器启动");
            //controller = new Controller(new LogBeforeUploadListener(), new PrintAfterDownloadListener(), getProperties());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void service() {
        byte[] buff = new byte[1028];
        logger.info("socket服务端初始化成功！");
        while (true) {
            try {
                DatagramPacket dataPacket = new DatagramPacket(buff, buff.length);
                socket.receive(dataPacket);// 等待接收来自客户端的数据包


                //接收到一个包就就开启一个线程(科大)
                //UdpServerThread thread = new UdpServerThread(dataPacket, socket, buff,controller);
                //百度
                BDUdpServerThread thread = new BDUdpServerThread(dataPacket, socket, buff,controller);
                //启动线程
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws Exception {
        new Server2().service();
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
}