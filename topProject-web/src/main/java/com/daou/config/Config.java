package com.daou.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.daou.lotto.Lotto;

@Configuration
public class Config {
	@Bean
	public Lotto mskwList() {
		return new Lotto(5);
	}
}
