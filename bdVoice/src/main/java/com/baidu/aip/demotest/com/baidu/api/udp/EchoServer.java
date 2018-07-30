package com.baidu.aip.demotest.com.baidu.api.udp;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class EchoServer {

    private DatagramSocket datagramSocket;

    private final int port = 8088;

    public static void main(String[] args) throws SocketException {
        new EchoServer().service();
    }

    public EchoServer() throws SocketException{
        datagramSocket = new DatagramSocket(port);
        System.out.println("服务器启动...");
    }

    public String echo(String msg){
        return "echo:"+msg;
    }

    public void service(){
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(new byte[512], 512);
                datagramSocket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                System.out.println(packet.getAddress()+"/"+packet.getPort()+" msg:"+msg);
                //packet.setData(echo(msg).getBytes());
                msg="你好1";
                packet.setData(echo(msg).getBytes());
                datagramSocket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}
