package mqttserver;

import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

public class Test {

	
	public static void main(String[] args) throws InterruptedException, UnknownHostException {
	
//////		Logger logger=Logger.getLogger(Test.class);
////////		
////////		logger.debug("111");
////////		logger.info("1112");
////////		logger.warn("xxx");
//////		
//////		//MqttTcpServer mqttServer=new MqttTcpServer();
////		MqttTcpServer mqttServer=new MqttTcpServer();
////		mqttServer.start(1000);
////		
////		System.out.println(InetAddress.getLocalHost().getHostAddress());
//		
//		
//		ConcurrentHashMap<String, String> concurrentHashMap=new ConcurrentHashMap<>();
//		
//		System.out.println(concurrentHashMap.put("a", "b"));
//		System.out.println( concurrentHashMap.put("a", "c"));
//		System.out.println( concurrentHashMap.put("a", "e"));
//		System.out.println( concurrentHashMap.put("a", "d"));
//	
//		System.out.println(concurrentHashMap);
		
		System.out.println(string2Unicode("你好"));
		
		System.out.println(unicode2String("\\u4f60\\u597d"));
		
	}
	
	public static String string2Unicode(String string) {
		 
	    StringBuffer unicode = new StringBuffer();
	 
	    for (int i = 0; i < string.length(); i++) {
	 
	        // 取出每一个字符
	        char c = string.charAt(i);
	 
	        // 转换为unicode
	        unicode.append("\\u" + Integer.toHexString(c));
	    }
	 
	    return unicode.toString();
	}
	
	public static String unicode2String(String unicode) {
		 
	    StringBuffer string = new StringBuffer();
	 
	    String[] hex = unicode.split("\\\\u");
	 
	    for (int i = 1; i < hex.length; i++) {
	 
	        // 转换出每一个代码点
	        int data = Integer.parseInt(hex[i], 16);
	 
	        // 追加成string
	        string.append((char) data);
	    }
	 
	    return string.toString();
	}

}
