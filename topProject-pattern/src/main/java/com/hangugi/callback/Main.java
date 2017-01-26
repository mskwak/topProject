package com.hangugi.callback;

import com.hangugi.callback.template.ExecutorTemplate;


public class Main {
	public static void main(String[] args) {
		ExecutorTemplate executorTemplate = new ExecutorTemplate();

		System.out.println(executorTemplate.queueCount());
		System.out.println(executorTemplate.queueDelete());
	}
}