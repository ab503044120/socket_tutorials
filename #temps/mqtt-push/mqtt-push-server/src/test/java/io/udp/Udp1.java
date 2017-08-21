package io.udp;

import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class Udp1 {

	public static void main(String[] args) throws IOException {
		
		
//		DatagramSocket datagramSocket=new DatagramSocket(8080);
//		datagramSocket.setSendBufferSize(65535);
//		byte[] bs="你好".getBytes();
//		DatagramPacket datagramPacket=new DatagramPacket(bs,bs.length,InetAddress.getByName("localhost"),8888);
//		
//		datagramSocket.send(datagramPacket);
//	
//		datagramSocket.close();
		
	
		Enumeration<NetworkInterface> enumeration= NetworkInterface.getNetworkInterfaces();
		
		while(enumeration.hasMoreElements()){
			
			
			NetworkInterface interface1=enumeration.nextElement();
			if(interface1.isVirtual())
				continue;
			if(interface1.isLoopback())
				continue;
			System.out.println(interface1+"："+interface1.supportsMulticast());
		}
	}
}
