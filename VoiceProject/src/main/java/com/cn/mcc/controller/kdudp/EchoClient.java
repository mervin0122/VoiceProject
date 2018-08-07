package com.cn.mcc.controller.kdudp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * 客户端
 */
public class EchoClient {

    private String remoteHost="localhost";
    private int remotePort=8088;
    private DatagramSocket datagramSocket;

    public EchoClient() throws SocketException{
        datagramSocket = new DatagramSocket();
    }

    public static void main(String[] args) throws SocketException {
        //new EchoClient().talk("msc/8k_test.pcm");
        new EchoClient().talk("msc/1.txt");
    }

    public void talk(String message){
        try {
           // BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            //String msg = null;
            InetAddress address = InetAddress.getByName(remoteHost);
           // while ((msg=reader.readLine())!=null) {
                //发送数据报
                byte [] buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer,buffer.length, address, remotePort);
                datagramSocket.send(packet);
                //接收数据报
                DatagramPacket inputPacket = new DatagramPacket(new byte[51200], 51200);
                datagramSocket.receive(inputPacket);
                System.out.println(new String(inputPacket.getData(), 0 , inputPacket.getLength()));
              //  if("bye".equals(msg)){
              //      break;
              //  }
          //  }

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            datagramSocket.close();
        }
    }
  /*  public void talk(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String msg = null;
            InetAddress address = InetAddress.getByName(remoteHost);
            while ((msg=reader.readLine())!=null) {
                //发送数据报
                byte [] buffer = msg.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer,buffer.length, address, remotePort);
                datagramSocket.send(packet);
                //接收数据报
                DatagramPacket inputPacket = new DatagramPacket(new byte[512], 512);
                datagramSocket.receive(inputPacket);
                System.out.println(new String(inputPacket.getData(), 0 , inputPacket.getLength()));
                if("bye".equals(msg)){
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            datagramSocket.close();
        }
    }
*/
}
