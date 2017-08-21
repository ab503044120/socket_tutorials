package org.huihui.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by huihui on 2017/8/20.
 */
public class NIOServer {
    private int PORT = 8888;
    private int blockSize = 4096;
    private ByteBuffer sendBuffer = ByteBuffer.allocate(blockSize);
    private ByteBuffer receiveBuffer = ByteBuffer.allocate(blockSize);
    private Selector mSelector;

    public NIOServer() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        //绑定ip和端口
        serverSocket.bind(new InetSocketAddress(PORT));
        //打开选择器
        mSelector = Selector.open();
        serverSocketChannel.register(mSelector, SelectionKey.OP_ACCEPT);
        System.out.println("server Start on " + PORT);
    }

    //监听事件
    public void listen() throws IOException {
        while (true) {
            mSelector.select();
            Set<SelectionKey> selectionKeys = mSelector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                iterator.remove();
                handlekey(next);
            }
        }
    }

    public void handlekey(SelectionKey selectionKey) throws IOException {
        ServerSocketChannel server = null;
        SocketChannel client = null;
        String recievText;
        String sendText;
        int count = 0;
        if (selectionKey.isAcceptable()) {
            server = (ServerSocketChannel) selectionKey.channel();
            client = server.accept();
            client.configureBlocking(false);
            client.register(mSelector, SelectionKey.OP_READ);
        } else if (selectionKey.isReadable()) {
            client = (SocketChannel) selectionKey.channel();
            count = client.read(receiveBuffer);
            if (count > 0) {
                recievText = new String(receiveBuffer.array(), 0, count);
                System.out.println("服务端接收到客户端的信息:" + recievText);
            }
        }
//        else if (selectionKey.isWritable()) {
//            sendBuffer.clear();
//            client = (SocketChannel) selectionKey.channel();
//            sendText = "我收到你的msg了";
//            sendBuffer.put(sendText.getBytes("utf-8"));
//            sendBuffer.flip();
//            client.write(sendBuffer);
//            System.out.println("服务端发送数据给客户端: " + sendText);
//            client.register(mSelector, SelectionKey.OP_READ);
//        }

    }

    public static void main(String[] args) throws IOException {
        NIOServer nioServer = new NIOServer();
        nioServer.listen();
    }
}
