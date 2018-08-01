package com.cn.mcc.controller;

import com.baidu.aip.talker.facade.Controller;
import com.baidu.aip.talker.facade.upload.LogBeforeUploadListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Properties;

public class EchoServer {
    //设置APPID/AK/SK
    public static final String APP_ID = "11572625";
    public static final String API_KEY = "t5fTe2YN25P36FYMnBTQcUga";
    public static final String SECRET_KEY = "6SgSI55vVqCXOHTamtYwG3yE7A9uKvGX";

    private DatagramSocket datagramSocket=null;

    private final int port = 8088;

    public static void main(String[] args) throws Exception {
        new EchoServer().talk();
    }
    public EchoServer() throws SocketException{
        datagramSocket = new DatagramSocket(port);
        System.out.println("服务器启动...");
    }
    public String echo(String msg){
        return "echo:"+msg;
    }
//接收传来的语音文件 创建session
    public void talk()throws Exception {
        try {
            System.out.println("请等待程序正常退出， 否则测试用户将导致10分钟内无法正常使用。");

            DatagramPacket packet = new DatagramPacket(new byte[512], 512);
            datagramSocket.receive(packet);
            String msg = new String(packet.getData(), 0, packet.getLength());

            System.out.println(packet.getAddress()+"/"+packet.getPort()+" msg:"+msg);
            //packet.setData(echo(msg).getBytes());
            msg="你好1";
            packet.setData(echo(msg).getBytes());
            datagramSocket.send(packet);
            //  String dir = "src/test/resources/pcm";
            Controller controller = new Controller(new LogBeforeUploadListener(), new PrintAfterDownloadListener(), getProperties());
            if (packet.getLength()>0){
                BiccTest.asrOne(controller,msg);
            }
            controller.stop();
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
    /**
     * 默认读取conf/sdk.properties, 您也可以用下面的构造方法，传入Properties类
     *
     * Controller controller =
     * new Controller(new LogBeforeUploadListener(), new PrintAfterDownloadListener(), getProperties());
     * @return
     * @throws Exception
     */
    private static Properties getProperties() throws Exception {
        //String fullFilename = System.getProperty("user.dir") + "/conf/sdk.properties";
        String fullFilename = "src/main/resources/application.properties";
        Properties properties = new Properties();
        FileInputStream is = null;
        try {
            is = new FileInputStream(fullFilename);
            properties.load(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return properties;
    }
}
