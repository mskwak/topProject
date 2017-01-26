package com.hangugi.tma2.crawler.domino.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hangugi.tma2.crawler.domino.config.DominoServerConfig;
import com.hangugi.tma2.crawler.domino.config.migration.DominoMigrationServerConfig;
import com.hangugi.tma2.crawler.domino.queue.DominoUnidSharedQueue;
import com.hangugi.tma2.crawler.domino.thread.DominoMigrationUnidFetcher;

@Service
public class DominoMigrationService implements AbstractDominoService {
	private static final Logger logger = LoggerFactory.getLogger(DominoMigrationService.class);
	private final List<DominoMigrationUnidFetcher> dominoMigrationUnidFetcherList = new ArrayList<DominoMigrationUnidFetcher>();

	public List<DominoMigrationUnidFetcher> getDominoMigrationUnidFetcherList() {
		return this.dominoMigrationUnidFetcherList;
	}

	@Override
	public void init() {
		logger.info("DS:init DominoMigrationService");
	}

	@Override
	public void shutdown() {
		logger.info("DS:shutdown DominoMigrationService");

		for (DominoMigrationUnidFetcher dominoMigrationUnidFetcher : this.dominoMigrationUnidFetcherList) {
			dominoMigrationUnidFetcher.interrupt();
		}
	}

	@Override
	public void launch(List<DominoServerConfig> dominoServerConfigList) {
		for (DominoServerConfig dominoServerConfig : dominoServerConfigList) {
			if (dominoServerConfig instanceof DominoMigrationServerConfig) {
				this.spawn((DominoMigrationServerConfig) dominoServerConfig);
			}
		}
	}

	private void spawn(DominoMigrationServerConfig dominoMigrationServerConfig) {
		ExecutorService executorService = Executors.newFixedThreadPool(1);
		DominoUnidSharedQueue dominoUnidSharedQueue = new DominoUnidSharedQueue(1);
		DominoMigrationUnidFetcher dominoMigrationUnidFetcher = new DominoMigrationUnidFetcher(executorService, dominoMigrationServerConfig, dominoUnidSharedQueue);

		this.dominoMigrationUnidFetcherList.add(dominoMigrationUnidFetcher);

		dominoMigrationUnidFetcher.start();
	}
}
