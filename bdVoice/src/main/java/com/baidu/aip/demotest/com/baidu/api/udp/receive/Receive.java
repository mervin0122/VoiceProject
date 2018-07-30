package com.baidu.aip.demotest.com.baidu.api.udp.receive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Receive {
    //接收消息的端口
    private static final int PORT_RECEIVE = 9090;

    public static void main(String[] args) {
        handler();
    }

    private static void handler() {
        DatagramSocket sendSocket = null;
        DatagramSocket receiveSocket = null;
        try {
            //实例化两个套接字，一个用于接收消息，一个用于发送消息
            sendSocket = new DatagramSocket();
            receiveSocket = new DatagramSocket(PORT_RECEIVE);
            //实例化线程并启动，一个用于接收消息，一个用于发送消息
            new Thread(new SendThread(sendSocket)).start();
            new Thread(new ReceiveThread(receiveSocket)).start();
        } catch (SocketException e) {
            System.out.println("handler:异常！");
        }
    }
}

/*
 * 发送消息的线程类
 */
class SendThread implements Runnable{
    //将消息发送到指定端口和地址
    private static final int PORT_SEND = 8080;
    private static final String IP_SEND = "localhost";
    private DatagramSocket sendSocket;

    public SendThread(DatagramSocket sendSocket) {
        this.sendSocket = sendSocket;
    }

    @Override
    public void run() {
        BufferedReader br = null;
        try {
            while(true){
                //键盘录入
                br = new BufferedReader(new InputStreamReader(System.in));
                String line = null;
                while((line = br.readLine()) != null){
                    //将键盘录入的内容转换成字节数组
                    byte[] buf = line.getBytes();
                    //实例化一个数据包，指定发送的内容，内容长度，发送的地址和端口
                    DatagramPacket dp = new DatagramPacket(buf, buf.length, InetAddress.getByName(IP_SEND), PORT_SEND);
                    //发送数据包
                    sendSocket.send(dp);
                    //打印发送的内容
                    System.out.println("I:" + line);
                }
            }
        } catch (IOException e) {
            System.out.println("send fail");
        }finally{
            if(sendSocket != null){
                sendSocket.close();
            }
            if(br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

/*
 * 接收消息的线程类
 */
class ReceiveThread implements Runnable{
    private DatagramSocket receiveSocket;

    public ReceiveThread(DatagramSocket receiveSocket) {
        this.receiveSocket = receiveSocket;
    }

    @Override
    public void run() {
        try {
            while(true){
                //一次接收的内容的最大容量
                byte[] buf = new byte[1024];
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                //接收数据包
                receiveSocket.receive(dp);
                String data = new String(dp.getData(), 0, dp.getLength());
                //取得数据包里的内容
                System.out.println("Other:" + data);
            }
        } catch (IOException e) {
            System.out.println("receive fail");
        }finally{
            if(receiveSocket != null){
                receiveSocket.close();
            }
        }
    }
}
