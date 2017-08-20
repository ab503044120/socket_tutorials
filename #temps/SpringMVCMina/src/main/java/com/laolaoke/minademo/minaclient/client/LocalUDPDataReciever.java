/*
 * Copyright (C) 2016 即时通讯网(52im.net) The MobileIMSDK Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/MobileIMSDK
 *  
 * 即时通讯网(52im.net) - 即时通讯技术社区! PROPRIETARY/CONFIDENTIAL.
 * Use is subject to license terms.
 * 
 * LocalUDPDataReciever.java at 2016-2-20 11:25:57, code by Jack Jiang.
 * You can contact author with jack.jiang@52im.net or jb2011@163.com.
 */
package com.laolaoke.minademo.minaclient.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import com.laolaoke.minademo.minaclient.client.utils.ConfigEntity;
import com.laolaoke.minademo.minaclient.client.utils.Log;



public class LocalUDPDataReciever
{
	private static final String TAG = LocalUDPDataReciever.class.getSimpleName();
	private static LocalUDPDataReciever instance = null;
	private Thread thread = null;

	public static LocalUDPDataReciever getInstance()
	{
		if (instance == null)
			instance = new LocalUDPDataReciever();
		return instance;
	}

	public void startup()
	{
		this.thread = new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					Log.d(LocalUDPDataReciever.TAG, "本地UDP端口侦听中，端口=" + ConfigEntity.localUDPPort + "...");

					//开始侦听
					LocalUDPDataReciever.this.udpListeningImpl();
				}
				catch (Exception eee)
				{
					Log.w(LocalUDPDataReciever.TAG, "本地UDP监听停止了(socket被关闭了?)," + eee.getMessage(), eee);
				}
			}
		});
		this.thread.start();
	}

	private void udpListeningImpl() throws Exception
	{
		while (true)
		{
			byte[] data = new byte[1024];
			// 接收数据报的包
			DatagramPacket packet = new DatagramPacket(data, data.length);

			DatagramSocket localUDPSocket = LocalUDPSocketProvider.getInstance().getLocalUDPSocket();
			if ((localUDPSocket == null) || (localUDPSocket.isClosed()))
				continue;
			
			// 阻塞直到收到数据
			localUDPSocket.receive(packet);
			
			// 解析服务端发过来的数据
			String pFromServer = new String(packet.getData(), 0 , packet.getLength(), "UTF-8");
			Log.w(LocalUDPDataReciever.TAG, "【NOTE】>>>>>> 收到服务端的消息："+pFromServer);
		}
	}
}