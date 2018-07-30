package com.baidu.aip.demotest.com.baidu.api.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author Administrator
 */
public class EchoUDPClient {
    private int       remotePort;
    private InetAddress remoteIP;

    private DatagramSocket socket; //UDP套接字

    public EchoUDPClient(String ip,String port)throws IOException{
        this.remotePort=Integer.parseInt(port);
        this.remoteIP=InetAddress.getByName(ip);

        //创建一个UDP套接字，与本地任意一个未使用的UDP端口绑定
        socket=new DatagramSocket();
        //与本地一个固定的UDP端口绑定
        //   socket=new DatagramSocket(9000);
    }

    //定义一个数据的发送方法。
    public  void send(String msg){
        try {
            //先准备一个待发送的数据报
            byte[] outputData=msg.getBytes("GB2312");
            //构建一个数据报文。
            DatagramPacket outputPacket=new DatagramPacket(outputData,
                    outputData.length,remoteIP,remotePort);
            //给EchoUDPServer发送数据报
            socket.send(outputPacket);  //给EchoUDPServer发送数据报
        } catch (IOException ex) { }
    }

    //定义一个数据的接收方法。
    public String receive(){//throws IOException{
        String msg;
        //先准备一个空数据报文
       // DatagramPacket inputPacket=new DatagramPacket(new byte[512],512);
        byte[] buf = new byte[1024];
        DatagramPacket dp = new DatagramPacket(buf, buf.length);
        try {
            //接收数据包
            socket.receive(dp);
            //取得数据包里的内容
             msg = new String(dp.getData(), 0, dp.getLength());
            //打印接收到的数据
            System.out.println("Other:" + msg);
           /* //阻塞语句，有数据就装包，以装完或装满为此.
            socket.receive(inputPacket);
            //从报文中取出字节数据并装饰成字符。
            msg=new String(inputPacket.getData(),
                    0,inputPacket.getLength(),"GB2312");
            System.out.println("------>"+msg);*/
        } catch (IOException ex) {
            msg=null;
        }
        return msg;
    }

    public void close(){
        if(socket!=null)
            socket.close();//释放本地端口.
    }
    public static void main(String args[])throws IOException{
       new  EchoUDPClient("127.0.0.1","6666").send("测试你好");
       String rString= new  EchoUDPClient("127.0.0.1","6666").receive();
        System.out.println("------>"+rString);
    }
}
