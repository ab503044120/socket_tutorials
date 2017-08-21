package io.mqtt.test;

import java.util.Properties;

import com.teamsun.mqttclient.Conn.Connetor;
import com.teamsun.mqttclient.service.DefaultApiService;

public class TestRecon {

	public static void main(String[] args) throws Exception {
		
		
		final DefaultApiService apiService=new DefaultApiService();
		Properties properties=new Properties();
		
		properties.put("host", "localhost");
		properties.put("port", 1000);
		properties.put("username", "user");
		properties.put("password", "user123456");
		
		properties.put("pingtime", 60);
		properties.put("recontimes", 5);
		properties.put("deviceId", "123456");
		
		Connetor connetor=new Connetor();
		int recon=(Integer)properties.get("recontimes");
		
		for(int i=0;i<recon;i++){
			connetor.connection(properties, apiService).sync();
		}
		
	}
}
