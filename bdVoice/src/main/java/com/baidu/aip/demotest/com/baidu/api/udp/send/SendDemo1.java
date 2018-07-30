package com.baidu.aip.demotest.com.baidu.api.udp.send;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class SendDemo1 {

    private static final int PORT = 8080;
    private static final String IP = "localhost";
    public static void main(String[] args) {
        sendHandler();
    }

    private static void sendHandler(){
        DatagramSocket sendSocket = null;
        try {
            while(true){
                //创建套接字
                sendSocket = new DatagramSocket();
                //键盘录入
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String send = br.readLine();
                byte[] buf = send.getBytes();
                //设定数据包内容，长度，发送到的地址和端口
                DatagramPacket dp = new DatagramPacket(buf, buf.length, InetAddress.getByName(IP), PORT);
                //发送数据包
                sendSocket.send(dp);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(sendSocket != null){
                sendSocket.close();
            }
        }
    }

}
