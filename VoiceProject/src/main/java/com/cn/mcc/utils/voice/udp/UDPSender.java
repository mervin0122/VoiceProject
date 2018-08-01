package com.cn.mcc.utils.voice.udp;

import java.net.*;
import java.io.*;
import java.util.Arrays;

/**
 * 客户端
 * https://blog.csdn.net/luanpeng825485697/article/details/78176387?fps=1&locationNum=10
 * https://blog.csdn.net/yxfei666/article/details/7196584
 */
public class UDPSender implements Sender{
    private File theFile;
    private FileInputStream fileReader;
    private DatagramSocket s;
    private int fileLength, currentPos, bytesRead, toPort;
    private byte[]  msg, buffer;
    private String toHost,initReply;
    private InetAddress toAddress;
	// 每次发送的数据大小 1280 字节
	private static final int CHUNCKED_SIZE = 2560;

    /**
     * Creates a new UDPSender object capable of sending a file to the specified address and port.
     * @param address  the address of the receiving host
     * @param port    the listening port on the receiving host
     */
    public UDPSender(InetAddress address, int port) throws IOException{
		toPort = port;
		toAddress = address;
		msg = new byte[CHUNCKED_SIZE];
		buffer = new byte[2560];
		s = new DatagramSocket();
        s.connect(toAddress, toPort);
    }

	public void sendByte(byte[] data) throws IOException{
		int length = 0;

		System.out.println(" -- Filename: "+theFile.getName());
		System.out.println(" -- Bytes to send: "+data.length);
		byte[] bytes = new byte[CHUNCKED_SIZE];
		try {
			while ((length = fileReader.read(bytes, 0, bytes.length)) > 0) {
				int len = -1;
				while ((len = fileReader.read(bytes)) != -1) {
					if (len < CHUNCKED_SIZE) {
						//send(Arrays.copyOfRange(bytes, 0, length));
						send((theFile.getName()+"::"+Arrays.copyOfRange(bytes, 0, length)).getBytes());
						break;
					}
					//send(bytes);
				}
				send((theFile.getName()+"::"+bytes).getBytes());
				Thread.sleep(160);
				//send((theFile.getName()+"::"+Arrays.copyOfRange(bytes, 0, fileLength)).getBytes());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
/*
		// 发送音频
		byte[] bytes = new byte[CHUNCKED_SIZE];
		try (RandomAccessFile raf = new RandomAccessFile(theFile.getName(), "r")) {
			int len = -1;
			while ((len = raf.read(bytes)) != -1) {
				if (len < CHUNCKED_SIZE) {
					//send(client, bytes = Arrays.copyOfRange(bytes, 0, len));
					send((theFile.getName()+"::"+fileLength).getBytes());
					break;
				}

				send((theFile.getName()+"::"+fileLength).getBytes());
				// 每隔40毫秒发送一次数据
				Thread.sleep(160);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}*/

		// 1. Send the filename to the receiver
		send((theFile.getName()+"::"+fileLength).getBytes());

		// 2. Wait for a reply from the receiver
		DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
		s.receive(reply);
		//initReply = (new String(reply.getData(), 0, reply.getLength()));


		// 3. Send the content of the file
		if (new String(reply.getData(), 0, reply.getLength()).equals("OK")) {
			System.out.println("  -- Got OK from receiver - sending the file ");

			while (currentPos<fileLength){
				//System.out.println("Will read at pos: "+currentPos);
				bytesRead = fileReader.read(msg);
				send(msg);
				//System.out.println("Bytes read: "+bytesRead);
				currentPos = currentPos + bytesRead;
			}
			System.out.println("  -- File transfer complete...");
		} else{
			System.out.println("Recieved something other than OK... exiting");
		}
	}
    /**
     * Sends a file to the bound host.
     * Reads the contents of the specified file, and sends it via UDP to the host 
     * and port specified at when the object was created.
     *
     * @param theFile  the file to send
     */
    public void sendFile(File theFile) throws IOException{
		int length = 0;
		// Init stuff
		fileReader = new FileInputStream(theFile);
		fileLength = fileReader.available();
	
		System.out.println(" -- Filename: "+theFile.getName());
		System.out.println(" -- Bytes to send: "+fileLength);
		byte[] bytes = new byte[CHUNCKED_SIZE];
		try {
			while ((length = fileReader.read(bytes, 0, bytes.length)) > 0) {
				int len = -1;
				while ((len = fileReader.read(bytes)) != -1) {
					if (len < CHUNCKED_SIZE) {
						//send(Arrays.copyOfRange(bytes, 0, length));
						send((theFile.getName()+"::"+Arrays.copyOfRange(bytes, 0, length)).getBytes());
						break;
					}
					//send(bytes);
				}
				send((theFile.getName()+"::"+bytes).getBytes());
				Thread.sleep(160);
				//send((theFile.getName()+"::"+Arrays.copyOfRange(bytes, 0, fileLength)).getBytes());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
/*
		// 发送音频
		byte[] bytes = new byte[CHUNCKED_SIZE];
		try (RandomAccessFile raf = new RandomAccessFile(theFile.getName(), "r")) {
			int len = -1;
			while ((len = raf.read(bytes)) != -1) {
				if (len < CHUNCKED_SIZE) {
					//send(client, bytes = Arrays.copyOfRange(bytes, 0, len));
					send((theFile.getName()+"::"+fileLength).getBytes());
					break;
				}

				send((theFile.getName()+"::"+fileLength).getBytes());
				// 每隔40毫秒发送一次数据
				Thread.sleep(160);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}*/

	// 1. Send the filename to the receiver
		send((theFile.getName()+"::"+fileLength).getBytes());
	
	// 2. Wait for a reply from the receiver
		DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
		s.receive(reply);
	//initReply = (new String(reply.getData(), 0, reply.getLength()));
	
	
	// 3. Send the content of the file
	if (new String(reply.getData(), 0, reply.getLength()).equals("OK")) {
		System.out.println("  -- Got OK from receiver - sending the file ");
		
		while (currentPos<fileLength){
		    //System.out.println("Will read at pos: "+currentPos);
		    bytesRead = fileReader.read(msg);
		    send(msg);
		    //System.out.println("Bytes read: "+bytesRead);
		    currentPos = currentPos + bytesRead;
		}
		System.out.println("  -- File transfer complete...");
	    } else{
		System.out.println("Recieved something other than OK... exiting");
	    }
    }
    

    private void send(byte[] message) throws IOException {
		DatagramPacket packet = new DatagramPacket(message, message.length);
		s.send(packet);
    }	
}