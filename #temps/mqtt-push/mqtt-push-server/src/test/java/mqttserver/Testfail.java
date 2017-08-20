package mqttserver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Testfail {

	
	public static void main(String[] args) {
		
		
		ExecutorService executorService=Executors.newCachedThreadPool();
		
		executorService.submit(new Runnable() {
			
			@Override
			public void run() {
				
			}
		});
		
	}
}
