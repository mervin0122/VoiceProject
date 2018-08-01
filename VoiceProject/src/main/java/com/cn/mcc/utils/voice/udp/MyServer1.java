package com.cn.mcc.utils.voice.udp;
import java.net.*;
import java.io.*;
import java.util.*;
/**
 * Created by yyzc on 2018/8/1.
 */
public class MyServer1 {
    public static void main(String[] args) throws IOException {
//创建DatagramSocket 对象 服务端
        DatagramSocket server=new DatagramSocket(8888);
        byte[] b=new byte[2056];
//封装DatagramPacket
        DatagramPacket data=new DatagramPacket(b,b.length);
        server.receive(data);
//封装数据转换为data2
        byte[] data2 = data.getData();

        int len = data.getLength();
//接收的数据转换为String
        String data1=new String(data2,0,len);
//把数据写到内存中
       // DataInputStream in=new DataInputStream(new ByteArrayInputStream(data2));
       // double data1 = in.readDouble();
        System.out.println(data1);
//释放资源
        server.close();

    }
}
