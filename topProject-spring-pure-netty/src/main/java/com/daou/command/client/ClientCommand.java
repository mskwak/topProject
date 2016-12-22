package com.daou.command.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;


public interface ClientCommand {
	public String executeCommand(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter, String emailAddress) throws IOException;
}
