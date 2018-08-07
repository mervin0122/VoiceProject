package com.cn.mcc.controller.bdudp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

public class SocketClientUDP {

	private int PORT;

	public SocketClientUDP(int PORT) {
		this.PORT=PORT;
	}

	public String getMessage() {
		String msg = "";
		try {

			//String str = "hello world ";
			String str ="END"+GenerateGUID();
			byte[] buf = str.getBytes();
			DatagramSocket socket = new DatagramSocket();

			// 发送UPD消息给服务端
			DatagramPacket requestPacket = new DatagramPacket(buf, buf.length, InetAddress.getLocalHost(), PORT);
			socket.send(requestPacket);

			// 接收服务端返回的UDP消息
			byte[] data = new byte[2056];
			DatagramPacket responsePacket = new DatagramPacket(data,data.length);
			socket.receive(responsePacket);
			msg = new String(responsePacket.getData(), 0,responsePacket.getLength());
			socket.close();
		} catch (UnknownHostException e) {
			System.err.println("Exception: host could not be found");
		} catch (Exception e) {
			System.err.println("Exception: " + e);
			e.printStackTrace();
		}
		return msg;
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
	public static void main(String[] args){
		SocketClientUDP clientUdp = new SocketClientUDP(8090);
		String str1=   clientUdp.getMessage();
		System.out.println("服务器返回的数据为:"+str1);
	}
}
