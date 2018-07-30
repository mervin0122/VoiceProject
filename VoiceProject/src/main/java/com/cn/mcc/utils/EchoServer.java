package com.cn.mcc.utils;

import com.cn.mcc.bean.Iat;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * 服务端
 */
public class EchoServer {

    private DatagramSocket datagramSocket;

    private final int port = 8088;

    public static void main(String[] args) throws Exception {
        new EchoServer().service();
    }

    public EchoServer() throws SocketException{
        datagramSocket = new DatagramSocket(port);
        System.out.println("服务器启动...");
    }

    public String echo(String msg){
        return "echo:"+msg;
    }

    public void service() throws Exception{
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(new byte[512], 512);
                datagramSocket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                if (StringUtils.isNotEmpty(msg)){
                    Iat iat=new Iat();
                    iat.setFilePath(msg);
                    String url ="http://localhost:8080/voice/vysb" ;
                    //开放平台实时撰写业务，按并发路数收费，鸡棚，一时间允许进行实时撰写的western连接数，单价为2万元路每年
                    String res = HttpClientUtil.getHttpData(url, iat,true);
                }
                System.out.println(packet.getAddress()+"/"+packet.getPort()+" msg:"+msg);
                //packet.setData(echo(msg).getBytes());
                msg="你好1";
                packet.setData(echo(msg).getBytes());
                datagramSocket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}
