/*
 * Copyright (C) 2017  即时通讯网(52im.net) & Jack Jiang.
 * The MobileIMSDK_X (MobileIMSDK v3.x) Project. 
 * All rights reserved.
 * 
 * > Github地址: https://github.com/JackJiang2011/MobileIMSDK
 * > 文档地址: http://www.52im.net/forum-89-1.html
 * > 即时通讯技术社区：http://www.52im.net/
 * > 即时通讯技术交流群：320837163 (http://www.52im.net/topic-qqgroup.html)
 *  
 * "即时通讯网(52im.net) - 即时通讯开发者社区!" 推荐开源工程。
 * 
 * CharsetHelper.java at 2017-5-2 15:49:27, code by Jack Jiang.
 * You can contact author with jack.jiang@52im.net or jb2011@163.com.
 */
package net.openmob.mobileimsdk.server.protocal;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class CharsetHelper
{
	public static final CharsetDecoder decoder = (Charset.forName(CharsetHelper.DECODE_CHARSET)).newDecoder();
	
	public final static String ENCODE_CHARSET = "UTF-8";
	public final static String DECODE_CHARSET = "UTF-8";
	
	public static String getString(byte[] b, int len)
	{
		try
		{
			return new String(b, 0 , len, DECODE_CHARSET);
		}
		catch (UnsupportedEncodingException e)
		{
			return new String(b, 0 , len);
		}
	}
	public static String getString(byte[] b, int start,int len)
	{
		try
		{
			return new String(b, start , len, DECODE_CHARSET);
		}
		catch (UnsupportedEncodingException e)
		{
			return new String(b, start , len);
		}
	}
	
	public static byte[] getBytes(String str)
	{
		if(str != null)
		{
			try
			{
				return str.getBytes(ENCODE_CHARSET);
			}
			catch (UnsupportedEncodingException e)
			{
				return str.getBytes();
			}
		}
		else
			return new byte[0];
	}
}
