package com.hangugi.tma2.crawler.domino.thread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.naming.directory.DirContext;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hangugi.tma2.crawler.domino.config.Constants;
import com.hangugi.tma2.crawler.domino.config.migration.DominoMigrationServerConfig;
import com.hangugi.tma2.crawler.domino.document.DominoDocumentSet;
import com.hangugi.tma2.crawler.domino.queue.DominoUnidSharedQueue;
import com.hangugi.tma2.crawler.domino.util.DominoFactory;

public class DominoMigrationUnidFetcher extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(DominoMigrationUnidFetcher.class);
	private final ExecutorService executorService;
	private final DominoMigrationServerConfig dominoMigrationServerConfig;
	private final DominoUnidSharedQueue dominoUnidSharedQueue;
	private Database database;
	private DirContext dirContext = null;
	private Object targetAuthObject;
	private boolean interrupted = false;

	public DominoMigrationUnidFetcher(ExecutorService executorService, DominoMigrationServerConfig dominoMigrationServerConfig, DominoUnidSharedQueue dominoUnidSharedQueue) {
		this.executorService = executorService;
		this.dominoMigrationServerConfig = dominoMigrationServerConfig;
		this.dominoUnidSharedQueue = dominoUnidSharedQueue;
	}

	@Override
	public void run() {
		try {
			this.init();
			this.doJob();
		} catch (Exception e) {
			logger.error(e.toString(), e);
			this.interrupted = true;
		} finally {
			this.shutdownExecutorService();

			if (this.interrupted) {
				logger.info("stop Migration");
				Thread.currentThread().interrupt();
			}
		}
	}

	private void init() throws Exception {
		logger.info("start Migration");

		if (!this.interrupted) {
			this.database = DominoFactory.getDataBase(this.database, this.dominoMigrationServerConfig);
			this.dirContext = DominoFactory.getDirContext(this.dirContext, this.dominoMigrationServerConfig);
			this.targetAuthObject = DominoFactory.getTargetAuthObject(this.targetAuthObject, this.dominoMigrationServerConfig);
		}
	}

	private void shutdownExecutorService() {
		while (!this.executorService.isTerminated()) {
			this.executorService.shutdown();

			try {
				this.executorService.awaitTermination(5, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				logger.error(e.toString(), e);
			}
		}

		this.interrupted = true;
	}

	private void doJob() throws Exception {
		DocumentCollection documentCollection = this.database.getAllDocuments();
		int documentCount = documentCollection.getCount();
		Map<String, Integer> unidMap = this.readUnidHistory();

		logger.info("DS:the total server document count:" + Integer.toString(documentCount) + " " + "QSZ:" + this.dominoUnidSharedQueue.getQueueSize());

		this.doMigrationJob(documentCount, documentCollection, unidMap);
	}

	private void doMigrationJob(int documentCount, DocumentCollection documentCollection, Map<String, Integer> unidMap) throws Exception {
		for (int i = 1; i <= documentCount; i++) {
			Document document = documentCollection.getNthDocument(i);

			if (document == null) {
				logger.info("DS:document is null");
				continue;
			}

			if (this.interrupted) {
				throw new Exception(); //NOPMD
			}

			String documentUnid = document.getUniversalID();

			if (unidMap.containsKey(documentUnid)) {
				logger.info("UNID:" + documentUnid + " " + "DS:document already migrated.");
				continue;
			}

			if (documentUnid.length() == Constants.DOMINO_UNID_LENGTH && this.dominoUnidSharedQueue.put(documentUnid)) {
				DominoDocumentSet dominoDocumentSet = new DominoDocumentSet();

				dominoDocumentSet.setDocument(document);
				dominoDocumentSet.setDocumentUnid(documentUnid);
				dominoDocumentSet.setDominoServerConfig(this.dominoMigrationServerConfig);
				dominoDocumentSet.setDirContext(this.dirContext);
				dominoDocumentSet.setTargetAuthObject(this.targetAuthObject);

				DominoDocumentFetcher dominoDocumentFetcher = new DominoDocumentFetcher(dominoDocumentSet, this.dominoUnidSharedQueue);

				this.executorService.execute(dominoDocumentFetcher);
			}
		}
	}

	private Map<String, Integer> readUnidHistory() throws IOException {
		String fullPathString = this.dominoMigrationServerConfig.getHistoryDir() + "/" + Constants.DOMINO_HISTORY_FILE_BY_UNID;
		File fullPathFile = new File(fullPathString);

		if (!fullPathFile.exists()) {
			fullPathFile.createNewFile();
		}

		BufferedReader bufferedReader = null;
		String line = null;
		Map<String, Integer> unidMap = new HashMap<String, Integer>();

		try {
			bufferedReader = new BufferedReader(new FileReader(fullPathFile));

			while ((line = bufferedReader.readLine()) != null) {
				unidMap.put(line, 1);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(bufferedReader);
		}

		return unidMap;
	}
}