package com.hangugi;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


public class FilterdComponentMain {

	public static void main(String[] args) throws Exception {
//		File file = new File(args[0]);
		File file = new File("/KOO.pdf");
		String fileName = file.getAbsolutePath();
		System.out.println(fileName);
		long fileSize = file.length();
		InputStream is = new FileInputStream(fileName);

		FilterdComponent filterdComponent = new FilterdComponent();
		String str = filterdComponent.extractTxt(is, fileSize, fileName, "base64");

		System.out.println(str);
	}
}
