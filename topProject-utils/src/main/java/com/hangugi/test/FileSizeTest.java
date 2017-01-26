package com.hangugi.test;

import java.io.File;

public class FileSizeTest {

	public static void main(String[] args) {
		String str = "/tma2data/queue/crawler/domino/unid/175.115.94.177/mailjrn.nsf/10/E334B7BC2CE60CC049257C780022B85F";
		System.out.println(new File(str).length());
	}
}
