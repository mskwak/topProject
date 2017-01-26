package com.hangugi.quartz.group02;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class ExtendedJobDetailBean02 extends QuartzJobBean {

	@Autowired
	private Job03 job03;

	@Autowired
	private Job04 job04;

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		this.job03.print();
		this.job04.print();
	}
}
