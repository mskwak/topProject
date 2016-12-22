package com.daou.test;

import java.util.HashMap;
import java.util.Map;

public class MapTest {

	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();

		map.put("1", "1");
		map.put("2", "1");

		System.out.println(map.size());

	}

}
