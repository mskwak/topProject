package com.daou.tma2.crawler.domino.thread;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daou.tma2.crawler.domino.config.Constants;
import com.daou.tma2.crawler.domino.config.DominoServerConfig;
import com.daou.tma2.crawler.domino.envelope.Envelope;
import com.daou.tma2.crawler.domino.envelope.EnvelopeGenerator;
import com.daou.tma2.crawler.domino.sender.MimeHandler;
import com.daou.tma2.crawler.domino.util.DominoFactory;
import com.daou.tma2.crawler.domino.util.DominoUtil;

public class RetryDocumentFetcher extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(RetryDocumentFetcher.class);
	private final static long sleepTime = 5 * 1000;
	private final static long timeToClean = 86400 * 1000;
	private final static long timeToRetry = 60 * 1000;
	private final static int retryMaxJobCount = 1000;
	private boolean shutdown = false;
	private DominoServerConfig dominoServerConfig;
	private Object targetAuthObject;

	public RetryDocumentFetcher(DominoServerConfig dominoServerConfig) {
		this.dominoServerConfig = dominoServerConfig;
	}

	public void shutdown() {
		this.shutdown = true;
	}

	@Override
	public void run() {
		while (!this.shutdown) {
			this.init();

			if (DominoUtil.isSend(this.dominoServerConfig)) {
				String protocol = this.dominoServerConfig.getDominoTargetConfig().getProtocol();
				this.sendUnidDocument(protocol);
				this.sendRetryDocument(protocol);
			} else {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
				}
			}
		}

		logger.info("DS:shutdown RetryDocumentFetcher");
	}

	private void init() {
		try {
			this.targetAuthObject = DominoFactory.getTargetAuthObject(this.targetAuthObject, this.dominoServerConfig);
		} catch (MessagingException e) {
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e1) {
			}
		}
	}

	private void sendUnidDocument(String protocol) {
		String unidDirString = this.dominoServerConfig.getUnidDir();
		File baseDirFile = new File(unidDirString);
		List<String> list = new ArrayList<String>();

		this.getFileList(baseDirFile, list);

		for (String str : list) {
			String[] splitString = str.split("(\\\\|/)");
			String documentUnid = splitString[splitString.length - 1];
			String randomDirNumber = splitString[splitString.length - 2];

			if (documentUnid.length() == Constants.DOMINO_UNID_LENGTH) {
				String metaPath = unidDirString + "/" + randomDirNumber + "/" + documentUnid;
				String mimePath = metaPath + ".mime";
				String emlPath = metaPath + ".eml";

				if (this.isArchivingTarget(metaPath, mimePath, emlPath)) {
					this.send(documentUnid, metaPath, mimePath, emlPath, protocol);
				} else {
					this.clean(metaPath, mimePath, emlPath);
				}
			}
		}
	}

	private void sendRetryDocument(String protocol) {
		String retryDirString = this.dominoServerConfig.getRetryDir();
		File retryDirFile = new File(retryDirString);
		File[] fileArray = retryDirFile.listFiles();
		int maxJobCount = retryMaxJobCount;
		int retryDocumentCount = fileArray.length;

		logger.debug("DS:read retry queue directory:" + retryDirString);
		logger.debug("DS:the total retry document count on local queue:" + retryDocumentCount);

		if (retryDocumentCount < retryMaxJobCount) {
			try {
				logger.debug("DS:retry document thread sleep:" + timeToRetry / 1000 + " seconds");
				Thread.sleep(timeToRetry);
			} catch (InterruptedException e) {
				//NOPMD
			}
		}

		for (File file : fileArray) {
			String documentUnid = file.getName();

			logger.debug("DS:retry document thread, get file name: " + documentUnid);

			String metaPath = retryDirString + "/" + documentUnid;
			String mimePath = metaPath + ".mime";
			String emlPath = metaPath + ".eml";

			if (documentUnid.length() == Constants.DOMINO_UNID_LENGTH) {
				if (!this.isArchivingTarget(metaPath, mimePath, emlPath)) {
					logger.debug("DS:retry document thread, skip archive: " + documentUnid);
					continue;
				}

				if (--maxJobCount < 0) {
					break;
				}

				this.send(documentUnid, metaPath, mimePath, emlPath, protocol);
			}
		}
	}

	private void send(String documentUnid, String metaPath, String mimePath, String emlPath, String protocol) {
		EnvelopeGenerator envelopeGenerator = DominoUtil.getEnvelopeGenerator("file");
		MimeHandler mimeHandler = DominoUtil.getMimeHandler(protocol);
		boolean isSuccess = false;

		try {
			logger.debug("DS:retry document thread, try to send: " + documentUnid);

			Envelope envelope = envelopeGenerator.generateEnvelope(null, metaPath, emlPath, null, this.dominoServerConfig);
			isSuccess = mimeHandler.send(documentUnid, envelope, this.targetAuthObject);
			logger.debug("DS:retry document thread, send success.: " + documentUnid);
		} catch (Exception e) {
			logger.error(e.toString() + " DS:retry document thread Exception", e);

			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e1) {
			}
			//logger.info("Exception Occurs: " + documentUnid, e);
		} finally {
			if (isSuccess) {
				logger.debug("DS:retry document thread, delete local document: " + documentUnid);
				mimeHandler.deleteLocalDocument(documentUnid, metaPath, mimePath, emlPath);
			}
		}
	}

	private void clean(String metaPath, String mimePath, String emlPath) {
		File[] fileList = { new File(metaPath), new File(mimePath), new File(emlPath) };

		boolean isExist = this.isExist(fileList);
		boolean isOld = this.isOld(fileList);
		boolean isZeroSize = this.isZeroSize(fileList);

		if (!isExist && isOld && isZeroSize) {
			for (File file : fileList) {
				if (file.delete()) {
					logger.info("FILE:" + file.getAbsolutePath() + " DS:clean job. delete file.");
				}
			}
		}
	}

	private boolean isExist(File[] fileList) {
		boolean isExist = true;

		for (File file : fileList) {
			if (!file.exists()) {
				isExist = false;
				break;
			}
		}

		return isExist;
	}

	private boolean isOld(File[] fileList) {
		boolean isOld = false;

		for (File file : fileList) {
			if (this.isOld(file.lastModified(), timeToClean)) {
				isOld = true;
				break;
			}
		}

		return isOld;
	}

	private boolean isZeroSize(File[] fileList) {
		boolean isZeroSize = false;

		for (File file : fileList) {
			if (file.length() == 0) {
				isZeroSize = true;
				break;
			}
		}

		return isZeroSize;
	}

	private boolean isArchivingTarget(String metaPath, String mimePath, String emlPath) {
		boolean isArchivingTarget = true;
		File[] fileList = { new File(metaPath), new File(mimePath), new File(emlPath) };

		for (File file : fileList) {
			if (!file.exists()) {
				isArchivingTarget = false;
				logger.debug("DS:retry document thread, skip archive. file does not exist.: " + isArchivingTarget + " : " + file.toString());
				break;
			}

			if (!this.isOld(file.lastModified(), timeToRetry)) {
				isArchivingTarget = false;
				logger.debug("DS:retry document thread, skip archive. file too young.: " + isArchivingTarget + " : " + file.toString());
				break;
			}

			if (file.length() == 0) {
				isArchivingTarget = false;
				logger.debug("DS:retry document thread, skip archive. file length is zero.: " + isArchivingTarget + " : " + file.toString());
				break;
			}
		}

		return isArchivingTarget;
	}

	private boolean isOld(long mtime, long howOld) {
		long ctime = System.currentTimeMillis();
		long period = ctime - mtime;

		// 마지막 수정 시간이 1초 안쪽이라면 아카이빙 건너뛰기
		if (mtime < 1 * 1000) {
			logger.debug("DS:retry document thread, skip archive. file too young." + " ctime:" + ctime + " mtime:" + mtime + " period:" + period + " howOld:" + howOld);
			return false;
		}

		// 60초 보다 오래되었을 경우 아카이빙 시도
		if (period > howOld) {
			logger.debug("DS:retry document thread, try to archive." + " ctime:" + ctime + " mtime:" + mtime + " period:" + period + " howOld:" + howOld);
			return true;
		}

		return false;
	}

	private void getFileList(File targetPath, List<String> list) {
		if (targetPath.isDirectory()) {
			String[] fl = targetPath.list();
			File tmpFile = null;

			for (int i = 0; i < fl.length; i++) {
				tmpFile = new File(targetPath.getAbsolutePath() + "/" + fl[i]);

				if (tmpFile.isDirectory()) {
					this.getFileList(tmpFile, list);
				}
				else {
					list.add(targetPath.getPath() + File.separator + fl[i]);
				}
			}
		}
	}
}