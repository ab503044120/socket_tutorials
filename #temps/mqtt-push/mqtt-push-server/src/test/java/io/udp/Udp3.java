package io.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Udp3 {

	public static void main(String[] args) throws  Exception {
		
		
		DatagramSocket datagramSocket=new DatagramSocket(8888);
		datagramSocket.setSendBufferSize(65535);
		datagramSocket.setReceiveBufferSize(65535);
		
		byte[] bs=new byte[1024];
		DatagramPacket datagramPacket=new DatagramPacket(bs, bs.length);
		while(true){
			datagramSocket.receive(datagramPacket);
			System.out.println(new String(bs,0,datagramPacket.getLength()));
			Thread.sleep(1000);
		}
	}
}
