package com.daou.tma2.crawler.domino.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daou.tma2.crawler.domino.config.DominoServerConfig;

@Service
public interface AbstractDominoService {
	public abstract void init();

	public abstract void launch(List<DominoServerConfig> dominoServerConfigList);

	public abstract void shutdown();
}
