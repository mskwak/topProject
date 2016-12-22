package com.daou.tma2.crawler.domino.envelope;

import javax.naming.directory.DirContext;

import lotus.domino.Document;

import com.daou.tma2.crawler.domino.config.DominoServerConfig;

public interface EnvelopeGenerator {
	public abstract Envelope generateEnvelope(Document document,
			String metaPath, String emlPath,
			DirContext dirContext, DominoServerConfig dominoServerConfig) throws Exception;
}
