package com.hangugi.command.server;

import java.util.List;

public interface ServerCommand {
	public String executeCommand(List<String> commandList);

	public boolean isClose();
}
