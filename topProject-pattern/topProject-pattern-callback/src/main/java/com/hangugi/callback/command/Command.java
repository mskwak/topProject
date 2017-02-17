package com.hangugi.callback.command;

import java.net.Socket;

public interface Command {
	public String executeCommand(Socket socket);
}
