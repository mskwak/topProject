package com.daou.lotto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Lotto {
	private final int gameCount;

	private final Random random = new Random();

	public Lotto(int gameCount) {
		this.gameCount = gameCount;
	}

	public void printLotto() {
		for(int i = 0; i < this.gameCount; i++) {
			List<Integer> list = this.doOneGame();
			Iterator<Integer> iterator = list.iterator();
			String lottoNumber = "";

			while(iterator.hasNext()) {
				Integer number = iterator.next();
				lottoNumber = lottoNumber + " " + number.toString();
			}

			System.out.println(lottoNumber);
		}
	}

	public Map<Integer, List<Integer>> getLottoAsMap() {
		Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();

		for(int i = 0; i < this.gameCount; i++) {
			map.put(i, this.doOneGame());
		}

		return map;
	}

	public List<List<Integer>> getLottoAsList() {
		List<List<Integer>> list = new ArrayList<List<Integer>>();

		for(int i = 0; i < this.gameCount; i++) {
			list.add(this.doOneGame());
		}

		return list;
	}

	private List<Integer> doOneGame() {
		Set<Integer> set = new HashSet<Integer>();
		List<Integer> sortedList = null;

		for(;;) {
			int number = this.random.nextInt(45) + 1;
			set.add(number);

			if(set.size() == 6) {
				sortedList = new ArrayList<Integer>(set);
				Collections.sort(sortedList);
				break;
			}
		}

		return sortedList;
	}
}
