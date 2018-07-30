package com.baidu.aip.demotest;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**--------文件名：UDPServer.java-------------*/
public class UDPServerss {


    public static void main(String args[]) {
        // 定义一些常量
          int MAX_LENGTH = 1024; // 最大接收字节长度
          int PORT_NUM   = 5066;   // port号
        // 用以存放接收数据的字节数组
         byte[] receMsgs = new byte[MAX_LENGTH];
        // 数据报套接字
         DatagramSocket datagramSocket=null;
        // 用以接收数据报
         DatagramPacket datagramPacket;
        try {
            System.out.println("UDP服务端启动成功，等待接收消息...");

            /******* 接收数据流程**/
            // 创建一个数据报套接字，并将其绑定到指定port上
            datagramSocket = new DatagramSocket(PORT_NUM);
            // DatagramPacket(byte buf[], int length),建立一个字节数组来接收UDP包
            datagramPacket = new DatagramPacket(receMsgs, receMsgs.length);
            // receive()来等待接收UDP数据报
            datagramSocket.receive(datagramPacket);

            /****** 解析数据报****/
            String receStr = new String(datagramPacket.getData(), 0 , datagramPacket.getLength());
            System.out.println("Server Rece:" + receStr);
            System.out.println("Server Port:" + datagramPacket.getPort());

            /***** 返回ACK消息数据报*/
            // 组装数据报
            byte[] buf = "I receive the message".getBytes();
            DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, datagramPacket.getAddress(), datagramPacket.getPort());
            // 发送消息
            datagramSocket.send(sendPacket);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭socket
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        }
    }
}