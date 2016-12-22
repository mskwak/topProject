package com.daou.test;

public class RunTimeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Runtime runtime = Runtime.getRuntime();
		runtime.addShutdownHook(new Hook());
		
		for(;;) {
			System.out.println(runtime.availableProcessors());
			try {
				Thread.sleep(1 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}


class Hook extends Thread {
	@Override
	public void run() {
		System.out.println("call hook.");
	}
}