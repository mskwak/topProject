package com.daou.quartz.group01;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class ExtendedJobDetailBean01 extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		Job01 job01 = new Job01();
		job01.print();

		Job02 job02 = new Job02();
		job02.print();
	}
}
