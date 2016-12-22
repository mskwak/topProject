package com.daou.callback.template;

import java.net.Socket;

import org.apache.commons.io.IOUtils;

import com.daou.callback.command.Command;


public class ExecutorTemplate {

	public String queueCount() {
		Command command = new Command() {
			public String executeCommand(Socket socket) {
				int i = 0;
				return Integer.toString(i);
			}
		};

		return this.template(command);
	}

	public String queueDelete() {
		Command command = new Command() {
			public String executeCommand(Socket socket) {
				return "queueDelete";
			}
		};

		return this.template(command);
	}

	private String template(Command command) {

		Socket socket = new Socket();

		String str = command.executeCommand(socket);

		IOUtils.closeQuietly(socket);

		return str;
	}
}
