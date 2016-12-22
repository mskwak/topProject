package com.daou.callback;

import com.daou.callback.template.ExecutorTemplate;


public class Main {
	public static void main(String[] args) {
		ExecutorTemplate executorTemplate = new ExecutorTemplate();

		System.out.println(executorTemplate.queueCount());
		System.out.println(executorTemplate.queueDelete());
	}
}