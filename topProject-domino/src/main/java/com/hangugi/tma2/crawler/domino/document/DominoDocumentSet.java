package com.hangugi.tma2.crawler.domino.document;

import javax.naming.directory.DirContext;

import lotus.domino.Document;

import com.hangugi.tma2.crawler.domino.config.DominoServerConfig;

public class DominoDocumentSet {
	private DominoServerConfig dominoServerConfig;
	private String documentUnid;
	private Document document;
	private DirContext dirContext;
	private Object targetAuthObject;

	public DominoServerConfig getDominoServerConfig() {
		return this.dominoServerConfig;
	}

	public void setDominoServerConfig(DominoServerConfig dominoServerConfig) {
		this.dominoServerConfig = dominoServerConfig;
	}

	public String getDocumentUnid() {
		return this.documentUnid;
	}

	public void setDocumentUnid(String documentUnid) {
		this.documentUnid = documentUnid;
	}

	public Document getDocument() {
		return this.document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public DirContext getDirContext() {
		return this.dirContext;
	}

	public void setDirContext(DirContext dirContext) {
		this.dirContext = dirContext;
	}

	public Object getTargetAuthObject() {
		return this.targetAuthObject;
	}

	public void setTargetAuthObject(Object targetAuthObject) {
		this.targetAuthObject = targetAuthObject;
	}
}
