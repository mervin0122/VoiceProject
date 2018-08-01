package com.cn.mcc.utils.voice.udp;

import java.net.*;
import java.io.*;
import java.util.*;

public class UDPReceiver{
    DatagramSocket s;
    String filename, initString;
    byte[] buffer;
    DatagramPacket initPacket, receivedPacket;
    FileOutputStream fileWriter;
    int bytesReceived, bytesToReceive;
	    
    public UDPReceiver(int port) throws IOException {
	// Init stuff
		s = new DatagramSocket(port);
		buffer = new byte[2560];
		System.out.println(" -- Ready to receive file on port: "+port);
		
	
	// 1. Wait for a sender to transmit the filename
		initPacket = receivePacket();
	
		initString = "Recieved-"+new String(initPacket.getData(), 0, initPacket.getLength());
		StringTokenizer t = new StringTokenizer(initString, "::");
		filename = t.nextToken();
		bytesToReceive = new Integer(t.nextToken()).intValue();
	
		System.out.println("  -- The file will be saved as: "+filename);
		System.out.println("  -- Expecting to receive: "+bytesToReceive+" bytes");
	
	// 2. Send an reply containing OK to the sender
		send(initPacket.getAddress(), initPacket.getPort(), (new String("OK")).getBytes());
	//System.out.println("send something to port: "+initPacket.getPort());
	
	
	// 3. Receive the contents of the file
		fileWriter = new FileOutputStream(filename);
	
	while(bytesReceived < bytesToReceive) {
		receivedPacket = receivePacket();
		fileWriter.write(receivedPacket.getData(), 0,  receivedPacket.getLength());
		bytesReceived = bytesReceived + receivedPacket.getLength();
	}
		System.out.println("  -- File transfer complete.");
    }
    
    
    public DatagramPacket receivePacket() throws IOException{
	
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		s.receive(packet);
		return packet;
    }
    
    public byte[] receiveData() throws IOException{

		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		s.receive(packet);
		return packet.getData();
    }

    public void send(InetAddress recv, int port,byte[] message) throws IOException {
		//InetAddress recv = InetAddress.getByName(host);
		DatagramPacket packet =
	    new DatagramPacket(message, message.length, recv, port);
        s.send(packet);
   }	
    
}