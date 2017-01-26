package com.hangugi.tma2.crawler.domino.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hangugi.tma2.crawler.domino.config.journaling.DominoScheduleConfig;

public class DominoTimeUtil {
	private static final Logger logger = LoggerFactory.getLogger(DominoTimeUtil.class);

	public static boolean isJobTime(DominoScheduleConfig dominoScheduleConfig) {
		String scheduleType = dominoScheduleConfig.getType();
		String batchStartTime = dominoScheduleConfig.getBatchStartTime();
		String batchEndTime = dominoScheduleConfig.getBatchEndTime();

		if ("realtime".equalsIgnoreCase(scheduleType)) {
			return true;
		} else if ("batch".equalsIgnoreCase(scheduleType)) {
			return isBatchJobTime(batchStartTime, batchEndTime);
		} else {
			logger.info("DS:Invalid scheduleType");
			return false;
		}
	}

	private static boolean isBatchJobTime(String batchStartTime, String batchEndTime) { //NOPMD
		// 필드값이 비어 있을 경우 realtime으로 간주한다.
		// batchStartTime과 batchEndTime이 같을 경우 realtime으로 간주한다.
		if (StringUtils.isEmpty(batchStartTime) || StringUtils.isEmpty(batchEndTime) || batchStartTime.equalsIgnoreCase(batchEndTime)) {
			return true;
		}

		if (batchStartTime.length() == 1) {
			batchStartTime = "0" + batchStartTime;
		}

		if (batchEndTime.length() == 1) {
			batchEndTime = "0" + batchEndTime;
		}

		// SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat yyyyMMddHH = new SimpleDateFormat("yyyyMMddHH");

		String yyyymmddStr = yyyyMMdd.format(new Date());

		long startHour = Long.parseLong(yyyymmddStr + batchStartTime);
		long currentHour = Long.parseLong(yyyyMMddHH.format(new Date()));
		long endHour = Long.parseLong(yyyymmddStr + batchEndTime);

		if (startHour > endHour) {
			try {
				Date startHourDate = yyyyMMddHH.parse(Long.toString(startHour));
				long startHourTimestamp = startHourDate.getTime() + (86400000 * -1);
				Date reStartHourDate = new Date(startHourTimestamp);
				startHour = Long.parseLong(yyyyMMddHH.format(reStartHourDate));
			} catch (ParseException e) {
				startHour = Long.parseLong(yyyymmddStr + batchStartTime);
				logger.error(e.toString(), e);
			}
		}

		if (currentHour >= startHour && currentHour < endHour) {
			return true;
		}

		return false;
	}
}
