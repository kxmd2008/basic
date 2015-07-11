package org.luis.basic.job;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;

/**
 * Quartz JobListener
 * @description 
 */

public class JobListener extends JobListenerSupport {
	
	
	private final static Logger logger = Logger.getLogger(JobListener.class);

	@Override
	public String getName() {
		return "ngbfJobListener";
	}
	
	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		String triggerName = context.getTrigger().getName();
		logger.info(triggerName + " " + "jobToBeExecuted");
	}


	@Override
	public void jobWasExecuted(JobExecutionContext context,
			JobExecutionException jobException) {
		String triggerName = context.getTrigger().getName();
		logger.info(triggerName + " " + "jobWasExecuted");
		// context.getTrigger().getName(), jobException.getErrorCode(), jobException.getMessage());
	}
	
	

}
