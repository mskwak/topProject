package com.hangugi;

public class Digit {

	public static void main(String[] args) {
		int i = 1;
		Integer ii = 1;
//		System.out.println(ii.byteValue());
		


		System.out.println(Integer.toBinaryString(234567890));


		byte[] b = intToByteArray(12345678);
		
		for(byte bb: b) {
//			System.out.println(bb);
		}
		
		
	}
	
	public static byte[] intToByteArray(int value) {
		byte[] byteArray = new byte[4];
		byteArray[0] = (byte)(value >> 24);
		byteArray[1] = (byte)(value >> 16);
		byteArray[2] = (byte)(value >> 8);
		byteArray[3] = (byte)(value);
		return byteArray;
	}
	
	public  int byteArrayToInt(byte bytes[]) {
		return ((((int)bytes[0] & 0xff) << 24) |
				(((int)bytes[1] & 0xff) << 16) |
				(((int)bytes[2] & 0xff) << 8) |
				(((int)bytes[3] & 0xff)));
	} 
}
