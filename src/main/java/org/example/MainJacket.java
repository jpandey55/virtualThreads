package org.example;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This program shows that we can start millions of Virtual Threads
 * without the program or machine crashing. Virtual Threads are 
 * cheap and efficient to create. Virtual threads are not 
 * considered as an expensive resource.
 * 
 *  Vary the NUM_THREADS variable and see that this is the case.
 * 
 * @author vshetty
 *
 */
public class MainJacket {
	
	private static final int NUM_THREADS = 10;
	
	private static void handleUserRequest() {
		 System.out.println("Starting thread " + Thread.currentThread());
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 System.out.println("Ending thread " + Thread.currentThread());

	}
	
	public static void main(String[] args) throws Exception {
		
		System.out.println("Starting main " + Thread.currentThread());

		try(ExecutorService srv = Executors.newVirtualThreadPerTaskExecutor()) {
			for (int j= 0; j < NUM_THREADS; j++) {
				srv.submit(() -> handleUserRequest());
			}
		}

		System.out.println("Ending main");
	}


}
