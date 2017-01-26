package com.hangugi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Test {

	@Autowired
	GutenTag gutenTag;

	public void hello() {
//		System.out.println("HelloTest!!!");
		this.gutenTag.guten();
	}
}
