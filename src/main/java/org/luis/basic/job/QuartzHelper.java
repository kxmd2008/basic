package org.luis.basic.job;

import org.quartz.CronExpression;

/**
 * @description 
 */

public final class QuartzHelper {
	
	public final static String GROUP_JOB = "job.group";
	public final static String GROUP_TRIGGER = "trigger.group";
	
	/**
	 * 验证表达式是否正确
	 * @param cronExpression
	 * @return
	 */
	public static boolean isValidCronExpression(String cronExpression){
		return CronExpression.isValidExpression(cronExpression);
	}
	
	/**
	 * 获得Class对象
	 * @param jobClassName
	 * @return
	 */
	public static Class<?> getJobClass(String jobClassName) {
		try {
			Object executor = Class.forName(jobClassName).newInstance();
			return executor.getClass();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;	
	}
}
