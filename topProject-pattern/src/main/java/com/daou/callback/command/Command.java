package com.daou.callback.command;

import java.net.Socket;

public interface Command {
	public String executeCommand(Socket socket);
}
