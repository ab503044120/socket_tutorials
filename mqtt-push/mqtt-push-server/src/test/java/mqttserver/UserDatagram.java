package mqttserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MulticastChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

public class UserDatagram {

	public static void main(String[] args) throws IOException {
		
		DatagramChannel channel=DatagramChannel.open();
		Selector selector=Selector.open();
		channel.configureBlocking(false);
		channel.register(selector, SelectionKey.OP_READ);
		channel.bind(new InetSocketAddress("localhost", 8888));
		
		while(true){
			
			selector.select();
			
			 Iterator<SelectionKey> iterator=
					 selector.selectedKeys().iterator();
			 
			 while(iterator.hasNext()){
				 
				 SelectionKey key=iterator.next();
				 ByteBuffer buffer=ByteBuffer.allocate(1024);
				 
				 if(!channel.isConnected()){
					 //channel.connect(channel.getRemoteAddress());
				 }
				 if(key.isReadable()){
					 
					 channel.receive(buffer);
					 buffer.flip();
					 byte[] bs=new byte[buffer.limit()];
					 System.out.println(new String(bs));
					 channel.register(selector, SelectionKey.OP_READ);
				 }
				 if(key.isWritable()){
					 
					 ByteBuffer byteBuffer=ByteBuffer.wrap("你好".getBytes());
					 channel.write(byteBuffer);
				 }
				 iterator.remove();
			 }
			
		}
		

	}

}
