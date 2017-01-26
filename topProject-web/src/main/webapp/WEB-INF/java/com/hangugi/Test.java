package com.hangugi;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Test {
	public static void main(String[] args) throws IOException {
		SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMddHHmmss");

		System.out.println(formatter.format(new Date()));

		for(int i = 0; i < 10; i++) {
			doo(i);
		}
		
		List<String> list = new ArrayList<String>();
		
		list.add("aaa");
		list.add("bbbb");
		list.add("ccccc");
	}

	private static void doo(int i) throws SocketTimeoutException, IOException {

		System.out.println(UUID.randomUUID().toString());

		if(i == 0) {
//			throw new SocketTimeoutException();
		} else {
//			throw new IOException();
		}
	}
}
