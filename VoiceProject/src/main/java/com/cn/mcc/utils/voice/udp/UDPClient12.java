package com.cn.mcc.utils.voice.udp;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.DatagramSocket;
import java.util.Arrays;

/**
 * Created by yyzc on 2018/8/1.
 *  客户端(发送方)
 */
public class UDPClient12 {
    private static final int CHUNCKED_SIZE = 2056;
    public static void main(String[] args) {

        //String msg = "Hello, World";
        String msg = "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" ;
        byte[] buf = msg.getBytes();
        System.out.println(buf.length);
        try {
            InetAddress address = InetAddress.getByName("172.24.194.220"); //服务器地址
            int port = 8080; //服务器的端口号
        //创建发送方的数据报信息
            int len = -1;
          /*  DatagramPacket dataGramPacket = new DatagramPacket(buf, buf.length, address, port);
            DatagramSocket socket = new DatagramSocket(); //创建套接字
            while ((len = buf.length) != -1) {
                if (len < CHUNCKED_SIZE) {
                    socket.send(dataGramPacket); //通过套接字发送数据
                    break;
                }

                send(client, bytes);
                // 每隔40毫秒发送一次数据
                Thread.sleep(40);
            }*/

            DatagramPacket dataGramPacket = new DatagramPacket(buf, buf.length, address, port);
            DatagramSocket socket = new DatagramSocket(); //创建套接字
            socket.send(dataGramPacket); //通过套接字发送数据
        //接收服务器反馈数据
            byte[] backbuf = new byte[2056];
            DatagramPacket backPacket = new DatagramPacket(backbuf, backbuf.length);
            socket.receive(backPacket); //接收返回数据
            String backMsg = new String(backbuf, 0, backPacket.getLength());
            System.out.println("服务器返回的数据为:" + backMsg);
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
