package com.hangugi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hangugi.lotto.Lotto;

@Configuration
public class Config {
	@Bean
	public Lotto mskwList() {
		return new Lotto(5);
	}
}
