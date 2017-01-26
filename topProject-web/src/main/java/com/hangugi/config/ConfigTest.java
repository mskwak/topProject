package com.hangugi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hangugi.lotto.Lotto;

@Component
public class ConfigTest {
	@Autowired
	private Lotto lotto;

	public void useMskwList() {
//		System.out.println(this.mskwList.get(0));
	}
}
