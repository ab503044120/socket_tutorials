package com.teamsun.mqttserver.middle;


import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.teamsun.mqttserver.handle.MyChannelInitializer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.udt.nio.NioUdtProvider;
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * 
 * @author 
 * 
 */
public class MqttUdtServer {

	private int sobacklog=1024;

	private int port=1000;

	private int bossthreads=Runtime.getRuntime().availableProcessors();


	private int workthreads=Runtime.getRuntime().availableProcessors()*2;

	private int readtimeout=10000;

	public void start(int port) throws InterruptedException {

		
		final ThreadFactory acceptFactory = new DefaultThreadFactory("accept");
	    final ThreadFactory connectFactory = new DefaultThreadFactory("connect");
		ServerBootstrap bootstrap = new ServerBootstrap();// 启动辅助类

		// io线程和工作线程
		NioEventLoopGroup group = new NioEventLoopGroup(bossthreads,acceptFactory,NioUdtProvider.BYTE_PROVIDER);

	
		final NioEventLoopGroup workGroup = new NioEventLoopGroup(workthreads,connectFactory,NioUdtProvider.BYTE_PROVIDER);

		ApplicationContext applicationContext=new ClassPathXmlApplicationContext("application-service.xml");

		bootstrap.group(group, workGroup).channelFactory(NioUdtProvider.BYTE_ACCEPTOR);
	//	bootstrap.channel(NioSctpServerChannel.class);//
		bootstrap.localAddress(new InetSocketAddress(port));// 绑定端口
		bootstrap.option(ChannelOption.SO_BACKLOG, sobacklog);
		try {
			bootstrap.childHandler(new MyChannelInitializer(applicationContext));
			ChannelFuture f = bootstrap.bind().sync();// 实际绑定操作
			System.out.println(MqttUdtServer.class.getName() + " 服务器启动成功 "
					+ f.channel().localAddress());

			f.channel().closeFuture().sync();// ;// 等待 服务器关闭
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully().sync();// 优雅关闭
			workGroup.shutdownGracefully().sync();
		}
	}

	public int getSobacklog() {
		return sobacklog;
	}

	public void setSobacklog(int sobacklog) {
		this.sobacklog = sobacklog;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getBossthreads() {
		return bossthreads;
	}

	public void setBossthreads(int bossthreads) {
		this.bossthreads = bossthreads;
	}

	public int getWorkthreads() {
		return workthreads;
	}

	public void setWorkthreads(int workthreads) {
		this.workthreads = workthreads;
	}

	public int getReadtimeout() {
		return readtimeout;
	}

	public void setReadtimeout(int readtimeout) {
		this.readtimeout = readtimeout;
	}

}