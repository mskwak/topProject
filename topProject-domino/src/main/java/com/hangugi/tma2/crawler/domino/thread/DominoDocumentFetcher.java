package com.hangugi.tma2.crawler.domino.thread;

import javax.naming.directory.DirContext;

import lotus.domino.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hangugi.tma2.crawler.domino.config.DominoServerConfig;
import com.hangugi.tma2.crawler.domino.document.DominoDocument;
import com.hangugi.tma2.crawler.domino.document.DominoDocumentSet;
import com.hangugi.tma2.crawler.domino.envelope.Envelope;
import com.hangugi.tma2.crawler.domino.queue.DominoUnidSharedQueue;
import com.hangugi.tma2.crawler.domino.sender.MimeHandler;
import com.hangugi.tma2.crawler.domino.util.DominoUtil;

public class DominoDocumentFetcher implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(DominoDocumentFetcher.class);
	private final DominoUnidSharedQueue dominoUnidSharedQueue;
	private final DominoDocumentSet dominoDocumentSet;
	private boolean interrupted = false;

	public DominoDocumentFetcher(DominoDocumentSet dominoDocumentSet, DominoUnidSharedQueue dominoUnidSharedQueue) {
		this.dominoDocumentSet = dominoDocumentSet;
		this.dominoUnidSharedQueue = dominoUnidSharedQueue;
	}

	@Override
	public void run() {
		String documentUnid = this.dominoDocumentSet.getDocumentUnid();
		Document document = this.dominoDocumentSet.getDocument();
		DominoServerConfig dominoServerConfig = this.dominoDocumentSet.getDominoServerConfig();
		DirContext dirContext = this.dominoDocumentSet.getDirContext();
		Object targetAuthObject = this.dominoDocumentSet.getTargetAuthObject();

		String unidDirString = dominoServerConfig.getUnidDir();
		String retryDirString = dominoServerConfig.getRetryDir();
		String historyDirString = dominoServerConfig.getHistoryDir();
		String targetProtocol = dominoServerConfig.getTargetProtocol();

		String metaPath = DominoUtil.getMetaPath(documentUnid, unidDirString);
		String mimePath = metaPath + ".mime";
		String emlPath = metaPath + ".eml";

		DominoDocument dominoDocument = new DominoDocument(documentUnid, document);

		/*
		 * envelope 정보 생성 및 envelope 정보를 로컬 큐 파일에 저장
		 * eml 파일을 로컬 큐 파일에 저장
		 */
		Envelope envelope = null;
		try {
			if (!dominoDocument.isArchivingTarget()) {
				this.dominoUnidSharedQueue.remove(documentUnid);
				return;
			}

			envelope = dominoDocument.generateEnvelope(targetProtocol, metaPath, emlPath, dirContext, dominoServerConfig);
			dominoDocument.generateEml(mimePath, emlPath, dominoServerConfig);

			if (DominoUtil.isValid(metaPath, mimePath, emlPath)) {
				logger.info("UNID:" + documentUnid + " " + "FILE:" + emlPath + " " + "DS:file saved successfully.");
				dominoDocument.deleteServerDocument(metaPath, emlPath, dominoServerConfig);
			} else {
				logger.error("UNID:" + documentUnid + " " + "FILE:" + emlPath + " " + "DS:failed to save the file.");
				this.interrupted = true;
			}
		} catch (Exception e) {
			logger.error(e.toString() + ":" + documentUnid, e);
			this.dominoUnidSharedQueue.setContinue(false);
			this.interrupted = true;
		} finally {
			if (this.interrupted) {
				this.clean(documentUnid);
				return;
			}
		}

		/*
		 * 전송 로직
		 */
		MimeHandler mimeHandler = DominoUtil.getMimeHandler(targetProtocol);

		try {
			if (DominoUtil.isSend(dominoServerConfig)) {
				if (mimeHandler.send(documentUnid, envelope, targetAuthObject)) {
					mimeHandler.deleteLocalDocument(documentUnid, metaPath, mimePath, emlPath);
					mimeHandler.recordUnidHistory(documentUnid, historyDirString);
				} else {
					mimeHandler.moveLocalDocument(documentUnid, retryDirString, metaPath, mimePath, emlPath);
				}
			} else {
				mimeHandler.moveLocalDocument(documentUnid, retryDirString, metaPath, mimePath, emlPath);
			}
		} catch (Exception e) {
			logger.error(e.toString() + ":" + documentUnid, e);
			if (mimeHandler != null) {
				mimeHandler.moveLocalDocument(documentUnid, retryDirString, metaPath, mimePath, emlPath);
			}
		} finally {
			this.clean(documentUnid);
		}
	}

	private void clean(String documentUnid) {
		this.dominoUnidSharedQueue.remove(documentUnid);

		if (this.interrupted) {
			Thread.currentThread().interrupt();
		}
	}
}
