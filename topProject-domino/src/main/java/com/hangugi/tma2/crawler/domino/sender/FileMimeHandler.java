package com.hangugi.tma2.crawler.domino.sender;

import com.hangugi.tma2.crawler.domino.envelope.Envelope;

public class FileMimeHandler extends MimeHandler {

	@Override
	public boolean send(String documentUnid, Envelope envelope, Object targetAuthObject) throws Exception {
		return true;
	}

	@Override
	public void recordUnidHistory(String documentUnid, String historyDirString) {
		return;
	}
}
