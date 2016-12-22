package com.daou.tma2.crawler.domino;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.daou.tma2.crawler.domino.config.DominoConfig;
import com.daou.tma2.crawler.domino.config.DominoServerConfig;
import com.daou.tma2.crawler.domino.service.AbstractDominoService;
import com.daou.tma2.crawler.domino.util.DominoFactory;

public class DominoMain {
	private static final Logger logger = LoggerFactory.getLogger(DominoMain.class);
	private static ApplicationContext applicationContext;

	public static void main(String[] args) {
		logger.info("DS:start DominoMain");

		AbstractDominoService abstractDominoService = null;

		try {
			applicationContext = new ClassPathXmlApplicationContext("classpath:META-INF/domino.xml");

			DominoConfig dominoConfig = new DominoConfig(args);
			dominoConfig.readDominoConfig();
			dominoConfig.generateQueueDirectory();
			int size = dominoConfig.getMap().size();

			if (size > 1) {
				logger.error("Two more type not allowed. exit...");
			} else {
				List<DominoServerConfig> dominoServerConfigList = dominoConfig.getDominoServerConfigList();

				if (dominoServerConfigList.isEmpty()) {
					logger.info("DS:invalid config format. exit...");
				} else {
					// type must be migration or journaling
					String type = dominoConfig.getMap().keySet().iterator().next();
					abstractDominoService = DominoFactory.getDominoServie(type, applicationContext);
					abstractDominoService.init();
					abstractDominoService.launch(dominoServerConfigList);
				}
			}
		} catch (Throwable e) {
			logger.error(e.toString(), e);
		}

		addShutdownHook(abstractDominoService);
	}

	// 미나프로젝트의 서브프로젝트 ftpserver-core-1.0.6.jar 파일 org.apache.ftpserver.main.CommandLine 참조.
	// 터미널에서 kill -15로 종료 시그널 발생 시킬 때, 해당 시그널 받고 종료 절차로 들어간다.
	// kill -9 에 대해서는 반응하지 않는다.
	private static void addShutdownHook(final AbstractDominoService abstractDominoService) {
		// create shutdown hook
		Runnable shutdownHook = new Runnable() {
			@Override
			public void run() {
				logger.info("DS:Stopping server...");
				abstractDominoService.shutdown();
			}
		};

		// add shutdown hook
		Runtime runtime = Runtime.getRuntime();
		runtime.addShutdownHook(new Thread(shutdownHook));
	}
}