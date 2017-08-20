package com.teamsun.mqttserver;

import com.teamsun.mqttserver.middle.MqttTcpServer;

public class BootServer {

	public static void main(String[] args) {
		MqttTcpServer mqttServer=new MqttTcpServer();
		try {
			mqttServer.start(40000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
