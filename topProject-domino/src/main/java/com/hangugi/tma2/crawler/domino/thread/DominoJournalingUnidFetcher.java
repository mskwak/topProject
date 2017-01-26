package com.hangugi.tma2.crawler.domino.thread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.naming.directory.DirContext;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.NotesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hangugi.tma2.crawler.domino.config.Constants;
import com.hangugi.tma2.crawler.domino.config.journaling.DominoJournalingServerConfig;
import com.hangugi.tma2.crawler.domino.document.DominoDocumentSet;
import com.hangugi.tma2.crawler.domino.queue.DominoUnidSharedQueue;
import com.hangugi.tma2.crawler.domino.util.DominoFactory;
import com.hangugi.tma2.crawler.domino.util.DominoTimeUtil;

public class DominoJournalingUnidFetcher extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(DominoJournalingUnidFetcher.class);
	private final static long sleepTime = 5 * 1000;
	private final static String compactTime = "04";
	private final ExecutorService executorService;
	private final DominoJournalingServerConfig dominoJournalingServerConfig;
	private final DominoUnidSharedQueue dominoUnidSharedQueue;
	private boolean shutdown = false;
	private boolean compact = false;
	private DirContext dirContext = null;
	private Database database;
	private Object targetAuthObject;
	private boolean interrupted = true;

	public DominoJournalingUnidFetcher(ExecutorService executorService, DominoJournalingServerConfig dominoJournalingServerConfig, DominoUnidSharedQueue dominoUnidSharedQueue) {
		this.executorService = executorService;
		this.dominoJournalingServerConfig = dominoJournalingServerConfig;
		this.dominoUnidSharedQueue = dominoUnidSharedQueue;
	}

	@Override
	public void run() {
		while (!this.shutdown) {
			try {
				this.init();
				this.doCompactJob();
				this.doJob();
			} catch (Exception e) {
				this.interrupted = true;
				logger.error(e.toString(), e);
				this.sleepThread(sleepTime);
			} finally {
				if (this.interrupted) {
					Thread.currentThread().interrupt();
				}
			}
		}

		logger.info("DS:shutdown DominoJournalingUnidFetcher");
	}

	public void shutdown() {
		this.shutdown = true;
		this.shutdownExecutorService();
	}

	private void shutdownExecutorService() {
		while (!this.executorService.isTerminated()) {
			this.executorService.shutdown();

			try {
				if (this.executorService.awaitTermination(5, TimeUnit.SECONDS)) {
					logger.info("DS:shutdown ExecutorService");
				}
			} catch (InterruptedException e) {
				logger.error(e.toString(), e);
			}
		}
	}

	private void init() throws Exception {
		if (this.interrupted) {
			this.database = DominoFactory.getDataBase(this.database, this.dominoJournalingServerConfig);
			this.dirContext = DominoFactory.getDirContext(this.dirContext, this.dominoJournalingServerConfig);
			this.targetAuthObject = DominoFactory.getTargetAuthObject(this.targetAuthObject, this.dominoJournalingServerConfig);
			this.interrupted = false;
			this.dominoUnidSharedQueue.setContinue(true);
		}
	}

	private void doJob() throws Exception {
		if (!DominoTimeUtil.isJobTime(this.dominoJournalingServerConfig.getDominoScheduleConfig())) {
			this.sleepThread(sleepTime);
			return;
		}

		DocumentCollection documentCollection = this.database.getAllDocuments();
		int documentCount = documentCollection.getCount();

		logger.info("DS:the total document count on server:" + Integer.toString(documentCount) + " " + "QSZ:" + this.dominoUnidSharedQueue.getQueueSize());

		this.doArchiveJob(documentCount, documentCollection);

		if (documentCount < 5) {
			this.sleepThread(sleepTime);
		}
	}

	// 서버에 문서를 보존하는 경우(/opt/daou/TMA2/config/domino.config 에서 document_deletion이 off의 경우), 스레드는 무한 루프를 돌면서 동일 메일을 계속 처리한다. 즉, 메일은 중복 아카이빙된다.
	// 고객사든 사내든 데이터 검증과 같은 테스트 환경에서만 사용할 수 있다.
	// 실전에서는 서버의 문서를 무조건 삭제하도록 해야 한다.
	private void doArchiveJob(int documentCount, DocumentCollection documentCollection) throws Exception {
		for (int i = 1; i <= documentCount; i++) {
			Document document = documentCollection.getNthDocument(i);

			if (document == null) {
				logger.info("DS:document is null");
				continue;
			}

			if (!this.dominoUnidSharedQueue.isContinue()) {
				throw new Exception(); //NOPMD
			}

			String documentUnid = document.getUniversalID();

			if (documentUnid.length() == Constants.DOMINO_UNID_LENGTH && this.dominoUnidSharedQueue.put(documentUnid)) {
				DominoDocumentSet dominoDocumentSet = new DominoDocumentSet();
				dominoDocumentSet.setDocument(document);
				dominoDocumentSet.setDocumentUnid(documentUnid);
				dominoDocumentSet.setDominoServerConfig(this.dominoJournalingServerConfig);
				dominoDocumentSet.setDirContext(this.dirContext);
				dominoDocumentSet.setTargetAuthObject(this.targetAuthObject);

				DominoDocumentFetcher dominoDocumentFetcher = new DominoDocumentFetcher(dominoDocumentSet, this.dominoUnidSharedQueue);

				this.executorService.execute(dominoDocumentFetcher);
			}
		}
	}

	// 매일 04시 대에 compact 실행
	// compact 작업은 도미노 서버가 가지고 있는 고유기능으로써, 데이터베이스 파일(*.nsf)의 크기를 resizing 해주는 기능이다.
	private void doCompactJob() throws NotesException {
		if (this.database == null) {
			this.sleepThread(sleepTime);
			return;
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
		String currentHour = simpleDateFormat.format(new Date());

		if (compactTime.equals(currentHour)) {
			if (!this.compact) {
				logger.info("DS:It's time to compact database.");
				this.database.compactWithOptions(Database.CMPC_RECOVER_INPLACE);
				this.compact = true;
			}
		} else {
			this.compact = false;
		}

		return;
	}

	private void sleepThread(long sec) {
		try {
			Thread.currentThread();
			Thread.sleep(sec);
		} catch (InterruptedException e) {
			// don't do anything
		}
	}
}