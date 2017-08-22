package org.huihui.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by huihui on 2017/8/20.
 */
public class NIOClient {
    private int PORT = 8889;
    private static int blockSize = 1024 * 100;
    private static ByteBuffer sendBuffer = ByteBuffer.allocate(blockSize);
    private static ByteBuffer receiveBuffer = ByteBuffer.allocate(blockSize);
    private Selector mSelector;

    private final static InetSocketAddress serverAddress = new InetSocketAddress("localhost", 8889);


    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(serverAddress);

        Set<SelectionKey> selectionKeys;
        Iterator<SelectionKey> selectionKeyIterator;
        SocketChannel client;
        String recieveText;
        String sendText;
        while (true) {
            selector.select();
            selectionKeys = selector.selectedKeys();
            selectionKeyIterator = selectionKeys.iterator();
            while (selectionKeyIterator.hasNext()) {
                SelectionKey next = selectionKeyIterator.next();
                if (next.isConnectable()) {
                    System.out.println("client connect");
                    client = (SocketChannel) next.channel();
                    if (client.isConnectionPending()) {
                        client.finishConnect();
                        System.out.println("client connected success");
                        sendText = "你好服务端";
                        byte[] bytes = sendText.getBytes("utf-8");
                        byte a[] = new byte[blockSize];
                        for (int i = 0; i < blockSize; i++) {
                            a[i] = bytes[i % 10];
                        }
                        sendBuffer.clear();
                        sendBuffer.put(a);
                        sendBuffer.flip();
                        client.write(sendBuffer);
                    }
                    client.register(selector, SelectionKey.OP_READ);
                } else if (next.isReadable()) {
                    client = (SocketChannel) next.channel();
                    receiveBuffer.clear();
                    int count = client.read(receiveBuffer);
                    if (count > 0) {
                        recieveText = new String(receiveBuffer.array());
                        System.out.println("客户端接收到服务端数据: " + recieveText);
                        client.register(selector, SelectionKey.OP_WRITE);
                    }else{
                        next.cancel();
                    }
                } else if (next.isWritable()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendBuffer.clear();
                    client = (SocketChannel) next.channel();
                    sendText = "发送到服务端的数据";
                    sendBuffer.put(sendText.getBytes("utf-8"));
                    sendBuffer.flip();
                    client.write(sendBuffer);
                    System.out.println("客户端发给服务端: " + sendText);
                    client.register(selector, SelectionKey.OP_READ);
                }
            }
            selectionKeys.clear();
        }
    }
}
