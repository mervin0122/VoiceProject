package com.cn.mcc.utils.voice.udp;

import java.net.*;
import java.io.*;
public class FileSender{

    public static void main(String[] args) throws IOException
    {
	Sender theSender;
	int thePort = 0;
	File theFile;
	InetAddress theAddress = null;

	// Checking the arguments
	if (args.length != 4) printError("Wrong number of arguments");
	
	if (!args[0].equals("tcp") && !args[0].equals("udp")) printError("Unrecognized protocol: "+args[0]);
	
	try
	    {thePort = Integer.parseInt(args[2]);}
	catch(NumberFormatException e)
	    { printError("The port must be a number between 0 and 65536");}
	if(thePort<0 || thePort>65536) printError("The port must be a number between 0 and 65536");
	
	try
	    {theAddress = InetAddress.getByName(args[1]);}
	catch(UnknownHostException e)
	    {printError("The specified host could not be found on the network");}

        theFile = new File(args[3]);
	if(!theFile.canRead()) printError("There was an error opening the specified file");



	// Create the sender object
	if (args[0].equals("udp")) 
	    theSender = new UDPSender(theAddress, thePort);
	else 
	    theSender = new TCPSender(theAddress, thePort);

	// Send the file
	theSender.sendFile(theFile);
    }

    public static void printError(String error)
    {
	System.out.println(" - Error: "+error);
	System.out.println(" - Usage: FileSender [protocol] [host] [port] [filename]");
	System.exit(1);
    }
}