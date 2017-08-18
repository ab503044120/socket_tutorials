package mqttserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class SpinBuff {

	public static void main(String[] args) {
		
		
		
		ByteBuf buf=Unpooled.wrappedBuffer("nihao".getBytes());
		
		byte[] bs=new byte[buf.readableBytes()];
		
		buf.readBytes(bs);
		
System.out.println(new String(bs));
	}

}
