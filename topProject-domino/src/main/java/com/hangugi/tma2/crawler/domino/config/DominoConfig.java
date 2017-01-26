package com.hangugi.tma2.crawler.domino.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hangugi.tma2.crawler.domino.config.journaling.DominoJournalingServerConfig;
import com.hangugi.tma2.crawler.domino.config.migration.DominoMigrationServerConfig;

public class DominoConfig {
	private static final Logger logger = LoggerFactory.getLogger(DominoConfig.class);
	private String baseQueueDirectory;
	private final String[] args;
	private final List<DominoServerConfig> dominoServerConfigList = new ArrayList<DominoServerConfig>();
	private final Map<String, String> map = new HashMap<String, String>();

	public List<DominoServerConfig> getDominoServerConfigList() {
		return this.dominoServerConfigList;
	}

	public Map<String, String> getMap() {
		return this.map;
	}

	public DominoConfig(String[] args) {
		this.args = args;
	}

	public void readDominoConfig() throws Exception {
		BufferedReader bufferedReader = null;
		try {
			String configFile = System.getProperty("journal.configurationFile");
			File file = new File(configFile);

			if (!file.exists()) {
				configFile = Constants.DOMINO_CONFIG_FILE;
			}

			bufferedReader = new BufferedReader(new FileReader(configFile));
			String line = null;

			while ((line = bufferedReader.readLine()) != null) {
				line = line.trim();

				if (line.length() == 0 || line.startsWith("#")) {
					continue;
				}

				if ("end".equalsIgnoreCase(line)) {
					break;
				}

				String[] args = line.split("\\s+");
				String key = null;
				String val = null;
				int argsLength = args.length;

				if (argsLength == 0) {
					continue;
				} else if (argsLength == 1) {
					key = args[0].trim();
					this.map.put(key, key);
				} else if (argsLength == 2) {
					key = args[0].trim();
					val = args[1].trim();
				} else {
					continue;
				}

				if ("base_queue_directory".equalsIgnoreCase(key)) {
					this.baseQueueDirectory = val;
				}
				else if ("journaling".equalsIgnoreCase(key)) {
					DominoJournalingServerConfig dominoJournalingServerConfig = this.readDominoJournalingConfig(bufferedReader);
					dominoJournalingServerConfig.setTargetProtocol(dominoJournalingServerConfig.getDominoTargetConfig().getProtocol());
					this.dominoServerConfigList.add(dominoJournalingServerConfig);
				}
				else if ("migration".equalsIgnoreCase(key)) {
					DominoMigrationServerConfig dominoMigrationServerConfig = this.readDominoMigrationConfig(bufferedReader);
					dominoMigrationServerConfig.setTargetProtocol(dominoMigrationServerConfig.getDominoTargetConfig().getProtocol());
					this.dominoServerConfigList.add(dominoMigrationServerConfig);
				}
			}
		} catch (FileNotFoundException e) {
			logger.error(e.toString(), e);
			throw e;
		} catch (IOException e) {
			logger.error(e.toString(), e);
			throw e;
		} finally {
			IOUtils.closeQuietly(bufferedReader);
		}
	}

	public void generateQueueDirectory() {
		this.generateDefaultQueueDirectory();
		this.generateSubordinateQueueDirectory();
	}

	private void generateSubordinateQueueDirectory() {
		for (DominoServerConfig dominoServerConfig : this.dominoServerConfigList) {

			String ip = dominoServerConfig.getDominoServerIpAddress();
			String journalDbName = dominoServerConfig.getJournalDbName();
			String[] defaultDir = { "unid", "retry", "history" };

			for (String dir : defaultDir) {
				String dirString = this.baseQueueDirectory + "/" + dir + "/" + ip + "/" + journalDbName;
				File dirFile = new File(dirString);

				if (!dirFile.exists()) {
					dirFile.mkdirs();
				}

				// /tma2data/queue/crawler/domino/unid/175.115.94.177/mail/mskw1.nsf 하위에 0~1021 디렉토리 만들기
				if ("unid".equalsIgnoreCase(dir)) {
					for (int i = 0; i <= 1021; i++) {
						File f = new File(dirString + "/" + Integer.toString(i));

						if (!f.exists()) {
							f.mkdirs();
						}
					}
					dominoServerConfig.setUnidDir(dirString);
				} else if ("retry".equalsIgnoreCase(dir)) {
					dominoServerConfig.setRetryDir(dirString);
				} else if ("history".equalsIgnoreCase(dir)) {
					dominoServerConfig.setHistoryDir(dirString);
				}
			}
		}
	}

	private void generateDefaultQueueDirectory() {
		if (this.baseQueueDirectory == null || this.baseQueueDirectory.length() == 0) {
			this.baseQueueDirectory = Constants.DOMINO_QUEUE_DIRECTORY;
		}

		File files = new File(this.baseQueueDirectory);

		if (!files.exists()) {
			files.mkdirs();
		}
	}

	private DominoMigrationServerConfig readDominoMigrationConfig(BufferedReader bufferedReader) throws Exception {
		Map<String, String> userInfoMap = this.getUserInfo();
		DominoMigrationServerConfig dominoMigrationServerConfig = new DominoMigrationServerConfig(userInfoMap.get("user"), userInfoMap.get("database"));
		dominoMigrationServerConfig.readDominoServerConfig(bufferedReader);
		return dominoMigrationServerConfig;
	}

	private DominoJournalingServerConfig readDominoJournalingConfig(BufferedReader bufferedReader) throws Exception {
		DominoJournalingServerConfig dominoJournalingServerConfig = new DominoJournalingServerConfig();
		dominoJournalingServerConfig.readDominoServerConfig(bufferedReader);
		return dominoJournalingServerConfig;
	}

	private Map<String, String> getUserInfo() {
		if (this.args.length != 4) {
			throw new IllegalArgumentException("invalid argument.");
		}

		Map<String, String> userInfoMap = new HashMap<String, String>();

		String user = this.getUserInfoFromArgs("-user", this.args);
		String database = this.getUserInfoFromArgs("-database", this.args);

		userInfoMap.put("user", user);
		userInfoMap.put("database", database);

		return userInfoMap;
	}

	private String getUserInfoFromArgs(String type, String[] args) {
		if (!type.equalsIgnoreCase(args[0]) && !type.equalsIgnoreCase(args[2])) {
			throw new IllegalArgumentException("invalid argument.");
		} else {
			if (type.equalsIgnoreCase(args[0])) {
				return args[1];
			} else {
				return args[3];
			}
		}
	}
}
