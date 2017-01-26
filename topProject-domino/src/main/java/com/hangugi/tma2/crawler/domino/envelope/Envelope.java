package com.hangugi.tma2.crawler.domino.envelope;

import java.util.List;

public class Envelope {
	private List<String> rcptList;
	private String emlPath;

	public List<String> getRcptList() {
		return this.rcptList;
	}

	public void setRcptList(List<String> rcptList) {
		this.rcptList = rcptList;
	}

	public String getEmlPath() {
		return this.emlPath;
	}

	public void setEmlPath(String path) {
		this.emlPath = path;
	}

	@Override
	public String toString() {
		return "Envelope [rcptList=" + this.rcptList + ", emlPath=" + this.emlPath + "]";
	}
}
