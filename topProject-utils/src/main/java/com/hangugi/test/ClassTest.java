package com.hangugi.test;

import java.lang.reflect.Method;

public class ClassTest {
	public static void main(String[] args) throws ClassNotFoundException {
		@SuppressWarnings("rawtypes")
		Class clazz = Class.forName("java.util.ArrayList");

		Method[] methods = clazz.getMethods();

		for (Method method : methods) {
			System.out.println(method.getName());
		}
		
		java.lang.reflect.Field[] fields = clazz.getFields();
		
		for (java.lang.reflect.Field field: fields) {
			System.out.println(field.getName());
		}

		//String str = clazz.getSimpleName();
		//System.out.println(str);
	}
}
