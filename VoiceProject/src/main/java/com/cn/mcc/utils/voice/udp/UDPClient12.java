package com.cn.mcc.utils.voice.udp;

import org.jaudiotagger.audio.asf.data.GUID;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.UUID;

/**
 * Created by yyzc on 2018/8/1.
 *  客户端(发送方)
 *
 *  https://blog.csdn.net/lirx_tech/article/details/50996014
 */
public class UDPClient12 {
    private static final int CHUNCKED_SIZE = 2056;
    private static final String AUDIO_PATH = "msc/1.txt";
    public static void main(String[] args) {

        //String msg = "Hello, World";
      /*  String msg = "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" +
                "123456789123456789123456789123456789123456789123456789123456789123456789123456789==" ;*/
        String msg ="CRA"+GenerateGUID();
       byte[] buf = msg.getBytes();
       System.out.println(buf.length);
        try {
            InetAddress address = InetAddress.getByName("172.24.194.220"); //服务器地址172.24.194.220  192.168.1.103
            int port = 8080; //服务器的端口号
        //创建发送方的数据报信息
           /* byte[] buf = new byte[CHUNCKED_SIZE];
            byte[] receiveBuf = new byte[1];
            int readSize = -1;
            RandomAccessFile accessFile = new RandomAccessFile(AUDIO_PATH, "r");
            DatagramPacket dataGramPacket = new DatagramPacket(buf, buf.length, address, port);
            DatagramSocket socket = new DatagramSocket(); //创建套接字
            int sendCount = 0;
            while((readSize = accessFile.read(buf,0,buf.length)) != -1){
                dataGramPacket.setData(buf, 0, readSize);
                socket.send(dataGramPacket);
                // wait server response
                {
                    while(true){
                        dataGramPacket.setData(receiveBuf, 0, receiveBuf.length);
                        socket.receive(dataGramPacket);

                     *//*   // confirm server receive
                        if(!UDPUtils.isEqualsByteArray(UDPUtils.successData,receiveBuf,dpk.getLength())){
                            System.out.println("resend ...");
                            dataGramPacket.setData(buf, 0, readSize);
                            socket.send(dataGramPacket);
                        }else*//*
                            break;
                    }
                }

                System.out.println("send count of "+(++sendCount)+"!");
            }
*/


         /*   int len = -1;
            DatagramPacket dataGramPacket = new DatagramPacket(buf, buf.length, address, port);
            DatagramSocket socket = new DatagramSocket(); //创建套接字
            while ((len = buf.length) != -1) {
                if (len < CHUNCKED_SIZE) {
                    socket.send(dataGramPacket); //通过套接字发送数据
                    break;
                }
                dataGramPacket.setData(buf, 0, buf.length);
                socket.send(dataGramPacket);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
