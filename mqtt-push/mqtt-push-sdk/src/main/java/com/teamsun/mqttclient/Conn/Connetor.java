package com.teamsun.mqttclient.Conn;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.teamsun.mqttclient.handle.ConnectionHandle;
import com.teamsun.mqttclient.handle.Iden2PingHandle;
import com.teamsun.mqttclient.handle.PubHandle;
import com.teamsun.mqttclient.handle.SubHandle;
import com.teamsun.mqttclient.service.ApiService;
import com.teamsun.mqttclient.service.DefaultMessageListener;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class Connetor {
	
	static Logger logger=Logger.getLogger(Connetor.class);
	final EventLoopGroup group = new NioEventLoopGroup();

	
	
	
	public ChannelFuture connection(Properties properties,final ApiService apiService) {

		// Configure the client.

		String host = (String) properties.getOrDefault("host", "127.0.0.1");
		Integer remotePort = (Integer) properties.getOrDefault("port", 1000);
		final Integer pingtime = (Integer) properties.getOrDefault("pingtime", 60);
		final String deviceId = (String) properties.getOrDefault("deviceId", "0000");
		final String username = (String) properties.getOrDefault("username", "0000");
		final String password = (String) properties.getOrDefault("password", "0000");
		

		final DefaultMessageListener defaultMessageListener = new DefaultMessageListener();

		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline p = ch.pipeline();
							p.addLast(
									new ReadTimeoutHandler(pingtime*2),
									MqttEncoder.INSTANCE, new MqttDecoder(),
									new Iden2PingHandle(pingtime),
									new ConnectionHandle(apiService,deviceId, username, password),
									new PubHandle(defaultMessageListener), new SubHandle());
						}
					});

			ChannelFuture f = b.connect(host, remotePort).sync();
			return f.channel().closeFuture();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
			group.shutdownGracefully();
		} finally {
		}
		return null;
	}
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		logger.info("销毁");
	}
	
	
}
