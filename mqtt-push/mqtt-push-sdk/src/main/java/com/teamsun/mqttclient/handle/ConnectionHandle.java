package com.teamsun.mqttclient.handle;

import org.apache.log4j.Logger;

import com.teamsun.mqttclient.service.ApiService;
import com.teamsun.mqttclient.service.DefaultApiService;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.util.AttributeKey;

/**
 * 处理连接
 * @author acer
 *
 */
public class ConnectionHandle extends ChannelInboundHandlerAdapter{

	Logger logger=Logger.getLogger(getClass());
	
	final String devicetopPre="/root/chat/";//设备主题前缀
	String username;
	String password;
	String deviceId;
	ApiService apiService;
	
	public ConnectionHandle(ApiService apiService,String deviceId,String username, String password) {
		super();
		this.deviceId=deviceId;
		this.username = username;
		this.password = password;
		
		this.apiService=(apiService==null)?
				DefaultApiService.intanceof():apiService;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		apiService.setChannel(ctx.channel());
		apiService.login(deviceId, username, password);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
	
			if (msg instanceof MqttMessage) {

				MqttMessage message = (MqttMessage) msg;
				MqttFixedHeader fixedHeader = message.fixedHeader();
				MqttMessageType messageType = fixedHeader.messageType();

				switch (messageType) {
				case CONNACK:
					ack(ctx,(MqttConnAckMessage)message);
					break;
				case DISCONNECT:
					ctx.close();
					break;
				default:
					ctx.fireChannelRead(msg);
					break;
				}
			} else
				ctx.close();
		
	}
	
	public void ack(ChannelHandlerContext ctx,MqttConnAckMessage ackMessage){
		
		switch( ackMessage.variableHeader().connectReturnCode()){
			
			case CONNECTION_ACCEPTED:
				
				AttributeKey<Boolean> loginKey = AttributeKey.valueOf("login");				
				final Channel channel=ctx.channel();
				channel.attr(loginKey).set(true);
				apiService.subscribe(devicetopPre+deviceId, MqttQoS.AT_LEAST_ONCE);
				//登录成功
				logger.info("登录成功");
				break;				
				default:
					//登录失败
					logger.info("登录失败");
					break;
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		
		super.exceptionCaught(ctx, cause);
		
	}

	
	
}
