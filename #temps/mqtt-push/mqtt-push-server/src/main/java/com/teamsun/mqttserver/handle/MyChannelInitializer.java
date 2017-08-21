package com.teamsun.mqttserver.handle;



import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;

/**
 * 链路初始化
 * @author acer
 *
 */
public class MyChannelInitializer extends ChannelInitializer<Channel>{

	ApplicationContext applicationContext;

	Logger logger=Logger.getLogger(getClass());
	
	public MyChannelInitializer(ApplicationContext applicationContext) {
		this.applicationContext=applicationContext;
	}
	@Override
	protected void initChannel(Channel ch) throws Exception {
		
		logger.debug(ch+"链路链接");
		ch.pipeline()
		//.addLast(new LoggingHandler(LogLevel.DEBUG))
		.addLast(MqttEncoder.INSTANCE,
				new MqttDecoder(),
				new ConnectionHandle(applicationContext),
				new SubServiceHandle(applicationContext),
				new PushServiceHandle(applicationContext));
	}

}
