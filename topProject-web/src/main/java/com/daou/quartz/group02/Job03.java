package com.daou.quartz.group02;

import org.springframework.stereotype.Component;

@Component
public class Job03 {
	public void print() {
		System.out.println("I'am Job03. I run every 15 seconds.");
	}
}
