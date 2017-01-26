package com.hangugi.tma2.crawler.domino.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DominoUnidSharedQueue {
	private static final Logger logger = LoggerFactory.getLogger(DominoUnidSharedQueue.class);
	private final ArrayBlockingQueue<String> arrayBlockingQueue;
	private final ReentrantLock reentrantLock = new ReentrantLock(true);
	private final AtomicLong atomicLong = new AtomicLong(0);
	private boolean isContinue;

	public DominoUnidSharedQueue(int queueSize) {
		this.arrayBlockingQueue = new ArrayBlockingQueue<String>(queueSize);
	}

	public synchronized boolean isContinue() {
		return this.isContinue;
	}

	public synchronized void setContinue(boolean isContinue) {
		this.isContinue = isContinue;
	}

	public synchronized int getQueueSize() {
		return this.arrayBlockingQueue.size();
	}

	public boolean put(String unid) throws InterruptedException {
		this.reentrantLock.lock();

		try {
			if (this.arrayBlockingQueue.contains(unid)) {
				logger.debug("UNID:" + unid + " " + "DS:domino unid is already queued.");
				return false;
			}
		} finally {
			this.reentrantLock.unlock();
		}

		this.arrayBlockingQueue.put(unid);
		logger.info("UNID:" + unid + " " + "DS:put the domino unid." + " " + "QSZ:" + this.getQueueSize());

		return true;
	}

	public void remove(String unid) {
		this.reentrantLock.lock();

		try {
			this.arrayBlockingQueue.remove(unid);
			logger.info("UNID:" + unid + " " + "DS:remove the domino unid." + " " + "QSZ:" + this.getQueueSize());
		} finally {
			this.reentrantLock.unlock();
		}
	}

	// 핫필드 최소화
	public void countUp(String unid) {
		logger.info("UNID:" + unid + " " + "DS:count up." + " " + "COUNT:" + this.atomicLong.incrementAndGet());
	}
}
