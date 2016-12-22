package com.daou.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class RegTest {

	public static void main(String[] args)  {
		try (BufferedReader in = new BufferedReader(new FileReader("/file.txt"))){
			String s;
			while ((s = in.readLine()) != null) {
				System.out.println(s);

				  String addr_trim = s.trim();

				  if (addr_trim.indexOf("\\\"") > 0) {
					  addr_trim = addr_trim.replaceAll("\\\\\"", "");
					  System.out.println(addr_trim);
				  }
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
