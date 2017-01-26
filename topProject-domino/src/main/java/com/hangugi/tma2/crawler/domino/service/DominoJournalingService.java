package com.hangugi.tma2.crawler.domino.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hangugi.tma2.crawler.domino.config.DominoServerConfig;
import com.hangugi.tma2.crawler.domino.config.journaling.DominoJournalingServerConfig;
import com.hangugi.tma2.crawler.domino.queue.DominoUnidSharedQueue;
import com.hangugi.tma2.crawler.domino.thread.DominoJournalingUnidFetcher;
import com.hangugi.tma2.crawler.domino.thread.RetryDocumentFetcher;

@Service
public class DominoJournalingService implements AbstractDominoService {
	private static final Logger logger = LoggerFactory.getLogger(DominoJournalingService.class);
	private final List<RetryDocumentFetcher> retryDocumentFetcherList = new ArrayList<RetryDocumentFetcher>();
	private final List<DominoJournalingUnidFetcher> dominoJournalingUnidFetcherList = new ArrayList<DominoJournalingUnidFetcher>();

	public List<DominoJournalingUnidFetcher> getDominoJournalingUnidFetcherList() {
		return this.dominoJournalingUnidFetcherList;
	}

	@Override
	public void init() {
		logger.info("DS:init DominoJournalingService");
	}

	@Override
	public void shutdown() {
		logger.info("DS:shutdown DominoJournalingService");
		this.shutdownJournalingThread();
		this.shutdownRetryThread();
	}

	private void shutdownJournalingThread() {
		for (DominoJournalingUnidFetcher dominoJournalingUnidFetcher : this.dominoJournalingUnidFetcherList) {
			dominoJournalingUnidFetcher.shutdown();
			dominoJournalingUnidFetcher.interrupt();
		}
	}

	private void shutdownRetryThread() {
		for (RetryDocumentFetcher retryDocumentFetcher : this.retryDocumentFetcherList) {
			retryDocumentFetcher.shutdown();
			retryDocumentFetcher.interrupt();
		}
	}

	@Override
	public void launch(List<DominoServerConfig> dominoServerConfigList) {
		for (DominoServerConfig dominoServerConfig : dominoServerConfigList) {
			if (dominoServerConfig instanceof DominoJournalingServerConfig) {
				this.spawnJournalingThread((DominoJournalingServerConfig) dominoServerConfig);
				this.spawnRetryThread(dominoServerConfig);
			}
		}
	}

	private void spawnRetryThread(DominoServerConfig dominoServerConfig) {
		RetryDocumentFetcher retryDocumentFetcher = new RetryDocumentFetcher(dominoServerConfig);
		this.retryDocumentFetcherList.add(retryDocumentFetcher);
		retryDocumentFetcher.start();
	}

	private void spawnJournalingThread(DominoJournalingServerConfig dominoJournalingServerConfig) {
		int workerThreadCount = dominoJournalingServerConfig.getWorkerThread();
		int maxWorkerThreadCount = Runtime.getRuntime().availableProcessors() * 3;
		workerThreadCount = (workerThreadCount > maxWorkerThreadCount) ? maxWorkerThreadCount : workerThreadCount;

		ExecutorService executorService = Executors.newFixedThreadPool(workerThreadCount);
		DominoUnidSharedQueue dominoUnidSharedQueue = new DominoUnidSharedQueue(workerThreadCount);
		DominoJournalingUnidFetcher dominoJournalingUnidFetcher = new DominoJournalingUnidFetcher(executorService, dominoJournalingServerConfig, dominoUnidSharedQueue);

		this.dominoJournalingUnidFetcherList.add(dominoJournalingUnidFetcher);

		// 데몬 스레드: 메인 스레드가 종료되면 같이 종료된다.
		//dominoJournalingUnidFetcher.setDaemon(true);
		dominoJournalingUnidFetcher.start();
	}
}
