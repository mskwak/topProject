package com.daou.test;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

public class SystemPropertyTest {
	public static void main(String[] args) {
		Properties properties = System.getProperties();
		Iterator<Entry<Object, Object>> iterator = properties.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<Object, Object> entry = iterator.next();

			System.out.println((String) entry.getKey() + ":" + (String) entry.getValue());
			//System.out.println(entry.getKey().getClass() + ":" + entry.getValue().getClass());
			//			System.out.println(entry.getKey().getClass().getCanonicalName() + ":" + entry.getValue().getClass().getCanonicalName());
			//			System.out.println(entry.getKey().getClass().getName() + ":" + entry.getValue().getClass().getName());
			//			System.out.println(entry.getKey().getClass().getSimpleName() + ":" + entry.getValue().getClass().getSimpleName());
		}
	}
}
