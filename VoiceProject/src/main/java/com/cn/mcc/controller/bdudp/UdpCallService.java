package com.cn.mcc.controller.bdudp;

import com.baidu.aip.talker.facade.Controller;
import com.baidu.aip.talker.facade.upload.LogBeforeUploadListener;
import com.cn.mcc.controller.BiccTest;
import com.cn.mcc.controller.PrintAfterDownloadListener;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Properties;

/**
 * 功能说明：执行具体业务的线程
 */
public class UdpCallService implements Runnable {
	Logger logger = Logger.getLogger(UdpCallService.class);
	private DatagramPacket packet;
    private DatagramSocket dataSocket;
	Controller controller=null;
	private static String txts;
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
			if (controller==null){
				try{
					controller = new Controller(new LogBeforeUploadListener(), new PrintAfterDownloadListener(), getProperties());
				}catch (Exception e){
					e.printStackTrace();
				}

			}
			feedback =getMsg.substring(3,19)+ "创建通话成功";
		}else if (str.equals("SED")){
			if (controller==null){
				try{
					controller = new Controller(new LogBeforeUploadListener(), new PrintAfterDownloadListener(), getProperties());
				}catch (Exception e){
					e.printStackTrace();
				}

			}
			 BiccTest.asrOne(controller,"msc/test_1.pcm");
			System.out.println("================="+txts);
			feedback = "【语音数据】"+txts;
		}else if (str.equals("END")){
			feedback = "通话结束，释放接口";
			controller.stop();
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

	public static  String parseTxt(JsonNode node) throws IOException {
		//private boolean parseTxt(JsonNode node) throws IOException {
		String text="";
		if (node.has("roleCategory") && node.has("content")) {
			text = node.get("roleCategory").asText() + " ";
			if (node.has("extJson")) {
				JsonNode nodeExt = node.get("extJson");
				if (nodeExt.has("completed")) {
					if (nodeExt.get("completed").asInt() == 1) {
						text += "临时";
					} else if (nodeExt.get("completed").asInt() == 3) {
						text += "最终";
					}
				}
			}
			text += "识别结果：" + node.get("content").asText();
			txts=text;
			System.out.println(text);
			// return true;
		}
		// return false;
		return text;
	}
	private static Properties getProperties() throws Exception{
		//String fullFilename = System.getProperty("user.dir") + "/conf/sdk.properties";
		String fullFilename = "src/main/resources/application.properties";
		Properties properties = new Properties();
		FileInputStream is = null;
		try {
			is = new FileInputStream(fullFilename);
			properties.load(is);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if (is != null) {
				try {
					is.close();
				}catch (IOException e){
					e.printStackTrace();
				}

			}
		}
		return properties;
	}
}
