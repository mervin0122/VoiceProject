package com.baidu.aip.demotest;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * //服务器端接受客户端数据
 */
public class UDPServer {
    public static void main(String[] args) throws Exception{

        //创建数据接收码头，只接受和客户端约定的3000端口的数据
        DatagramSocket ds=new DatagramSocket(3000);

        //创建数据接收的数据缓冲区
        byte[] buf=new byte[1024];
        DatagramPacket dp=new DatagramPacket(buf,1024);

        //接受来自端口1024的数据包，并存储在集装箱datagramPacket中：注意一旦服务器开启，就会自动监听3000端口，如果 没有数据，则进行阻塞
        ds.receive(dp);

        //解析数据包中的信息
        String data=new String(dp.getData(),0,dp.getLength());
        String ip=dp.getAddress().getHostAddress();
        int port=dp.getPort();

        System.out.println("ip地址："+ip+" 端口号："+port+" 消息："+data);
        ds.close();
    }
}
