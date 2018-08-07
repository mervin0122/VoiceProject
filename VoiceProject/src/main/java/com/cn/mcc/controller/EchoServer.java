package com.cn.mcc.controller;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Properties;

/**
 * 服务端
 */
public class EchoServer {

    private DatagramSocket datagramSocket;

    private static StringBuffer mResult = new StringBuffer();

    private final int port = 8088;

    public static void main(String[] args) throws Exception {
        new EchoServer().service();
    }

    public EchoServer() throws SocketException{
        datagramSocket = new DatagramSocket(port);
        System.out.println("服务器启动......");
    }

    public String echo(String msg){
        return "echo:"+msg;
    }

    public void service() throws Exception{
        while (true) {
            try {

                DatagramPacket packet = new DatagramPacket(new byte[51200], 51200);
                datagramSocket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                //Controller controller = new Controller(new LogBeforeUploadListener(), new PrintAfterDownloadListener(), getProperties());

             /*   if (StringUtils.isNotEmpty(msg)){
                    System.out.println("请等待程序正常退出， 否则测试用户将导致10分钟内无法正常使用。");
                    //  String dir = "src/test/resources/pcm";

                    // 选择开启 asrOne 或者 asrBoth ， asrOne即一个通话1路音频， asrBoth为1个通话，2路音频
                    // 8k_test.pcm 较短  salesman.pcm 较长，客服  customer.pcm 用户
                    //  BiccTest.asrOne(controller, dir + "/8k_test.pcm");
                    // BiccTest.asrBoth(controller, "src/test/resources/pcm/8k_test.pcm", "src/test/resources/pcm/8k_test.pcm");

                    // 请等待程序正常退出，即end包发送完成。否则测试用户将导致10分钟内无法正常使用。
                    BiccTest.asrOne(controller,msg);
                    // BiccTest.asrOne(controller,dir + "/8k_test.pcm");
                    // BiccTest.asrBoth(controller, dir + "/salesman.pcm", dir + "/customer.pcm");

                    controller.stop();
                }*/
                System.out.println(packet.getAddress()+"/"+packet.getPort()+" msg:"+msg);
                //packet.setData(echo(msg).getBytes());
                msg=mResult.toString();
                packet.setData(echo(msg).getBytes());
                datagramSocket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
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
    public static String parseTxt(JsonNode node) throws IOException {
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
                        mResult.append("\n"+node.get("content").asText());
                    }
                }
            }
            text += "识别结果：" + node.get("content").asText();
           // mResult.append("\n"+text.toString());
            System.out.println(text);
            // return true;
        }
        // return false;
        return text;
    }
}
