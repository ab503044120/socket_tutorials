package mqttserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

public class Node2 {
	private static int port = 8000;
	private static String address = "224.0.2.1";

	public static void main(String[] args) throws Exception {
		InetAddress group = InetAddress.getByName(address);
		MulticastSocket msr = null;
		try {
			msr = new MulticastSocket(port);
			
			
			msr.joinGroup(new InetSocketAddress(address, port),
					NetworkInterface.getByName("etho"));
		//	msr.joinGroup(group);
			byte[] buffer = new byte[1024];
			while (true) {
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
				msr.receive(dp);
				String s = new String(dp.getData(), 0, dp.getLength());
				System.out.println("receive from node1:" + s+":"+dp.getSocketAddress());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}