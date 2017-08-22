package org.huihui.nio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Administrator on 2017/8/22.
 */

public class NIO_Buffer {
    public static void main(String[] args) throws Exception {
        RandomAccessFile aFile = new RandomAccessFile("D:\\1.txt", "rw");
        FileChannel inChannel = aFile.getChannel();

//create buffer with capacity of 48 bytes
        ByteBuffer buf = ByteBuffer.allocate(48);

        int bytesRead = inChannel.read(buf); //read into buffer.
//        while (bytesRead != -1) {

            buf.flip();  //make buffer ready for read

            while (buf.hasRemaining()) {
                System.out.print((char) buf.get() + "   " + buf.position() + "\n"); // read 1 byte at a time
            }
            System.out.println("读取完成" + new String(buf.array(),"gbk"));
            buf.clear(); //make buffer ready for writing
            bytesRead = inChannel.read(buf);
//        }
        aFile.close();
    }

}
