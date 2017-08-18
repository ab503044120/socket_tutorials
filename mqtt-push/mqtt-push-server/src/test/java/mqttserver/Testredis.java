package mqttserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.teamsun.mqttserver.entity.DeviceMq;
import com.teamsun.mqttserver.entity.MsgRep;
import com.teamsun.mqttserver.entity.SendMsgStatus;
import com.teamsun.mqttserver.service.AnsyncService;
import com.teamsun.mqttserver.service.MQManagerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-service.xml")
public class Testredis {

	
	@Autowired
	 MQManagerService mqservice;
	
	
	@Autowired
	AnsyncService asyncservice;
	
	@Test
	public void test(){
//		
		//MsgRep msgRep=new MsgRep("a/b/","/root",new byte[]{1,2,2,3});
////		
//		for (int i = 0; i < 100; i++) {
//			MsgRep msgRep=new MsgRep("a/b/","/root",new byte[]{(byte)0,(byte)0});
//			mqservice.pushMsg(msgRep);
//			
//			for (int j = 0; j < 100; j++) {
//				mqservice.pushDeviceMq("1111", new SendMsgStatus(msgRep.getMessageid(), SendMsgStatus.MsgStatus.SEND_FAIL));
//			}
//		}
	
		
		
	//System.out.println(	mqservice.getDeviceQueMsg("1111").getMsgRep());
		
		DeviceMq deviceMq=null;
		do{
			deviceMq=mqservice.getDeviceQueMsg("1111");
		}while(deviceMq.isNext());
	}
}
