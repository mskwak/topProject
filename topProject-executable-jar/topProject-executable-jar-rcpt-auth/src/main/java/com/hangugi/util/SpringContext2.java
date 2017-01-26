package com.hangugi.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/*
<bean id="moduleService" class="com.hangugi.tma2.common.service.ModuleService" primary="true" scope="singleton"/>
*/

public class SpringContext2 {
	@Autowired
	private SpringContext2(ApplicationContext applicationContext) {
		staticContext = applicationContext;
	}

	private volatile static ApplicationContext staticContext; // NOPMD by ysko static으로 접근하기 위한 목적을 위해서 만튼 코드라서 규약을 지킬 수 없음

	public static Object getBean(String name) {
		return staticContext.getBean(name);
	}

	public static <T> T getBean(Class<T> type) {
		return staticContext.getBean(type);
	}

	public static <T> T getBean(String name, Class<T> type) {
		return staticContext.getBean(name, type);
	}

	public static boolean contains(String beanName) {
		return staticContext.containsBean(beanName);
	}
}
