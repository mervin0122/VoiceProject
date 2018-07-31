package com.cn.mcc.utils.voice.udp;

import java.net.*;
import java.io.*;
public class FileReceiver{

    public static void main(String[] args) throws IOException{
	int thePort = 0;

	// Checking the arguments
	if (args.length != 2) printError("Wrong number of arguments");
	
	if (!args[0].equals("tcp") && !args[0].equals("udp")) printError("Unrecognized protocol: "+args[0]);

	try
	    {thePort = Integer.parseInt(args[1]);}
	catch(NumberFormatException e)
	    { printError("The port must be a number between 0 and 65536");}
	if(thePort<0 || thePort>65536) printError("The port must be a number between 0 and 65536");



	// Create the receiver object
	if (args[0].equals("udp")) 
	    new UDPReceiver(thePort);
	else 
	    new TCPReceiver(thePort);
    } 



    public static void printError(String error){
	System.out.println(" - Error: "+error);
	System.out.println(" - Usage: FileSender [protocol] [host] [port] [filename]");
	System.exit(1);
    }
}