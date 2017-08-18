package io.mqtt.test;

import com.teamsun.mqttclient.Conn.Connetor;
import com.teamsun.mqttclient.service.DefaultApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import io.netty.channel.ChannelFuture;

public class TestConcurrent {

	public static void main(String[] args) throws  Exception {
		
		
		List<ChannelFuture> channelFutures=new ArrayList<>();
		Connetor connetor=new Connetor();
//		for (int i = 0; i < 1000; i++) {
			
			DefaultApiService apiService=new DefaultApiService();
			Properties properties=new Properties();
			
			properties.put("host", "localhost");
			properties.put("port", 40000);
			properties.put("username", "user");
			properties.put("password", "user123456");
			
			properties.put("pingtime", 10);
			properties.put("recontimes", 5);
			properties.put("deviceId", "123456");
			channelFutures.add(connetor.connection(properties,apiService));
//		}
		
		for(ChannelFuture channelFuture:channelFutures){
			channelFuture.sync();
		}
	}
}
