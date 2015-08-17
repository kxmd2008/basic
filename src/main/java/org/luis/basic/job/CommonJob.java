package org.luis.basic.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @description
 * @author guoliang.li
 * @date 2013-8-15 下午12:17:06
 * @since 1.0.0.0
 */
public abstract class CommonJob implements Job {

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		executeContext();
	}

	public abstract void executeContext();

}
