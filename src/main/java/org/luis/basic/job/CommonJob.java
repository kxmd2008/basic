package org.luis.basic.job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @description
 * @author guoliang.li
 * @date 2013-8-15 下午12:17:06
 * @since 1.0.0.0
 */

public class CommonJob implements Job {

	private static Logger logger = Logger.getLogger(CommonJob.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		executeContext();
	}

	public void executeContext() {
		logger.debug("executed...");
	}

}
