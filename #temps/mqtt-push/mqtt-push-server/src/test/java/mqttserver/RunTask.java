package mqttserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.teamsun.mqttserver.service.AnsyncService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-service.xml")
public class RunTask {

	@Autowired
	AnsyncService ansync;
	@Test
	public void test() throws InterruptedException{
		
		ansync.runTask(new Runnable() {
			
			@Override
			public void run() {
					System.out.println(Thread.currentThread());
				
			}
		});
		
		Thread.sleep(11);
	}
}
