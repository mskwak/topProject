package com.hangugi.test;

import java.io.IOException;
import java.net.Socket;

import org.apache.commons.io.IOUtils;

public class ForContinueBreakFinallyTest {

	public static void main(String[] args) {
		Socket socket = null;
//		SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 25);
		
		for(int i = 0; i < 2; i++) {
			try {
				socket = new Socket();
				socket.setSoTimeout(10 * 1000);
				break;
//				socket.connect(socketAddress, 10 * 1000);
//				socket.setSoTimeout(10 * 1000);
//				socket.setReuseAddress(true);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(socket);
				System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaa");
			}
		}
	}
}
