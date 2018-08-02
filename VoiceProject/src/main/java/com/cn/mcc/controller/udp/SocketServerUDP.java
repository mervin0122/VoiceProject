package com.cn.mcc.controller.udp;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket; 
import java.net.SocketException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.baidu.aip.talker.facade.Controller;
import com.baidu.aip.talker.facade.upload.LogBeforeUploadListener;
import com.cn.mcc.controller.PrintAfterDownloadListener;
import org.apache.log4j.Logger;

/**
 * 功能说明：socket  UDP服务端及线程池的初始化
 * 
 * 类名：SocketServerUDP.java
 * 
 * 作者：Administrator 创建时间：Jan 6, 2012 5:16:28 PM
 * 
 * 修改人： 修改时间：
 */
public class SocketServerUDP {
	Logger logger = Logger.getLogger(SocketServerUDP.class);

	private int port = 8090;
	private DatagramSocket dataSocket;
	private ExecutorService executorService;// 线程池
	Controller controller=null;
	private final int POOL_SIZE = 100;// 单个CPU线程池大小

	public SocketServerUDP() throws Exception{
		try {
			dataSocket = new DatagramSocket(port);
			executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * POOL_SIZE);
			logger.info("端口号为" + port + "的服务器启动");
			System.out.println("请等待程序正常退出， 否则测试用户将导致10分钟内无法正常使用。");
			//controller = new Controller(new LogBeforeUploadListener(), new PrintAfterDownloadListener(), getProperties());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void service() {
		//System.out.println("socket初始化成功！");
		logger.info("socket服务端初始化成功！");
		 
		while (true) { 
			try {
				 byte[] buff = new byte[128];// 传输消息不超过64字  
	             DatagramPacket dataPacket = new DatagramPacket(buff, buff.length);   
	             dataSocket.receive(dataPacket);// 等待接收来自客户端的数据包   
	             
				 executorService.execute(new UdpCallService(dataPacket,controller));//接收到消息后执行具体的业务
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
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
	public static void main(String[] args) throws Exception {
		new SocketServerUDP().service();
	}

}

/**
 * 功能说明：执行具体业务的线程   
 */
class UdpCallService implements Runnable {
	Logger logger = Logger.getLogger(UdpCallService.class);
	private DatagramPacket packet;   
    private DatagramSocket dataSocket;
	Controller controller=null;
    public UdpCallService(DatagramPacket packet,Controller controller) {
    	try {
			this.controller=controller;
    		this.packet = packet;   
    		// 创建本机可以端口的DatagramSocket   
            dataSocket = new DatagramSocket();   
        } catch (SocketException e) {   
            e.printStackTrace();   
        }   
    }    

	public void run() { 	   
		String getMsg=new String(packet.getData());
		System.out.println("客户端发送的数据为：" + getMsg);
		    //返回数据给客户端
	    String str=getMsg.substring(0,3);
		String feedback = "";
		if (str.equals("CRA")){
			feedback =getMsg.substring(3,19)+ "创建通话成功";
		}else if (str.equals("SED")){
			feedback = "语音数据";
			// BiccTest.asrOne(controller,dir + "msc/8k_test.pcm");
		}else if (str.equals("END")){
			feedback = "通话结束，释放接口";
		}
		responeSocket(feedback);
	}
	
	//返回消息给客户端
	public void responeSocket(String message){
		 // 构造响应数据包   
        byte[] response = message.toString().getBytes();   
        DatagramPacket dataPacket = new DatagramPacket(response, response.length, packet.getAddress(), packet.getPort());   
        try {// 发送   
            dataSocket.send(dataPacket);   
        } catch (IOException e) {   
            e.printStackTrace();   
        }    
	}

	

}
