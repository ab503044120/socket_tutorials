package com.teamsun.mqttserver.handle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttMessage;

/**
 * 
 * @author acer
 *
 */
public interface Handle {

	
	public void connec(ChannelHandlerContext context);
	
	public void onMessage(ChannelHandlerContext context,MqttMessage message);
	
	public void disconnect(ChannelHandlerContext context);
	
	
	
	
}
