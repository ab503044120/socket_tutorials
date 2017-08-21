package mqttserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Node1 {
	private static int port = 8000;
	private static String address = "224.0.2.1";

	public static void main(String[] args) throws Exception {
		
		DatagramSocket datagramSocket=null;
		try {
			InetAddress group = InetAddress.getByName(address);
			 datagramSocket=new DatagramSocket();
			// datagramSocket.connect(InetAddress.getByName("192.168.2.113"), 8888);
//			MulticastSocket mss = new MulticastSocket(port);
//			mss.setTimeToLive(255);
//			
//			mss.joinGroup(group);
			
			//System.out.println(mss.getNetworkInterface());
			while (true) {
				String message = "Hello from node1";
				byte[] buffer = message.getBytes();
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("224.0.2.1"), 8000);
				datagramSocket.send(dp);
				//mss.send(dp);
				//mss.send(dp);
				
				Thread.sleep(1000);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			//datagramSocket.close();
		}
		
	
	}
}