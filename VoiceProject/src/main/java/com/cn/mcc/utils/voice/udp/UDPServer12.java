package com.cn.mcc.utils.voice.udp;
import java.io.IOException;
import java.net.*;

/**
 * Created by yyzc on 2018/8/1.
 * 服务器端程序(接收)
 */
public class UDPServer12 {

    private DatagramSocket datagramSocket;

    private final int port = 8080;

    public static void main(String[] args) throws Exception {
        new UDPServer12().service();
    }

    public UDPServer12() throws SocketException {
        datagramSocket = new DatagramSocket(port);
        System.out.println("服务器启动......");
    }
    public void service() throws Exception{
        while (true) {
            try {
                InetAddress address = InetAddress.getLocalHost();
               // int port = 8080;
                //创建DatagramSocket对象
               // DatagramSocket socket = new DatagramSocket(port, address);
                byte[] buf = new byte[2056]; //定义byte数组
                DatagramPacket packet = new DatagramPacket(buf, buf.length); //创建DatagramPacket对象
                datagramSocket.receive(packet); //通过套接字接收数据
                String getMsg = new String(buf, 0, packet.getLength());
                System.out.println("客户端发送的数据为：" + getMsg);
                //从服务器返回给客户端数据
                InetAddress clientAddress = packet.getAddress(); //获得客户端的IP地址
                int clientPort = packet.getPort(); //获得客户端的端口号
                SocketAddress sendAddress = packet.getSocketAddress();
                String feedback = "Received";
                byte[] backbuf = feedback.getBytes();
                System.out.println(backbuf.length);
                DatagramPacket sendPacket = new DatagramPacket(backbuf, backbuf.length, sendAddress); //封装返回给客户端的数据
                datagramSocket.send(sendPacket); //通过套接字反馈服务器数据
              // datagramSocket.close(); //关闭套接字
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch(IOException e) {
                e.printStackTrace();
            }

        }
    }
  /*  public static void main(String[] args) {
        try {
            InetAddress address = InetAddress.getLocalHost();
            int port = 8080;
        //创建DatagramSocket对象
            DatagramSocket socket = new DatagramSocket(port, address);
            byte[] buf = new byte[2056]; //定义byte数组
            DatagramPacket packet = new DatagramPacket(buf, buf.length); //创建DatagramPacket对象
            socket.receive(packet); //通过套接字接收数据
            String getMsg = new String(buf, 0, packet.getLength());
            System.out.println("客户端发送的数据为：" + getMsg);
        //从服务器返回给客户端数据
            InetAddress clientAddress = packet.getAddress(); //获得客户端的IP地址
            int clientPort = packet.getPort(); //获得客户端的端口号
            SocketAddress sendAddress = packet.getSocketAddress();
            String feedback = "Received";
            byte[] backbuf = feedback.getBytes();
            System.out.println(backbuf.length);
            DatagramPacket sendPacket = new DatagramPacket(backbuf, backbuf.length, sendAddress); //封装返回给客户端的数据
            socket.send(sendPacket); //通过套接字反馈服务器数据
            socket.close(); //关闭套接字
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }*/

}
