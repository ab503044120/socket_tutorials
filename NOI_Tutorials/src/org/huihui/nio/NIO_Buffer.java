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
        //这里是可以读多少
        int bytesRead = inChannel.read(buf); //read into buffer.
        System.out.println("总共: " + bytesRead);
        while (bytesRead != -1) {

            buf.flip();  //make buffer ready for read

            while (buf.hasRemaining()) {
                byte[] dst = new byte[48];
                buf.get(dst, 0, buf.limit());
                System.out.print(new String(dst, "gbk") + "\n"); // read 1 byte at a time
            }
            //clear不会清除里面的数据
            buf.clear(); //make buffer ready for writing
//            buf = ByteBuffer.allocate(48);
            bytesRead = inChannel.read(buf);
            System.out.println("总共: " + bytesRead);
        }
        aFile.close();
    }

}
