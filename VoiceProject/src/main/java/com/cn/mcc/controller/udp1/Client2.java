package com.cn.mcc.controller.udp1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.UUID;

/*
 * Udp客户端
 * https://blog.csdn.net/ccsuxwz/article/details/77855403
 * https://blog.csdn.net/joeyjobs/article/details/44836453
 * SED1234567891234567msc/test_1.pcm
 * SED1234567891234568msc/655.pcm
 */
public class Client2 {
    //enum PORT{one,two,three}
    private final static int PORT = 9960; 					//通信端口要与服务器端一致
    public static void main(String[] args) throws IOException {
        String msg = "";
        // TODO Auto-generated method stub
        //创建套接字
        DatagramSocket client = new DatagramSocket();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        InetAddress ip = InetAddress.getByName("127.0.0.1");			//使用本机ip地址

        //创建数据包
        //DatagramPacket(byte[] buf, int length, InetAddress address, int port)
        //client.connect(ip,PORT);

        String buf = null;

        while((buf = in.readLine()) !=null) {
            //创建数据包 -- 发送UPD消息给服务端
            byte[] sendbuf =  buf.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendbuf, sendbuf.length,ip,PORT);
            client.send(sendPacket);							//阻塞函数

            //接收服务器端发来的消息
            byte[] recvbuf = new byte[256];
            DatagramPacket recvPacket = new DatagramPacket(recvbuf, recvbuf.length);
            client.receive(recvPacket);
            msg = new String(recvPacket.getData(), 0,recvPacket.getLength());
            System.out.println("服务器返回的数据为:"+msg);

          /*  byte[] buf2 = recvPacket.getData();
            int len = recvPacket.getLength();
            String result = new String(buf2, 0, len);
            System.out.println("发送了：" + result);*/

            if("exit".equals(buf))
                break;
        }
        client.close();     //关闭套接字
        in.close();
    }
    public static final String GenerateGUID(){
        // UUID uuid = UUID.randomUUID();
        // return uuid.toString();
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {//有可能是负数
            hashCodeV = - hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }
}
