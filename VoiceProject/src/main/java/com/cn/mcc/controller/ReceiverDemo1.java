package com.cn.mcc.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class ReceiverDemo1 {

    private static final int PORT = 8080;
    public static void main(String[] args) {
        receiveHandler();
    }

    private static void receiveHandler() {
        DatagramSocket receiveSocket = null;
        try {
            //在8080端口监听
            receiveSocket = new DatagramSocket(PORT);
            //while循环，一直在等待接收数据
            while(true){
                //创建一个包，并接收数据
                byte[] buf = new byte[1024];
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                receiveSocket.receive(dp);

                //取得IP地址和包里的数据内容
                String ip = dp.getAddress().getHostAddress();
                String data = new String(dp.getData(), 0, dp.getLength());

                System.out.println(ip + ":" + data);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(receiveSocket != null){
                receiveSocket.close();
            }
        }
    }
}
