package com.teamsun.mqttp2p;


import java.net.InetAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.oio.OioDatagramChannel;

/**
 * 模拟P2P客户端
 * @author 
 *
 */
public class EchoClient{
    
    public static void main(String[] args) {
        int port = 7779;
        if(args.length != 0){
            port = Integer.parseInt(args[0]);
        }
        Bootstrap b = new Bootstrap();
        EventLoopGroup group = new OioEventLoopGroup();
        
        try {
            b.group(group)
             .channel(OioDatagramChannel.class)
             .option(ChannelOption.SO_BROADCAST, true)
             .option(ChannelOption.IP_MULTICAST_ADDR, InetAddress.getByName("228.0.0.4"))
             .handler(new EchoClientHandler());
            b.bind(port).sync().channel().closeFuture().await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            group.shutdownGracefully();
        }
    }
}